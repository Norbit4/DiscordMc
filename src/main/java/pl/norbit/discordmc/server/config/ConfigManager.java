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
        DiscordInfo.setDiscordPrefix(config.getString("discord-prefix"));
        DiscordInfo.setMcPrefix(config.getString("mc-prefix"));
        DiscordInfo.setChannelName(config.getString("channel-name"));
        DiscordInfo.setBotActivity(config.getString("bot-activity"));
        DiscordInfo.setNickColor(config.getString("nick-color"));
        DiscordInfo.setMessageColor(config.getString("message-color"));
        DiscordInfo.setMessageMark(config.getString("message-mark"));
    }


}
