package br.com.logicmc.skywars.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.com.logicmc.skywars.SkyMain;
import br.com.logicmc.skywars.events.SkywarsPlayerEliminatedEvent;

public class SkywarsEventsListener implements Listener{

	private final SkyMain skywars;
	
	public SkywarsEventsListener(SkyMain skywars) {
		this.skywars=skywars;
	}
	
	@EventHandler
	public void onremove(SkywarsPlayerEliminatedEvent event) {
		Player player = Bukkit.getPlayer(event.getGameplayer().getUuid());
		event.getGameplayer().setSpectator(true);
		
		skywars.utils.cleanPlayer(player);
		skywars.utils.clearChat(player);
		
		player.setGameMode(GameMode.SPECTATOR);
		
		//teleport to the top
		
	}
}
