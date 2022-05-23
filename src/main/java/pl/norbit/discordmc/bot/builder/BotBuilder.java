package pl.norbit.discordmc.bot.builder;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class BotBuilder {
    public static JDABuilder build(String token){
        JDABuilder builder = JDABuilder.createDefault(token)
                .setActivity(Activity.watching("YOUR MUM"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB);
        return builder;
    }
}
