package de.peaqe.xchunk.provider;
/*
 *
 *  Class by peaqe created in 2023
 *  Class: PlayerManager
 *
 *  Information's:
 *  Type: Java-Class
 *  Created: 18.07.2023 / 09:22
 *
 */

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.peaqe.devapi.DevAPI;
import de.peaqe.devapi.objects.PlayerObject;
import de.peaqe.xchunk.XChunk;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

@SuppressWarnings(value = "unused")
public class PlayerManager {

    XChunk main = XChunk.getInstance();
    DevAPI devAPI = main.getAPI();

    MongoClient mongoClient;
    private final MongoCollection<Document> collection;

    @SuppressWarnings(value = "deprecation")
    public PlayerManager() {

        MongoCredential mongoCredential = MongoCredential.createCredential("noviaplugins", "noviamc", "noviapluginpassword".toCharArray());
        mongoClient = new MongoClient(new ServerAddress("localhost", 27017), List.of(mongoCredential));

        MongoDatabase database = mongoClient.getDatabase("noviamc");
        String collectionName = "player_manager";
        collection = database.getCollection(collectionName);

    }

    public void updatePlayer(PlayerObject player) {

        var player_name = player.getName();
        var player_uuid = player.getUUID();

        Document document = new Document("uuid", player_uuid.toString()).append("name", player_name);

        if (collection.find(new Document("uuid", player_uuid.toString())).first() != null) {
            collection.deleteOne(new Document("uuid", player_uuid.toString()));
        }

        collection.insertOne(document);

    }

    public UUID getPlayerUUID(String playerName) {

        Document document = collection.find(new Document("name", playerName)).first();
        if (document == null) return null;

        return UUID.fromString(document.getString("uuid"));

    }

    public String getPlayerName(UUID playerUUID) {

        Document document = collection.find(new Document("uuid", playerUUID.toString())).first();
        if (document == null) return null;

        return document.getString("name");

    }

}
