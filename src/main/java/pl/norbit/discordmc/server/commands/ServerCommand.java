package pl.norbit.discordmc.server.commands;

import org.bukkit.entity.Player;
import pl.norbit.discordmc.server.commands.args.*;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.utils.ChatUtil;
import pl.norbit.discordmc.utils.PermissionUtil;

import java.util.HashMap;

public class ServerCommand {

    private static final HashMap<CommandType, ServerCommand> commands = new HashMap<>();

    public static void registerCommands(){
        new RankReloadCommand(new String[]{"discordmc.rankreload", "discordmc.*", "*"});
        new SyncClearCommand(new String[]{"discordmc.syncclear", "discordmc.*", "*"});
        new ReloadCommand(new String[]{"discordmc.reload", "discordmc.*", "*"});
        new SyncCommand(new String[]{"discordmc.sync", "discordmc.*", "*"});
        new ChannelCommand(new String[]{"discordmc.channel", "discordmc.*", "*"});
    }

    public static void execute(CommandType commandType, Player p, String[] args){
        if(!commands.containsKey(commandType)) return;

        commands.get(commandType).execute(p, args);
    }
    public static void execute(CommandType commandType, Player p){
        if(!commands.containsKey(commandType)) return;

        commands.get(commandType).execute(p);
    }

    private final CommandType commandType;
    private final String[] perms;

    public ServerCommand(CommandType commandType, String[] perms){
        this.commandType = commandType;
        this.perms = perms;
        commands.put(commandType, this);
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public boolean hasPermission(Player p){
        PermissionUtil permissionUtil = new PermissionUtil(p);

        if(permissionUtil.hasPermission(perms)) return true;

        String message = PluginConfig.PERMISSION_MESSAGE;
        p.sendMessage(ChatUtil.format(message));

        return false;
    }

    public void execute(Player p, String[] args){
    }
    public void execute(Player p){
    }
}
