package pl.norbit.discordmc.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.DiscordMc;

import java.util.List;

public class ConfigManager {

    public static void loadConfig(boolean start){

        JavaPlugin javaPlugin = DiscordMc.getInstance();

        if(!start){
            javaPlugin.reloadConfig();
        }

        FileConfiguration config = javaPlugin.getConfig();

        if(start) {
            config.options().copyDefaults();
            javaPlugin.saveDefaultConfig();
        }

        PluginConfig.MODULE_OFF_MESSAGE = "&cModule {MODULE} is disable! Enable module in config.yml";
        PluginConfig.PLACEHOLDER_API_EXIST = false;

        //plugin options
        PluginConfig.SERVER_ID = config.getString("discord-server-id");
        PluginConfig.PLUGIN_ENABLE = config.getBoolean("enable");
        PluginConfig.COMMAND_PREFIX = config.getString("prefix-command");
        PluginConfig.TOKEN = config.getString("bot-token");
        PluginConfig.DISCORD_PREFIX = config.getString("discord-prefix");
        PluginConfig.BOT_ACTIVITY = config.getString("bot-activity");
        PluginConfig.WRONG_ARGS_MESSAGE = config.getString("wrong-args-message");
        PluginConfig.SYNC_COMMAND_ARG = config.getString("sync-command-arg");
        PluginConfig.DATABASE_TYPE = config.getString("database-type");
        PluginConfig.SYNC_COMMAND_CLEAR_ARG = config.getString("sync-command-clear-arg");
        PluginConfig.SYNC_CLEAR_MESSAGE = config.getString("sync-clear-mc");
        PluginConfig.PERMISSION_MESSAGE = config.getString("permission-message");

        PluginConfig.EMBED_PROFILE_ONLINE = config.getStringList("embed-profile-online");
        PluginConfig.EMBED_PROFILE_OFFLINE = config.getStringList("embed-profile-offline");

        PluginConfig.EMBED_PROFILE_TITLE = config.getString("embed-profile-title");
        PluginConfig.USER_IS_NOT_SYNC = config.getString("user-in-not-sync");
        PluginConfig.ARG_USER_IS_NOT_SYNC = config.getString("arg-user-in-not-sync");
        PluginConfig.DISCORD_USER_IS_SYNC = config.getString("discord-user-is-sync");
        PluginConfig.SYNC_ROLES = config.getStringList("sync-roles");
        PluginConfig.SYNC_RANK_ENABLE = config.getBoolean("sync-rank-enable");
        PluginConfig.SYNC_NAME = config.getBoolean("sync-name");

        List<Integer> profileRGB = config.getIntegerList("embed-profile-rgb");
        PluginConfig.EMBED_PROFILE_R = profileRGB.get(0);
        PluginConfig.EMBED_PROFILE_G = profileRGB.get(1);
        PluginConfig.EMBED_PROFILE_B = profileRGB.get(2);

        //discord-chat-module
        PluginConfig.MC_PREFIX = config.getString("mc-prefix");
        PluginConfig.CHAT_MODULE = config.getBoolean("discord-chat-module");
        PluginConfig.CHAT_CHANNEL_ID = config.getString("channel-chat-id");
        PluginConfig.NICK_COLOR = config.getString("nick-color");
        PluginConfig.MESSAGE_COLOR = config.getString("message-color");
        PluginConfig.MESSAGE_MARK = config.getString("message-mark");
        PluginConfig.DISCORD_MESSAGE_MARK = config.getString("discord-message-mark");
        PluginConfig.COMMAND_CHAT_CHANGE = config.getString("command-chat-change-arg");
        PluginConfig.MINECRAFT_CHAT_ARG = config.getString("minecraft-chat-arg");
        PluginConfig.DISCORD_CHAT_ARG = config.getString("discord-chat-arg");
        PluginConfig.CHANNEL_CHANGE_MESSAGE = config.getString("channel-change-message");
        PluginConfig.COMMAND_CHAT_CHANGE_ARG = config.getString("command-chat-change-arg");;

        PluginConfig.COMMAND_ARGS_LIST = config.getStringList("command-args-list");

        List<Integer> rgbDiscordList = config.getIntegerList("discord-user-rgb");
        PluginConfig.EMBED_DISCORD_R = rgbDiscordList.get(0);
        PluginConfig.EMBED_DISCORD_G = rgbDiscordList.get(1);
        PluginConfig.EMBED_DISCORD_B = rgbDiscordList.get(2);

        List<Integer> rgbMcList = config.getIntegerList("mc-user-rgb");
        PluginConfig.EMBED_MC_R = rgbMcList.get(0);
        PluginConfig.EMBED_MC_G = rgbMcList.get(1);
        PluginConfig.EMBED_MC_B = rgbMcList.get(2);

        //database
        PluginConfig.DATABASE_PORT = config.getInt("port");
        PluginConfig.DATABASE_HOST = config.getString("host");
        PluginConfig.DATABASE_PASS = config.getString("password");
        PluginConfig.DATABASE_USER = config.getString("user");
        PluginConfig.DATABASE_NAME = config.getString("database");
        PluginConfig.DATABASE_SSL = config.getBoolean("ssl");

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
        PluginConfig.SYNC_INFO_DC = config.getString("sync-info");
        PluginConfig.SYNC_SUCCESS_DC = config.getString("sync-success");

        //mc messages
        PluginConfig.SYNC_INFO_MC = config.getString("sync-info-mc");
        PluginConfig.SYNC_SUCCESS_MC = config.getString("sync-success-mc");
        PluginConfig.SYNC_TIME_OUT = config.getString("sync-time-out");

        //discord console module
        PluginConfig.BLOCKED_COMMANDS = config.getStringList("blocked-commands");
        PluginConfig.CONSOLE_MODULE = config.getBoolean("discord-console-module");
        PluginConfig.CONSOLE_CHANNEL_ID = config.getString("channel-console-id");
        PluginConfig.DISCORD_CONSOLE_COMMANDS = config.getBoolean("discord-console-commands");
        PluginConfig.BLOCK_WARN_MESSAGES = config.getBoolean("block-warn-messages");
        PluginConfig.BLOCK_ERROR_MESSAGES = config.getBoolean("block-error-messages");
        PluginConfig.DISCORD_CONSOLE_DISPLAY = config.getBoolean("discord-console-display");

        //discord info module
        PluginConfig.DISCORD_INFO_MODULE = config.getBoolean("discord-info-module");
        PluginConfig.CHANNEL_INFO_ID = config.getString("channel-info-id");
        PluginConfig.IGNORE_FOLDERS = config.getStringList("ignore-folders");
        List<Integer> rgbInfoList = config.getIntegerList("discord-info-rgb");
        PluginConfig.EMBED_INFO_R = rgbInfoList.get(0);
        PluginConfig.EMBED_INFO_G = rgbInfoList.get(1);
        PluginConfig.EMBED_INFO_B = rgbInfoList.get(2);
        PluginConfig.EMBED_INFO_TITTLE = config.getString("embed-tittle");
        PluginConfig.EMBED_INFO_DESC = config.getString("embed-desc");
        PluginConfig.EMBED_INFO_ARGS = config.getStringList("embed-info");
        PluginConfig.FALSE_INFO = config.getString("false-info");
        PluginConfig.TRUE_INFO = config.getString("true-info");
        PluginConfig.MESSAGE_RELOAD_TIME = config.getInt("message-reload-time");

        PluginConfig.RANK_RELOAD_COMMAND_ARG = config.getString("rank-reload-command-arg");
        PluginConfig.PLAYER_IS_NOT_SYNC = config.getString("player-is-not-sync");
        PluginConfig.RANK_RELOAD_MESSAGE = config.getString("rank-reload-message");
    }
}
