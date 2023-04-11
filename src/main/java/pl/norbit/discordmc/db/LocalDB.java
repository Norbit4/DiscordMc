package pl.norbit.discordmc.db;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.players.DiscordPlayerService;
import pl.norbit.pluginutils.file.LocalDatabase;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class LocalDB {

    private static List<LocalRecord> databaseList;
    private static LocalDatabase<LocalRecord> localDatabase;

    @Data
    public static class LocalRecord {
        private final UUID playerUUID;
        private final String userID;

        public LocalRecord(UUID playerUUID, String userID) {
            this.playerUUID = playerUUID;
            this.userID = userID;
        }

        public UUID getPlayerUUID() {
            return playerUUID;
        }

        public String getUserID() {
            return userID;
        }
    }

    public static void init(JavaPlugin javaPlugin){
        String directory = javaPlugin.getDataFolder().getAbsolutePath() + "/database";

        localDatabase = new LocalDatabase<>(directory, LocalRecord.class);

        try {
            databaseList = localDatabase.getAllObjects();
            DiscordPlayerService.loadPlayers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteUser(UUID playerUUID){
        String name = Bukkit.getPlayer(playerUUID).getName();
        localDatabase.deleteObject(name);

        int index = -1;

        for (int i = 0; i < databaseList.size(); i++) {

            if(databaseList.get(i).playerUUID.equals(playerUUID)){
                index = i;
                break;
            }
        }

        if(index != -1) databaseList.remove(index);
    }
    public static void addUser(String playerUUID, String userID){
        deleteUser(UUID.fromString(playerUUID));
        Player player = Bukkit.getPlayer(UUID.fromString(playerUUID));

        LocalRecord localRecord = new LocalRecord(UUID.fromString(playerUUID), userID);

        try {
            localDatabase.saveObject(player.getName(), localRecord);
            databaseList.add(localRecord);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static LocalRecord getUser(UUID playerUUID){

        for (LocalRecord localRecord : databaseList) {

            if(localRecord.playerUUID.equals(playerUUID)) return localRecord;
        }
        return null;
    }

    public static LocalRecord getUser(String discordID){

        for (LocalRecord localRecord : databaseList) {

            if(localRecord.userID.equals(discordID)) return localRecord;
        }
        return null;
    }

    public static List<LocalRecord> getDatabaseList() {
        return databaseList;
    }
}
