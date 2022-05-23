package pl.norbit.discordmc.bot.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.norbit.discordmc.bot.utils.ChatUtil;
import pl.norbit.discordmc.bot.utils.DiscordInfo;
import pl.norbit.discordmc.server.enums.Channel;
import pl.norbit.discordmc.server.objects.GamePlayer;

public class OnSendMessageEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String channelName = message.getChannel().getName();

        if(channelName.equals(DiscordInfo.channelName)){

            sendMessage(event);
        }
    }

    private void sendMessage(MessageReceivedEvent event){
        User author = event.getAuthor();
        Message message = event.getMessage();

        GamePlayer.getPlayersList(Channel.DISCORD).forEach(gamePlayer -> {
            String formatMessage;
            String botName = event.getJDA().getSelfUser().getAsTag();

            if(author.getAsTag().equals(botName)){
                String messageDiscord = message.getContentDisplay();
                String[] spiltMessage = messageDiscord.split(DiscordInfo.getMessageMark());
                String nickName = spiltMessage[0].replace(" ","");
                String playerMessage = spiltMessage[1].replace(" ","");;

                formatMessage = ChatUtil.format(DiscordInfo.mcPrefix + DiscordInfo.getNickColor() + nickName
                        + DiscordInfo.getMessageMark() + DiscordInfo.getMessageColor() + playerMessage);
            }else {
                formatMessage = ChatUtil.format(DiscordInfo.discordPrefix + DiscordInfo.getNickColor()
                        + author.getAsTag() + DiscordInfo.getMessageMark() + DiscordInfo.getMessageColor()
                        + message.getContentDisplay());
            }
            gamePlayer.getPlayer().sendMessage(formatMessage);
        });
    }
}
