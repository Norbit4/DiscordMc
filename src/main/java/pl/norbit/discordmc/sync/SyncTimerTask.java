package pl.norbit.discordmc.sync;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SyncTimerTask {

    private static final List<SyncPlayer> syncPlayerList =  new LinkedList<>();

    public static void runTaskTimer(JavaPlugin javaPlugin) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if(syncPlayerList.isEmpty()) return;

                syncPlayerList.forEach(syncPlayer -> {
                    if(syncPlayer.time() == 1) removeSyncPlayer(syncPlayer);
                });
            }
        }.runTaskTimerAsynchronously(javaPlugin, 0, 20);
    }

    public static void addSyncPlayer(SyncPlayer syncPlayer){
        syncPlayerList.add(syncPlayer);
    }

    public static SyncPlayer getSyncPlayer(UUID playerUUID){
        return syncPlayerList.stream()
                 .filter(syncPlayer1 -> syncPlayer1.getPlayer().getUniqueId().equals(playerUUID))
                 .findFirst().orElse(null);
    }

    public static void removeSyncPlayer(SyncPlayer syncPlayer){
        syncPlayerList.remove(syncPlayer);
    }

    public static List<SyncPlayer> getSyncPlayerList() {
        return syncPlayerList;
    }
}
