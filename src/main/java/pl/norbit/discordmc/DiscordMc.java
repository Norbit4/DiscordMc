package pl.norbit.discordmc;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.DiscordBot;
import pl.norbit.discordmc.bot.commands.CommandManager;
import pl.norbit.discordmc.db.MongoDB;
import pl.norbit.discordmc.db.PluginDBManager;
import pl.norbit.discordmc.server.commands.SyncCommandMc;
import pl.norbit.discordmc.server.config.PluginConfig;
import pl.norbit.discordmc.server.commands.ChangeChannel;
import pl.norbit.discordmc.server.config.ConfigManager;
import pl.norbit.discordmc.server.events.EventManager;
import pl.norbit.discordmc.sync.SyncTimerTask;

public final class DiscordMc extends JavaPlugin {
    private DiscordBot discordBot;

    @Override
    public void onEnable() {

        ConfigManager.loadConfig(this);

        if(PluginConfig.PLUGIN_ENABLE) {

            discordBot = new DiscordBot(PluginConfig.TOKEN);
            discordBot.start();

            new CommandManager(discordBot.getJda());

            //events
            EventManager.registerEvents(this, discordBot.getJda());

            //commands
            getServer().getPluginCommand(PluginConfig.COMMAND_PREFIX).setExecutor(new ChangeChannel());
            getServer().getPluginCommand("sync").setExecutor(new SyncCommandMc());

            PluginDBManager.init(discordBot.getJda());

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

}
