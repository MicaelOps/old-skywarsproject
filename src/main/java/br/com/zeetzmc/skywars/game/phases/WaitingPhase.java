package br.com.zeetzmc.skywars.game.phases;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import br.com.zeetzmc.skywars.game.GameLogic;
import br.com.zeetzmc.skywars.game.addons.PhaseController;

public class WaitingPhase implements PhaseController{

	private int time = 180;
	
	@Override
	public void onTimeChange(GameLogic logic) {
		
		time--;
		
		if(time == 0 )
			return;
		
		if(time > 59 && time%60 == 0)
			Bukkit.broadcastMessage(ChatColor.YELLOW + "O jogo comeca em "+time+" minuto(s)");
		else if(time < 6) 
			Bukkit.broadcastMessage(ChatColor.YELLOW + "O jogo comeca em "+time+" segundo(s)");
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public void begin(GameLogic logic) {}

	@Override
	public PhaseController nextPhase(GameLogic logic) {
		return new StartedPhase();
	}

	@Override
	public boolean end() {
		return false;
	}

	@Override
	public void setTime(int time) {
		this.time=time;
	}
}
