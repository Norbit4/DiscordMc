package pl.norbit.discordmc.server.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.bot.utils.ChatUtil;
import pl.norbit.discordmc.db.PluginDBManager;
import pl.norbit.discordmc.server.config.PluginConfig;
import pl.norbit.discordmc.server.objects.GamePlayer;
import pl.norbit.discordmc.sync.SyncPlayer;
import pl.norbit.discordmc.sync.SyncTimerTask;

import java.awt.*;
import java.util.UUID;

public class SyncCommandMc implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        UUID playerUUID = player.getUniqueId();

        SyncPlayer syncPlayer = SyncTimerTask.getSyncPlayer(playerUUID);

        if(syncPlayer != null){
            String userID = syncPlayer.getUser().getId();
            PluginDBManager.createUser(playerUUID, userID);
            GamePlayer gamePlayer = GamePlayer.getGamePLayer(player);
            gamePlayer.setDiscordUser(userID);

            player.sendMessage(player.getName());
            player.sendMessage(syncPlayer.getUser().getAsTag());
            SyncTimerTask.removeSyncPlayer(syncPlayer);

            MessageEmbed embed = Embed.getInfoMessage(PluginConfig.SUCCESS_TITTLE," " + player.getName() + " with "
                            + syncPlayer.getUser().getAsTag() + "!",
                    new Color(PluginConfig.EMBED_SUCCESS_R, PluginConfig.EMBED_SUCCESS_G, PluginConfig.EMBED_SUCCESS_B))
                    .build();

            syncPlayer.getMessageChannel().sendMessageEmbeds(embed).queue();

            String mcMessage = PluginConfig.SYNC_SUCCESS_MC;

            player.sendMessage(ChatUtil.format(mcMessage));
        } else{
            String mcMessage = PluginConfig.SYNC_TIME_OUT;

            player.sendMessage(ChatUtil.format(mcMessage));
        }

        return true;
    }
}
