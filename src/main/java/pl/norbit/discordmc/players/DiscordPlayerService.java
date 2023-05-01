package pl.norbit.discordmc.players;

import org.bukkit.entity.Player;
import pl.norbit.discordmc.db.DatabaseService;
import pl.norbit.discordmc.db.objects.DatabaseRecord;
import pl.norbit.discordmc.discord.DiscordUserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DiscordPlayerService {

    private static final HashMap<UUID, DiscordPlayer> playersList = new HashMap<>();
    private static final HashMap<String, UUID> playersListDiscord = new HashMap<>();

    public static DiscordPlayer getDiscordPlayerByPlayerUUID(UUID uuid){
        return playersList.get(uuid);
    }
    public static DiscordPlayer getDiscordPlayerByDiscordID(String id){
        if(!playersListDiscord.containsKey(id)) return null;

        return playersList.get(playersListDiscord.get(id));
    }

    public static void loadPlayers(){

        List<DatabaseRecord> databaseRecords = DatabaseService.getALL();

        if(databaseRecords == null) return;

        databaseRecords.forEach(DiscordPlayerService::loadDiscordPlayer);
    }
    public static void loadDiscordPlayer(DatabaseRecord databaseRecord){
        DiscordPlayer discordPlayer = new DiscordPlayer();

        UUID playerUUID = databaseRecord.getPlayerUUID();

        discordPlayer.setPlayerUUID(playerUUID);

        if(databaseRecord.getUserId() != null) {
            String discordId = databaseRecord.getUserId();

            discordPlayer.setDiscordId(discordId );
            playersListDiscord.put(discordId, playerUUID);
        }
        playersList.put(playerUUID, discordPlayer);
    }
    public static Collection<DiscordPlayer> getPlayersList(){
        return playersList.values();
    }

    public static void joinPlayer(Player p){
        DiscordPlayer gamePLayerByPlayerUUID = getDiscordPlayerByPlayerUUID(p.getUniqueId());

        if(gamePLayerByPlayerUUID != null) return;

        DiscordPlayer discordPlayer = new DiscordPlayer();
        discordPlayer.setPlayerUUID(p.getUniqueId());
        playersList.put(p.getUniqueId(), discordPlayer);
    }

    public static boolean checkIsSynced(UUID playerUUID, String discordID){

        DiscordPlayer discordPlayer = playersList.get(playerUUID);

        if(discordPlayer.isSync()) return true;

        UUID uuid = playersListDiscord.get(discordID);

        return uuid != null;
    }
    public static void sync(UUID playerUUID, String discordID){

        DiscordPlayer discordPlayer = playersList.get(playerUUID);

        if(discordPlayer == null) {
            return;
        }

        if(discordPlayer.isSync()) return;
        discordPlayer.setDiscordId(discordID);

        //update rank and name
        DiscordUserService.updateDiscordUser(playerUUID, discordID);

        DatabaseService.createUser(playerUUID, discordID);
        playersListDiscord.put(discordPlayer.getDiscordId(), playerUUID);
    }

    public static void syncClear(UUID playerUUID){

        DiscordPlayer discordPlayer = playersList.get(playerUUID);

        if(!discordPlayer.isSync()) return;

        playersListDiscord.remove(discordPlayer.getDiscordId());
        DatabaseService.deleteUser(discordPlayer.getPlayerUUID());

        DiscordUserService.clear(discordPlayer.getDiscordId(), discordPlayer.getPlayerUUID());

        discordPlayer.syncClear();
    }
    public static void syncClear(String discordID){

        UUID playerUUID = playersListDiscord.get(discordID);

        if(playerUUID == null) return;

        DiscordPlayer discordPlayer = playersList.get(playerUUID);

        if(!discordPlayer.isSync()) return;

        playersListDiscord.remove(discordPlayer.getDiscordId());
        DatabaseService.deleteUser(discordPlayer.getPlayerUUID());

        DiscordUserService.clear(discordPlayer.getDiscordId(), discordPlayer.getPlayerUUID());

        discordPlayer.syncClear();
    }
}
