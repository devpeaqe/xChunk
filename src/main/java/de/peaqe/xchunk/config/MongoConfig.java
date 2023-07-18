package de.peaqe.xchunk.config;
/*
 *
 *  Class by peaqe created in 2023
 *  Class: MongoConfig
 *
 *  Information's:
 *  Type: Java-Class
 *  Created: 17.07.2023 / 18:09
 *
 */

import de.peaqe.devapi.manager.ConfigManager;
import de.peaqe.xchunk.XChunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@SuppressWarnings(value = "unused")
public class MongoConfig {

    XChunk main = XChunk.getInstance();

    File file = new File(main.getDataFolder().getAbsolutePath(), "mongo_connection.yml");
    FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    ConfigManager manager = new ConfigManager(main, file, config);
    final String path = "Mongo Connection.";

    public void create() {

        manager.createNewFile();

        manager.save(this.path + "username", "USERNAME");
        manager.save(this.path + "password", "PASSWORD");
        manager.save(this.path + "database", "DATABASE");

    }

    public String get(String value) {
        return (String) manager.get(this.path + value);
    }


}
