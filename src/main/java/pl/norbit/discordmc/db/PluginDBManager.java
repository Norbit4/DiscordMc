package pl.norbit.discordmc.db;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
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
            MysqlDB.start(javaPlugin);
            System.out.println("mysql");
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MONGODB")) {
            MongoDatabase.start();
            System.out.println("mongo");
        }else{
            System.out.println("[Error] Invalid database type in config.yml! (mysql/mongodb)");
            javaPlugin.getPluginLoader().disablePlugin(javaPlugin);
        }
    }

    public static void createUser(UUID playerUUID, String discordID){

        if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MONGODB")) {
            Document document = new Document()
                    .append(GameUser.UUID.name(), playerUUID.toString())
                    .append(GameUser.DISCORD_ID.name(), discordID)
                    .append(GameUser.VOICE_CHAT.name(), false);
            MongoDatabase.addUser(document);
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MYSQL")) {
            MysqlDB.addUser(playerUUID.toString(), discordID);
        }
    }

    public static void deleteUser(UUID playerUUID){
        if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MONGODB")){
            MongoDatabase.deleteUser(GameUser.UUID.name(), playerUUID.toString());
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MYSQL")){
            MysqlDB.deleteUser(playerUUID.toString());
        }
    }

    public static DatabaseRecord getUser(String discordID){

        if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MONGODB")) {
            Document doc = MongoDatabase.getUser(GameUser.DISCORD_ID.name(), discordID);

            if (doc != null) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(doc.getString(GameUser.UUID.name())));
                User user = jda.getUserById(doc.getString(GameUser.DISCORD_ID.name()));
                return new DatabaseRecord(player, user);
            }
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MYSQL")){
            ResultSet user = MysqlDB.getUser(discordID);

            if(user != null) {
                try {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(user.getString(GameUser.UUID.name())));

                    User dcUser = jda.getUserById(discordID);

                    return new DatabaseRecord(player, dcUser);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static DatabaseRecord getUser(UUID playerUUID){

        if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MONGODB")) {
            Document doc = MongoDatabase.getUser(GameUser.UUID.name(), playerUUID.toString());
            if (doc != null) {

                OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
                User user = jda.getUserById(doc.getString(GameUser.DISCORD_ID.name()));
                return new DatabaseRecord(player, user);
            }
        }else if(PluginConfig.DATABASE_TYPE.equalsIgnoreCase("MYSQL")){
            ResultSet user = MysqlDB.getUser(playerUUID);

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
        }
        return null;
    }
}
