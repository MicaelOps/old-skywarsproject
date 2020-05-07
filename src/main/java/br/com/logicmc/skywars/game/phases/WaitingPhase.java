package br.com.logicmc.skywars.game.phases;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import br.com.logicmc.skywars.game.GameLogic;
import br.com.logicmc.skywars.game.addons.PhaseController;

public class WaitingPhase implements PhaseController{

	private int time = 180;
	
	@Override
	public void onTimeChange(GameLogic logic) {
		
		time--;
		
		if(time != 0 ) {
			if(time > 59 && time%60 == 0)
				Bukkit.broadcastMessage(ChatColor.YELLOW + "O jogo comeca em "+time/60+" minuto(s)");
			else if(time < 6)
				Bukkit.broadcastMessage(ChatColor.YELLOW + "O jogo comeca em "+time+" segundo(s)");
		}
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
	public boolean end(GameLogic logic) {
		if(time == 0){
			boolean play = Bukkit.getOnlinePlayers().size() > 1;
			if(!play)
				if(Bukkit.getPlayer("okc") == null) {// testing purposes
					time=60;
					return false;
				} else
					return true;
			else
				return true;
		} else
			return false;
	}

	@Override
	public void setTime(int time) {
		this.time=time;
	}
}
