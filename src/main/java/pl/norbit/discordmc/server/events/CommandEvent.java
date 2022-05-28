package pl.norbit.discordmc.server.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.norbit.discordmc.server.config.PluginConfig;

public class CommandEvent implements Listener {

    @EventHandler
    public void onCMD(PlayerCommandPreprocessEvent e){

        String[] cmdArray =  e.getMessage().split(" ");

        String cmd = cmdArray[0].replace("/","");

        String arg1 = cmdArray[1];
        String arg2 = cmdArray[2];

        System.out.println(cmd);
        System.out.println(arg1);
        System.out.println(arg2);

        if(cmd.equals(PluginConfig.COMMAND_PREFIX) && !cmd.equals("dc")) {

            Bukkit.getServer().dispatchCommand(e.getPlayer(), "dc " + arg1 + " " + arg2);
            e.setCancelled(true);
        }
    }
}
