package pl.norbit.discordmc.server.commands.args;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.players.DiscordPlayerService;
import pl.norbit.discordmc.server.commands.CommandType;
import pl.norbit.discordmc.server.commands.ServerCommand;
import pl.norbit.discordmc.utils.ChatUtil;

public class SyncClearCommand extends ServerCommand {

    public SyncClearCommand(String[] perms) {
        super(CommandType.SYNC_CLEAR, perms);
    }

    @Override
    public void execute(Player p) {

        if (!super.hasPermission(p)) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                DiscordPlayerService.syncClear(p.getUniqueId());

                String message = PluginConfig.SYNC_CLEAR_MESSAGE;

                p.sendMessage(ChatUtil.format(message));
            }
        }.runTaskLaterAsynchronously(DiscordMc.getInstance(),  2);
    }
}
