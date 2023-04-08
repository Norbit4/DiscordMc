package pl.norbit.discordmc.server.events;

import net.dv8tion.jda.api.JDA;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.config.PluginConfig;

public class EventManager {

    public static void registerEvents(){
        JavaPlugin javaPlugin = DiscordMc.getInstance();

        PluginManager pluginManager = javaPlugin.getServer().getPluginManager();
        pluginManager.registerEvents(new OnJoinServerEvent(), javaPlugin);
        pluginManager.registerEvents(new CommandEvent(), javaPlugin);

        if(PluginConfig.CHAT_MODULE) {
            pluginManager.registerEvents(new OnMessageEvent(), javaPlugin);
        }
    }
}
