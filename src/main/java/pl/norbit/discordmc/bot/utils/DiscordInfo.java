package pl.norbit.discordmc.bot.utils;

public class DiscordInfo {
    public static String channelName, discordPrefix, token, mcPrefix, botActivity, nickColor, messageColor, messageMark;
    private static boolean discordChat;

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

    public static String getNickColor() {
        return nickColor;
    }

    public static void setNickColor(String nickColor) {
        DiscordInfo.nickColor = nickColor;
    }

    public static String getMessageColor() {
        return messageColor;
    }

    public static void setMessageColor(String messageColor) {
        DiscordInfo.messageColor = messageColor;
    }

    public static String getMessageMark() {
        return messageMark;
    }

    public static void setMessageMark(String messageMark) {
        DiscordInfo.messageMark = messageMark;
    }

    public static boolean isDiscordChat() {
        return discordChat;
    }

    public static void setDiscordChat(boolean discordChat) {
        DiscordInfo.discordChat = discordChat;
    }
}
