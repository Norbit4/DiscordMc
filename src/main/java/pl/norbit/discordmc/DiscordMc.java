package pl.norbit.discordmc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.DiscordBot;
import pl.norbit.discordmc.bot.builder.BotBuilder;
import pl.norbit.discordmc.bot.commands.CommandManager;
import pl.norbit.discordmc.discord.DiscordUserUpdateTask;
import pl.norbit.discordmc.placeholders.DiscordPlaceholderAPI;
import pl.norbit.discordmc.server.commands.ServerCommand;
import pl.norbit.discordmc.discord.DiscordUserService;
import pl.norbit.discordmc.utils.ChatUtil;
import pl.norbit.discordmc.db.DatabaseService;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.server.commands.MainCMD;
import pl.norbit.discordmc.config.ConfigManager;
import pl.norbit.discordmc.server.events.EventManager;
import pl.norbit.discordmc.serverinfo.InfoUpdater;
import pl.norbit.discordmc.sync.SyncTimerTask;
import pl.norbit.discordmc.utils.PlaceholderUtil;

public final class DiscordMc extends JavaPlugin {
    private DiscordBot discordBot;
    private static Long timeServer;
    private static DiscordMc instance;

    private static Guild guild;

    private static ConsoleCommandSender commandSender;
    private static JDA jda;

    @Override
    public void onEnable() {
        boolean start;
        try {
            start = start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(!start) disablePlugin();
    }
    private boolean start() throws InterruptedException {
        commandSender = getServer().getConsoleSender();
        instance = this;

        onStart();

        ConfigManager.loadConfig(true);

        if(!PluginConfig.PLUGIN_ENABLE) {
            sendMessage("&cEnable plugin in config.yml");
            return false;
        }

        BotBuilder.init(this);

        discordBot = new DiscordBot(PluginConfig.TOKEN);

        if(!discordBot.start()){
            sendMessage("&c[ERROR] Wrong token!");
            sendMessage("&cHow to get discord bot token?");
            sendMessage("&chttps://github.com/Norbit4/DiscordMc/wiki/Configuration#bot-token");
            return false;
        }

        jda = discordBot.getJda();

        guild = jda.awaitReady().getGuildById(PluginConfig.SERVER_ID);

        if(guild == null) {
            sendMessage("&c[ERROR] Wrong discord server ID");
            sendMessage("&cHow to get discord server id?");
            sendMessage("&chttps://github.com/Norbit4/DiscordMc/wiki/Configuration#id");

            return false;
        }

        timeServer = System.currentTimeMillis();

        PlaceholderUtil.start();
        ServerCommand.registerCommands();
        DatabaseService.init();

        DiscordUserService.init();
        DiscordUserUpdateTask.start();

        //console module
        if(PluginConfig.CONSOLE_MODULE){
            boolean check = checkDiscordChannel(PluginConfig.CONSOLE_CHANNEL_ID, "CONSOLE_MODULE");
            if(!check) return false;
        }

        //chat module
        if(PluginConfig.CHAT_MODULE){
            boolean check = checkDiscordChannel(PluginConfig.CHAT_CHANNEL_ID, "CHAT_MODULE");
            if(!check) return false;
        }

        new CommandManager();

        //logger
        if(PluginConfig.CONSOLE_MODULE) {

            if(PluginConfig.DISCORD_CONSOLE_DISPLAY) {
                LogAppender appender = new LogAppender(jda, this);
                appender.start();

                Logger logger = (Logger) LogManager.getRootLogger();
                logger.addAppender(appender);
            }else {
                LogAppender.consoleStartMessage(jda);
            }
        }

        //events
        EventManager.registerEvents();

        //commands
        getServer().getPluginCommand("discordmc").setExecutor(new MainCMD());

        //info module
        if(PluginConfig.DISCORD_INFO_MODULE){
            boolean check = checkDiscordChannel(PluginConfig.CHANNEL_INFO_ID, "INFO_MODULE");

            if(!check) return false;

            InfoUpdater.start(jda);
        }

        SyncTimerTask.runTaskTimer(this);

        if (PluginConfig.PLACEHOLDER_API_EXIST) new DiscordPlaceholderAPI().register();
        return true;
    }

    private boolean checkDiscordChannel(String channelID, String module){

        MessageChannel messageChannel;
        if(channelID.isEmpty()){
            sendModuleDisable(module);
            return false;
        }

        try {
            messageChannel = discordBot.getJda().awaitReady().getTextChannelById(channelID);
        } catch (InterruptedException e) {
            sendModuleDisable(module);
            return false;
        }

        if(messageChannel == null){
            sendModuleDisable(module);
            return false;
        }
        return true;
    }
    private static void sendModuleDisable(String module){
        sendMessage("&c[ERROR] Wrong discord channel ID -> " + module);
        sendMessage("&cHow to get discord channel id?");
        sendMessage("&chttps://github.com/Norbit4/DiscordMc/wiki/Configuration#id");
    }

    @Override
    public void onDisable() {
        try {
            if(PluginConfig.PLUGIN_ENABLE) {
                discordBot.close();
                DatabaseService.close();
            }
            onStop();
        } catch (Exception ignored) {
        }
    }

    private void onStart(){
        sendMessage("");
        sendMessage("&7------------[&aDiscordMC&7]------------");
        sendMessage("&aHI :)");
        sendMessage("&fWiki:&b https://github.com/Norbit4/DiscordMc/wiki");
        sendMessage("&fSpigot:&b https://www.spigotmc.org/resources/discordmc-1-8-1-19-sync-your-minecraft-server-with-discord-server.103901/");
        sendMessage("&fPlugin created by&e Norbit4");
        sendMessage("&7-----------------------------------");
    }

    private void onStop(){
        sendMessage("");
        sendMessage("&7------------[&cDiscordMC&7]------------");
        sendMessage("&cBYE :(");
        sendMessage("&7-----------------------------------");
    }

    public static Long getTimeServer() {
        return DiscordMc.timeServer;
    }

    public static void disablePlugin(){
        instance.getServer().getPluginManager().disablePlugin(instance);
    }
    public static void sendMessage(String message){
        commandSender.sendMessage(ChatUtil.format(message));
    }

    public static DiscordMc getInstance() {
        return instance;
    }

    public static JDA getJda() {
        return jda;
    }

    public static Guild getGuild(){
        return guild;
    }
}
