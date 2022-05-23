package pl.norbit.discordmc.server.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.utils.DiscordInfo;

public class ConfigManager {

    public static void loadConfig(JavaPlugin javaPlugin) {

        FileConfiguration config = javaPlugin.getConfig();
        config.options().copyDefaults();
        javaPlugin.saveDefaultConfig();

        DiscordInfo.setToken(config.getString("token"));
        DiscordInfo.setDiscordPrefix(config.getString("discordprefix"));
        DiscordInfo.setMcPrefix(config.getString("mcprefix"));
        DiscordInfo.setChannelName(config.getString("channelname"));
        DiscordInfo.setBotActivity(config.getString("botactivity"));
    }


}
