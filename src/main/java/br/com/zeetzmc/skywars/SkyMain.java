package br.com.zeetzmc.skywars;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.PluginManager;

import br.com.zeetzmc.core.CorePlugin;
import br.com.zeetzmc.skywars.commands.Tempo;
import br.com.zeetzmc.skywars.game.GameLogic;
import br.com.zeetzmc.skywars.listeners.PhaseListeners;
import br.com.zeetzmc.skywars.listeners.PlayerListeners;
import br.com.zeetzmc.skywars.messages.SkyMessages;

public class SkyMain extends CorePlugin{
	
	
	private GameLogic gamelogic;
	
	@Override
	public void onEnable() {
		
		super.onEnable();
		
		World world = Bukkit.getWorlds().get(0); 
		world.setTime(6000L);
		world.getEntities().stream().filter(ent -> ent.getType() == EntityType.DROPPED_ITEM).forEach(Entity::remove);
		world.getLivingEntities().forEach(LivingEntity::remove);
		
		gamelogic = new GameLogic();
		gamelogic.startTimer(this);
		
		messagehandler.loadMessage(SkyMessages.GAME_START, this);
		
		getCommand("tempo").setExecutor(new Tempo(this));
		
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PhaseListeners(this), this);
		pm.registerEvents(new PlayerListeners(this), this);
	}
	
	
	
	@Override
	public void onDisable() {
		
		super.onDisable();
		
	}
	
	public GameLogic getGamelogic() {
		return gamelogic;
	}
	
	
}
