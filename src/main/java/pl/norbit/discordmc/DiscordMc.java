package pl.norbit.discordmc;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.DiscordBot;
import pl.norbit.discordmc.bot.builder.BotBuilder;
import pl.norbit.discordmc.bot.commands.CommandManager;
import pl.norbit.discordmc.bot.utils.ChatUtil;
import pl.norbit.discordmc.db.PluginDBManager;
import pl.norbit.discordmc.server.config.PluginConfig;
import pl.norbit.discordmc.server.commands.MainCMD;
import pl.norbit.discordmc.server.config.ConfigManager;
import pl.norbit.discordmc.server.events.EventManager;
import pl.norbit.discordmc.serverinfo.InfoUpdater;
import pl.norbit.discordmc.sync.SyncTimerTask;



public final class DiscordMc extends JavaPlugin {
    private DiscordBot discordBot;
    private static Long timeServer;
    private static JavaPlugin javaPlugin;

    private static ConsoleCommandSender commandSender;


    @Override
    public void onEnable() {
        commandSender = getServer().getConsoleSender();
        javaPlugin = this;

        onStart();

        ConfigManager.loadConfig(this, true);

        if(PluginConfig.PLUGIN_ENABLE) {
            BotBuilder.init(this);

            discordBot = new DiscordBot(PluginConfig.TOKEN, this);
            discordBot.start();
            timeServer = System.currentTimeMillis();
            PluginDBManager.init(discordBot.getJda(), this);

            //console module
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

            //chat module
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
            getServer().getPluginCommand("discordmc").setExecutor(new MainCMD(this));

            //info module
            if(PluginConfig.DISCORD_INFO_MODULE){
                InfoUpdater.start(discordBot.getJda(), this);
            }

            SyncTimerTask.runTaskTimer(this);

        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Enable plugin in config.yml");
            getServer().getPluginManager().disablePlugin(this);
        }
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
