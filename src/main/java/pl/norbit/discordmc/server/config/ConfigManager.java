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

        //mongo db
        PluginConfig.MONGO_PORT = config.getInt("port");
        PluginConfig.MONGO_HOST = config.getString("host");
        PluginConfig.MONGO_PASS = config.getString("password");
        PluginConfig.MONGO_USER = config.getString("user");
        PluginConfig.MONGO_DATABASE = config.getString("database");
        PluginConfig.MONGO_SSL = config.getBoolean("ssl");

        //error dc message
        PluginConfig.ERROR_TITTLE = config.getString("error-message-tittle");
        List<Integer> rgbErrorList = config.getIntegerList("error-message-rgb");
        PluginConfig.EMBED_ERROR_R = rgbErrorList.get(0);
        PluginConfig.EMBED_ERROR_G = rgbErrorList.get(1);
        PluginConfig.EMBED_ERROR_B = rgbErrorList.get(2);

        //success dc message
        PluginConfig.SUCCESS_TITTLE = config.getString("success-message-tittle");
        List<Integer> rgbSuccessList = config.getIntegerList("success-message-rgb");
        PluginConfig.EMBED_SUCCESS_R = rgbSuccessList .get(0);
        PluginConfig.EMBED_SUCCESS_G  = rgbSuccessList .get(1);
        PluginConfig.EMBED_SUCCESS_B  = rgbSuccessList .get(2);

        //warn dc message
        PluginConfig.WARN_TITTLE = config.getString("warn-message-tittle");
        List<Integer> rgbWarnList = config.getIntegerList("warn-message-rgb");
        PluginConfig.EMBED_WARN_R = rgbWarnList.get(0);
        PluginConfig.EMBED_WARN_G  = rgbWarnList.get(1);
        PluginConfig.EMBED_WARN_B = rgbWarnList.get(2);

        //discord messages
        PluginConfig.PLAYER_IS_SYNC_DC = config.getString("player-is-sync");
        PluginConfig.OFFLINE_PLAYER_DC = config.getString("offline-player");
        PluginConfig.SYNC_INFO_DC = config.getString("sync-info");
        PluginConfig.SYNC_SUCCESS_DC = config.getString("sync-success");

        //mc messages
        PluginConfig.SYNC_INFO_MC = config.getString("sync-info-mc");
        PluginConfig.SYNC_SUCCESS_MC = config.getString("sync-success-mc");
        PluginConfig.SYNC_TIME_OUT = config.getString("sync-time-out");

        PluginConfig.SERVER_ID = config.getString("server-id");

    }
}
