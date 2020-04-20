package br.com.zeetzmc.skywars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.zeetzmc.skywars.SkyMain;
import br.com.zeetzmc.skywars.game.GameLogic;
import br.com.zeetzmc.skywars.game.addons.GamePhase;
import br.com.zeetzmc.skywars.game.player.GamePlayer;
import br.com.zeetzmc.skywars.game.player.GamePlayerManager;
import br.com.zeetzmc.skywars.messages.SkyMessages;

public class PlayerListeners implements Listener{

	
	private final SkyMain skywars;
	
	public PlayerListeners(SkyMain skywars) {
		this.skywars=skywars;
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		GameLogic logic = skywars.getGamelogic();
		
		skywars.utils.cleanPlayer(player);
		skywars.utils.clearChat(player);
		
		event.setJoinMessage(null);
		
		if(logic.getCurrentphase() == GamePhase.WAITING) { // new player to join game
			
			logic.getPlayerManager().addGamePlayer(new GamePlayer(player.getUniqueId()));
			
			for(Player all : Bukkit.getOnlinePlayers()) {
				all.sendMessage(skywars.messagehandler.getMessage(SkyMessages.WAITING_JOIN, skywars.playermanager.getPlayerBase(player.getUniqueId()).getPreferences().getLang()).replace("{player}", player.getName())
						+ " " +ChatColor.GRAY+ Bukkit.getOnlinePlayers().size()+"/"+Bukkit.getMaxPlayers());
			}
			
			// give join itens
		} else {
			GamePlayerManager gamemanager = logic.getPlayerManager();
			
			if(gamemanager.inCountdown(player.getUniqueId())) // ingame player left by accident?	
				gamemanager.remotePlayerCountdown(player.getUniqueId());
			else if(!gamemanager.existsStats(player.getUniqueId())) { // not entered the server before = spectator
				gamemanager.addGamePlayer(new GamePlayer(player.getUniqueId()).setSpectator(true));
				gamemanager.values().stream().filter(gameplayer->!gameplayer.isSpectator()).forEach(onlineplayer -> Bukkit.getPlayer(onlineplayer.getUuid()).showPlayer(player));
				skywars.messagehandler.sendMessage(player, SkyMessages.SPECTATOR_JOIN_MODE);
				
				player.setGameMode(GameMode.ADVENTURE);
				player.setAllowFlight(true);
				player.setFlying(true);
				player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999, 3));
			}
		}
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		GameLogic logic = skywars.getGamelogic();
		
		if(logic.getCurrentphase() == GamePhase.WAITING) { // left in waiting phase
			logic.getPlayerManager().remotePlayerCountdown(player.getUniqueId());
			for(Player all : Bukkit.getOnlinePlayers()) {
				all.sendMessage(skywars.messagehandler.getMessage(SkyMessages.WAITING_LEFT, skywars.playermanager.getPlayerBase(player.getUniqueId()).getPreferences().getLang()).replace("{player}", player.getName())
						+ " " +ChatColor.GRAY+ (Bukkit.getOnlinePlayers().size()-1)+"/"+Bukkit.getMaxPlayers());
			}
			
		} else {
			GamePlayerManager gamemanager = logic.getPlayerManager();
			
			if(!gamemanager.getGamePlayer(player.getUniqueId()).isSpectator()) { // player leaving , giving 8 second countdown chance to return
				gamemanager.addPlayerCountdown(player.getUniqueId());
				gamemanager.getGamePlayer(player.getUniqueId()).setLastseen(System.currentTimeMillis());
			} else 
				gamemanager.removeGamePlayer(player.getUniqueId());
			
		}
	}
	
	@EventHandler
	public void chat(AsyncPlayerChatEvent event) {
		
	}
}
