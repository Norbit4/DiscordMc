package pl.norbit.discordmc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.DiscordBot;
import pl.norbit.discordmc.bot.builder.BotBuilder;
import pl.norbit.discordmc.bot.commands.CommandManager;
import pl.norbit.discordmc.sync.SyncManager;
import pl.norbit.discordmc.utils.ChatUtil;
import pl.norbit.discordmc.db.PluginDBManager;
import pl.norbit.discordmc.server.config.PluginConfig;
import pl.norbit.discordmc.server.commands.MainCMD;
import pl.norbit.discordmc.server.config.ConfigManager;
import pl.norbit.discordmc.server.events.EventManager;
import pl.norbit.discordmc.serverinfo.InfoUpdater;
import pl.norbit.discordmc.sync.SyncTimerTask;

import static pl.norbit.discordmc.server.config.PluginConfig.CHAT_MODULE;

public final class DiscordMc extends JavaPlugin {
    private DiscordBot discordBot;
    private static Long timeServer;
    private static JavaPlugin javaPlugin;

    private static ConsoleCommandSender commandSender;


    @Override
    public void onEnable() {
        boolean start = false;
        try {
            start = start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(!start){
            disablePlugin();
        }
    }
    private boolean start() throws InterruptedException {
        commandSender = getServer().getConsoleSender();
        javaPlugin = this;

        onStart();

        ConfigManager.loadConfig(this, true);

        if(PluginConfig.PLUGIN_ENABLE) {
            BotBuilder.init(this);

            discordBot = new DiscordBot(PluginConfig.TOKEN, this);
            discordBot.start();

            JDA jda = discordBot.getJda();

            if(discordBot.getJda() == null) return false;

            if(jda.awaitReady().getGuildById(PluginConfig.SERVER_ID) == null) {
                sendMessage("&c[ERROR] Wrong discord server ID");
                sendMessage("&cHow to get discord server id?");
                sendMessage("&chttps://github.com/Norbit4/DiscordMc/wiki/Configuration#id");

                return false;
            }

            timeServer = System.currentTimeMillis();
            PluginDBManager.init(jda, this);


            if(PluginConfig.SYNC_RANK_ENABLE) {
                SyncManager.init(jda);
            }

            //console module
            if(PluginConfig.CONSOLE_MODULE){
                boolean b = checkDiscordChannel(PluginConfig.CONSOLE_CHANNEL_ID, "CONSOLE_MODULE");
                if(!b) return false;
            }

            //chat module
            if(CHAT_MODULE){
                boolean b = checkDiscordChannel(PluginConfig.CHAT_CHANNEL_ID, "CHAT_MODULE");
                if(!b) return false;
            }

            new CommandManager(discordBot.getJda(), this);

            //logger
            if(PluginConfig.CONSOLE_MODULE) {
                LogAppender appender = new LogAppender(discordBot.getJda(), this);
                appender.start();

                Logger logger = (Logger) LogManager.getRootLogger();
                logger.addAppender(appender);
            }

            //events
            EventManager.registerEvents(this, discordBot.getJda());

            //commands
            getServer().getPluginCommand("discordmc").setExecutor(new MainCMD(this));

            //info module
            if(PluginConfig.DISCORD_INFO_MODULE){
                boolean b = checkDiscordChannel(PluginConfig.CHANNEL_INFO_ID, "INFO_MODULE");

                if(!b) return false;

                InfoUpdater.start(discordBot.getJda(), this);
            }

            SyncTimerTask.runTaskTimer(this);

        } else {
            sendMessage("&cEnable plugin in config.yml");
            disablePlugin();
        }
        return true;
    }

    private boolean checkDiscordChannel(String channelID, String module){

        MessageChannel messageChannel = null;
        try {
            messageChannel = discordBot.getJda().awaitReady().getTextChannelById(channelID);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(messageChannel == null){
            sendMessage("&c[ERROR] Wrong discord channel ID -> " + module);
            sendMessage("&cHow to get discord channel id?");
            sendMessage("&chttps://github.com/Norbit4/DiscordMc/wiki/Configuration#id");
        }

        return messageChannel != null;
    }

    @Override
    public void onDisable() {
        if(PluginConfig.PLUGIN_ENABLE) {
            discordBot.close();
            PluginDBManager.close();
        }
            onStop();
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
        javaPlugin.getServer().getPluginManager().disablePlugin(javaPlugin);
    }
    public static void sendMessage(String message){
        commandSender.sendMessage(ChatUtil.format(message));
    }
}
