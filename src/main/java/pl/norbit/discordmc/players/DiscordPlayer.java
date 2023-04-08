package pl.norbit.discordmc.players;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.server.Channel;
import pl.norbit.discordmc.utils.ChatUtil;

import java.util.UUID;

@Data
@NoArgsConstructor
public class DiscordPlayer {
    private Channel channel = Channel.GLOBAL;
    private UUID playerUUID;
    private String discordId;
    public boolean isSync(){
        return discordId != null;
    }
    public void syncClear(){
        this.discordId = null;
    }

    public String getDiscordName(){
        User discordUser = DiscordMc.getJda().retrieveUserById(discordId).complete();
        if(discordUser == null) return null;

        return discordUser.getName();
    }

    public String getDiscordFullName(){
        User discordUser = DiscordMc.getJda().retrieveUserById(discordId).complete();
        if(discordUser == null) return null;

        return discordUser.getAsTag();
    }


    public void sendDiscordMessage(String message){
        User discordUser= DiscordMc.getJda().retrieveUserById(discordId).complete();
        if(discordUser == null) return;

        discordUser.openPrivateChannel().complete().sendMessage(message).queue();
    }

    public void sendMcMessage(String message) {

        Player player = Bukkit.getPlayer(playerUUID);

        if(player == null) return;

        player.sendMessage(ChatUtil.format(message));
    }
}
