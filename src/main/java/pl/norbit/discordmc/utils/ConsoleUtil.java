package pl.norbit.discordmc.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.config.PluginConfig;

public class ConsoleUtil {
    public static void executeCommand(Player p, String... command){
        for (String cmd : command) {

            if (cmd == null) continue;

            if(cmd.isEmpty()) continue;

            String replace = cmd.replace("{PLAYER}", p.getName());

            if(PluginConfig.PLACEHOLDER_API_EXIST) replace = PlaceholderAPI.setPlaceholders(p, replace);
            Server server = DiscordMc.getInstance().getServer();

            server.dispatchCommand(server.getConsoleSender(), replace);
        }
    }
}
