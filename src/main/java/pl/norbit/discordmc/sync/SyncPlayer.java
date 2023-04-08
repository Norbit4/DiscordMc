package pl.norbit.discordmc.sync;

import lombok.Data;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;

@Data
public class SyncPlayer {
    private final Player player;
    private final User user;
    private int time;
    public MessageChannel messageChannel;

    public SyncPlayer(Player player, User user, MessageChannel messageChannel) {
        this.player = player;
        this.user = user;
        this.time = 60;
        this.messageChannel = messageChannel;
    }
    public int time() {
        this.time -= 1;

        if(time == 0){
            return 1;
        }
        return 0;
    }
}
