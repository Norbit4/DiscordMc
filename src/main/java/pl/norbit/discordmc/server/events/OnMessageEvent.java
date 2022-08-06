package pl.norbit.discordmc.server.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.utils.ChatUtil;
import pl.norbit.discordmc.server.config.PluginConfig;
import pl.norbit.discordmc.server.enums.Channel;
import pl.norbit.discordmc.server.objects.GamePlayer;

import java.awt.*;

public class OnMessageEvent implements Listener {
    private final JDA jda;

    public OnMessageEvent(JDA jda) {
        this.jda = jda;
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        MessageChannel messageChannel = jda.getTextChannelById(PluginConfig.CHAT_CHANNEL_ID);
        String playerMessage = e.getMessage();
        Player sender = e.getPlayer();
        GamePlayer gamePlayer = GamePlayer.getGamePLayer(sender);

        if (gamePlayer.getChannel().equals(Channel.DISCORD)) {
            String playerNick = gamePlayer.getPlayer().getDisplayName();

            EmbedBuilder embedBuilder = Embed.getDiscordMessage(playerNick, playerMessage,
                    new Color(
                            PluginConfig.EMBED_MC_R,
                            PluginConfig.EMBED_MC_G,
                            PluginConfig.EMBED_MC_B),
                    "");

            messageChannel.sendMessageEmbeds(embedBuilder.build()).queue();

            e.setCancelled(true);

            GamePlayer.getPlayersList(Channel.DISCORD).forEach(gamePlayer1 -> {
                gamePlayer1.getPlayer().sendMessage(ChatUtil.format(PluginConfig.MC_PREFIX + PluginConfig.NICK_COLOR
                        + playerNick + PluginConfig.MESSAGE_MARK + PluginConfig.MESSAGE_COLOR + playerMessage));
            });
        }
    }
}