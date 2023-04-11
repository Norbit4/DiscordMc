package pl.norbit.discordmc.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiscordUserService {
    public final static List<Rank> configRanks= new ArrayList<>();
    private static JDA jda;
    public static void init() {
        DiscordUserService.jda = DiscordMc.getJda();

        if(!PluginConfig.SYNC_RANK_ENABLE) return;

        List<String> configList = PluginConfig.SYNC_ROLES;

        for (String perm : configList) {

            String[] split = perm.split(":");
            String permission = split[0].replaceAll(" ", "");
            String id = split[1].replaceAll(" ", "");

            new Rank(permission, id);
        }
    }
    private static Rank getRankByPerm(String perm){

        for (Rank configRank : configRanks) {

            if(configRank.permission.equals(perm)){
                return configRank;
            }
        }
        return null;
    }
    public static void changeToMinecraftName(Player p, String discordUUID){

        String minecraftNick = p.getName();

        Guild guild = jda.getGuildById(PluginConfig.SERVER_ID);

        Member member  = guild.retrieveMemberById(discordUUID).complete();

        if(!member.isOwner()) member.modifyNickname(minecraftNick).queue();
    }
    private static void clearName(String discordUUID){

        Guild guild = jda.getGuildById(PluginConfig.SERVER_ID);

        Member member  = guild.retrieveMemberById(discordUUID).complete();

        if(!member.isOwner()) member.modifyNickname(member.getUser().getName()).queue();
    }

    private static void roleDeleteFromUser(Member member, Role role){

        configRanks.forEach(rank -> {

            if(rank.role.equals(role))
                jda.getGuildById(PluginConfig.SERVER_ID).removeRoleFromMember(member.getUser(), role).queue();
        });
    }

    private static void clearRanks(String userID){

        Member memberById =  jda.getGuildById(PluginConfig.SERVER_ID).retrieveMemberById(userID).complete();

        if(memberById == null) return;

        List<Role> roles = memberById.getRoles();

        roles.forEach(role -> roleDeleteFromUser(memberById, role));
    }

    private static List<String> getPermissionRanks(){
        List<String> permList = new ArrayList<>();

        configRanks.forEach(rank -> permList.add(rank.permission));

        return permList;
    }
    public static class Rank {
        private final String permission;
        private final Role role;

        public Rank(String permission, String ID) {
            this.permission = permission;
            Guild guildById = null;

            try {
                guildById = jda.awaitReady().getGuildById(PluginConfig.SERVER_ID);
            } catch (InterruptedException ignored) {

            }
            assert guildById != null;
            role = guildById.getRoleById(ID);

            if(role != null){
                configRanks.add(this);
            }else{
                DiscordMc.sendMessage("&cPermission: &4" + permission + " &cwith &4" + ID + " &chas not been loaded!"
                        + " This User ID does not exist!");
            }
        }
    }

    public static void updateDiscordUser(UUID playerUUID, String userID){
        Player player = Bukkit.getPlayer(playerUUID);

        if(player == null) return;

        PermissionUtil permissionUtil = new PermissionUtil(player);

        List<String> samePermissions = permissionUtil.getSamePermissions(getPermissionRanks());

        Member memberById =  jda.getGuildById(PluginConfig.SERVER_ID).retrieveMemberById(userID).complete();

        if(memberById == null) return;

        List<Role> roles = memberById.getRoles();

        roles.forEach(role -> {
            roleDeleteFromUser(memberById, role);
        });

        if(samePermissions == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Rank rank = getRankByPerm(samePermissions.get(0));

                jda.getGuildById(PluginConfig.SERVER_ID).addRoleToMember(memberById.getUser(), rank.role).queue();;
            }
        }.runTaskLaterAsynchronously(DiscordMc.getInstance(),  4);

        if(PluginConfig.SYNC_NAME){
            DiscordUserService.changeToMinecraftName(player, userID);
        }
    }

    public static void clear(String id){
        if(!PluginConfig.SYNC_RANK_ENABLE) return;

        if(id == null) return;

        DiscordUserService.clearRanks(id);

        if(!PluginConfig.SYNC_NAME) return;

        DiscordUserService.clearName(id);
    }
}
