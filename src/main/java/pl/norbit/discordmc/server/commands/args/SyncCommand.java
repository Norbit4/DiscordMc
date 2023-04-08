package pl.norbit.discordmc.server.commands.args;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.players.DiscordPlayerService;
import pl.norbit.discordmc.server.commands.CommandType;
import pl.norbit.discordmc.server.commands.ServerCommand;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.sync.SyncPlayer;
import pl.norbit.discordmc.sync.SyncTimerTask;
import pl.norbit.discordmc.utils.ChatUtil;

import java.awt.*;
import java.util.UUID;

public class SyncCommand extends ServerCommand {
    public SyncCommand(String[] perms) {
        super(CommandType.SYNC, perms);
    }

    @Override
    public void execute(Player p) {

        if(!super.hasPermission(p)) return;

        UUID playerUUID = p.getUniqueId();

        SyncPlayer syncPlayer = SyncTimerTask.getSyncPlayer(playerUUID);

        if(syncPlayer != null){

            new BukkitRunnable() {
                @Override
                public void run() {
                    DiscordPlayerService.sync(playerUUID, syncPlayer.getUser().getId());

                    SyncTimerTask.removeSyncPlayer(syncPlayer);

                    String message = PluginConfig.SYNC_SUCCESS_DC.replace("{DISCORD}",
                                    syncPlayer.getUser().getAsTag())
                            .replace("{PLAYER}", syncPlayer.getPlayer().getName());

                    MessageEmbed embed = Embed.getInfoMessage(PluginConfig.SUCCESS_TITTLE, message,
                                    new Color(
                                            PluginConfig.EMBED_SUCCESS_R,
                                            PluginConfig.EMBED_SUCCESS_G,
                                            PluginConfig.EMBED_SUCCESS_B))
                            .build();

                    syncPlayer.getMessageChannel().sendMessageEmbeds(embed).queue();

                    String mcMessage = PluginConfig.SYNC_SUCCESS_MC
                            .replace("{DISCORD}",
                                    syncPlayer.getUser().getAsTag())
                            .replace("{PLAYER}",  syncPlayer.getPlayer().getName());

                    p.sendMessage(ChatUtil.format(mcMessage));
                }
            }.runTaskLaterAsynchronously(DiscordMc.getInstance(),  2);

        } else{
            String mcMessage = PluginConfig.SYNC_TIME_OUT;

            p.sendMessage(ChatUtil.format(mcMessage));
        }
    }
}
