package pl.norbit.discordmc.server.objects;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;
import pl.norbit.discordmc.db.PluginDBManager;
import pl.norbit.discordmc.db.objects.DatabaseRecord;
import pl.norbit.discordmc.server.enums.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GamePlayer {
    private static final List<GamePlayer> playersList;
    private Channel channel;
    private final Player player;
    private User discordUser;
    private boolean voiceChat;
    private final JDA jda;

    static {
        playersList = new ArrayList<>();
    }

    public GamePlayer(Player player, JDA jda) {
        this.jda = jda;
        this.player = player;
        this.channel = Channel.GLOBAL;
        playersList.add(this);

        //Document document = MongoDB.getUser(GameUser.UUID.name(), player.getUniqueId().toString());
        DatabaseRecord databaseRecord = PluginDBManager.getUser(player.getUniqueId());

        if(databaseRecord != null){
            discordUser = databaseRecord.getUser();
        }else{
            discordUser = null;
        }
        voiceChat = false;
    }

    public static GamePlayer getGamePLayer(Player player){
        Optional<GamePlayer> optionalGamePlayer = playersList
                .stream()
                .filter(gamePlayer -> gamePlayer.getPlayer().equals(player))
                .findFirst();

        return optionalGamePlayer.orElse(null);
    }

    public static List<GamePlayer> getPlayersList(){
        return playersList;
    }

    public static List<GamePlayer> getPlayersList(Channel channel){
        return playersList
                .stream()
                .filter(gamePlayer -> gamePlayer.getChannel().equals(channel))
                .collect(Collectors.toList());
    }

    public void remove(){
        playersList.remove(this);
    }

    public Player getPlayer() {
        return player;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public User getDiscordUser() {
        return discordUser;
    }

    public void setDiscordUser(String discordID) {

        if(discordID != null) {
            this.discordUser = jda.getUserById(discordID);
        }else{
            this.discordUser = null;
        }
    }

    public boolean isVoiceChat() {
        return voiceChat;
    }

    public void setVoiceChat(boolean voiceChat) {
        this.voiceChat = voiceChat;
    }
}
