package br.com.logicmc.skywars.game.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class GamePlayerManager {


	private final HashSet<UUID> cooldownplayers;
	private final HashMap<UUID, GamePlayer> gamestats;
	
	public GamePlayerManager() {
		gamestats= new HashMap<>();
		cooldownplayers = new HashSet<>();
	}
	
	public Collection<GamePlayer> values(){
		return gamestats.values();
	}
	public Set<UUID> keyset(){
		return gamestats.keySet();
	}
	
	public boolean existsStats(UUID uuid) {
		return gamestats.containsKey(uuid);
	}
	
	public void addGamePlayer(GamePlayer gameplayer) {
		gamestats.put(gameplayer.getUuid(), gameplayer);
	}
	
	public GamePlayer getGamePlayer(UUID uuid) {
		return gamestats.get(uuid);
	}
	public void removeGamePlayer(UUID uuid) {
		gamestats.remove(uuid);
	}
	
	public boolean isPlaying(UUID uuid) {
		return !gamestats.get(uuid).isSpectator();
	}
	public void addPlayerCountdown(UUID uuid) {
		cooldownplayers.add(uuid);
	}
	public void remotePlayerCountdown(UUID uuid) {
		cooldownplayers.remove(uuid);
	}
	public boolean inCountdown(UUID uuid) {
		return cooldownplayers.contains(uuid);
	}
	public Stream<UUID> getCooldownPlayersStream(){
		return cooldownplayers.stream();
	}
}
