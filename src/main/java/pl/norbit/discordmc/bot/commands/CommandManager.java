package pl.norbit.discordmc.bot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.server.config.PluginConfig;

public class CommandManager {

    public CommandManager(JDA jda, JavaPlugin javaPlugin) {

        try {
            jda.awaitReady().getGuildById(PluginConfig.SERVER_ID)
                    .upsertCommand("sync","sync mc with dc")
                    .addOption(OptionType.STRING,
                            CmdOptions.NICK.name().toLowerCase(), "o", true)
                    .queue();
            jda.awaitReady().getGuildById(PluginConfig.SERVER_ID)
                    .upsertCommand("profile","show your profile")
                    .addOption(OptionType.MENTIONABLE,
                            CmdOptions.MENTION.name().toLowerCase(), "o", false)
                    .queue();

            jda.updateCommands().queue();

        } catch (InterruptedException e) {
            javaPlugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[ERROR] Wrong server ID");
        }
    }
}
