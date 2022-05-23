package pl.norbit.discordmc.server.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangeChannel implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof  Player){
            if(args.length > 1) {
                if (args[0].equalsIgnoreCase("channel")) {

                }
            }
        }
        return true;
    }
}
