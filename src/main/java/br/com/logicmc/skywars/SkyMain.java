package br.com.logicmc.skywars;

import java.io.File;
import java.lang.reflect.Field;
import java.util.UUID;

import br.com.logicmc.core.CorePlugin;
import br.com.logicmc.core.addons.ServerType;
import br.com.logicmc.core.system.mysql.MySQL;
import br.com.logicmc.core.system.redis.listener.DataListener;
import br.com.logicmc.skywars.commands.SetChest;
import br.com.logicmc.skywars.commands.SetSpawn;
import br.com.logicmc.skywars.extra.SchematicLoader;
import br.com.logicmc.skywars.extra.YamlFile;
import br.com.logicmc.skywars.listeners.SkywarsEventsListener;
import br.com.logicmc.skywars.messages.SkyMessages;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import br.com.logicmc.skywars.commands.StartGame;
import br.com.logicmc.skywars.commands.Tempo;
import br.com.logicmc.skywars.chest.ChestManager;
import br.com.logicmc.skywars.game.GameLogic;
import br.com.logicmc.skywars.game.player.SkywarsDataPlayer;
import br.com.logicmc.skywars.listeners.PhaseListener;
import br.com.logicmc.skywars.listeners.PlayerListener;
import org.bukkit.scoreboard.*;

public class SkyMain extends CorePlugin<SkywarsDataPlayer> {
	
	
	private GameLogic gamelogic;
	private Location spawnLocation;
	private YamlFile configfile;

	@Override
	public void onEnable() {
		
		super.onEnable();


		if(!getDataFolder().exists())
			getDataFolder().mkdirs();

		configfile = new YamlFile("config.yml");
		gamelogic = new GameLogic();

		if(!configfile.loadResource(this))
			Bukkit.getPluginManager().disablePlugin(this);


		if(!configfile.getConfig().getBoolean("loaded")) { //load the schematic map
			SchematicLoader lobby = new SchematicLoader().read(getResource("LobbySw.schematic"));
			if(lobby != null) {
				System.out.println("Pasting lobby schematic...");
				lobby.paste(new Location(Bukkit.getWorld("world"), 0, 100,0));
			}

			SchematicLoader schematicLoader = new SchematicLoader().read(new File(getDataFolder(), configfile.getConfig().getString("schematic")));
			if(schematicLoader == null) {
				System.out.println("Unable to load schematic");
				Bukkit.getPluginManager().disablePlugin(this);
			} else {
				System.out.println("Pasting map schematic...");
				schematicLoader.paste(new Location(Bukkit.getWorld("world"), 230, 50,230));
				configfile.getConfig().set("loaded", true);
				configfile.save();
			}
		} else {
			if(!ChestManager.getInstance().loadChests(this))  // chests are only loaded when map is loaded to avoid conflict
				Bukkit.getPluginManager().disablePlugin(this);

			spawnLocation = configfile.getLocation("spawn");
			configfile.loopThroughSectionKeys("ilhas", (key) -> {
				gamelogic.getIslands().add(configfile.getLocation("ilhas."+key));
			});
		}

		messagehandler.loadMessage(SkyMessages.GAME_START, this);

		gamelogic.startTimer(this);

		getCommand("setchest").setExecutor(new SetChest(this));
		getCommand("setspawn").setExecutor(new SetSpawn(this));
		getCommand("start").setExecutor(new StartGame(this));
		getCommand("tempo").setExecutor(new Tempo(this));
		
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PhaseListener(this), this);
		pm.registerEvents(new PlayerListener(this), this);
		pm.registerEvents(new SkywarsEventsListener(this), this);

		buildScoreboard();
		prepareWorld();

		if(pm.isPluginEnabled(this))
			node.listen(new DataListener(this), "exchange");
	}

    @Override
	public ServerType getServerType() {
		return ServerType.GAME;
	}

	public GameLogic getGamelogic() {
		return gamelogic;
	}


	@Override
	public void write(MySQL mysql, UUID uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SkywarsDataPlayer read(MySQL mysql, UUID uuid) {

		return null;
	}


	public Location getSpawnLocation() {
		return spawnLocation;
	}

	private void prepareWorld() {
		World world = Bukkit.getWorld("world");

		world.setTime(5000L);
		world.setStorm(false);
		world.setAutoSave(false);
		world.setThundering(false);
		world.setThunderDuration(0);
		world.setWeatherDuration(0);
		world.setDifficulty(Difficulty.PEACEFUL);
		world.getEntities().forEach(Entity::remove);
		world.getLivingEntities().forEach(LivingEntity::remove);
		world.setGameRuleValue("doDaylightCycle", "false");

	}

	private void buildScoreboard() {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		scoreboard.getObjectives().forEach(Objective::unregister);
		scoreboard.getTeams().forEach(Team::unregister);
		Objective objective = scoreboard.registerNewObjective("skywars","dummy");

		objective.setDisplayName("§b§lSkywars");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.getScore("§5").setScore(5);
		objective.getScore("§4").setScore(4);
		objective.getScore("§3").setScore(3);
		objective.getScore("§2").setScore(2);
		objective.getScore("§1").setScore(1);
		objective.getScore("§0").setScore(0);

		createTeam(scoreboard, "time", "§fTempo: ","§a00:00","§4");
		createTeam(scoreboard, "players", "§fOnline: ","§a-1","§2");
		createTeam(scoreboard, "site", "§7www.logic","§7mc.com.br","§0");

	}
	private void createTeam(Scoreboard scoreboard, String name, String prefix, String suffix, String entry) {
		Team team = scoreboard.registerNewTeam(name);
		team.setPrefix(prefix);
		team.setSuffix(suffix);
		team.addEntry(entry);
	}
	public void updateSuffix(Player player, String team, String suffix) {
		net.minecraft.server.v1_8_R3.Scoreboard scoreboard = ((CraftScoreboard)Bukkit.getScoreboardManager().getMainScoreboard()).getHandle();
		PacketPlayOutScoreboardTeam updatepacket = new PacketPlayOutScoreboardTeam(scoreboard.getTeam(team), 2);
		try {
			Field field = updatepacket.getClass().getDeclaredField("d");
			field.setAccessible(true);
			field.set(updatepacket, suffix);
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(updatepacket);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			player.sendMessage("failed to update scoreboard please report this to the administrator.");
		}
	}

	public YamlFile getConfigfile() {
		return configfile;
	}
}
