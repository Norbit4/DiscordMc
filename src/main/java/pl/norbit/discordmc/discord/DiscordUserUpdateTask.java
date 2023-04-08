package pl.norbit.discordmc.discord;

import org.bukkit.scheduler.BukkitRunnable;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.players.DiscordPlayer;
import pl.norbit.discordmc.players.DiscordPlayerService;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class DiscordUserUpdateTask {

    private static final Queue<UUID> queue = new LinkedList<>();

    public static void start(){
        if(!PluginConfig.SYNC_RANK_ENABLE) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (DiscordPlayer discordPlayer : DiscordPlayerService.getPlayersList()) {
                    if(!discordPlayer.isSync()) continue;

                    queue.add(discordPlayer.getPlayerUUID());
                }
            }
        }.runTaskTimerAsynchronously(DiscordMc.getInstance(), 20 * 3, 20 * 60 * 30);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(queue.isEmpty()) return;

                for (int i = 0; i < 30; i++) {

                    UUID playerUUID = queue.poll();

                    DiscordPlayer gamePLayer= DiscordPlayerService.getGamePLayerByPlayerUUID(playerUUID);

                    if(gamePLayer == null) continue;

                    DiscordUserService.updateDiscordUser(gamePLayer.getPlayerUUID(), gamePLayer.getDiscordId());
                }
            }
        }.runTaskTimerAsynchronously(DiscordMc.getInstance(), 20 * 10, 10);
    }
}
