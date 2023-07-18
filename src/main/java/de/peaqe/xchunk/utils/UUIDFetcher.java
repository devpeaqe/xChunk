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

    private static final String MOJANG_API_BASE_URL = "https://api.mojang.com/users/profiles/minecraft/";

    /**
     * Fetches the UUID of a player based on their Minecraft username.
     *
     * @param username The Minecraft username of the player.
     * @return The UUID of the player, or null if the player was not found.
     */
    public static UUID fetchUUID(String username) {
        try {
            URL url = new URL(MOJANG_API_BASE_URL + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                reader.close();

                // Print the API response
                System.out.println("API Response: " + response);

                // Check if the response is empty
                if (response.isEmpty()) {
                    System.out.println("Player with username '" + username + "' not found.");
                    return null;
                }

                // Parse the JSON response to get the UUID
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(response);
                String uuidString = (String) jsonObject.get("id");
                return UUID.fromString(uuidString);
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                System.out.println("Player with username '" + username + "' not found.");
                return null;
            } else {
                System.out.println("Error while fetching UUID. Response code: " + responseCode);
                return null;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static String fetchUsername(UUID uuid) {
        try {
            URL url = new URL("https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "") + "/names");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response to get the username
                JSONParser parser = new JSONParser();
                JSONArray jsonArray = (JSONArray) parser.parse(response.toString());
                JSONObject jsonObject = (JSONObject) jsonArray.get(jsonArray.size() - 1); // Get the latest username
                return jsonObject.get("name").toString();
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                System.out.println("Player with UUID '" + uuid + "' not found.");
                return null;
            } else {
                System.out.println("Error while fetching username. Response code: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
