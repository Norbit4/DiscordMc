package pl.norbit.discordmc.api;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import pl.norbit.discordmc.players.DiscordPlayer;
import pl.norbit.discordmc.players.DiscordPlayerService;

import java.util.UUID;

public class DiscordMcAPI {

    /**
     * @param playerUUID UUID of player
     * @return true if player is sync with discord
     */

    public static boolean playerIsSync(UUID playerUUID){
        DiscordPlayer discordPlayer = DiscordPlayerService.getDiscordPlayerByPlayerUUID(playerUUID);
        return discordPlayer != null && discordPlayer.isSync();
    }

    /**
     * @param playerUUID UUID of player
     * @return DiscordPlayer object
     */
    public static DiscordPlayer getDiscordPlayer(UUID playerUUID){
        return DiscordPlayerService.getDiscordPlayerByPlayerUUID(playerUUID);
    }

    /**
     * @param discordID Discord ID of player
     * @return OfflinePlayer object
     */
    public static OfflinePlayer getPlayer(String discordID){
        DiscordPlayer discordPlayer = DiscordPlayerService.getDiscordPlayerByDiscordID(discordID);

        if(discordPlayer == null) return null;

        UUID playerUUID = discordPlayer.getPlayerUUID();

        return Bukkit.getOfflinePlayer(playerUUID);
    }

    /**
     * Clear player sync
     *
     * @param playerUUID UUID of player
     */

    public static void removeSync(UUID playerUUID){
        DiscordPlayerService.syncClear(playerUUID);
    }
    /**
     * Clear player sync
     *
     * @param  discordID Discord ID of player
     */
    public static void removeSync(String discordID){
        DiscordPlayerService.syncClear(discordID);
    }

    /**
     * Sync player with discord
     *
     * @param playerUUID UUID of player
     * @param discordID Discord ID of player
     */
    public static void sync(UUID playerUUID, String discordID){
        DiscordPlayerService.sync(playerUUID, discordID);
    }
}
