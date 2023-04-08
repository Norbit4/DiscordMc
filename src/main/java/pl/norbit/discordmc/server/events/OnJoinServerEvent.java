package pl.norbit.discordmc.server.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.players.DiscordPlayerService;

public class OnJoinServerEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        new BukkitRunnable() {
            @Override
            public void run() {
                DiscordPlayerService.joinPlayer(e.getPlayer());
            }
        }.runTaskLaterAsynchronously(DiscordMc.getInstance(), 4);
    }
}
