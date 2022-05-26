package pl.norbit.discordmc.bot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class CommandManager {

    public CommandManager(JDA jda) {

        try {
            jda.awaitReady().getGuildById("691008107345739826").upsertCommand("sync","sync mc with dc")
                    .addOption(OptionType.STRING, CmdOptions.NICK.name().toLowerCase(), "o", true)
                    .queue();

            jda.updateCommands().queue();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
