package br.com.zeetzmc.skywars.game.addons;

import br.com.zeetzmc.skywars.game.GameLogic;

public interface PhaseController {

	void begin(GameLogic logic);
	
	void onTimeChange(GameLogic logic); // change in time is made in this method
	
	int getTime();
	
	void setTime(int time);
	
	boolean end();
	
	PhaseController nextPhase(GameLogic logic);
	
	
	
}
