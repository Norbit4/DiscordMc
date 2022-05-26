package pl.norbit.discordmc.server.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.norbit.discordmc.db.PluginDBManager;
import pl.norbit.discordmc.sync.SyncPlayer;
import pl.norbit.discordmc.sync.SyncTimerTask;

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

            player.sendMessage(player.getName());
            player.sendMessage(syncPlayer.getUser().getAsTag());
            SyncTimerTask.removeSyncPlayer(syncPlayer);
        } else{
            player.sendMessage("time out");
        }

        return true;
    }
}
