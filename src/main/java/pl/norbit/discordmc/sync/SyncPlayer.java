package pl.norbit.discordmc.sync;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;

public class SyncPlayer {
    private final Player player;
    private final User user;
    int time;
    public MessageChannel messageChannel;

    public SyncPlayer(Player player, User user, MessageChannel messageChannel) {
        this.player = player;
        this.user = user;
        this.time = 60;
        this.messageChannel = messageChannel;
    }

    public Player getPlayer() {
        return player;
    }

    public User getUser() {
        return user;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int time() {
        this.time -= 1;

        if(time == 0){
            return 1;
        }
        return 0;
    }

    public MessageChannel getMessageChannel() {
        return messageChannel;
    }
}
