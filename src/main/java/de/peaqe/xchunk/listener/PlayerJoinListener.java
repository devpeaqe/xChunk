package de.peaqe.xchunk.listener;
/*
 *
 *  Class by peaqe created in 2023
 *  Class: PlayerJoinListener
 *
 *  Information's:
 *  Type: Java-Class
 *  Created: 18.07.2023 / 09:27
 *
 */

import de.peaqe.devapi.helper.LoggerHelper;
import de.peaqe.devapi.objects.PlayerObject;
import de.peaqe.xchunk.provider.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@SuppressWarnings(value = "unused")
public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        PlayerObject player = new PlayerObject(event.getPlayer());
        new PlayerManager().updatePlayer(player);

        LoggerHelper helper = new LoggerHelper(Bukkit.getLogger());
        helper.info("Player " + player.getName() + " was successfully updated in MongoDB!");

    }

}
