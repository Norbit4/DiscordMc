package pl.norbit.discordmc.server.objects;

import org.bukkit.entity.Player;
import pl.norbit.discordmc.server.enums.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GamePlayer {
    private static final List<GamePlayer> playersList;
    private Channel channel;
    private final Player player;

    static {
        playersList = new ArrayList<>();
    }

    public GamePlayer(Player player) {
        this.player = player;
        this.channel = Channel.GLOBAL;
        playersList.add(this);
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

}
