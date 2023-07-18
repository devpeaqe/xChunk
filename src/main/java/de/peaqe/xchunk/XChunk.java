package de.peaqe.xchunk;

import de.peaqe.devapi.DevAPI;
import de.peaqe.devapi.hooks.CommandHook;
import de.peaqe.devapi.hooks.EventHook;
import de.peaqe.xchunk.cache.PlayerChunkCache;
import de.peaqe.xchunk.commands.ChunkCommand;
import de.peaqe.xchunk.events.ChunkBlockBreakEvent;
import de.peaqe.xchunk.listener.PlayerJoinListener;
import de.peaqe.xchunk.listener.ChunkEventRegisterer;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class XChunk extends JavaPlugin {

    private static XChunk instance;
    private DevAPI devAPI;

    @Override
    public void onLoad() {
        devAPI = DevAPI.getInstance();
    }

    public PlayerChunkCache chunkCache;

    /**
     * TODO; Fix block break internal error
     * TODO; Add /chunk tp command
     * TODO; Add /chunk visit command
     * TODO; Fix player damage by Player on Chunks
     * TODO; Add Chunk flags? (IDEA)
     */

    @Override
    public void onEnable() {

        instance = this;

        // Create Config
        //new MongoConfig().create();

        chunkCache = new PlayerChunkCache();

        // Code...
        CommandHook commandHook = new CommandHook();
        commandHook.registerCommand("chunk", new ChunkCommand(this, chunkCache));

        EventHook eventHook = new EventHook();
        eventHook.registerListener(new PlayerJoinListener(), this);
        eventHook.registerListener(new ChunkEventRegisterer(chunkCache), this);

    }

    public final String prefix = "§c§lCHUNKS §8• §7";

    @Override
    public void onDisable() {

    }

    public DevAPI getAPI() {
        return devAPI;
    }

    public static XChunk getInstance() {
        return instance;
    }

}
