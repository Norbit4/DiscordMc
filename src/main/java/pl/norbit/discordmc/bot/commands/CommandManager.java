package pl.norbit.discordmc.bot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.server.config.PluginConfig;

public class CommandManager {

    public CommandManager(JDA jda, JavaPlugin javaPlugin) {
        Guild guildById;

        try {
            guildById = jda.awaitReady().getGuildById(PluginConfig.SERVER_ID);
        } catch (InterruptedException e) {
            //javaPlugin.getPluginLoader().disablePlugin(javaPlugin);
            throw new RuntimeException(e);
        }

        guildById
                .upsertCommand("sync", "sync mc with dc")
                .addOption(OptionType.STRING,
                        CmdOptions.NICK.name().toLowerCase(), "o", true)
                .queue();

        guildById
                .upsertCommand("profile", "show your profile")
                .addOption(OptionType.MENTIONABLE,
                        CmdOptions.MENTION.name().toLowerCase(), "o", false)
                .queue();
        jda.updateCommands().queue();
    }

}
