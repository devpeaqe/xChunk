package de.peaqe.xchunk.cache;
/*
 *
 *  Class by peaqe created in 2023
 *  Class: PlayerChunkCache
 *
 *  Information's:
 *  Type: Java-Class
 *  Created: 18.07.2023 / 11:30
 *
 */

import de.peaqe.devapi.objects.PlayerObject;
import de.peaqe.xchunk.manager.PlayerChunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

@SuppressWarnings(value = "unused")
public class PlayerChunkCache {

    private final Map<String, PlayerChunk> cache;
    private final Map<UUID, List<PlayerChunk>> playerChunkHomes;

    public PlayerChunkCache() {
        this.cache = new HashMap<>();
        this.playerChunkHomes = new HashMap<>();
    }

    public PlayerChunk getPlayerChunk(Player player) {
        String cacheKey = getPlayerChunkCacheKey(player, player.getLocation());
        PlayerChunk chunk = cache.get(cacheKey);

        if (chunk == null) {
            chunk = new PlayerChunk(new PlayerObject(player));
            cache.put(cacheKey, chunk);
        }

        return chunk;
    }

    public PlayerChunk getPlayerChunk(PlayerObject playerObject) {
        UUID playerUUID = playerObject.getUUID();
        int chunkX = playerObject.getLocation().getChunk().getX();
        int chunkZ = playerObject.getLocation().getChunk().getZ();
        String chunkID = chunkX + "." + chunkZ;
        String cacheKey = playerUUID + "_" + chunkID;

        PlayerChunk chunk = cache.get(cacheKey);
        if (chunk == null) {
            chunk = new PlayerChunk(playerObject);
            cache.put(cacheKey, chunk);
        }

        return chunk;
    }

    public PlayerChunk getPlayerChunk(Player player, Location location) {
        String cacheKey = getPlayerChunkCacheKey(player, location);
        PlayerChunk chunk = cache.get(cacheKey);

        if (chunk == null) {
            chunk = new PlayerChunk(new PlayerObject(player), location);
            cache.put(cacheKey, chunk);
        }

        return chunk;
    }

    public List<PlayerChunk> getPlayerChunkHomes(UUID playerUUID) {
        return playerChunkHomes.getOrDefault(playerUUID, new ArrayList<>());
    }

    public void setPlayerChunkHomes(UUID playerUUID, List<PlayerChunk> chunkHomes) {
        playerChunkHomes.put(playerUUID, chunkHomes);
    }

    public int getChunkAmount(UUID playerUUID) {
        return getPlayerChunkHomes(playerUUID).size();
    }

    public String getPlayerChunkCacheKey(Player player, Location location) {
        UUID playerUUID = player.getUniqueId();
        int chunkX = location.getChunk().getX();
        int chunkZ = location.getChunk().getZ();
        String chunkID = chunkX + "." + chunkZ;
        return playerUUID + "_" + chunkID;
    }

    public void reloadPlayerChunk(Player player, Location location) {
        String cacheKey = getPlayerChunkCacheKey(player, location);
        cache.remove(cacheKey);
    }
}
