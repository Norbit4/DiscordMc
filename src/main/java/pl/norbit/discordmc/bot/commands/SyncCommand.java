package pl.norbit.discordmc.bot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.utils.ChatUtil;
import pl.norbit.discordmc.db.PluginDBManager;
import pl.norbit.discordmc.db.objects.DatabaseRecord;
import pl.norbit.discordmc.server.config.PluginConfig;
import pl.norbit.discordmc.server.objects.GamePlayer;
import pl.norbit.discordmc.sync.SyncPlayer;
import pl.norbit.discordmc.sync.SyncTimerTask;

import java.awt.*;

public class SyncCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getName().equals("sync")){
            String playerNick = event.getOption(CmdOptions.NICK.name().toLowerCase()).getAsString();

            Player player = Bukkit.getPlayer(playerNick);

            if(player != null ) {

                SyncPlayer syncPlayer = SyncTimerTask.getSyncPlayer(player.getUniqueId());

                if(syncPlayer == null) {

                    GamePlayer gamePlayer = GamePlayer.getGamePLayer(player);

                    if (gamePlayer.getDiscordUser() == null) {
                        DatabaseRecord user = PluginDBManager.getUser(event.getUser().getId());
                        if(user == null) {

                            SyncTimerTask.addSyncPlayer(new SyncPlayer(player, event.getUser(), event.getChannel()));

                            String message = PluginConfig.SYNC_INFO_DC
                                    .replace("{PREFIX}", PluginConfig.COMMAND_PREFIX)
                                    .replace("{ARG1}", PluginConfig.SYNC_COMMAND_ARG);


                            MessageEmbed embed = Embed.getInfoMessage(PluginConfig.SUCCESS_TITTLE, message,
                                    new Color(PluginConfig.EMBED_SUCCESS_R,
                                            PluginConfig.EMBED_SUCCESS_G,
                                            PluginConfig.EMBED_SUCCESS_B))
                                    .build();

                            event.reply("").addEmbeds(embed).queue();

                            String mcFormatMessage = PluginConfig.SYNC_INFO_MC
                                    .replace("{DISCORD}", event.getUser().getAsTag())
                                    .replace("{PREFIX}", PluginConfig.COMMAND_PREFIX)
                                    .replace("{ARG1}", PluginConfig.SYNC_COMMAND_ARG);

                            player.sendMessage(ChatUtil.format(mcFormatMessage));
                        }else{
                            MessageEmbed embed = Embed.getInfoMessage(PluginConfig.WARN_TITTLE, PluginConfig.DISCORD_USER_IS_SYNC,
                                            new Color(
                                                    PluginConfig.EMBED_WARN_R,
                                                    PluginConfig.EMBED_WARN_G,
                                                    PluginConfig.EMBED_WARN_B))
                                    .build();

                            event.reply("").addEmbeds(embed).queue();
                        }
                    } else {
                        MessageEmbed embed = Embed.getInfoMessage(PluginConfig.WARN_TITTLE, PluginConfig.PLAYER_IS_SYNC_DC,
                                        new Color(
                                                PluginConfig.EMBED_WARN_R,
                                                PluginConfig.EMBED_WARN_G,
                                                PluginConfig.EMBED_WARN_B))
                                .build();

                        event.reply("").addEmbeds(embed).queue();
                    }
                }else{
                    MessageEmbed embed = Embed.getInfoMessage(PluginConfig.WARN_TITTLE, "Time: " + syncPlayer.getTime(),
                                    new Color(
                                            PluginConfig.EMBED_WARN_R,
                                            PluginConfig.EMBED_WARN_G,
                                            PluginConfig.EMBED_WARN_B))
                            .build();

                    event.reply("").addEmbeds(embed).queue();
                }
            }else{
                MessageEmbed embed = Embed.getInfoMessage(PluginConfig.ERROR_TITTLE ,PluginConfig.OFFLINE_PLAYER_DC,
                        new Color(
                                PluginConfig.EMBED_ERROR_R,
                                PluginConfig.EMBED_ERROR_G,
                                PluginConfig.EMBED_ERROR_B))
                        .build();

                event.reply("").addEmbeds(embed).queue();
            }
        }
    }
}
