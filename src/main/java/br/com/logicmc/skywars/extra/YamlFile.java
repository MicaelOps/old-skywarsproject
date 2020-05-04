package br.com.logicmc.skywars.extra;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class YamlFile {

	private final String name;
	private FileConfiguration config;
	
	public YamlFile(String name) {
		this.name = name;
	}
	public boolean load(JavaPlugin plugin) {

		if(!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdirs();
		File file = new File(plugin.getDataFolder(), name);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), name));
		return true;
	}

	public void loopThroughSectionKeys(String section, Consumer<? super String> method) {
		ConfigurationSection configsection = config.getConfigurationSection(section);
		if(configsection != null)
			configsection.getKeys(false).forEach(method);
	}
	public Location getLocation(String path) {
		if (config !=null) {
			Location location = new Location(Bukkit.getWorlds().get(0), config.getDouble(path+".x"),config.getDouble(path+".y"),config.getDouble(path+".z"), config.getFloat(path+".yaw"), config.getFloat(path+".pitch") );
			return location;
		}
		return null;
	}
}
