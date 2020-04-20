package br.com.zeetzmc.skywars.game.player;

import java.util.UUID;

import br.com.zeetzmc.skywars.kit.Kit;

public class GamePlayer {

	
	private final UUID uuid;
	
	private int kills;
	private Kit kit;
	private boolean spectator;
	private long lastseen;
	
	public GamePlayer(UUID uuid) {
		this.uuid = uuid;
		this.spectator = false;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	public int getKills() {
		return kills;
	}
	
	public Kit getKit() {
		return kit;
	}
	
	public void setKit(Kit kit) {
		this.kit = kit;
	}
	
	public void increaseKill() {
		this.kills +=1;
	}
	public GamePlayer setSpectator(boolean spec) {
		this.spectator = spec;
		return this;
	}
	
	public boolean isSpectator() {
		return spectator;
	}

	public long getLastseen() {
		return lastseen;
	}

	public void setLastseen(long lastseen) {
		this.lastseen = lastseen;
	}
}
