package br.com.logicmc.skywars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

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
		event.setCancelled(!skywars.getGamelogic().getCurrentphase().isDamageAllowed());
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void entitydamage(EntityDamageByEntityEvent event) {
		event.setCancelled(!skywars.getGamelogic().getCurrentphase().isDamageAllowed());
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockdamage(BlockDamageEvent event) {
		event.setCancelled(!skywars.getGamelogic().getCurrentphase().isBuildAllowed());
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockbuild(BlockBreakEvent event) {
		event.setCancelled(!skywars.getGamelogic().getCurrentphase().isBuildAllowed());
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockbreal(BlockPlaceEvent event) {
		event.setCancelled(!skywars.getGamelogic().getCurrentphase().isBuildAllowed());
	}

}
