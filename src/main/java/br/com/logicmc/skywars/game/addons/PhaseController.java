package br.com.logicmc.skywars.game.addons;

import br.com.logicmc.skywars.game.GameLogic;

public interface PhaseController {

	boolean end(GameLogic logic);
	
	PhaseController nextPhase(GameLogic logic);
	
	void begin(GameLogic logic);
	
	void onTimeChange(GameLogic logic); // change in time is made in this method
	
	int getTime();
	
	void setTime(int time);

	
	
}
