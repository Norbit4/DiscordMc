package pl.norbit.discordmc.sync;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TimeTask extends BukkitRunnable {

    private final List<SyncPlayer> syncPlayerList;

    public TimeTask(JavaPlugin javaPlugin) {
        this.syncPlayerList = new ArrayList<>();
        this.runTaskTimer(javaPlugin, 0, 20);
    }

    @Override
    public void run() {

        if(!syncPlayerList.isEmpty()) {
            syncPlayerList.forEach(syncPlayer -> {

                if(syncPlayer.time() == 1){
                    syncPlayerList.remove(syncPlayer);
                }
            });
        }
    }

    public void addSyncPlayer(SyncPlayer syncPlayer){
        syncPlayerList.add(syncPlayer);
    }

    public SyncPlayer getSyncPlayer(UUID playerUUID){
        Optional<SyncPlayer> optionalSyncPlayer = syncPlayerList.stream()
                 .filter(syncPlayer1 -> syncPlayer1.getPlayer().getUniqueId().equals(playerUUID))
                 .findFirst();
        return optionalSyncPlayer.orElse(null);
    }
}
