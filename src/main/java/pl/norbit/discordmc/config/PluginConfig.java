package pl.norbit.discordmc.config;

import java.util.List;

public class PluginConfig {

    public static String CHAT_CHANNEL_ID, DISCORD_PREFIX, TOKEN, MC_PREFIX, BOT_ACTIVITY, NICK_COLOR, MESSAGE_COLOR,
            MESSAGE_MARK, DISCORD_MESSAGE_MARK, COMMAND_PREFIX, DATABASE_HOST, DATABASE_PASS, DATABASE_USER, DATABASE_NAME,
            PLAYER_IS_SYNC_DC, OFFLINE_PLAYER_DC, SYNC_INFO_DC, SYNC_SUCCESS_DC, SYNC_INFO_MC, SYNC_SUCCESS_MC,
            SYNC_TIME_OUT, SUCCESS_TITTLE, WARN_TITTLE, ERROR_TITTLE, SERVER_ID, CONSOLE_CHANNEL_ID, WRONG_ARGS_MESSAGE,
            COMMAND_CHAT_CHANGE, MINECRAFT_CHAT_ARG, DISCORD_CHAT_ARG, CHANNEL_CHANGE_MESSAGE, COMMAND_CHAT_CHANGE_ARG,
            SYNC_COMMAND_ARG, EMBED_INFO_TITTLE, EMBED_INFO_DESC, FALSE_INFO, TRUE_INFO, CHANNEL_INFO_ID, DATABASE_TYPE,
            SYNC_COMMAND_CLEAR_ARG, SYNC_CLEAR_MESSAGE, MODULE_OFF_MESSAGE, PERMISSION_MESSAGE, EMBED_PROFILE_TITLE,
            ARG_USER_IS_NOT_SYNC, USER_IS_NOT_SYNC, DISCORD_USER_IS_SYNC, RANK_RELOAD_COMMAND_ARG, PLAYER_IS_NOT_SYNC,
            RANK_RELOAD_MESSAGE;

    public static List<String> COMMAND_ARGS_LIST,EMBED_INFO_ARGS, EMBED_PROFILE_ONLINE, BLOCKED_COMMANDS,
            IGNORE_FOLDERS, SYNC_ROLES, EMBED_PROFILE_OFFLINE, COMMANDS_ON_SYNC, COMMANDS_ON_SYNC_CLEAR;

    public static boolean PLUGIN_ENABLE, DATABASE_SSL, CHAT_MODULE, CONSOLE_MODULE, DISCORD_CONSOLE_COMMANDS,
            BLOCK_WARN_MESSAGES, BLOCK_ERROR_MESSAGES, DISCORD_INFO_MODULE, SYNC_RANK_ENABLE, SYNC_NAME,
            DISCORD_CONSOLE_DISPLAY, PLACEHOLDER_API_EXIST, EXECUTE_COMMAND_ON_SYNC, EXECUTE_COMMAND_ON_SYNC_CLEAR;

    public static int EMBED_DISCORD_R, EMBED_DISCORD_G, EMBED_DISCORD_B, EMBED_MC_R, EMBED_MC_G, EMBED_MC_B, DATABASE_PORT,
            EMBED_ERROR_R, EMBED_ERROR_G, EMBED_ERROR_B, EMBED_WARN_R, EMBED_WARN_G, EMBED_WARN_B, EMBED_SUCCESS_R,
            EMBED_SUCCESS_G, EMBED_SUCCESS_B, EMBED_INFO_R, EMBED_INFO_G, EMBED_INFO_B, MESSAGE_RELOAD_TIME,
            EMBED_PROFILE_R, EMBED_PROFILE_G, EMBED_PROFILE_B;
}

