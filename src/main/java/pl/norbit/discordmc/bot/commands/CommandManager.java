package pl.norbit.discordmc.bot.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.config.PluginConfig;

public class CommandManager {

    public CommandManager() {
        Guild guildById;

        try {
            guildById = DiscordMc.getJda().awaitReady().getGuildById(PluginConfig.SERVER_ID);
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
        DiscordMc.getJda().updateCommands().queue();
    }

}
