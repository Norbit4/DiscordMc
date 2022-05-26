package pl.norbit.discordmc.db;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PluginDBManager {
    private static JDA jda;

    public static void init(JDA jda){
        PluginDBManager.jda = jda;
    }

    public static void createUser(UUID playerUUID, String discordID){
        Document document = new Document()
                .append(GameUser.UUID.name(), playerUUID.toString())
                .append(GameUser.DISCORD_ID.name(), discordID);

        MongoDB.addUser(document);
    }

    public static Player getUser(String discordID){
        Document document = MongoDB.getUser(GameUser.DISCORD_ID.name(), discordID);
        if(document != null) {
            UUID playerUUID = UUID.fromString(document.getString(GameUser.UUID.name()));
            return Bukkit.getPlayer(playerUUID);
        }
        return null;
    }

    public static User getUser(UUID playerUUID){

        Document document = MongoDB.getUser(GameUser.UUID.name(), playerUUID.toString());
        String discordID = document.getString(GameUser.DISCORD_ID);

        return jda.getUserById(discordID);
    }
}
