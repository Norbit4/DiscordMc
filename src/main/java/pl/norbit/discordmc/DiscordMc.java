package pl.norbit.discordmc;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.DiscordBot;
import pl.norbit.discordmc.bot.utils.DiscordInfo;
import pl.norbit.discordmc.server.commands.ChangeChannel;
import pl.norbit.discordmc.server.events.OnJoinServerEvent;
import pl.norbit.discordmc.server.events.OnMessageEvent;

public final class DiscordMc extends JavaPlugin {
    private DiscordBot discordBot;

    @Override
    public void onEnable() {

        discordBot = new DiscordBot(DiscordInfo.token);
        discordBot.start();

        //events
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new OnJoinServerEvent(),this);
        pluginManager.registerEvents(new OnMessageEvent(discordBot.getJda()),this);

        //commands
        getServer().getPluginCommand("discord").setExecutor(new ChangeChannel());
    }

    @Override
    public void onDisable() {
        discordBot.close();
    }
}
