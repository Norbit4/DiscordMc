package pl.norbit.discordmc;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.DiscordBot;
import pl.norbit.discordmc.bot.builder.BotBuilder;
import pl.norbit.discordmc.bot.commands.CommandManager;
import pl.norbit.discordmc.db.MongoDB;
import pl.norbit.discordmc.db.PluginDBManager;
import pl.norbit.discordmc.server.commands.SyncCommandMc;
import pl.norbit.discordmc.server.config.PluginConfig;
import pl.norbit.discordmc.server.commands.ChangeChannel;
import pl.norbit.discordmc.server.config.ConfigManager;
import pl.norbit.discordmc.server.events.EventManager;
import pl.norbit.discordmc.serverinfo.InfoUpdater;
import pl.norbit.discordmc.sync.SyncTimerTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class DiscordMc extends JavaPlugin {
    private DiscordBot discordBot;
    private final static ExecutorService executorService;

    static {
        executorService = Executors.newFixedThreadPool(2);
    }

    @Override
    public void onEnable() {

        ConfigManager.loadConfig(this);


        if(PluginConfig.PLUGIN_ENABLE) {
            BotBuilder.init(this);

            discordBot = new DiscordBot(PluginConfig.TOKEN, this);
            discordBot.start();

            if(PluginConfig.CONSOLE_MODULE){
                MessageChannel messageChannel = null;
                try {
                    messageChannel = discordBot.getJda().awaitReady().getTextChannelById(PluginConfig.CONSOLE_CHANNEL_ID);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(messageChannel == null){

                    this.getPluginLoader().disablePlugin(this);
                }

            }

            if(PluginConfig.CHAT_MODULE){
                MessageChannel messageChannel = null;
                try {
                    messageChannel = discordBot.getJda().awaitReady().getTextChannelById(PluginConfig.CHAT_CHANNEL_ID);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if(messageChannel == null){
                    this.getPluginLoader().disablePlugin(this);
                }

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
            getServer().getPluginCommand("dc").setExecutor(new ChangeChannel());
            getServer().getPluginCommand("sync").setExecutor(new SyncCommandMc());

            PluginDBManager.init(discordBot.getJda());
            InfoUpdater.start(discordBot.getJda());

            SyncTimerTask.runTaskTimer(this);
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Enable plugin in config.yml");
        }
    }

    @Override
    public void onDisable() {
        discordBot.close();
        MongoDB.close();
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }
}
