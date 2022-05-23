package pl.norbit.discordmc.server.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.norbit.discordmc.server.objects.GamePlayer;

public class OnJoinServerEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        new GamePlayer(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        GamePlayer gamePlayer = GamePlayer.getGamePLayer(e.getPlayer());
        gamePlayer.remove();
    }
}
