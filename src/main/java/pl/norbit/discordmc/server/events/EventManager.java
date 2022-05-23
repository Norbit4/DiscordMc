package pl.norbit.discordmc.server.events;

import net.dv8tion.jda.api.JDA;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.utils.DiscordInfo;

public class EventManager {

    public static void registerEvents(JavaPlugin javaPlugin, JDA jda){

        PluginManager pluginManager = javaPlugin.getServer().getPluginManager();
        pluginManager.registerEvents(new OnJoinServerEvent(), javaPlugin);

        if(DiscordInfo.isDiscordChat()) {
            pluginManager.registerEvents(new OnMessageEvent(jda), javaPlugin);
        }
    }
}
