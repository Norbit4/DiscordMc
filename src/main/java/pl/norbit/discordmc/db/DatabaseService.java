package pl.norbit.discordmc.db;

import org.bson.Document;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.db.objects.DatabaseRecord;
import pl.norbit.discordmc.config.PluginConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseService {

    public static void init(){
        DiscordMc discordMc = DiscordMc.getInstance();

        switch (PluginConfig.DATABASE_TYPE.toUpperCase()) {
            case "MYSQL":
                MysqlDatabase.start(discordMc);
                break;
            case "MONGODB":
                MongoDatabase.start();
                break;
            case "LOCAL":
                LocalDB.init(discordMc);
                break;
            default:
                DiscordMc.sendMessage("&cInvalid database type in config.yml! (mysql/mongodb/local)");
        }
    }

    public static void close(){

        switch (PluginConfig.DATABASE_TYPE.toUpperCase()) {
            case "MYSQL":
                MysqlDatabase.close();
                break;
            case "MONGODB":
                MongoDatabase.close();
                break;
        }
    }

    public static void createUser(UUID playerUUID, String discordID){

        switch (PluginConfig.DATABASE_TYPE.toUpperCase()) {
            case "MYSQL":
                MysqlDatabase.addUser(playerUUID.toString(), discordID);
                break;
            case "MONGODB":
                Document document = new Document()
                        .append(GameUser.UUID.name(), playerUUID.toString())
                        .append(GameUser.DISCORD_ID.name(), discordID);
                MongoDatabase.addUser(document);
                break;
            case "LOCAL":
                LocalDB.addUser(playerUUID.toString(), discordID);
                break;
        }
    }

    public static void deleteUser(UUID playerUUID){
        switch (PluginConfig.DATABASE_TYPE.toUpperCase()) {
            case "MYSQL":
                MysqlDatabase.deleteUser(playerUUID.toString());
                break;
            case "MONGODB":
                MongoDatabase.deleteUser(GameUser.UUID.name(), playerUUID.toString());
                break;
            case "LOCAL":
                LocalDB.deleteUser(playerUUID);
                break;
        }
    }

    public static DatabaseRecord getUser(String discordID){

        switch (PluginConfig.DATABASE_TYPE.toUpperCase()){
            case "MONGODB":
                Document doc = MongoDatabase.getUser(GameUser.DISCORD_ID.name(), discordID);

                if (doc != null) {
                    UUID playerUUID = UUID.fromString(doc.getString(GameUser.UUID.name()));

                    return new DatabaseRecord(playerUUID, discordID);
                }

                break;
            case "MYSQL":
                ResultSet mysqlUser = MysqlDatabase.getUser(discordID);

                if(mysqlUser != null) {
                    try {
                        UUID playerUUID = UUID.fromString(mysqlUser.getString(GameUser.UUID.name()));

                        return new DatabaseRecord(playerUUID, discordID);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "LOCAL":
                LocalDB.LocalRecord user = LocalDB.getUser(discordID);
                if(user != null) {

                    return new DatabaseRecord(user.getPlayerUUID(), discordID);
                }
                break;
        }
        return null;
    }

    public static List<DatabaseRecord > getALL(){
        List<DatabaseRecord> list = new ArrayList<>();

        switch (PluginConfig.DATABASE_TYPE.toUpperCase()){
            case "MONGODB":
                for (Document document : MongoDatabase.getDatabaseList()) {
                    UUID playerUUID = UUID.fromString(document.getString(GameUser.UUID.name()));
                    String userID = document.getString(GameUser.DISCORD_ID.name());

                    list.add(new DatabaseRecord(playerUUID, userID));
                }
                return list;
            case "MYSQL":
                for (ResultSet mysqlUser : MysqlDatabase.getDatabaseList()) {
                    try {
                        UUID playerUUID = UUID.fromString(mysqlUser.getString(GameUser.UUID.name()));
                        String userID = mysqlUser.getString(GameUser.DISCORD_ID.name());

                        list.add(new DatabaseRecord(playerUUID, userID));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
               return list;
            case "LOCAL":
                for (LocalDB.LocalRecord localRecord : LocalDB.getDatabaseList()) {
                    UUID playerUUID = localRecord.getPlayerUUID();
                    String userID = localRecord.getUserID();

                    list.add(new DatabaseRecord(playerUUID, userID));
                }
                return list;
        }
        return null;
    }

    public static DatabaseRecord getUser(UUID playerUUID){

        switch (PluginConfig.DATABASE_TYPE.toUpperCase()){
            case "MONGODB":
                Document doc = MongoDatabase.getUser(GameUser.UUID.name(), playerUUID.toString());

                if (doc != null) {
                    String userId = doc.getString(GameUser.DISCORD_ID.name());
                    return new DatabaseRecord(playerUUID, userId);
                }
                break;
            case "MYSQL":
                ResultSet mysqlUser = MysqlDatabase.getUser(playerUUID);

                if(mysqlUser != null) {
                    try {
                        String userId = mysqlUser.getString(GameUser.DISCORD_ID.name());
                        return new DatabaseRecord(playerUUID, userId);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "LOCAL":
                LocalDB.LocalRecord user = LocalDB.getUser(playerUUID);
                if(user != null) {
                    return new DatabaseRecord(playerUUID, user.getUserID());
                }
                break;
        }
        return null;
    }
}
