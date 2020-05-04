package br.com.logicmc.skywars.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkywarsWinnerEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	
	public SkywarsWinnerEvent() {}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}