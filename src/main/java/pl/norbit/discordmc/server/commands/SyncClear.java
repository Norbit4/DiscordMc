package pl.norbit.discordmc.server.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.norbit.discordmc.bot.utils.ChatUtil;
import pl.norbit.discordmc.db.PluginDBManager;
import pl.norbit.discordmc.server.config.PluginConfig;

public class SyncClear implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;

        Player p = (Player) sender;

        PluginDBManager.deleteUser(p.getUniqueId());

        String message = PluginConfig.SYNC_CLEAR_MC;
        p.sendMessage(ChatUtil.format(message));

        return false;
    }
}
