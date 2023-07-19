package de.peaqe.xchunk.utils;
/*
 *
 *  Class by peaqe created in 2023
 *  Class: TeleportUitls
 *
 *  Information's:
 *  Type: Java-Class
 *  Created: 19.07.2023 / 11:27
 *
 */

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@SuppressWarnings(value = "unused")
public class TeleportUtils {


    public Location getChunkLocation(Chunk chunk) {
        World world = chunk.getWorld();

        int centerX = (chunk.getX() << 4) + 8;
        int centerZ = (chunk.getZ() << 4) + 8;
        int highestY = world.getHighestBlockYAt(centerX, centerZ);

        int dx = 0;
        int dz = 0;

        if (centerX < chunk.getX() << 4) {
            dx = -2;
        } else if (centerX > (chunk.getX() << 4) + 15) {
            dx = 2;
        }
        if (centerZ < chunk.getZ() << 4) {
            dz = -2;
        } else if (centerZ > (chunk.getZ() << 4) + 15) {
            dz = 2;
        }

        return new Location(world, centerX + dx, highestY + 1.5, centerZ + dz);
    }


}
