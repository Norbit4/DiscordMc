package pl.norbit.discordmc.bot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.players.DiscordPlayerService;
import pl.norbit.discordmc.utils.ChatUtil;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.server.Channel;

import java.awt.*;

public class OnMessageChatEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String channelID = message.getChannel().getId();

        if(channelID.equals(PluginConfig.CHAT_CHANNEL_ID)) sendChatMessage(event);
    }

    private void sendChatMessage(MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();

        if (event.getAuthor().isBot()) return;

        event.getMessage().delete().queue();
        EmbedBuilder embedBuilder = Embed.getDiscordMessage(
                author.getAsTag(),
                message.getContentDisplay(),
                new Color(
                        PluginConfig.EMBED_DISCORD_R,
                        PluginConfig.EMBED_DISCORD_G,
                        PluginConfig.EMBED_DISCORD_B),
                author.getAsMention());

        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();

        String formatMessage = ChatUtil.format(PluginConfig.DISCORD_PREFIX + PluginConfig.NICK_COLOR
                + author.getAsTag() + PluginConfig.MESSAGE_MARK + PluginConfig.MESSAGE_COLOR
                + message.getContentDisplay());

        DiscordPlayerService.getPlayersList().forEach(discordPlayer -> {

            if (discordPlayer.getChannel().equals(Channel.DISCORD)) discordPlayer.sendMcMessage(formatMessage);
        });
    }
}
