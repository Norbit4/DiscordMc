package pl.norbit.discordmc.bot.builder;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import pl.norbit.discordmc.bot.events.OnSendMessageEvent;

public class BotBuilder {
    public static JDABuilder getBuilder(String token){
        JDABuilder builder = JDABuilder.createDefault(token)
                .setActivity(Activity.watching("YOUR MOM"))
                .addEventListeners(new OnSendMessageEvent())
                .setStatus(OnlineStatus.DO_NOT_DISTURB);
        return builder;
    }
}
