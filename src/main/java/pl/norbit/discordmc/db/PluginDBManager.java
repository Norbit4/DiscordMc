package pl.norbit.discordmc.db;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.db.objects.DatabaseRecord;
import pl.norbit.discordmc.server.config.PluginConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PluginDBManager {
    private static JDA jda;

    public static void init(JDA jda, JavaPlugin javaPlugin){
        PluginDBManager.jda = jda;

        if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MYSQL")){
            MysqlDatabase.start(javaPlugin);
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MONGODB")) {
            MongoDatabase.start();
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("LOCAL")) {
            LocalDB.init(javaPlugin);
        }else{
            DiscordMc.sendMessage("&cInvalid database type in config.yml! (mysql/mongodb/local)");
        }
    }

    public static void close(){
        if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MYSQL")){
            MysqlDatabase.close();
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MONGODB")) {
            MongoDatabase.close();
        }
    }

    public static void createUser(UUID playerUUID, String discordID){

        if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MONGODB")) {
            Document document = new Document()
                    .append(GameUser.UUID.name(), playerUUID.toString())
                    .append(GameUser.DISCORD_ID.name(), discordID);
            MongoDatabase.addUser(document);
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MYSQL")) {
            MysqlDatabase.addUser(playerUUID.toString(), discordID);
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("LOCAL")) {
               LocalDB.addUser(playerUUID.toString(), discordID);
        }
    }

    public static void deleteUser(UUID playerUUID){
        if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MONGODB")){
            MongoDatabase.deleteUser(GameUser.UUID.name(), playerUUID.toString());
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MYSQL")) {
            MysqlDatabase.deleteUser(playerUUID.toString());
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("LOCAL")){
            LocalDB.deleteUser(playerUUID);
        }
    }

    public static DatabaseRecord getUser(String discordID){

        if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MONGODB")) {
            Document doc = MongoDatabase.getUser(GameUser.DISCORD_ID.name(), discordID);

            if (doc != null) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(doc.getString(GameUser.UUID.name())));
                User user = jda.retrieveUserById(doc.getString(GameUser.DISCORD_ID.name())).complete();
                return new DatabaseRecord(player, user);
            }
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MYSQL")){
            ResultSet user = MysqlDatabase.getUser(discordID);

            if(user != null) {
                try {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(user.getString(GameUser.UUID.name())));

                    User dcUser = jda.retrieveUserById(discordID).complete();

                    return new DatabaseRecord(player, dcUser);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("LOCAL")){
            LocalDB.LocalRecord user = LocalDB.getUser(discordID);
            if(user != null) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(user.getPlayerUUID());
                User dcUser = jda.retrieveUserById(discordID).complete();
                return new DatabaseRecord(player, dcUser);
            }
        }
        return null;
    }

    public static DatabaseRecord getUser(UUID playerUUID){

        if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MONGODB")) {
            Document doc = MongoDatabase.getUser(GameUser.UUID.name(), playerUUID.toString());
            if (doc != null) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
                User user = jda.retrieveUserById(doc.getString(GameUser.DISCORD_ID.name())).complete();
                return new DatabaseRecord(player, user);
            }
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MYSQL")){
            ResultSet user = MysqlDatabase.getUser(playerUUID);

            if(user != null) {
                try {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
                    String id = user.getString(GameUser.DISCORD_ID.name());

                    User dcUser = jda.retrieveUserById(id).complete();

                    return new DatabaseRecord(player, dcUser);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("LOCAL")){
            LocalDB.LocalRecord user = LocalDB.getUser(playerUUID);
            if(user != null) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
                User dcUser = jda.retrieveUserById(user.getUserID()).complete();
                return new DatabaseRecord(player, dcUser);
            }
        }
        return null;
    }
}
