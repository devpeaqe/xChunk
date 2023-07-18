package de.peaqe.xchunk.listener;
/*
 *
 *  Class by peaqe created in 2023
 *  Class: ChunkEventRegisterer
 *
 *  Information's:
 *  Type: Java-Class
 *  Created: 18.07.2023 / 09:47
 *
 */

import de.peaqe.devapi.objects.PlayerObject;
import de.peaqe.xchunk.cache.PlayerChunkCache;
import de.peaqe.xchunk.events.ChunkBlockBreakEvent;
import de.peaqe.xchunk.events.ChunkEnterEvent;
import de.peaqe.xchunk.manager.ChunkRole;
import de.peaqe.xchunk.manager.PlayerChunk;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

@SuppressWarnings(value = "unused")
public class ChunkEventRegisterer implements Listener {

    PlayerChunkCache cache;

    public ChunkEventRegisterer(PlayerChunkCache cache) {
        this.cache = cache;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {

        if (event.isCancelled()) return;
        if (event.getPlayer().hasPermission("chunks.break.admin")) return;

        Player player = event.getPlayer();
        Block block = event.getBlock();
        Chunk schunk = block.getLocation().getChunk();

        ChunkBlockBreakEvent custom_event = new ChunkBlockBreakEvent(player, schunk, block);
        Bukkit.getPluginManager().callEvent(custom_event);

        Player raw_breaker = event.getPlayer();
        PlayerObject breaker = new PlayerObject(raw_breaker);

        PlayerChunk chunk = cache.getPlayerChunk(player, block.getLocation());

        if (!(chunk.getRole().equals(ChunkRole.OWNER) || chunk.getRole().equals(ChunkRole.TRUSTED))) {
            event.setCancelled(true);
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(PlayerMoveEvent event) {
        if (event.getPlayer().hasPermission("chunks.enter.admin")) return;

        // TODO: Teleport the Player to the Chunk border

        Player player = event.getPlayer();
        Chunk currentChunk = player.getLocation().getChunk();
        UUID playerUUID = player.getUniqueId();

        ChunkEnterEvent custom_event = new ChunkEnterEvent(player, currentChunk);
        Bukkit.getPluginManager().callEvent(custom_event);

        PlayerObject joined = new PlayerObject(player);
        PlayerChunk playerChunk = cache.getPlayerChunk(player);

        if (playerChunk.getRole().equals(ChunkRole.BANNED)) {
            event.setCancelled(true);
        }

    }

    @SuppressWarnings(value = "all")
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getPlayer().hasPermission("chunks.interact.admin")) return;
        if (event.getClickedBlock() == null) return;

        Player player = event.getPlayer();
        Chunk chunk = player.getLocation().getChunk();

        ChunkEnterEvent custom_event = new ChunkEnterEvent(player, chunk);
        Bukkit.getPluginManager().callEvent(custom_event);

        PlayerObject joined = new PlayerObject(player);
        PlayerChunk playerChunk = cache.getPlayerChunk(player, event.getClickedBlock().getLocation());

        if (!(playerChunk.getRole().equals(ChunkRole.OWNER) || playerChunk.getRole().equals(ChunkRole.TRUSTED))) {
            event.setCancelled(true);
        }
    }

}
