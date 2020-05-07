package br.com.logicmc.skywars.extra;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.logicmc.skywars.SkyMain;
import br.com.logicmc.skywars.events.SkywarsPlayerEliminatedEvent;
import br.com.logicmc.skywars.game.GameLogic;
import br.com.logicmc.skywars.game.addons.GamePhase;
import br.com.logicmc.skywars.game.player.GamePlayer;
import br.com.logicmc.skywars.messages.SkyMessages;

public class TimeScheduler implements Runnable{

	private final SkyMain skywars;
	
	public TimeScheduler(SkyMain main) {
		this.skywars = main;
	
	}
	
	@Override
	public void run() {

		GameLogic logic = skywars.getGamelogic();
		logic.callTimeChange();
		
		if(logic.endPhase())
			logic.changePhase();
		
		if (logic.getCurrentphase() != GamePhase.WAITING)
			skywars.getGamelogic().getPlayerManager().getCooldownPlayersStream().forEach(new CheckCooldown());


		int time = logic.getTime();
		int i = time/60;
		Bukkit.getScoreboardManager().getMainScoreboard().getTeam("time").setSuffix("§a" + (i < 10 ? "0"+i+":" : i+":") + (time%60 < 10 ? "0"+time%60 :time%60));
	}

	private class CheckCooldown implements Consumer<UUID> {

		@Override
		public void accept(UUID uuid) {
			
			GamePlayer gplayer = skywars.getGamelogic().getPlayerManager().getGamePlayer(uuid);
			
			if(gplayer.getLastseen() == 0L)
				return;
			
			if(gplayer.getLastseen() + TimeUnit.SECONDS.toMillis(8) <= System.currentTimeMillis()  ) {
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.sendMessage(skywars.messagehandler.getMessage(SkyMessages.DEATH_LOGGEDOUT, skywars.playermanager.getPlayerBase(all.getUniqueId()).getPreferences().getLang()));
				}
				Bukkit.getPluginManager().callEvent(new SkywarsPlayerEliminatedEvent(gplayer));
			}
		}
		
	}

}
