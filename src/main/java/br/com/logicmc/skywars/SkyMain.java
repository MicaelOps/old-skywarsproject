package br.com.logicmc.skywars;

import java.lang.reflect.Field;
import java.util.UUID;

import br.com.logicmc.core.CorePlugin;
import br.com.logicmc.core.addons.ServerType;
import br.com.logicmc.core.system.mysql.MySQL;
import br.com.logicmc.core.system.redis.listener.DataListener;
import br.com.logicmc.core.system.redis.packet.PacketExecutor;
import br.com.logicmc.core.system.redis.packet.PacketManager;
import br.com.logicmc.core.system.redis.packet.RedisPacket;
import br.com.logicmc.skywars.extra.VoidWorld;
import br.com.logicmc.skywars.listeners.SkywarsEventsListener;
import br.com.logicmc.skywars.messages.SkyMessages;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginManager;

import br.com.logicmc.skywars.commands.StartGame;
import br.com.logicmc.skywars.commands.Tempo;
import br.com.logicmc.skywars.extra.ChestManager;
import br.com.logicmc.skywars.game.GameLogic;
import br.com.logicmc.skywars.game.player.SkywarsDataPlayer;
import br.com.logicmc.skywars.listeners.PhaseListener;
import br.com.logicmc.skywars.listeners.PlayerListener;
import org.bukkit.scoreboard.*;

public class SkyMain extends CorePlugin<SkywarsDataPlayer> {
	
	
	private GameLogic gamelogic;
	
	@Override
	public void onEnable() {
		
		super.onEnable();
		
		Bukkit.getWorlds().forEach(world -> Bukkit.unloadWorld(world, false));
		Bukkit.createWorld(new VoidWorld("abyss"));

		Bukkit.getWorld("abyss").setSpawnLocation(0, 100 , 0);
		if(!ChestManager.getInstance().loadChests(this)) {
			Bukkit.shutdown();
		}
		
		messagehandler.loadMessage(SkyMessages.GAME_START, this);
		
		gamelogic = new GameLogic();
		gamelogic.startTimer(this);
		
		getCommand("start").setExecutor(new StartGame(this));
		getCommand("tempo").setExecutor(new Tempo(this));
		
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PhaseListener(this), this);
		pm.registerEvents(new PlayerListener(this), this);
		pm.registerEvents(new SkywarsEventsListener(this), this);

		buildScoreboard();

		PacketManager.getInstance().registerPacket(2001, (packet, plugin) -> {
			JsonObject data = new JsonObject();
			data.addProperty("nick", "wing of icarus");
			PacketManager.getInstance().sendPacket(plugin.node, "exchange", packet.returnPacket(2001, "lobby", data));
		});
		node.listen(new DataListener(this), "exchange");
	}
	
	
	
	@Override
	public void onDisable() {
		super.onDisable();
		
	}

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidWorld.EmptyChunkGenerator();
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

		createTeam(scoreboard, "time", "§fTempo:","§a00:00","§4");
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
}
