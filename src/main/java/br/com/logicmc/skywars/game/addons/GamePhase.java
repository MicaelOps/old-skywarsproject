package br.com.logicmc.skywars.game.addons;

public enum GamePhase {

	WAITING(false,false),
	INVINCIBLE(false,true),
	STARTED(true,true),
	DEATHMATCH(true,true),
	END(false,false);
	
	private final boolean damageAllowed,buildAllowed;
	
	GamePhase(boolean damage, boolean build){
		this.damageAllowed = damage;
		this.buildAllowed = build;
	}
	
	public boolean isDamageAllowed() {
		return this.damageAllowed;
	}
	public boolean isBuildAllowed() {
		return buildAllowed;
	}
}
