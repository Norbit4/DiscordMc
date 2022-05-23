package pl.norbit.discordmc.server.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.norbit.discordmc.server.enums.Channel;
import pl.norbit.discordmc.server.objects.GamePlayer;

public class ChangeChannel implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length > 1) {
                if (args[0].equalsIgnoreCase("channel")) {
                    GamePlayer gamePlayer = GamePlayer.getGamePLayer(player);
                    Channel ch = Channel.valueOf(args[1].toUpperCase());
                    gamePlayer.setChannel(ch);

                    player.sendMessage("zmieniono na :" + ch);
                }
            }
        }
        return true;
    }
}
