package pl.norbit.discordmc.bot.events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.norbit.discordmc.bot.utils.DiscordInfo;
import pl.norbit.discordmc.server.enums.Channel;
import pl.norbit.discordmc.server.objects.GamePlayer;

public class OnSendMessageEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message message = event.getMessage();
        String channelName = message.getChannel().getName();

        if(channelName.equals(DiscordInfo.channelName)){
            User author = event.getAuthor();

            GamePlayer.getPlayersList(Channel.DISCORD).forEach(gamePlayer -> {
                String formatMessage = DiscordInfo.discordPrefix + " " + author.getName() + ": "
                        + message.getContentDisplay();
                gamePlayer.getPlayer().sendMessage(formatMessage);
            });
        }
    }
}
