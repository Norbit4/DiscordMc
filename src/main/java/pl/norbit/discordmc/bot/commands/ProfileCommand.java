package pl.norbit.discordmc.bot.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.players.DiscordPlayer;
import pl.norbit.discordmc.players.DiscordPlayerService;

import java.awt.*;
import java.util.List;

public class ProfileCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("profile")) return;

        String discordId;
        String message;

        if (event.getOptions().isEmpty()) {
            discordId = event.getUser().getId();

            message = PluginConfig.USER_IS_NOT_SYNC;
        } else {
            IMentionable asMentionable = event.getOption(CmdOptions.MENTION.name().toLowerCase()).getAsMentionable();

            discordId = asMentionable.getId();

            message = PluginConfig.ARG_USER_IS_NOT_SYNC;
        }

        DiscordPlayer discordPlayer = DiscordPlayerService.getGamePLayerByDiscordID(discordId);

        if (discordPlayer == null) {
            MessageEmbed embed = Embed.getInfoMessage(
                            PluginConfig.ERROR_TITTLE, message,
                            new Color(
                                    PluginConfig.EMBED_ERROR_R,
                                    PluginConfig.EMBED_ERROR_G,
                                    PluginConfig.EMBED_ERROR_B))
                    .build();
            event.reply("").addEmbeds(embed).queue();
            return;
        }
        Player player = Bukkit.getPlayer(discordPlayer.getPlayerUUID());

        List<String> lines = PluginConfig.EMBED_PROFILE_ONLINE;

        if(player == null) {
            lines = PluginConfig.EMBED_PROFILE_OFFLINE;
        }

        EmbedBuilder profileMessage = Embed.getProfileMessage();

        for (String line : lines) {
            if (line.equalsIgnoreCase("{EMPTY_LINE}")) {
                profileMessage.addBlankField(false);
            } else {
                String formattedLine;

                if (PluginConfig.PLACEHOLDER_API_EXIST) {
                    if(player != null) {
                        formattedLine = PlaceholderAPI.setPlaceholders(player, line);

                        profileMessage.setColor(new Color(62, 249, 36));
                    } else {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(discordPlayer.getPlayerUUID());

                        formattedLine = PlaceholderAPI.setPlaceholders(offlinePlayer, line);
                        profileMessage.setColor(new Color(212, 29, 25));
                    }
                } else {
                    formattedLine = line;
                }

                String[] lineArgs = formattedLine.split("//");
                if (lineArgs.length > 1) {
                    String arg1 = lineArgs[0];
                    String arg2 = lineArgs[1];

                    profileMessage.addField(arg1, arg2, false);
                }
            }
        }
        event.reply("").addEmbeds(profileMessage.build()).queue();
    }
}
