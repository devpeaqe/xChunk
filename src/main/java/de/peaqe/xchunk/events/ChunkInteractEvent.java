package de.peaqe.xchunk.events;
/*
 *
 *  Class by peaqe created in 2023
 *  Class: ChunkEnterEvent
 *
 *  Information's:
 *  Type: Java-Class
 *  Created: 18.07.2023 / 09:48
 *
 */

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings(value = "unused")
public class ChunkInteractEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Chunk chunk;
    private boolean cancelled;

    public ChunkInteractEvent(Player player, Chunk chunk) {
        this.player = player;
        this.chunk = chunk;
    }

    public Player getPlayer() {
        return player;
    }

    public Chunk getChunk() {
        return chunk;
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
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
