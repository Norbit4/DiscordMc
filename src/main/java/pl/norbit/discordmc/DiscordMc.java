package pl.norbit.discordmc;

import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.DiscordBot;
import pl.norbit.discordmc.bot.utils.DiscordInfo;
import pl.norbit.discordmc.server.commands.ChangeChannel;
import pl.norbit.discordmc.server.config.ConfigManager;
import pl.norbit.discordmc.server.events.EventManager;

public final class DiscordMc extends JavaPlugin {
    private DiscordBot discordBot;

    @Override
    public void onEnable() {

        ConfigManager.loadConfig(this);

        discordBot = new DiscordBot(DiscordInfo.token);
        discordBot.start();

        //events
        EventManager.registerEvents(this, discordBot.getJda());

        //commands
        getServer().getPluginCommand("dc").setExecutor(new ChangeChannel());
    }

    @Override
    public void onDisable() {
        discordBot.close();
    }

}
