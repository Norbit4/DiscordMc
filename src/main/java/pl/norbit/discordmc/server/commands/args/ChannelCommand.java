package pl.norbit.discordmc.server.commands.args;

import org.bukkit.entity.Player;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.players.DiscordPlayer;
import pl.norbit.discordmc.players.DiscordPlayerService;
import pl.norbit.discordmc.server.commands.CommandType;
import pl.norbit.discordmc.server.commands.ServerCommand;
import pl.norbit.discordmc.server.Channel;
import pl.norbit.discordmc.utils.ChatUtil;

public class ChannelCommand extends ServerCommand {

    public ChannelCommand(String[] perms) {
        super(CommandType.CHANNEL, perms);
    }

    @Override
    public void execute(Player p, String[] args) {

        if (!super.hasPermission(p)) return;

        if (args.length > 1) {
            String arg = args[1].toUpperCase();

            if (arg.equalsIgnoreCase(PluginConfig.MINECRAFT_CHAT_ARG)
                    || arg.equalsIgnoreCase(PluginConfig.DISCORD_CHAT_ARG)) {

                Channel channel;
                if (PluginConfig.MINECRAFT_CHAT_ARG.equalsIgnoreCase(arg)) {
                    channel = Channel.GLOBAL;
                } else {
                    channel = Channel.DISCORD;
                }

                DiscordPlayer discordPlayer = DiscordPlayerService.getGamePLayerByPlayerUUID(p.getUniqueId());

                discordPlayer.setChannel(channel);

                String message = PluginConfig.CHANNEL_CHANGE_MESSAGE
                        .replace("{CHANNEL}", channel.toString());

                p.sendMessage(ChatUtil.format(message));
            } else {

                sendArgWarnMessage(p);
            }
        } else {

            sendArgWarnMessage(p);
        }
    }
    private void sendArgWarnMessage(Player p){

        String message = PluginConfig.WRONG_ARGS_MESSAGE
                .replace("{PREFIX}", PluginConfig.COMMAND_PREFIX)
                .replace("{ARG1}", PluginConfig.COMMAND_CHAT_CHANGE_ARG)
                .replace("{ARG2}", PluginConfig.MINECRAFT_CHAT_ARG + "/"
                        + PluginConfig.DISCORD_CHAT_ARG);

        p.sendMessage(ChatUtil.format(message));
    }
}

