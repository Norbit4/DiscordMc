package pl.norbit.discordmc.server.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.norbit.discordmc.utils.ChatUtil;
import pl.norbit.discordmc.config.PluginConfig;

import java.util.List;

public class MainCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if(args.length > 0) {

            String cmd = args[0];

            if (cmd.equalsIgnoreCase(PluginConfig.COMMAND_CHAT_CHANGE_ARG)) {

                if(PluginConfig.CHAT_MODULE) {

                    ServerCommand.execute(CommandType.CHANNEL, p, args);
                    return true;
                }
                sendHelpMessage(p);

            }else if(cmd.equalsIgnoreCase(PluginConfig.SYNC_COMMAND_ARG)){

                ServerCommand.execute(CommandType.SYNC, p);

            } else if(cmd.equalsIgnoreCase(PluginConfig.SYNC_COMMAND_CLEAR_ARG)){

                ServerCommand.execute(CommandType.SYNC_CLEAR, p);

            }else if(cmd.equalsIgnoreCase("reload")){

                ServerCommand.execute(CommandType.RELOAD, p);

            }else if(args[0].equalsIgnoreCase(PluginConfig.RANK_RELOAD_COMMAND_ARG)){
                if(PluginConfig.SYNC_PERM_ENABLE) {

                    ServerCommand.execute(CommandType.RANK_RELOAD, p);
                    return true;
                }
                sendHelpMessage(p);
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
}
