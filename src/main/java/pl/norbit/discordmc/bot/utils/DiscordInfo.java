package pl.norbit.discordmc.bot.utils;

public class DiscordInfo {
    public static String channelName, discordPrefix, token, mcPrefix, botActivity;

    static {
        channelName = "";
        discordPrefix = "";
        mcPrefix = "";
        token = "";
        botActivity = "";
    }

    public static String getChannelName() {
        return channelName;
    }

    public static void setChannelName(String channelName) {
        DiscordInfo.channelName = channelName;
    }

    public static String getDiscordPrefix() {
        return discordPrefix;
    }

    public static void setDiscordPrefix(String discordPrefix) {
        DiscordInfo.discordPrefix = discordPrefix;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        DiscordInfo.token = token;
    }

    public static String getMcPrefix() {
        return mcPrefix;
    }

    public static void setMcPrefix(String mcPrefix) {
        DiscordInfo.mcPrefix = mcPrefix;
    }

    public static String getBotActivity() {
        return botActivity;
    }

    public static void setBotActivity(String botActivity) {
        DiscordInfo.botActivity = botActivity;
    }
}
