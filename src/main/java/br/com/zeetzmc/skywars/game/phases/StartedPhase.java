package br.com.zeetzmc.skywars.game.phases;

import br.com.zeetzmc.skywars.game.GameLogic;
import br.com.zeetzmc.skywars.game.addons.GamePhase;
import br.com.zeetzmc.skywars.game.addons.PhaseController;

public class StartedPhase implements PhaseController{
	
	
	private final int refill = 300; // 5 minutes
	private int time = 0;
	
	@Override
	public void onTimeChange(GameLogic logic) {
		
		time++;
		
		if(time ==8) // end of invincibility
			logic.setCurrentphase(GamePhase.STARTED);
		
		if(time > refill-6 && time < refill ) {
			// warn refill time in 5 seconds
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
	}

	@Override
	public PhaseController nextPhase(GameLogic logic) {
		return null;
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
