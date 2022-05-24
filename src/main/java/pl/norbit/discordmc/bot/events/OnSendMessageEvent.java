package pl.norbit.discordmc.bot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.bot.utils.ChatUtil;
import pl.norbit.discordmc.server.config.PluginConfig;
import pl.norbit.discordmc.server.enums.Channel;
import pl.norbit.discordmc.server.objects.GamePlayer;

import java.awt.*;

public class OnSendMessageEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String channelName = message.getChannel().getName();

        if(channelName.equals(PluginConfig.CHANNEL_NAME)){

            sendMessage(event);
        }
    }

    private void sendMessage(MessageReceivedEvent event){
        User author = event.getAuthor();
        Message message = event.getMessage();
        //String botName = event.getJDA().getSelfUser().getAsTag();
        boolean isBot = event.getAuthor().isBot();

        if(!isBot) {
            //author.getAsMention();
            event.getMessage().delete().queue();
            EmbedBuilder embedBuilder = Embed.getDiscordMessage(author.getAsTag(),
                    message.getContentDisplay(), new Color(142, 87, 178),author.getAsMention());

            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();

            String formatMessage = ChatUtil.format(PluginConfig.DISCORD_PREFIX + PluginConfig.NICK_COLOR
                    + author.getAsTag() + PluginConfig.MESSAGE_MARK + PluginConfig.MESSAGE_COLOR
                    + message.getContentDisplay());
            GamePlayer.getPlayersList(Channel.DISCORD).forEach(gamePlayer -> {
                gamePlayer.getPlayer().sendMessage(formatMessage);
            });
        }
    }
}
