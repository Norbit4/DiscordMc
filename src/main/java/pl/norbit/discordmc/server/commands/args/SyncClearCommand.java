package pl.norbit.discordmc.server.commands.args;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.api.events.SyncClearEvent;
import pl.norbit.discordmc.api.events.SyncEvent;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.players.DiscordPlayer;
import pl.norbit.discordmc.players.DiscordPlayerService;
import pl.norbit.discordmc.server.commands.CommandType;
import pl.norbit.discordmc.server.commands.ServerCommand;
import pl.norbit.discordmc.utils.ChatUtil;
import pl.norbit.discordmc.utils.ConsoleUtil;

import java.util.UUID;

public class SyncClearCommand extends ServerCommand {

    public SyncClearCommand(String[] perms) {
        super(CommandType.SYNC_CLEAR, perms);
    }

    @Override
    public void execute(Player p) {

        if (!super.hasPermission(p)) return;
        DiscordPlayer discordPlayer = DiscordPlayerService.getDiscordPlayerByPlayerUUID(p.getUniqueId());
        DiscordMc instance = DiscordMc.getInstance();

        if(discordPlayer == null) return;

        String discordId = discordPlayer.getDiscordId();
        UUID playerUUID = p.getUniqueId();

        DiscordPlayer gamePLayerByPlayerUUID = DiscordPlayerService.getDiscordPlayerByPlayerUUID(p.getUniqueId());

        if(gamePLayerByPlayerUUID.isSync()) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    DiscordPlayerService.syncClear(p.getUniqueId());

                    String message = PluginConfig.SYNC_CLEAR_MESSAGE;

                    p.sendMessage(ChatUtil.format(message));

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (PluginConfig.EXECUTE_COMMAND_ON_SYNC_CLEAR)
                                ConsoleUtil.executeCommand(p, PluginConfig.COMMANDS_ON_SYNC_CLEAR.toArray(new String[0]));


                            instance.getServer().getPluginManager().callEvent(new SyncClearEvent(playerUUID, discordId));
                        }
                    }.runTaskLater(DiscordMc.getInstance(), 4);
                }
            }.runTaskLaterAsynchronously(instance, 0);
        } else {
            p.sendMessage(ChatUtil.format(PluginConfig.PLAYER_IS_NOT_SYNC));
        }
    }
}
