package de.peaqe.xchunk.commands;

import de.peaqe.devapi.contents.MessageContents;
import de.peaqe.xchunk.XChunk;
import de.peaqe.xchunk.manager.ChunkRole;
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

        if (!(sender instanceof Player player)) return true;

        MessageContents contents = new MessageContents(main.prefix);

        PlayerChunk playerChunk = XChunk.getInstance().chunkCache.getPlayerChunk(player);

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("trust") || args[0].equalsIgnoreCase("add")) {

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
                if (!playerChunk.getAuthorUUID().equals(player.getUniqueId())) {
                    player.sendMessage(main.prefix + "Du darfst keine Spieler auf fremden §cChunks §7vertrauen!");
                    return true;
                }

                // Check if the entered Player is the Chunk owner
                if (player.getUniqueId().equals(player_uuid)) {
                    player.sendMessage(main.prefix + "Du kannst dich nicht selber auf deinem §cChunk §7als §cHelfer §7hinzufügen!");
                    return true;
                }

                // Check if he is already in trust
                if (playerChunk.isPlayerTrusted(player_uuid)) {
                    player.sendMessage(main.prefix + "Dieser §cSpieler §7ist bereits auf deinem §cChunk §7vertraut!");
                    return true;
                }

                playerChunk.trustPlayer(player_name);
                XChunk.getInstance().chunkCache.reloadPlayerChunk((Player) sender, player.getLocation());
                player.sendMessage(main.prefix + "Du hast den Spieler §c" + player_name + "§a erfolgreich §7auf deinem §cChunk §7vertraut!");

                return true;

            } else if (args[0].equalsIgnoreCase("deny") || args[0].equalsIgnoreCase("ban")) {

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
                if (!playerChunk.getAuthorUUID().equals(player.getUniqueId())) {
                    player.sendMessage(main.prefix + "Du darfst keine Spieler auf fremden §cChunks §7den Zugang verbieten!");
                    return true;
                }

                // Check if the entered Player is the Chunk owner
                if (player.getUniqueId().equals(player_uuid)) {
                    player.sendMessage(main.prefix + "Du kannst dir nicht selber auf deinem §cChunk §7den Zutritt verbieten!");
                    return true;
                }

                // Check if he is already in trust
                if (playerChunk.isPlayerBanned(player_uuid)) {
                    player.sendMessage(main.prefix + "Dieser §cSpieler §7ist bereits den Zutritt, zu deinem §cChunk §7verboten!");
                    return true;
                }

                playerChunk.banPlayer(player_name);
                XChunk.getInstance().chunkCache.reloadPlayerChunk((Player) sender, player.getLocation());
                player.sendMessage(main.prefix + "Du hast den Spieler §c" + player_name + "§a erfolgreich §7von deinem §cChunk §7gebannt!");

                return true;

            } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("untrust") || args[0].equalsIgnoreCase("undeny") || args[0].equalsIgnoreCase("unban")) {

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
                if (!playerChunk.getAuthorUUID().equals(player.getUniqueId())) {
                    player.sendMessage(main.prefix + "Du darfst keine Spieler auf fremden §cChunks §7entfernen!");
                    return true;
                }

                // Check if the entered Player is the Chunk owner
                if (player.getUniqueId().equals(player_uuid)) {
                    player.sendMessage(main.prefix + "Du kannst dich nicht selber von deinem §cChunk §7entfernen!");
                    return true;
                }

                // Check if he is already in trust
                if (!(playerChunk.isPlayerTrusted(player_uuid) || playerChunk.isPlayerBanned(player_uuid))) {
                    player.sendMessage(main.prefix + "Dieser §cSpieler §7ist nicht auf deinem §cChunk §7vertraut oder verboten!");
                    return true;
                }

                playerChunk.removePlayer(player_name);
                XChunk.getInstance().chunkCache.reloadPlayerChunk((Player) sender, player.getLocation());
                player.sendMessage(main.prefix + "Du hast den Spieler §c" + player_name + "§a erfolgreich §7auf deinem §cChunk §7vertraut!");

                return true;
            }

        }

        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")) {

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
                    player.sendMessage(main.prefix + "Dieser §cChunk wurde bereits geclaimt!");
                    return true;
                }

                playerChunk.claim();
                XChunk.getInstance().chunkCache.reloadPlayerChunk(player, player.getLocation());
                player.sendMessage(main.prefix + "Der Chunk, mit der ID §c" + playerChunk.getChunkID() + " §7wurde §aerfolgreich §7für dich gesichert!");

                return true;
            }

            if (args[0].equalsIgnoreCase("unclaim")) {

                // Check if the Chunk is claimed
                if (!playerChunk.isClaimed()) {
                    player.sendMessage(main.prefix + "Dieser §cChunk §7gehört niemanden!");
                    return true;
                }

                // Check if the Sender is the Chunk claimer
                if (!playerChunk.getRole().equals(ChunkRole.OWNER)) {
                    player.sendMessage(main.prefix + "Du bist nicht berechtigt den §cChunk§7, von §c" + playerChunk.getAuthorName() + " §7zu entsichern!");
                    return true;
                }

                playerChunk.setClaimed(false);
                XChunk.getInstance().chunkCache.reloadPlayerChunk((Player) sender, player.getLocation());
                player.sendMessage(main.prefix + "Du hast dein §cChunk §aerfolgreich §7aufgelöst.");

                return true;
            }

        }

        player.sendMessage(contents.getUsage("chunk", "info"));
        player.sendMessage(contents.getUsage("chunk", "claim"));
        player.sendMessage(contents.getUsage("chunk", "unclaim"));
        player.sendMessage(contents.getUsage("chunk", "ban", "Spieler"));
        player.sendMessage(contents.getUsage("chunk", "trust", "Spieler"));
        player.sendMessage(contents.getUsage("chunk", "remove", "Spieler"));

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> matches = new ArrayList<>();

        if (!(sender instanceof Player player)) return matches;
        PlayerChunk playerChunk = XChunk.getInstance().chunkCache.getPlayerChunk(player);

        if (args.length == 1) {
            matches.add("info");

            if (!playerChunk.isClaimed()) {
                matches.add("claim");
            } else {
                if (playerChunk.getRole().equals(ChunkRole.OWNER)) {
                    matches.add("trust");
                    matches.add("remove");
                    matches.add("ban");
                    matches.add("unclaim");
                }
            }

        }

        return matches;
    }
}