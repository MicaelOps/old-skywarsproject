package br.com.logicmc.skywars.listeners;

import br.com.logicmc.core.account.PlayerBase;
import br.com.logicmc.core.account.addons.Preferences;
import br.com.logicmc.skywars.game.player.SkywarsDataPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.logicmc.core.account.addons.Group;
import br.com.logicmc.core.events.ChangeAdminStateEvent;
import br.com.logicmc.skywars.SkyMain;
import br.com.logicmc.skywars.events.SkywarsPlayerEliminatedEvent;
import br.com.logicmc.skywars.game.GameLogic;
import br.com.logicmc.skywars.game.addons.GamePhase;
import br.com.logicmc.skywars.game.player.GamePlayer;
import br.com.logicmc.skywars.game.player.GamePlayerManager;
import br.com.logicmc.skywars.messages.SkyMessages;
import br.com.logicmc.skywars.utils.FixedItems;

public class PlayerListener implements Listener{

	
	private final SkyMain skywars;
	
	public PlayerListener(SkyMain skywars) {
		this.skywars=skywars;
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		skywars.playermanager.addBasePlayer(new PlayerBase<SkywarsDataPlayer>("127.0.0.1",player.getUniqueId(), player.getName(), 0,0,Group.ADMIN.getPower(), -1,-1, -1, new Preferences("pt",true,true)));
		GamePlayerManager gamemanager = skywars.getGamelogic().getPlayerManager();
		
		skywars.utils.cleanPlayer(player);
		skywars.utils.resetPlayer(player);
		skywars.utils.clearChat(player);
		
		event.setJoinMessage(null);
		
		if(skywars.getGamelogic().getCurrentphase() == GamePhase.WAITING) { // new player to join game
			
			gamemanager.addGamePlayer(new GamePlayer(player.getUniqueId()));


			// give join itens
			giveJoin(player);

			for(Player all : Bukkit.getOnlinePlayers()) {
				all.sendMessage(skywars.messagehandler.getMessage(SkyMessages.WAITING_JOIN, skywars.playermanager.getPlayerBase(all.getUniqueId()).getPreferences().getLang()).replace("{player}", player.getName())
						+ " " +ChatColor.GRAY+ Bukkit.getOnlinePlayers().size()+"/"+Bukkit.getMaxPlayers());
			}

		} else {
			if(gamemanager.inCountdown(player.getUniqueId())) { // ingame player left by accident?	
				gamemanager.remotePlayerCountdown(player.getUniqueId());
				gamemanager.getGamePlayer(player.getUniqueId()).setLastseen(0L); // reset
			} else if(!gamemanager.existsStats(player.getUniqueId())) { // not entered the server before = spectator
				gamemanager.addGamePlayer(new GamePlayer(player.getUniqueId()).setSpectator(true));
				gamemanager.values().stream().filter(gameplayer->!gameplayer.isSpectator()).forEach(onlineplayer -> Bukkit.getPlayer(onlineplayer.getUuid()).hidePlayer(player));
				skywars.messagehandler.sendMessage(player, SkyMessages.SPECTATOR_JOIN_MODE);
				giveSpectator(player);
			}
		}
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		GamePlayerManager gamemanager = skywars.getGamelogic().getPlayerManager();
		
		event.setQuitMessage(null);
		
		if(skywars.getGamelogic().getCurrentphase() == GamePhase.WAITING) { // left in waiting phase
			gamemanager.removeGamePlayer(player.getUniqueId());
			
			for(Player all : Bukkit.getOnlinePlayers()) {
				all.sendMessage(skywars.messagehandler.getMessage(SkyMessages.WAITING_LEFT, skywars.playermanager.getPlayerBase(all.getUniqueId()).getPreferences().getLang()).replace("{player}", player.getName())
						+ " " +ChatColor.GRAY+ (Bukkit.getOnlinePlayers().size()-1)+"/"+Bukkit.getMaxPlayers());
			}
			
		} else {
			
			if(!gamemanager.getGamePlayer(player.getUniqueId()).isSpectator()) { // player leaving , giving 8 second countdown chance to return
				gamemanager.addPlayerCountdown(player.getUniqueId());
				gamemanager.getGamePlayer(player.getUniqueId()).setLastseen(System.currentTimeMillis());
			} else 
				gamemanager.removeGamePlayer(player.getUniqueId());
			
		}
	}

	@EventHandler
	public void chat(AsyncPlayerChatEvent event) {
		
		GameLogic logic = skywars.getGamelogic();
		Player player = event.getPlayer();
		
		event.setCancelled(true);
		event.setFormat(player.getDisplayName() + ChatColor.GRAY + ": "+ event.getMessage()); // default
		
		if(logic.getCurrentphase() == GamePhase.WAITING || logic.getCurrentphase() == GamePhase.END || logic.getPlayerManager().isPlaying(player.getUniqueId()) || skywars.playermanager.hasPermission(player.getUniqueId(), Group.HELPER)) 
			Bukkit.broadcastMessage(event.getFormat());
		else
			logic.getPlayerManager().values().stream().filter(gameplayer->gameplayer.isSpectator()).forEach(spec->Bukkit.getPlayer(spec.getUuid()).sendMessage(ChatColor.YELLOW +
					skywars.messagehandler.getMessage(SkyMessages.SPECTATOR_TAG, skywars.playermanager.getPlayerBase(spec.getUuid()).getPreferences().getLang()) + " " + event.getFormat()));
	}
	
	@EventHandler
	public void adminchange(ChangeAdminStateEvent event) {
		//cancellable
		GamePlayerManager gamemanager = skywars.getGamelogic().getPlayerManager();
		
		if(skywars.getGamelogic().getCurrentphase() == GamePhase.WAITING) {
			// cancel
			event.getPlayer().sendMessage(ChatColor.RED + "This command is only available during game.");
		} else {
			boolean playing =gamemanager.isPlaying(event.getPlayer().getUniqueId());
			if(event.isActivated()) {
				if(playing) {
					SkywarsPlayerEliminatedEvent evepl = new SkywarsPlayerEliminatedEvent(event.getPlayer().getUniqueId());
					Bukkit.getPluginManager().callEvent(evepl);	
				}
			}
		}
	}
	
	private void giveSpectator(Player player) {
		player.setGameMode(GameMode.SPECTATOR);
		player.setAllowFlight(true);
		player.setFlying(true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999, 3));
		player.getInventory().setItem(2, FixedItems.SPECTATE_PLAYERS.getBuild(skywars.messagehandler, skywars.getLang(player)));
		player.getInventory().setItem(5, FixedItems.SPECTATE_JOINLOBBY.getBuild(skywars.messagehandler, skywars.getLang(player)));
		player.getInventory().setItem(6, FixedItems.SPECTATE_JOINNEXT.getBuild(skywars.messagehandler, skywars.getLang(player)));
	}
	
	private void giveJoin(Player player) {
		player.setGameMode(GameMode.SURVIVAL);
		player.getInventory().setItem(0, FixedItems.KIT_MENU.getBuild(skywars.messagehandler, skywars.getLang(player)));
	}

}
