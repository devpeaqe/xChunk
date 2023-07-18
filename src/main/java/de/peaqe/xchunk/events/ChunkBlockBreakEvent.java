package de.peaqe.xchunk.events;
/*
 *
 *  Class by peaqe created in 2023
 *  Class: ChunkBlockBreakEvent
 *
 *  Information's:
 *  Type: Java-Class
 *  Created: 18.07.2023 / 09:44
 *
 */

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings(value = "unused")
public class ChunkBlockBreakEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Chunk chunk;
    private final Block block;
    private boolean cancelled;

    public ChunkBlockBreakEvent(Player player, Chunk chunk, Block block) {
        this.player = player;
        this.chunk = chunk;
        this.block = block;
    }

    public Player getPlayer() {
        return player;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
