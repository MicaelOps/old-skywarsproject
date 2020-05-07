package br.com.logicmc.skywars.game.phases;

import br.com.logicmc.skywars.chest.ChestManager;
import br.com.logicmc.skywars.game.GameLogic;
import br.com.logicmc.skywars.game.addons.GamePhase;
import br.com.logicmc.skywars.game.addons.PhaseController;
import br.com.logicmc.skywars.game.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class StartedPhase implements PhaseController{


	private int time = 0;
	
	@Override
	public void onTimeChange(GameLogic logic) {
		
		time++;

		// 5 minutes
		int refill = 300;
		if(time ==8) // end of invincibility
			logic.setCurrentphase(GamePhase.STARTED);
		else if(time > refill -6 && time < refill) {
			// warn refill time in 5 seconds
			Bukkit.broadcastMessage(ChatColor.YELLOW+"Refill in "+(refill -time)+"s");
		} else if(time == refill) {
			ChestManager.getInstance().refilChests();
			Bukkit.broadcastMessage(ChatColor.YELLOW+"Chests are refilled");
		}
			
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public void begin(GameLogic logic) {
		// remove players from cage , etc
		logic.setCurrentphase(GamePhase.INVINCIBLE);
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
		objective.getScore("§8").setScore(8);
		objective.getScore("§7").setScore(7);
		objective.getScore("§6").setScore(6);

		Team event = scoreboard.registerNewTeam("nevent");
		event.setPrefix("§fRefill em:");
		event.addEntry("§7");

		Team tempo = scoreboard.getTeam("time");
		tempo.removeEntry("§4");
		tempo.addEntry("§6");
		tempo.setPrefix("");
		Team kills = scoreboard.registerNewTeam("kills");
		kills.setPrefix("§fKills: ");
		kills.setSuffix("§a0");
		kills.addEntry("§4");
		Player[] playarray = new Player[Bukkit.getOnlinePlayers().size()];
		Bukkit.getOnlinePlayers().toArray(playarray);
		int i = 0;
		for(Location location : logic.getIslands()) {
			if(i == playarray.length)
				break;
			else
				playarray[i].teleport(location);
			i++;
		}
	}

	@Override
	public PhaseController nextPhase(GameLogic logic) {
		return null;
	}

	@Override
	public boolean end(GameLogic logic) {
		return logic.getPlayerManager().values().stream().allMatch(GamePlayer::isSpectator); //testing purposes
		//return logic.getPlayerManager().values().stream().filter(gameplayer -> !gameplayer.isSpectator()).count() > 1; // real
	}

	@Override
	public void setTime(int time) {
		this.time=time;
	}
}
