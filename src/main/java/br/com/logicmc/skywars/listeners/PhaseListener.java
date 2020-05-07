package br.com.logicmc.skywars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import br.com.logicmc.skywars.game.addons.GamePhase;
import br.com.logicmc.skywars.SkyMain;

/***
 * All Game phase listeners are to be executed first
 * @author useless
 *
 */
public class PhaseListener implements Listener{
	
	
	private final SkyMain skywars;
	
	public PhaseListener(SkyMain skywars) {
		this.skywars=skywars;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void entitydamage(EntityDamageEvent event) {
		event.setCancelled(skywars.getGamelogic().getCurrentphase() == GamePhase.WAITING);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void entitydamage(EntityDamageByEntityEvent event) {
		event.setCancelled(skywars.getGamelogic().getCurrentphase() == GamePhase.WAITING);
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockdamage(BlockDamageEvent event) {
		event.setCancelled(skywars.getGamelogic().getCurrentphase() == GamePhase.WAITING);
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockbuild(BlockBreakEvent event) {
		event.setCancelled(skywars.getGamelogic().getCurrentphase() == GamePhase.WAITING);
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockbreal(BlockPlaceEvent event) {
		event.setCancelled(skywars.getGamelogic().getCurrentphase() == GamePhase.WAITING);
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockbreal(PlayerPickupItemEvent event) {
		event.setCancelled(skywars.getGamelogic().getCurrentphase() == GamePhase.WAITING);
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockbreal(PlayerDropItemEvent event) {
		event.setCancelled(skywars.getGamelogic().getCurrentphase() == GamePhase.WAITING);
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void foodlevelchange(FoodLevelChangeEvent event) {
		event.setCancelled(skywars.getGamelogic().getCurrentphase() == GamePhase.WAITING);
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockbreal(EntitySpawnEvent event) {
		event.setCancelled(skywars.getGamelogic().getCurrentphase() == GamePhase.WAITING);
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockbreal(CreatureSpawnEvent event) {
		event.setCancelled(event.getSpawnReason() == SpawnReason.CUSTOM);
	}
}
