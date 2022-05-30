package pl.norbit.discordmc.db;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pl.norbit.discordmc.server.objects.GamePlayer;

import java.util.UUID;

public class PluginDBManager {
    private static JDA jda;

    public static void init(JDA jda){
        PluginDBManager.jda = jda;
    }

    public static void createUser(UUID playerUUID, String discordID){
        Document document = new Document()
                .append(GameUser.UUID.name(), playerUUID.toString())
                .append(GameUser.DISCORD_ID.name(), discordID)
                .append(GameUser.VOICE_CHAT.name(), false);
        MongoDB.addUser(document);
    }

    public static DatabaseRecord getUser(String discordID){
        Document doc = MongoDB.getUser(GameUser.DISCORD_ID.name(), discordID);

        if(doc != null){
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(doc.getString(GameUser.UUID.name())));
            User user = jda.getUserById(doc.getString(GameUser.DISCORD_ID.name()));
            return new DatabaseRecord(player, user);
        }

        return null;
    }

    public static User getUser(UUID playerUUID){

        Document document = MongoDB.getUser(GameUser.UUID.name(), playerUUID.toString());
        String discordID = document.getString(GameUser.DISCORD_ID);

        return jda.getUserById(discordID);
    }
}
