package pl.norbit.discordmc.sync;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;

public class SyncPlayer {
    private final Player player;
    private final User user;
    int time;

    public SyncPlayer(Player player, User user) {
        this.player = player;
        this.user = user;
        this.time = 60;
    }

    public Player getPlayer() {
        return player;
    }

    public User getUser() {
        return user;
    }

    public int getTime() {
        return time;
    }

    public int time() {
        this.time -= 1;

        if(time == 0){
            return 1;
        }
        return 0;
    }
}
