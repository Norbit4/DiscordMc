package pl.norbit.discordmc.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.api.events.SyncClearEvent;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.utils.ConsoleUtil;
import pl.norbit.discordmc.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiscordUserService {
    public final static List<Rank> configMcRanks = new ArrayList<>();
    public final static List<Rank> configDiscordRanks = new ArrayList<>();
    private static JDA jda;
    public static class Rank {
        private final String permission;
        private final Role role;

        public Rank(String permission, String ID, RoleType roleType) {
            this.permission = permission;
            Guild guildById = null;

            try {
                guildById = jda.awaitReady().getGuildById(PluginConfig.SERVER_ID);
            } catch (InterruptedException ignored) {

            }
            assert guildById != null;
            role = guildById.getRoleById(ID);

            if(role != null){
                if(roleType == RoleType.MC) configMcRanks.add(this);
                else configDiscordRanks.add(this);
            }else{
                DiscordMc.sendMessage("&cPermission: &4" + permission + " &cwith &4" + ID + " &chas not been loaded!"
                        + " This User ID does not exist!");
            }
        }
    }
    public static void init(){
        DiscordUserService.jda = DiscordMc.getJda();

        if(PluginConfig.SYNC_PERM_ENABLE) formatConfig(RoleType.MC);

        if(PluginConfig.SYNC_ROLES_ENABLE) formatConfig(RoleType.DISCORD);
    }

    private static void formatConfig(RoleType roleType){
        List<String> configList;

        if(roleType == RoleType.MC) configList = PluginConfig.SYNC_PERMS_MC;
        else configList = PluginConfig.SYNC_ROLES_DISCORD;

        for (String roleString : configList) {

            String[] split = roleString.split(":");

            if(split.length < 2) continue;

            String permission = split[0].replaceAll(" ", "");
            String id = split[1].replaceAll(" ", "");

            new Rank(permission, id, roleType);
        }
    }
    private static Rank getRankByPerm(String perm){

        for (Rank configRank : configMcRanks) {
            if(configRank.permission.equals(perm)) return configRank;
        }
        return null;
    }
    public static void changeToMinecraftName(Player p, Member member){
        String minecraftNick = p.getName();

        if(!member.isOwner()) member.modifyNickname(minecraftNick).queue();
    }
    private static void clearName(String discordUUID){
        Member member = DiscordMc.getGuild().retrieveMemberById(discordUUID).complete();

        if(!member.isOwner()) member.modifyNickname(member.getUser().getName()).queue();
    }

    private static void roleDeleteFromUser(Member member, Role role){
        configMcRanks.forEach(rank -> {
            if(rank.role.equals(role)) DiscordMc.getGuild().removeRoleFromMember(member.getUser(), role).queue();
        });
    }

    private static void clearRanks(String userID){

        Member memberById =  DiscordMc.getGuild().retrieveMemberById(userID).complete();

        if(memberById == null) return;

        List<Role> roles = memberById.getRoles();

        roles.forEach(role -> roleDeleteFromUser(memberById, role));
    }

    private static void clearPermissions(UUID playerUUID){
        Player p = Bukkit.getPlayer(playerUUID);

        if(p == null) return;

        PermissionUtil perms = new PermissionUtil(p);

        configDiscordRanks.forEach(rank -> {
            if(!perms.hasPermission(rank.permission)) return;

            String cmd = PluginConfig.PERM_COMMAND_UNSET
                    .replace("{PLAYER}", p.getName())
                    .replace("{PERM}", rank.permission);
            new BukkitRunnable() {
                @Override
                public void run() {
                    ConsoleUtil.executeCommand(p, cmd);
                }
            }.runTaskLater(DiscordMc.getInstance(), 0);
        });

    }
    private static void addPermission(Player p, String perm){
        new BukkitRunnable() {
            @Override
            public void run() {
                String cmd = PluginConfig.PERM_COMMAND_SET
                        .replace("{PLAYER}", p.getName())
                        .replace("{PERM}", perm);

                ConsoleUtil.executeCommand(p, cmd);
            }
        }.runTaskLater(DiscordMc.getInstance(), 0);
    }

    private static void updatePermissions(Player p, Member member){
        PermissionUtil perms = new PermissionUtil(p);

        configDiscordRanks.forEach(rank -> {

            if(perms.hasPermission(rank.permission)) return;

            for (Role role : member.getRoles()) {

                if(!rank.role.getId().equals(role.getId())) continue;

                addPermission(p, rank.permission);
                break;
            }
        });
    }

    private static List<String> getPermissionRanks(){
        List<String> permList = new ArrayList<>();

        configMcRanks.forEach(rank -> permList.add(rank.permission));

        return permList;
    }

    public static void updateDiscordUser(UUID playerUUID, String userID){
        Player player = Bukkit.getPlayer(playerUUID);

        if(player == null) return;

        Member memberById = DiscordMc.getGuild().retrieveMemberById(userID).complete();

        if(memberById == null) return;

        if(PluginConfig.SYNC_PERM_ENABLE) DiscordUserService.updatePermToRoles(player, memberById);

        if(PluginConfig.SYNC_NAME) DiscordUserService.changeToMinecraftName(player, memberById);

        if(PluginConfig.SYNC_ROLES_ENABLE) DiscordUserService.updatePermissions(player, memberById);
    }

    private static void updatePermToRoles(Player p, Member member){
        List<Role> roles = member.getRoles();

        PermissionUtil permissionUtil = new PermissionUtil(p);

        List<String> samePermissions = permissionUtil.getSamePermissions(getPermissionRanks());

        roles.forEach(role -> {
            roleDeleteFromUser(member, role);
        });

        if(samePermissions == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Rank rank = getRankByPerm(samePermissions.get(0));

                if(rank == null) return;

                Role role = rank.role;

                if(role == null) return;

                DiscordMc.getGuild().addRoleToMember(member.getUser(), role).queue();;
            }
        }.runTaskLaterAsynchronously(DiscordMc.getInstance(),  4);
    }

    public static void clear(String id, UUID playerUUID) {

        if (id == null) return;

        if (PluginConfig.SYNC_PERM_ENABLE) DiscordUserService.clearRanks(id);

        if (PluginConfig.SYNC_ROLES_ENABLE) DiscordUserService.clearPermissions(playerUUID);

        if (PluginConfig.SYNC_NAME) DiscordUserService.clearName(id);
    }
}
