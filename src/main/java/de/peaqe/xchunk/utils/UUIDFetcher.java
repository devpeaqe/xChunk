package de.peaqe.xchunk.utils;
/*
 *
 *  Class by ChatGPT created in 2023
 *  Class: UUIDFetcher
 *
 *  Information's:
 *  Type: Java-Class
 *  Created: 17.07.2023 / 18:37
 *
 */

import de.peaqe.xchunk.provider.PlayerManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@SuppressWarnings(value = "unused")
public class UUIDFetcher {

    /**
     * Fetches the UUID of a player based on their Minecraft username.
     *
     * @param username The Minecraft username of the player.
     * @return The UUID of the player, or null if the player was not found.
     */
    public static UUID fetchUUID(String username) {
        PlayerManager manager = new PlayerManager();
        return manager.getPlayerUUID(username);
    }


    /**
     * Fetches the Name of a player based on their Minecraft uuid.
     *
     * @param uuid The Minecraft uuid of the player.
     * @return The Name of the player, or null if the player was not found.
     */
    public static String fetchUsername(UUID uuid) {
        PlayerManager manager = new PlayerManager();
        return manager.getPlayerName(uuid);
    }

}
