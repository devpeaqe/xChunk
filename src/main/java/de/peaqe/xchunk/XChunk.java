package de.peaqe.xchunk;

import de.peaqe.devapi.DevAPI;
import de.peaqe.devapi.hooks.CommandHook;
import de.peaqe.xchunk.commands.ChunkCommand;
import de.peaqe.xchunk.config.MongoConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class XChunk extends JavaPlugin {

    private static XChunk instance;
    private DevAPI devAPI;

    @Override
    public void onLoad() {
        devAPI = DevAPI.getInstance();
    }

    @Override
    public void onEnable() {

        instance = this;

        // Create Config
        //new MongoConfig().create();

        // Code...
        CommandHook commandHook = new CommandHook();
        commandHook.registerCommand("chunk", new ChunkCommand());

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
