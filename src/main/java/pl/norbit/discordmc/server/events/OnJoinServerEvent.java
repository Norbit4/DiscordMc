package pl.norbit.discordmc.server.events;

import net.dv8tion.jda.api.JDA;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import pl.norbit.discordmc.server.objects.GamePlayer;

public class OnJoinServerEvent implements Listener {
    private final JDA jda;
    private final JavaPlugin javaPlugin;

    public OnJoinServerEvent(JDA jda, JavaPlugin javaPlugin) {
        this.jda = jda;
        this.javaPlugin = javaPlugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        new BukkitRunnable() {
            @Override
            public void run() {
                new GamePlayer(e.getPlayer(), jda);
            }
        }.runTaskLater(javaPlugin, 4);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        GamePlayer gamePlayer = GamePlayer.getGamePLayer(e.getPlayer());
        gamePlayer.remove();
    }
}
