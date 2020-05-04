package br.com.logicmc.skywars.listeners;

import br.com.logicmc.skywars.events.SkywarsPlayerEliminatedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/***
 * combat log, player last hit etc
 */
public class DamageTrackListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void killEvent(EntityDamageByEntityEvent event) {
        if(!event.isCancelled()) {
            if(event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if(event.getFinalDamage() >=player.getHealth()) {
                    SkywarsPlayerEliminatedEvent eliminatedEvent = new SkywarsPlayerEliminatedEvent(player.getUniqueId());
                    Bukkit.getPluginManager().callEvent(eliminatedEvent);

                    if(event.getDamager() instanceof Player){
                        Bukkit.broadcastMessage(ChatColor.GREEN + player.getName()+" ");
                    }
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void killEvent(EntityDamageEvent event) {
        if(!event.isCancelled()) {
            if(event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if(event.getFinalDamage() >=player.getHealth()) {
                    if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK){ // not entitydamagebyentity
                        SkywarsPlayerEliminatedEvent eliminatedEvent = new SkywarsPlayerEliminatedEvent(player.getUniqueId());
                        Bukkit.getPluginManager().callEvent(eliminatedEvent);
                    }
                }
            }
        }
    }
}
