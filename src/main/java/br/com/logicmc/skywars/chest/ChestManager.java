package br.com.logicmc.skywars.chest;

import java.util.HashSet;

import br.com.logicmc.skywars.extra.YamlFile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestManager {

	
	private final HashSet<SkyChest> chestlocations = new HashSet<>();
	private final YamlFile yamlfile = new YamlFile("chests.yml");
	private final int NORMAL_CHEST = 0,FEAST_CHEST = 1;
	private final static ChestManager manager = new ChestManager();
	
	private ChestManager() {}


	public boolean loadChests(JavaPlugin plugin) {
		yamlfile.load(plugin);
		yamlfile.loopThroughSectionKeys("chests", key -> {
			Location loc = yamlfile.getLocation("chests."+key);
			int type = yamlfile.getConfig().getInt("chests."+key+".type");
			Block block = loc.getBlock();
			if(block.getType() == Material.CHEST) {
				Chest chest = (Chest) loc.getBlock().getState();
				Inventory inventory = chest.getInventory();
				inventory.clear();
				if(type == NORMAL_CHEST)
					inventory.setItem(0, new ItemStack(Material.IRON_SWORD));
				else
					inventory.setItem(0, new ItemStack(Material.DIAMOND_SWORD));
				chestlocations.add(new SkyChest(loc, type));
			}
		});
		System.out.println("[Chest Manager] "+chestlocations.size()+" baus carregados");
		return true;
	}

	public void addChest(int type , Location location) {
		yamlfile.getConfig().set("chests.chest"+chestlocations.size()+".type", type);
		yamlfile.setLocation("chests.chest"+chestlocations.size(), location);
		chestlocations.add(new SkyChest(location, 1));
	}
	public void fillChests() {

	}
	public void refilChests() {


	}
	public static ChestManager getInstance() {
		return manager;
	}
}
