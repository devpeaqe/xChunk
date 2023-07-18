package de.peaqe.xchunk.provider;
/*
 *
 *  Class by peaqe created in 2023
 *  Class: MongoProvider
 *
 *  Information's:
 *  Type: Java-Class
 *  Created: 17.07.2023 / 17:45
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
import de.peaqe.xchunk.manager.ChunkUser;
import de.peaqe.xchunk.utils.UUIDFetcher;
import org.bson.Document;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.*;

@SuppressWarnings(value = "unused")
public class DatabaseProvider {

    XChunk main = XChunk.getInstance();
    DevAPI devAPI = main.getAPI();

    MongoClient mongoClient;
    private final MongoCollection<Document> collection;

    @SuppressWarnings(value = "deprecation")
    public DatabaseProvider() {

        MongoCredential mongoCredential = MongoCredential.createCredential("noviaplugins", "noviamc", "noviapluginpassword".toCharArray());
        mongoClient = new MongoClient(new ServerAddress("localhost", 27017), List.of(mongoCredential));

        MongoDatabase database = mongoClient.getDatabase("noviamc");
        String collectionName = "player_chunks";
        collection = database.getCollection(collectionName);

    }

    public void saveChunk(Player player, Chunk chunk) {

        var player_name = player.getName();
        var player_uuid = player.getUniqueId();

        var chunk_id = chunk.getX() + "." + chunk.getZ();

        Document document = new Document("chunk_autor_uuid", player_uuid.toString())
                .append("chunk_autor_name", player_name)
                .append("chunk_id", chunk_id);

        if (collection.find(new Document("chunk_autor_uuid", player_uuid.toString())).first() != null) {
            collection.findOneAndDelete(new Document("player_uuid", player_uuid.toString()));
        }

        collection.insertOne(document);

    }

    public void saveChunk(PlayerObject player, Chunk chunk) {

        var player_name = player.getName();
        var player_uuid = player.getUUID();

        var chunk_id = chunk.getX() + "." + chunk.getZ();

        Document document = new Document("chunk_autor_uuid", player_uuid.toString())
                .append("chunk_autor_name", player_name)
                .append("chunk_id", chunk_id);

        if (collection.find(new Document("chunk_autor_uuid", player_uuid.toString())).first() != null) {
            collection.findOneAndDelete(new Document("player_uuid", player_uuid.toString()));
        }

        collection.insertOne(document);

    }

    public String getChunkID(UUID uuid) {

        Document document = collection.find(new Document("chunk_autor_uuid", uuid.toString())).first();
        if (document == null) return null;

        return document.getString("chunk_id");

    }

    public String chunkAuthorName(String chunkID) {

        Document document = collection.find(new Document("chunk_id", chunkID)).first();
        if (document == null) return null;

        return document.getString("chunk_autor_name");

    }

    public UUID chunkAuthorUuid(String chunkID) {

        Document document = collection.find(new Document("chunk_id", chunkID)).first();
        if (document == null) return null;

        return UUID.fromString(document.getString("chunk_autor_uuid"));

    }

    public boolean chunkClaimed(String chunkID) {
        return this.chunkAuthorUuid(chunkID) != null;
    }

    public final String getChunkID(Chunk chunk) {
        return chunk.getX() + "." + chunk.getZ();
    }

    public void trustPlayerToChunk(String playerName, Chunk chunk) {
        Document document = collection.find(new Document("chunk_id", this.getChunkID(chunk))).first();
        if (document == null) return;

        UUID playerUUID = UUIDFetcher.fetchUUID(playerName);
        if (playerUUID == null) {
            System.out.println("Failed to fetch UUID for player " + playerName);
            return;
        }

        List<UUID> trustedPlayers = document.getList("chunk_trusted", UUID.class, new ArrayList<>());
        trustedPlayers.add(playerUUID);

        var chunkAutorUUID = document.getString("chunk_autor_uuid");
        var chunkAutorName = document.getString("chunk_autor_name");
        var chunkId = this.getChunkID(chunk);

        Document updatedDocument = new Document("chunk_autor_uuid", chunkAutorUUID)
                .append("chunk_autor_name", chunkAutorName)
                .append("chunk_id", chunkId)
                .append("chunk_trusted", trustedPlayers);

        collection.replaceOne(document, updatedDocument);
    }

    public void removePlayerFromChunk(String playerName, Chunk chunk) {
        Document document = collection.find(new Document("chunk_id", this.getChunkID(chunk))).first();
        if (document == null) return;

        UUID playerUUID = UUIDFetcher.fetchUUID(playerName);
        if (playerUUID == null) {
            System.out.println("Failed to fetch UUID for player " + playerName);
            return;
        }

        List<UUID> trustedPlayers = document.getList("chunk_trusted", UUID.class, new ArrayList<>());
        trustedPlayers.remove(playerUUID);

        List<UUID> bannedPlayers = document.getList("chunk_banned", UUID.class, new ArrayList<>());
        bannedPlayers.remove(playerUUID);

        var chunkAutorUUID = document.getString("chunk_autor_uuid");
        var chunkAutorName = document.getString("chunk_autor_name");
        var chunkId = this.getChunkID(chunk);

        Document updatedDocument = new Document("chunk_autor_uuid", chunkAutorUUID)
                .append("chunk_autor_name", chunkAutorName)
                .append("chunk_id", chunkId)
                .append("chunk_trusted", trustedPlayers)
                .append("chunk_banned", bannedPlayers);

        collection.replaceOne(document, updatedDocument);
    }

    public void banPlayerFromChunk(String playerName, Chunk chunk) {
        Document document = collection.find(new Document("chunk_id", this.getChunkID(chunk))).first();
        if (document == null) return;

        UUID playerUUID = UUIDFetcher.fetchUUID(playerName);
        if (playerUUID == null) {
            System.out.println("Failed to fetch UUID for player " + playerName);
            return;
        }

        List<UUID> bannedPlayers = document.getList("chunk_banned", UUID.class, new ArrayList<>());
        bannedPlayers.add(playerUUID);

        var chunkAutorUUID = document.getString("chunk_autor_uuid");
        var chunkAutorName = document.getString("chunk_autor_name");
        var chunkId = this.getChunkID(chunk);

        Document updatedDocument = new Document("chunk_autor_uuid", chunkAutorUUID)
                .append("chunk_autor_name", chunkAutorName)
                .append("chunk_id", chunkId)
                .append("chunk_banned", bannedPlayers);

        collection.replaceOne(document, updatedDocument);
    }

    public List<UUID> getTrustedPlayers(Chunk chunk) {
        Document document = collection.find(new Document("chunk_id", this.getChunkID(chunk))).first();
        if (document == null) return new ArrayList<>();
        return document.getList("chunk_trusted", UUID.class, new ArrayList<>());
    }

    public List<UUID> getBannedPlayers(Chunk chunk) {
        Document document = collection.find(new Document("chunk_id", this.getChunkID(chunk))).first();
        if (document == null) return new ArrayList<>();
        return document.getList("chunk_banned", UUID.class, new ArrayList<>());
    }

    public void updateAuthorUUID(Chunk chunk, UUID authorUUID) {
        Document filter = new Document("chunk_id", this.getChunkID(chunk));
        Document update = new Document("$set", new Document("chunk_autor_uuid", authorUUID.toString()));
        collection.updateOne(filter, update);
    }

    public void updateAuthorName(Chunk chunk, String authorName) {
        Document filter = new Document("chunk_id", this.getChunkID(chunk));
        Document update = new Document("$set", new Document("chunk_autor_name", authorName));
        collection.updateOne(filter, update);
    }

    public void updateChunkID(Chunk chunk, String chunkID) {
        Document filter = new Document("chunk_id", this.getChunkID(chunk));
        Document update = new Document("$set", new Document("chunk_id", chunkID));
        collection.updateOne(filter, update);
    }

    public void updateTrustedPlayers(Chunk chunk, List<UUID> trustedPlayers) {
        Document filter = new Document("chunk_id", this.getChunkID(chunk));
        Document update = new Document("$set", new Document("chunk_trusted", trustedPlayers));
        collection.updateOne(filter, update);
    }

    public void updateBannedPlayers(Chunk chunk, List<UUID> bannedPlayers) {
        Document filter = new Document("chunk_id", this.getChunkID(chunk));
        Document update = new Document("$set", new Document("chunk_banned", bannedPlayers));
        collection.updateOne(filter, update);
    }

}
