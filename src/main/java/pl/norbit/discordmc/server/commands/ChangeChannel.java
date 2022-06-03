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
import pl.norbit.discordmc.server.enums.Channel;
import pl.norbit.discordmc.server.objects.GamePlayer;
import pl.norbit.discordmc.sync.SyncPlayer;
import pl.norbit.discordmc.sync.SyncTimerTask;

import java.awt.*;
import java.util.List;
import java.util.UUID;

public class ChangeChannel implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;

            Player player = (Player) sender;
            if(args.length > 0) {

                if (args[0].equalsIgnoreCase(PluginConfig.COMMAND_CHAT_CHANGE_ARG)) {

                    GamePlayer gamePlayer = GamePlayer.getGamePLayer(player);

                    if(args.length > 1) {
                        String arg = args[1].toUpperCase();

                        if (arg.equalsIgnoreCase("GLOBAL") || arg.equalsIgnoreCase("DISCORD")){
                            Channel ch = Channel.valueOf(arg);

                            gamePlayer.setChannel(ch);

                            String message = PluginConfig.CHANNEL_CHANGE_MESSAGE
                                    .replace("{CHANNEL}", ch.toString());

                            player.sendMessage(ChatUtil.format(message));
                        } else {

                            sendArgWarnMessage(player);
                        }
                    }else {

                        sendArgWarnMessage(player);
                    }

                }else if(args[0].equalsIgnoreCase(PluginConfig.SYNC_COMMAND_ARG)){
                    syncCmd(player);

                } else {

                    sendHelpMessage(player);
                }
            }else{
                sendHelpMessage(player);
            }

        return true;
    }

    private void sendHelpMessage(Player player){
        List<String> argsList = PluginConfig.COMMAND_ARGS_LIST;

        for (String message : argsList) {

            String formatMessage = message.replace("{PREFIX}", PluginConfig.COMMAND_PREFIX);

            player.sendMessage(ChatUtil.format(formatMessage));
        }
    }
    private void sendArgWarnMessage(Player player){

        String message = PluginConfig.WRONG_ARGS_MESSAGE
                .replace("{PREFIX}", PluginConfig.COMMAND_PREFIX)
                .replace("{ARG1}", PluginConfig.COMMAND_CHAT_CHANGE_ARG)
                .replace("{ARG2}", "discord/global");

        player.sendMessage(ChatUtil.format(message));
    }

    private void syncCmd(Player player){

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

            String message = PluginConfig.SYNC_SUCCESS_DC.replace("{DISCORD}",
                    syncPlayer.getUser().getAsTag()).replace("{PLAYER}", gamePlayer.getPlayer().getName());

            MessageEmbed embed = Embed.getInfoMessage(PluginConfig.SUCCESS_TITTLE, message,
                            new Color(PluginConfig.EMBED_SUCCESS_R, PluginConfig.EMBED_SUCCESS_G, PluginConfig.EMBED_SUCCESS_B))
                    .build();

            syncPlayer.getMessageChannel().sendMessageEmbeds(embed).queue();

            String mcMessage = PluginConfig.SYNC_SUCCESS_MC.replace("{DISCORD}",
                    syncPlayer.getUser().getAsTag()).replace("{PLAYER}", gamePlayer.getPlayer().getName());

            player.sendMessage(ChatUtil.format(mcMessage));
        } else{
            String mcMessage = PluginConfig.SYNC_TIME_OUT;

            player.sendMessage(ChatUtil.format(mcMessage));
        }
    }
}
