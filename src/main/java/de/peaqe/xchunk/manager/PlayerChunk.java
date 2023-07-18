package de.peaqe.xchunk.manager;
/*
 *
 *  Class by peaqe created in 2023
 *  Class: PlayerChunk
 *
 *  Information's:
 *  Type: Java-Class
 *  Created: 17.07.2023 / 17:45
 *
 */

import de.peaqe.devapi.objects.PlayerObject;
import de.peaqe.xchunk.provider.DatabaseProvider;
import de.peaqe.xchunk.utils.UUIDFetcher;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings(value = "unused")
public class PlayerChunk {

    private PlayerObject player;
    private UUID authorUUID;
    private String authorName;
    private String chunkID;
    private List<UUID> trustedPlayers;
    private List<UUID> bannedPlayers;
    private final boolean claimed;
    private ChunkRole role;

    public PlayerChunk(PlayerObject player) {

        DatabaseProvider provider = new DatabaseProvider();

        this.player = player;

        this.authorUUID = provider.chunkAuthorUuid(provider.getChunkID(player.getLocation().getChunk()));
        this.authorName = provider.chunkAuthorName(provider.getChunkID(player.getLocation().getChunk()));

        this.chunkID = provider.getChunkID(player.getLocation().getChunk());
        this.trustedPlayers = provider.getTrustedPlayers(player.getLocation().getChunk());
        this.bannedPlayers = provider.getBannedPlayers(player.getLocation().getChunk());

        if (player.getUUID().equals(this.authorUUID)) {
            this.role = ChunkRole.OWNER;
        } else if (this.isPlayerTrusted(player.getUUID())) {
            this.role = ChunkRole.TRUSTED;
        } else if (this.isPlayerBanned(player.getUUID())) {
            this.role = ChunkRole.BANNED;
        } else {
            this.role = ChunkRole.VISITOR;
        }

        this.claimed = provider.chunkClaimed(chunkID);

    }

    public PlayerChunk(PlayerObject player, Location location) {

        DatabaseProvider provider = new DatabaseProvider();
        var chunk = location.getChunk();

        this.authorUUID = provider.chunkAuthorUuid(provider.getChunkID(chunk));
        this.authorName = provider.chunkAuthorName(provider.getChunkID(chunk));

        this.chunkID = provider.getChunkID(chunk);
        this.trustedPlayers = provider.getTrustedPlayers(chunk);
        this.bannedPlayers = provider.getBannedPlayers(chunk);

        if (player.getUUID().equals(this.authorUUID)) {
            this.role = ChunkRole.OWNER;
        } else if (this.isPlayerTrusted(player.getUUID())) {
            this.role = ChunkRole.TRUSTED;
        } else if (this.isPlayerBanned(player.getUUID())) {
            this.role = ChunkRole.BANNED;
        } else {
            this.role = ChunkRole.VISITOR;
        }

        this.claimed = provider.chunkClaimed(chunkID);

    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean value) {
        if (value) {
            this.claim();
        } else {
            DatabaseProvider provider = new DatabaseProvider();
            provider.unclaimChunk(this.getChunkID());
        }
    }


    private PlayerObject getPlayer() {
        if (player == null) {
            throw new IllegalStateException("Player is null in PlayerChunk.");
        }
        return player;
    }


    public Chunk getChunk() {
        return this.getPlayer().getLocation().getChunk();
    }

    public Location getLocation() {
        return this.getPlayer().getLocation();
    }

    public UUID getAuthorUUID() {
        return authorUUID;
    }

    public void setAuthorUUID(UUID authorUUID) {
        this.authorUUID = authorUUID;
        DatabaseProvider provider = new DatabaseProvider();
        provider.updateAuthorUUID(this.getChunk(), authorUUID);
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
        DatabaseProvider provider = new DatabaseProvider();
        provider.updateAuthorName(this.getChunk(), authorName);
    }

    public String getChunkID() {
        return chunkID;
    }

    public void setChunkID(String chunkID) {
        this.chunkID = chunkID;
        DatabaseProvider provider = new DatabaseProvider();
        provider.updateChunkID(this.getChunk(), chunkID);
    }

    public List<UUID> getTrustedPlayers() {
        return trustedPlayers;
    }

    public void setTrustedPlayers(List<UUID> trustedPlayers) {
        this.trustedPlayers = trustedPlayers;
        DatabaseProvider provider = new DatabaseProvider();
        provider.updateTrustedPlayers(this.getChunk(), trustedPlayers);
    }

    public boolean isPlayerTrusted(UUID uuid) {
        return this.getTrustedPlayers().contains(uuid);
    }

    public void trustPlayer(String playerName) {
        UUID playerUUID = UUIDFetcher.fetchUUID(playerName);
        if (playerUUID == null) {
            System.out.println("Failed to fetch UUID for player " + playerName);
            return;
        }

        List<UUID> trustedPlayers = new ArrayList<>(this.getTrustedPlayers());
        trustedPlayers.add(playerUUID);

        DatabaseProvider provider = new DatabaseProvider();
        provider.trustPlayerToChunk(playerName, this.getChunk());

        this.setTrustedPlayers(trustedPlayers);
    }

    public List<UUID> getBannedPlayers() {
        return bannedPlayers;
    }

    public void setBannedPlayers(List<UUID> bannedPlayers) {
        this.bannedPlayers = bannedPlayers;
        DatabaseProvider provider = new DatabaseProvider();
        provider.updateBannedPlayers(this.getChunk(), bannedPlayers);
    }

    public boolean isPlayerBanned(UUID uuid) {
        return this.getBannedPlayers().contains(uuid);
    }

    public void banPlayer(String playerName) {
        UUID playerUUID = UUIDFetcher.fetchUUID(playerName);
        if (playerUUID == null) {
            System.out.println("Failed to fetch UUID for player " + playerName);
            return;
        }

        List<UUID> bannedPlayers = new ArrayList<>(this.getBannedPlayers());
        bannedPlayers.add(playerUUID);

        DatabaseProvider provider = new DatabaseProvider();
        provider.banPlayerFromChunk(playerName, this.getChunk());

        this.setBannedPlayers(bannedPlayers);
    }

    public void removePlayer(String playerName) {

        UUID playerUUID = UUIDFetcher.fetchUUID(playerName);
        if (playerUUID == null) {
            System.out.println("Failed to fetch UUID for player " + playerName);
            return;
        }

        DatabaseProvider provider = new DatabaseProvider();
        provider.removePlayerFromChunk(playerName, this.getChunk());

    }

    public void claim() {
        new DatabaseProvider().saveChunk(player, getChunk());
    }

    public ChunkRole getRole() {
        return role;
    }

    public void setRole(ChunkRole role) {
        this.role = role;
    }


}
