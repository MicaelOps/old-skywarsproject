package br.com.zeetzmc.skywars.game;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import br.com.zeetzmc.skywars.SkyMain;
import br.com.zeetzmc.skywars.extra.TimeScheduler;
import br.com.zeetzmc.skywars.game.addons.GamePhase;
import br.com.zeetzmc.skywars.game.addons.PhaseController;
import br.com.zeetzmc.skywars.game.phases.WaitingPhase;
import br.com.zeetzmc.skywars.game.player.GamePlayerManager;

public class GameLogic {

	
	private final GamePlayerManager manager;
	private BukkitTask task;
	
	private PhaseController phasecontroller;
	private GamePhase currentphase;
	
	public GameLogic() {
		currentphase = GamePhase.WAITING;
		phasecontroller = new WaitingPhase();
		manager = new GamePlayerManager();
	}
	

	public void startTimer(SkyMain plugin) {
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new TimeScheduler(plugin), 0L, 21L);
	}
	
	public void stopTimer() {
		task.cancel();
	}
	
	public void setCurrentphase(GamePhase currentphase) {
		this.currentphase = currentphase;
	}
	
	public GamePlayerManager getPlayerManager() {
		return manager;
	}
	public GamePhase getCurrentphase() {
		return currentphase;
	}
	public PhaseController getPhasecontroller() {
		return phasecontroller;
	}
	public int getTime() {
		return phasecontroller.getTime();
	}
	
	public void callTimeChange() {
		phasecontroller.onTimeChange(this);
	}

	public void changePhase() {
		phasecontroller.nextPhase(this);
	}
	public boolean endPhase() {
		return phasecontroller.end();
	}
}
