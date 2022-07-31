package pl.norbit.discordmc.server.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.bot.utils.ChatUtil;
import pl.norbit.discordmc.db.PluginDBManager;
import pl.norbit.discordmc.server.config.ConfigManager;
import pl.norbit.discordmc.server.config.PluginConfig;
import pl.norbit.discordmc.server.enums.Channel;
import pl.norbit.discordmc.server.objects.GamePlayer;
import pl.norbit.discordmc.sync.SyncPlayer;
import pl.norbit.discordmc.sync.SyncTimerTask;

import java.awt.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainCMD implements CommandExecutor {

    private JavaPlugin javaPlugin;

    public MainCMD(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if(args.length > 0) {

            AtomicBoolean permissionMessage = new AtomicBoolean(true);

            if (args[0].equalsIgnoreCase(PluginConfig.COMMAND_CHAT_CHANGE_ARG)) {
                if(PluginConfig.CHAT_MODULE) {

                    p.getEffectivePermissions().forEach(perm -> {
                        if(perm.getPermission().equalsIgnoreCase("discordmc.channel") ||
                                perm.getPermission().equalsIgnoreCase("discordmc.*") ||
                                perm.getPermission().equalsIgnoreCase("*"))
                        {
                            changeChannelCMD(args, p);
                            permissionMessage.set(false);
                        }
                    });

                    if(permissionMessage.get()){
                        String message = PluginConfig.PERMISSION_MESSAGE;
                        p.sendMessage(ChatUtil.format(message));
                    }

                }else{
                    sendHelpMessage(p);
                }

            }else if(args[0].equalsIgnoreCase(PluginConfig.SYNC_COMMAND_ARG)){

                p.getEffectivePermissions().forEach(perm -> {
                    if(perm.getPermission().equalsIgnoreCase("discordmc.sync")||
                            perm.getPermission().equalsIgnoreCase("discordmc.*") ||
                            perm.getPermission().equalsIgnoreCase("*"))
                    {
                        syncCmd(p);
                        permissionMessage.set(false);
                    }
                });

                if(permissionMessage.get()){
                    String message = PluginConfig.PERMISSION_MESSAGE;
                    p.sendMessage(ChatUtil.format(message));
                }

            } else if(args[0].equalsIgnoreCase(PluginConfig.SYNC_COMMAND_CLEAR_ARG)){

                p.getEffectivePermissions().forEach(perm -> {
                    if(perm.getPermission().equalsIgnoreCase("discordmc.syncclear")||
                            perm.getPermission().equalsIgnoreCase("discordmc.*") ||
                            perm.getPermission().equalsIgnoreCase("*"))
                    {
                        syncClearCMD(p);
                        permissionMessage.set(false);
                    }
                });

                if(permissionMessage.get()){
                    String message = PluginConfig.PERMISSION_MESSAGE;
                    p.sendMessage(ChatUtil.format(message));
                }

            }else if(args[0].equalsIgnoreCase("reload")){
                p.getEffectivePermissions().forEach(perm -> {
                    if(perm.getPermission().equalsIgnoreCase("discordmc.reload")||
                            perm.getPermission().equalsIgnoreCase("discordmc.*") ||
                            perm.getPermission().equalsIgnoreCase("*"))
                    {
                        ConfigManager.loadConfig(javaPlugin, false);

                        String message = "&aConfig has been reloaded!";
                        p.sendMessage(ChatUtil.format(message));
                    }
                });
            }else {

                sendHelpMessage(p);
            }
        }else{
            sendHelpMessage(p);
        }
        return true;
    }

    private void sendHelpMessage(Player player){
        List<String> argsList = PluginConfig.COMMAND_ARGS_LIST;

        for (String message : argsList) {

            String formatMessage = message
                    .replace("{PREFIX}", PluginConfig.COMMAND_PREFIX)
                    .replace("{SYNC_CMD}", PluginConfig.SYNC_COMMAND_ARG)
                    .replace("{SYNC_CLEAR_CMD}", PluginConfig.SYNC_COMMAND_CLEAR_ARG)
                    .replace("{CHANNEL_CMD}", PluginConfig.COMMAND_CHAT_CHANGE_ARG)
                    .replace("{CHANNEL_MC_ARG}", PluginConfig.MINECRAFT_CHAT_ARG)
                    .replace("{CHANNEL_DC_ARG}", PluginConfig.DISCORD_CHAT_ARG);

            player.sendMessage(ChatUtil.format(formatMessage));
        }
    }
    private void sendArgWarnMessage(Player player){

        String message = PluginConfig.WRONG_ARGS_MESSAGE
                .replace("{PREFIX}", PluginConfig.COMMAND_PREFIX)
                .replace("{ARG1}", PluginConfig.COMMAND_CHAT_CHANGE_ARG)
                .replace("{ARG2}", PluginConfig.MINECRAFT_CHAT_ARG + "/"
                        + PluginConfig.DISCORD_CHAT_ARG);

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

            SyncTimerTask.removeSyncPlayer(syncPlayer);

            String message = PluginConfig.SYNC_SUCCESS_DC.replace("{DISCORD}",
                    syncPlayer.getUser().getAsTag())
                    .replace("{PLAYER}", gamePlayer.getPlayer().getName());

            MessageEmbed embed = Embed.getInfoMessage(PluginConfig.SUCCESS_TITTLE, message,
                            new Color(
                                    PluginConfig.EMBED_SUCCESS_R,
                                    PluginConfig.EMBED_SUCCESS_G,
                                    PluginConfig.EMBED_SUCCESS_B)
                        )
                        .build();

            syncPlayer.getMessageChannel().sendMessageEmbeds(embed).queue();

            String mcMessage = PluginConfig.SYNC_SUCCESS_MC
                    .replace("{DISCORD}",
                    syncPlayer.getUser().getAsTag())
                    .replace("{PLAYER}", gamePlayer.getPlayer().getName());

            player.sendMessage(ChatUtil.format(mcMessage));
        } else{
            String mcMessage = PluginConfig.SYNC_TIME_OUT;

            player.sendMessage(ChatUtil.format(mcMessage));
        }
    }

    private void changeChannelCMD(String[] args, Player player){
        GamePlayer gamePlayer = GamePlayer.getGamePLayer(player);

        if (args.length > 1) {
            String arg = args[1].toUpperCase();

            if (arg.equalsIgnoreCase(PluginConfig.MINECRAFT_CHAT_ARG)
                    || arg.equalsIgnoreCase(PluginConfig.DISCORD_CHAT_ARG)) {

                String channel;
                if (PluginConfig.MINECRAFT_CHAT_ARG.equalsIgnoreCase(arg)) {
                    channel = "GLOBAL";
                } else {
                    channel = "DISCORD";
                }

                Channel ch = Channel.valueOf(channel);

                gamePlayer.setChannel(ch);

                String message = PluginConfig.CHANNEL_CHANGE_MESSAGE
                        .replace("{CHANNEL}", ch.toString());

                player.sendMessage(ChatUtil.format(message));
            } else {

                sendArgWarnMessage(player);
            }
        } else {

            sendArgWarnMessage(player);
        }
    }
    private void syncClearCMD(Player player){
        PluginDBManager.deleteUser(player.getUniqueId());

        String message = PluginConfig.SYNC_CLEAR_MESSAGE;
        GamePlayer gamePlayer = GamePlayer.getGamePLayer(player);
        gamePlayer.setDiscordUser(null);

        player.sendMessage(ChatUtil.format(message));
    }
}
