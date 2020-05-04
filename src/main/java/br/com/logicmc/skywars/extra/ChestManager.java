package br.com.logicmc.skywars.extra;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestManager {

	
	private final HashSet<Location> chestlocations = new HashSet<>();
	private final static ChestManager manager = new ChestManager();
	
	private ChestManager() {}


	public boolean loadChests(JavaPlugin plugin) {
		YamlFile yamlfile = new YamlFile("chests.yml");
		yamlfile.load(plugin);
		yamlfile.loopThroughSectionKeys("chests", key -> {
			Location loc = yamlfile.getLocation("chests."+key);
			Block block = loc.getBlock();
			if(block.getType() == Material.CHEST) {
				Chest chest = (Chest) loc.getBlock();
				chest.getInventory().clear();
				chestlocations.add(loc);
			}
		});
		System.out.println("[Chest Manager] "+chestlocations.size()+" baus carregados");
		return true;
	}


	public void fillChests() {

	}
	public void refilChests() {


	}
	public static ChestManager getInstance() {
		return manager;
	}
}
