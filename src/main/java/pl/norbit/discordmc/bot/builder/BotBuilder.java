package pl.norbit.discordmc.bot.builder;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.commands.SyncCommand;
import pl.norbit.discordmc.bot.events.OnMessageChatEvent;
import pl.norbit.discordmc.bot.events.OnMessageConsoleEvent;
import pl.norbit.discordmc.server.config.PluginConfig;

public class BotBuilder {
    private static JavaPlugin javaPlugin;

    public static void init(JavaPlugin javaPlugin){
        BotBuilder.javaPlugin = javaPlugin;
    }

    public static JDABuilder getBuilder(String token){
        JDABuilder builder = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListeners(new SyncCommand());

        if(!PluginConfig.BOT_ACTIVITY.equals("")){
            builder.setActivity(Activity.playing(PluginConfig.BOT_ACTIVITY));
        }

        if(PluginConfig.CHAT_MODULE){
            builder.addEventListeners(new OnMessageChatEvent());
        }

        if (PluginConfig.CONSOLE_MODULE){
            if(PluginConfig.DISCORD_CONSOLE_COMMANDS) {
                builder.addEventListeners(new OnMessageConsoleEvent(javaPlugin));
            }
        }
        return builder;
    }
}
