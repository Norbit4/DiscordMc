package pl.norbit.discordmc.server.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.norbit.discordmc.bot.utils.DiscordInfo;
import pl.norbit.discordmc.server.enums.Channel;
import pl.norbit.discordmc.server.objects.GamePlayer;

public class OnMessageEvent implements Listener{
    private final JDA jda;
    private MessageChannel messageChannel;

    public OnMessageEvent(JDA jda) {
        this.jda = jda;
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        this.messageChannel = jda.getTextChannelsByName(DiscordInfo.channelName,false).get(0);
        String playerMessage = e.getMessage();
        Player sender = e.getPlayer();
        GamePlayer gamePlayer = GamePlayer.getGamePLayer(sender);
        if (gamePlayer.getChannel().equals(Channel.DISCORD)) {
            String playerNick = gamePlayer.getPlayer().getDisplayName();
            String formatMessage = playerNick + ": " +  playerMessage;

            messageChannel.sendMessage(formatMessage).queue();
            e.setCancelled(true);
        }
//        }else{
//            GamePlayer.getPlayersList(Channel.GLOBAL).forEach(gamePlayer1 -> {
//                gamePlayer1.getPlayer().sendMessage("");
//            });
//        }
    }
}
