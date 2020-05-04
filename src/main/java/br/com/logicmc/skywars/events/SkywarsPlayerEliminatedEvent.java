package br.com.logicmc.skywars.events;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.logicmc.skywars.game.player.GamePlayer;

public class SkywarsPlayerEliminatedEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	private final UUID uuid;
	private GamePlayer gameplayer;
	
	public SkywarsPlayerEliminatedEvent(UUID uuid) {
		this.uuid=uuid;
	}
	
	public SkywarsPlayerEliminatedEvent(GamePlayer gameplayer) {
		this.uuid = gameplayer.getUuid();
		this.gameplayer = gameplayer;
	}


	public GamePlayer getGameplayer() {
		return gameplayer;
	}

	public UUID getUuid() {
		return uuid;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
