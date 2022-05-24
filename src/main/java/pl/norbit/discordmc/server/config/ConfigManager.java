package pl.norbit.discordmc.server.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    public static void loadConfig(JavaPlugin javaPlugin) {

        FileConfiguration config = javaPlugin.getConfig();
        config.options().copyDefaults();
        javaPlugin.saveDefaultConfig();

        PluginConfig.TOKEN = config.getString("token");
        PluginConfig.DISCORD_PREFIX = config.getString("discord-prefix");
        PluginConfig.MC_PREFIX = config.getString("mc-prefix");
        PluginConfig.CHANNEL_NAME = config.getString("channel-name");
        PluginConfig.BOT_ACTIVITY = config.getString("bot-activity");
        PluginConfig.NICK_COLOR = config.getString("nick-color");
        PluginConfig.MESSAGE_COLOR = config.getString("message-color");
        PluginConfig.MESSAGE_MARK = config.getString("message-mark");
        PluginConfig.CHAT_MODULE = config.getBoolean("discord-chat-module");
        PluginConfig.DISCORD_MESSAGE_MARK = config.getString("discord-message-mark");
        PluginConfig.COMMAND_PREFIX = config.getString("command-prefix");
        PluginConfig.PLUGIN_ENABLE = config.getBoolean("enable");
    }
}
