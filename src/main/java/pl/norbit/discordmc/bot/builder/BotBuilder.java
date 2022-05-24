package pl.norbit.discordmc.bot.builder;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import pl.norbit.discordmc.bot.events.OnSendMessageEvent;
import pl.norbit.discordmc.server.config.PluginConfig;

public class BotBuilder {
    public static JDABuilder getBuilder(String token){
        JDABuilder builder = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.DO_NOT_DISTURB);
        if(!PluginConfig.BOT_ACTIVITY.equals("")){
            builder.setActivity(Activity.watching(PluginConfig.BOT_ACTIVITY));
        }

        if(PluginConfig.CHAT_MODULE){
            builder.addEventListeners(new OnSendMessageEvent());
        }
        return builder;
    }
}
