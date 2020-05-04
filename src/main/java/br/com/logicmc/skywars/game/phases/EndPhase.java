package br.com.logicmc.skywars.game.phases;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import br.com.logicmc.skywars.events.SkywarsWinnerEvent;
import br.com.logicmc.skywars.game.GameLogic;
import br.com.logicmc.skywars.game.addons.GamePhase;
import br.com.logicmc.skywars.game.addons.PhaseController;

public class EndPhase implements PhaseController{

	private int time;
	
	private EndPhase() {
		time= 0;
	}
	@Override
	public boolean end(GameLogic logic) {
		return false;
	}

	@Override
	public PhaseController nextPhase(GameLogic logic) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void begin(GameLogic logic) {
		Bukkit.getPluginManager().callEvent(new SkywarsWinnerEvent());
		logic.setCurrentphase(GamePhase.END);
	}

	@Override
	public void onTimeChange(GameLogic logic) {
		time++;
		
		if(time == 10)
			Bukkit.shutdown();
		
		if(time < 5) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				all.playSound(all.getLocation(), Sound.FIREWORK_LAUNCH, 50L, 50L);
			}
		}
		if(time == 5) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				all.playSound(all.getLocation(), Sound.FIREWORK_LARGE_BLAST, 50L, 50L);
				all.playSound(all.getLocation(), Sound.FIREWORK_LARGE_BLAST, 50L, 50L);
				all.playSound(all.getLocation(), Sound.FIREWORK_LARGE_BLAST, 50L, 50L);
			}
		}
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public void setTime(int time) {
		this.time=time;
	}

}
