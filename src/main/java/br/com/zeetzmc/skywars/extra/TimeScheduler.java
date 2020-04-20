package br.com.zeetzmc.skywars.extra;

import br.com.zeetzmc.skywars.SkyMain;
import br.com.zeetzmc.skywars.game.GameLogic;

public class TimeScheduler implements Runnable{

	private final SkyMain skywars;
	private int internaltimer; // use for other things like players absence countdown
	
	public TimeScheduler(SkyMain main) {
		this.skywars = main;
	}
	
	@Override
	public void run() {
	
		internaltimer++;
		
		GameLogic logic = skywars.getGamelogic();
		logic.callTimeChange();
		
		if(logic.endPhase())
			logic.changePhase();
		
		
		
	}

}
