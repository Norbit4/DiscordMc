package pl.norbit.discordmc.server.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

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

        List<Integer> rgbDiscordList = config.getIntegerList("discord-user-rgb");
        PluginConfig.EMBED_DISCORD_R = rgbDiscordList.get(0);
        PluginConfig.EMBED_DISCORD_G = rgbDiscordList.get(1);
        PluginConfig.EMBED_DISCORD_B = rgbDiscordList.get(2);

        List<Integer> rgbMcList = config.getIntegerList("mc-user-rgb");
        PluginConfig.EMBED_MC_R = rgbMcList.get(0);
        PluginConfig.EMBED_MC_G = rgbMcList.get(1);
        PluginConfig.EMBED_MC_B = rgbMcList.get(2);

        PluginConfig.MONGO_PORT = config.getInt("port");
        PluginConfig.MONGO_HOST = config.getString("host");
        PluginConfig.MONGO_PASS = config.getString("password");
        PluginConfig.MONGO_USER = config.getString("user");
        PluginConfig.MONGO_DATABASE = config.getString("database");
        PluginConfig.MONGO_SSL = config.getBoolean("ssl");
    }
}
