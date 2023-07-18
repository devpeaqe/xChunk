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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings(value = "unused")
public class PlayerChunkCache {

    private final Map<String, PlayerChunk> cache;

    public PlayerChunkCache() {
        this.cache = new HashMap<>();
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

    public PlayerChunk getPlayerChunk(Player player, Location location) {
        String cacheKey = getPlayerChunkCacheKey(player, location);
        PlayerChunk chunk = cache.get(cacheKey);

        if (chunk == null) {
            chunk = new PlayerChunk(new PlayerObject(player), location);
            cache.put(cacheKey, chunk);
        }

        return chunk;
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
