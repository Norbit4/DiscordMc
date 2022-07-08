package pl.norbit.discordmc.server.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.norbit.discordmc.server.config.PluginConfig;

public class CommandEvent implements Listener {

    @EventHandler
    public void onCMD(PlayerCommandPreprocessEvent e) {

        String[] cmdArray = e.getMessage().split(" ");

        String cmd = cmdArray[0].replace("/", "");

        if (cmd.equalsIgnoreCase(PluginConfig.COMMAND_PREFIX) && !cmd.equalsIgnoreCase("discordmc")) {

            String arg1 = "";
            String arg2 = "";

            if (cmdArray.length > 1) {
                arg1 = cmdArray[1];

                if (cmdArray.length > 2) {
                    arg2 = cmdArray[2];
                }
            }
            Bukkit.getServer().dispatchCommand(e.getPlayer(), "discordmc " + arg1 + " " + arg2);

            e.setCancelled(true);
        }
    }
}
