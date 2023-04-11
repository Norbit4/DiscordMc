package pl.norbit.discordmc.server.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.players.DiscordPlayer;
import pl.norbit.discordmc.players.DiscordPlayerService;
import pl.norbit.discordmc.utils.ChatUtil;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.server.Channel;

import java.awt.*;

public class OnMessageEvent implements Listener {

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        JDA jda = DiscordMc.getJda();

        MessageChannel messageChannel = jda.getTextChannelById(PluginConfig.CHAT_CHANNEL_ID);
        String playerMessage = e.getMessage();
        Player sender = e.getPlayer();
        DiscordPlayer gamePLayerByPlayerUUID = DiscordPlayerService.getDiscordPlayerByPlayerUUID(sender.getUniqueId());

        if (!gamePLayerByPlayerUUID.getChannel().equals(Channel.DISCORD)) return;

        String playerNick = sender.getDisplayName();

        EmbedBuilder embedBuilder = Embed.getDiscordMessage(playerNick, playerMessage,
                new Color(
                        PluginConfig.EMBED_MC_R,
                        PluginConfig.EMBED_MC_G,
                        PluginConfig.EMBED_MC_B),
                "");

        messageChannel.sendMessageEmbeds(embedBuilder.build()).queue();

        e.setCancelled(true);
        String formatMessage = ChatUtil.format(PluginConfig.MC_PREFIX + PluginConfig.NICK_COLOR
                + playerNick + PluginConfig.MESSAGE_MARK + PluginConfig.MESSAGE_COLOR + playerMessage);

        DiscordPlayerService.getPlayersList().forEach(discordPlayer -> {

            if (discordPlayer.getChannel().equals(Channel.DISCORD)) discordPlayer.sendMcMessage(formatMessage);
        });
    }
}