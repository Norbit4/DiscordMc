package pl.norbit.discordmc.server.commands.args;

import org.bukkit.entity.Player;
import pl.norbit.discordmc.config.ConfigManager;
import pl.norbit.discordmc.server.commands.CommandType;
import pl.norbit.discordmc.server.commands.ServerCommand;
import pl.norbit.discordmc.utils.ChatUtil;

public class ReloadCommand extends ServerCommand {

    public ReloadCommand(String[] perms) {
        super(CommandType.RELOAD, perms);
    }

    @Override
    public void execute(Player p) {

        if (!super.hasPermission(p)) return;

        ConfigManager.loadConfig( false);

        String message = "&aConfig has been reloaded!";
        String messageWarn = "&cThis command is not recommended to use! Some features may not work properly!";
        String messageWarn2 = "&cRecommended to restart the server to apply changes!";
        p.sendMessage(ChatUtil.format(message));
        p.sendMessage(ChatUtil.format(messageWarn));
        p.sendMessage(ChatUtil.format(messageWarn2));
    }
}
