package pl.norbit.discordmc.bot.builder;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import pl.norbit.discordmc.bot.events.OnSendMessageEvent;
import pl.norbit.discordmc.bot.utils.DiscordInfo;

public class BotBuilder {
    public static JDABuilder getBuilder(String token){
        JDABuilder builder = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListeners(new OnSendMessageEvent());

        if(!DiscordInfo.getBotActivity().equals("")){
            builder.setActivity(Activity.watching(DiscordInfo.getBotActivity()));
        }
        return builder;
    }
}
