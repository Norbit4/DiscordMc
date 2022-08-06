package pl.norbit.discordmc.server.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.db.objects.DatabaseRecord;
import pl.norbit.discordmc.sync.SyncManager;
import pl.norbit.discordmc.utils.ChatUtil;
import pl.norbit.discordmc.db.PluginDBManager;
import pl.norbit.discordmc.server.config.ConfigManager;
import pl.norbit.discordmc.server.config.PluginConfig;
import pl.norbit.discordmc.server.enums.Channel;
import pl.norbit.discordmc.server.objects.GamePlayer;
import pl.norbit.discordmc.sync.SyncPlayer;
import pl.norbit.discordmc.sync.SyncTimerTask;
import pl.norbit.discordmc.utils.PermissionUtil;

import java.awt.*;
import java.util.List;
import java.util.UUID;

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

            PermissionUtil permissionUtil = new PermissionUtil(p);

            if (args[0].equalsIgnoreCase(PluginConfig.COMMAND_CHAT_CHANGE_ARG)) {
                if(PluginConfig.CHAT_MODULE) {
                    String[] perms = {"discordmc.channel", "discordmc.*", "*"};

                    if(permissionUtil.hasPermission(perms)){
                        changeChannelCMD(args, p);
                    }else{
                        String message = PluginConfig.PERMISSION_MESSAGE;
                        p.sendMessage(ChatUtil.format(message));
                    }
                }else{
                    sendHelpMessage(p);
                }

            }else if(args[0].equalsIgnoreCase(PluginConfig.SYNC_COMMAND_ARG)){

                String[] perms = {"discordmc.sync", "discordmc.*", "*"};

                if(permissionUtil.hasPermission(perms)){
                    syncCmd(p);
                }else{
                    String message = PluginConfig.PERMISSION_MESSAGE;
                    p.sendMessage(ChatUtil.format(message));
                }

            } else if(args[0].equalsIgnoreCase(PluginConfig.SYNC_COMMAND_CLEAR_ARG)){

                String[] perms = {"discordmc.syncclear", "discordmc.*", "*"};

                if(permissionUtil.hasPermission(perms)){
                    syncClearCMD(p);
                }else{
                    String message = PluginConfig.PERMISSION_MESSAGE;
                    p.sendMessage(ChatUtil.format(message));
                }

            }else if(args[0].equalsIgnoreCase("reload")){

                String[] perms = {"discordmc.reload", "discordmc.*", "*"};

                if(permissionUtil.hasPermission(perms)){
                    ConfigManager.loadConfig(javaPlugin, false);

                    String message = "&aConfig has been reloaded!";
                    p.sendMessage(ChatUtil.format(message));
                }else{
                    String message = PluginConfig.PERMISSION_MESSAGE;
                    p.sendMessage(ChatUtil.format(message));
                }
            }else if(args[0].equalsIgnoreCase("rankreload")){

                if(PluginConfig.SYNC_RANK_ENABLE) {
                    String[] perms = {"discordmc.rankreload", "discordmc.*", "*"};

                    if (permissionUtil.hasPermission(perms)) {
                        syncRank(p);
                    } else {
                        String message = PluginConfig.PERMISSION_MESSAGE;
                        p.sendMessage(ChatUtil.format(message));
                    }
                }else{
                    String message = "&cSync rank is disabled!";
                    p.sendMessage(ChatUtil.format(message));
                }
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
    private void syncRank(Player p){

        DatabaseRecord user = PluginDBManager.getUser(p.getUniqueId());

        if(user != null){

            boolean b = SyncManager.addPlayer(p.getUniqueId(), user.getUser().getId());

            if(PluginConfig.SYNC_NAME){
                SyncManager.changeToMinecraftName(p, user.getUser().getId());
            }

            String message = "&aRank reloaded!";

            p.sendMessage(ChatUtil.format(message));
        }else{

            String message = "&cYou are not sync!";

            p.sendMessage(ChatUtil.format(message));
        }
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
                                    PluginConfig.EMBED_SUCCESS_B))
                        .build();

            syncPlayer.getMessageChannel().sendMessageEmbeds(embed).queue();

            if(PluginConfig.SYNC_RANK_ENABLE) {
                SyncManager.addPlayer(playerUUID, userID);
            }

            if(PluginConfig.SYNC_NAME){
                SyncManager.changeToMinecraftName(player, userID);
            }

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

        if(gamePlayer.getDiscordUser() != null) {
            String id = gamePlayer.getDiscordUser().getId();
            SyncManager.clearRanks(id);
            SyncManager.clearName(id);
        }

        gamePlayer.setDiscordUser(null);

        player.sendMessage(ChatUtil.format(message));
    }
}
