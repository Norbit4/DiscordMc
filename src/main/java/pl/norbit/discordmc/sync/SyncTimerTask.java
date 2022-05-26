package pl.norbit.discordmc.sync;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SyncTimerTask {

    private static final List<SyncPlayer> syncPlayerList;

    static {
        syncPlayerList = new LinkedList<>();
    }

    public static void runTaskTimer(JavaPlugin javaPlugin) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!syncPlayerList.isEmpty()) {

                    syncPlayerList.forEach(syncPlayer -> {

                        System.out.println(syncPlayer.getPlayer().getName() + " " + syncPlayer.time);
                        if(syncPlayer.time() == 1){
                            removeSyncPlayer(syncPlayer);
                        }
                    });
                }
            }
        }.runTaskTimer(javaPlugin, 0, 20);
    }

    public static void addSyncPlayer(SyncPlayer syncPlayer){
        syncPlayerList.add(syncPlayer);
    }

    public static SyncPlayer getSyncPlayer(UUID playerUUID){
        Optional<SyncPlayer> optionalSyncPlayer = syncPlayerList.stream()
                 .filter(syncPlayer1 -> syncPlayer1.getPlayer().getUniqueId().equals(playerUUID))
                 .findFirst();
        return optionalSyncPlayer.orElse(null);
    }

    public static void removeSyncPlayer(SyncPlayer syncPlayer){
        syncPlayerList.remove(syncPlayer);
        System.out.println("work!");
    }
}
