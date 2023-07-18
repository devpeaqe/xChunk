package de.peaqe.xchunk.commands;

import de.peaqe.devapi.contents.MessageContents;
import de.peaqe.devapi.objects.PlayerObject;
import de.peaqe.xchunk.XChunk;
import de.peaqe.xchunk.manager.PlayerChunk;
import de.peaqe.xchunk.utils.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 *
 *  Class by peaqe created in 2023
 *  Class: ChunkCommand
 *
 *  Information's:
 *  Type: Java-Class | Bukkit Command
 *  Created: 17.07.2023 / 19:02
 *
 */

public class ChunkCommand implements CommandExecutor, TabExecutor {

    XChunk main = XChunk.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) return true;

        MessageContents contents = new MessageContents(main.prefix);

        PlayerObject player = new PlayerObject((Player) sender);
        PlayerChunk playerChunk = new PlayerChunk(player);

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("trust")) {

                var argument = args[1];

                // Check the Player
                if (!Bukkit.getOfflinePlayer(argument).hasPlayedBefore()) {
                    player.sendMessage(contents.getPlayerNotFound(argument));
                    return true;
                }

                var player_uuid = UUIDFetcher.fetchUUID(argument);
                if (player_uuid == null) {
                    player.sendMessage(contents.getPlayerNotFound(argument));
                    return true;
                }

                var player_name = UUIDFetcher.fetchUsername(player_uuid);
                if (player_name == null) {
                    player.sendMessage(contents.getPlayerNotFound(argument));
                    return true;
                }

                // Check if the Player is the Chunk owner
                if (!playerChunk.getAuthorUUID().equals(player.getUUID())) {
                    player.sendMessage(main.prefix + "Du darfst keine Spieler auf fremden §cChunks §7vertrauen!");
                    return true;
                }

                // Check if the entered Player is the Chunk owner
                if (player.getUUID().equals(player_uuid)) {
                    player.sendMessage("Du kannst dich nicht selber auf deinem §cChunk §7als §cHelfer §7hinzufügen!");
                    return true;
                }

                // Check if he is already in trust
                if (playerChunk.isPlayerTrusted(player_uuid)) {
                    player.sendMessage(main.prefix + "Dieser §cSpieler §7ist bereits auf deinem §cChunk §7vertraut!");
                    return true;
                }

                playerChunk.trustPlayer(player_name);
                player.sendMessage(main.prefix + "Du hast den Spieler §c" + player_name + "§a erfolgreich §7auf deinem §cChunk §7vertraut!");

                return true;

            } else if (args[0].equalsIgnoreCase("deny")) {

                var argument = args[1];

                // Check the Player
                if (!Bukkit.getOfflinePlayer(argument).hasPlayedBefore()) {
                    player.sendMessage(contents.getPlayerNotFound(argument));
                    return true;
                }

                var player_uuid = UUIDFetcher.fetchUUID(argument);
                if (player_uuid == null) {
                    player.sendMessage(contents.getPlayerNotFound(argument));
                    return true;
                }

                var player_name = UUIDFetcher.fetchUsername(player_uuid);
                if (player_name == null) {
                    player.sendMessage(contents.getPlayerNotFound(argument));
                    return true;
                }

                // Check if the Player is the Chunk owner
                if (!playerChunk.getAuthorUUID().equals(player.getUUID())) {
                    player.sendMessage(main.prefix + "Du darfst keine Spieler auf fremden §cChunks §7den Zugang verbieten!");
                    return true;
                }

                // Check if the entered Player is the Chunk owner
                if (player.getUUID().equals(player_uuid)) {
                    player.sendMessage("Du kannst dir nicht selber auf deinem §cChunk §7den Zutritt verbieten!");
                    return true;
                }

                // Check if he is already in trust
                if (playerChunk.isPlayerBanned(player_uuid)) {
                    player.sendMessage(main.prefix + "Dieser §cSpieler §7ist bereits den Zutritt, zu deinem §cChunk §7verboten!");
                    return true;
                }

                playerChunk.banPlayer(player_name);
                player.sendMessage(main.prefix + "Du hast den Spieler §c" + player_name + "§a erfolgreich §7von deinem §cChunk §7gebannt!");

                return true;

            } else if (args[0].equalsIgnoreCase("remove")) {

                var argument = args[1];

                // Check the Player
                if (!Bukkit.getOfflinePlayer(argument).hasPlayedBefore()) {
                    player.sendMessage(contents.getPlayerNotFound(argument));
                    return true;
                }

                var player_uuid = UUIDFetcher.fetchUUID(argument);
                if (player_uuid == null) {
                    player.sendMessage(contents.getPlayerNotFound(argument));
                    return true;
                }

                var player_name = UUIDFetcher.fetchUsername(player_uuid);
                if (player_name == null) {
                    player.sendMessage(contents.getPlayerNotFound(argument));
                    return true;
                }

                // Check if the Player is the Chunk owner
                if (!playerChunk.getAuthorUUID().equals(player.getUUID())) {
                    player.sendMessage(main.prefix + "Du darfst keine Spieler auf fremden §cChunks §7entfernen!");
                    return true;
                }

                // Check if the entered Player is the Chunk owner
                if (player.getUUID().equals(player_uuid)) {
                    player.sendMessage("Du kannst dich nicht selber von deinem §cChunk §7entfernen!");
                    return true;
                }

                // Check if he is already in trust
                if (!(playerChunk.isPlayerTrusted(player_uuid) || playerChunk.isPlayerBanned(player_uuid))) {
                    player.sendMessage(main.prefix + "Dieser §cSpieler §7ist nicht auf deinem §cChunk §7vertraut oder verboten!");
                    return true;
                }

                playerChunk.removePlayer(player_name);
                player.sendMessage(main.prefix + "Du hast den Spieler §c" + player_name + "§a erfolgreich §7auf deinem §cChunk §7vertraut!");

                return true;
            }

        }

        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info")) {

                if (playerChunk.isClaimed()) {

                    player.sendMessage(main.prefix + "§8§m                   §r §cCHUNK-INFO §8§m                    ");
                    player.sendMessage(main.prefix + "Chunk Besitzer: §c" + playerChunk.getAuthorName());
                    player.sendMessage(main.prefix + "Chunk ID: §c" + playerChunk.getChunkID());

                    // Trusted Player's
                    StringBuilder trusted = new StringBuilder();

                    for (UUID uuid : playerChunk.getTrustedPlayers()) {

                        var trusted_name = UUIDFetcher.fetchUsername(uuid);
                        trusted.append(" ").append(trusted_name);

                    }

                    StringBuilder denied = new StringBuilder();

                    for (UUID uuid : playerChunk.getBannedPlayers()) {

                        var denied_name = UUIDFetcher.fetchUsername(uuid);
                        denied.append(" ").append(denied_name);

                    }

                    if (trusted.isEmpty()) {
                        player.sendMessage(main.prefix + "Chunk Helfer: §cKeine");
                    } else {
                        player.sendMessage(main.prefix + "Chunk Helfer: §c" + trusted.toString().trim());
                    }

                    if (denied.isEmpty()) {
                        player.sendMessage(main.prefix + "Chunk Verbotene: §cKeine");
                    } else {
                        player.sendMessage(main.prefix + "Chunk Verbotene: §c" + denied.toString().trim());
                    }

                    player.sendMessage(main.prefix + "§8§m                   §r §cCHUNK-INFO §8§m                    ");

                } else {
                    player.sendMessage(main.prefix + "Der Chunk ist derzeit noch nicht vergeben.");
                }

                return true;
            }

            if (args[0].equalsIgnoreCase("claim")) {

                if (playerChunk.isClaimed()) {
                    player.sendMessage(main.prefix + "Dieser Chunk wurde bereits geclaimt!");
                    return true;
                }

                playerChunk.claim();
                player.sendMessage(main.prefix + "Der Chunk, mit der ID §c" + playerChunk.getChunkID() + " §7wurde §aerfolgreich §7für dich gesichert!");

                return true;
            }

        }

        player.sendMessage(contents.getUsage("chunk", "info"));
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> matches = new ArrayList<>();

        if (!(sender instanceof Player)) return matches;

        PlayerObject player = new PlayerObject((Player) sender);
        PlayerChunk playerChunk = new PlayerChunk(player);

        if (args.length == 1) {
            matches.add("info");

            if (!playerChunk.isClaimed()) {
                matches.add("claim");
            } else {
                if (playerChunk.getAuthorUUID().equals(player.getUUID())) {
                    matches.add("trust");
                    matches.add("remove");
                    matches.add("ban");
                }
            }

        }

        return matches;
    }
}