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
                .upsertCommand("sync", "Sync your account with minecraft")
                .addOption(OptionType.STRING,
                        CmdOptions.NICK.name().toLowerCase(), "Your minecraft nickname", true)
                .queue();

        guildById
                .upsertCommand("profile", "Show your profile")
                .addOption(OptionType.MENTIONABLE,
                        CmdOptions.MENTION.name().toLowerCase(), "Discord tag", false)
                .queue();
        DiscordMc.getJda().updateCommands().queue();
    }

}
