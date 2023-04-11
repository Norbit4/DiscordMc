package pl.norbit.discordmc.server.commands.args;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.players.DiscordPlayer;
import pl.norbit.discordmc.players.DiscordPlayerService;
import pl.norbit.discordmc.server.commands.CommandType;
import pl.norbit.discordmc.server.commands.ServerCommand;
import pl.norbit.discordmc.discord.DiscordUserService;
import pl.norbit.discordmc.utils.ChatUtil;

public class RankReloadCommand extends ServerCommand {
    public RankReloadCommand(String[] perms) {
        super(CommandType.RANK_RELOAD, perms);
    }

    @Override
    public void execute(Player p) {

        if (!super.hasPermission(p)) return;

        DiscordPlayer gamePLayerByPlayerUUID = DiscordPlayerService.getDiscordPlayerByPlayerUUID(p.getUniqueId());

        if(gamePLayerByPlayerUUID.isSync()){
            new BukkitRunnable() {
                @Override
                public void run() {
                    DiscordUserService.updateDiscordUser(
                            gamePLayerByPlayerUUID.getPlayerUUID(),
                            gamePLayerByPlayerUUID.getDiscordId());

                    p.sendMessage(ChatUtil.format(PluginConfig.RANK_RELOAD_MESSAGE));
                }
            }.runTaskLaterAsynchronously(DiscordMc.getInstance(),  0);
        }else{
            p.sendMessage(ChatUtil.format(PluginConfig.PLAYER_IS_NOT_SYNC));
        }
    }
}
