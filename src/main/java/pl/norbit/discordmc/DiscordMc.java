package pl.norbit.discordmc;

import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.DiscordBot;
import pl.norbit.discordmc.server.config.PluginConfig;
import pl.norbit.discordmc.server.commands.ChangeChannel;
import pl.norbit.discordmc.server.config.ConfigManager;
import pl.norbit.discordmc.server.events.EventManager;

public final class DiscordMc extends JavaPlugin {
    private DiscordBot discordBot;

    @Override
    public void onEnable() {

        ConfigManager.loadConfig(this);

        if(PluginConfig.PLUGIN_ENABLE) {

            discordBot = new DiscordBot(PluginConfig.TOKEN);
            discordBot.start();

            //events
            EventManager.registerEvents(this, discordBot.getJda());

            //commands
            getServer().getPluginCommand(PluginConfig.COMMAND_PREFIX).setExecutor(new ChangeChannel());


        } else {
            System.out.println("enable plugin in config.yml");
        }
    }

    @Override
    public void onDisable() {
        discordBot.close();
    }

}
