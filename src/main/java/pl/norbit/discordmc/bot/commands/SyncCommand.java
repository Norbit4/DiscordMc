package pl.norbit.discordmc.bot.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.bot.utils.ChatUtil;
import pl.norbit.discordmc.server.objects.GamePlayer;
import pl.norbit.discordmc.sync.SyncPlayer;
import pl.norbit.discordmc.sync.SyncTimerTask;

import java.awt.*;

public class SyncCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getName().equals("sync")){
            String playerNick = event.getOption(CmdOptions.NICK.name().toLowerCase()).getAsString();

            Player player = Bukkit.getPlayer(playerNick);

            if(player != null) {
                //Player player1 = PluginDBManager.getUser(event.getUser().getId());
                GamePlayer gamePlayer = GamePlayer.getGamePLayer(player);
                if(gamePlayer.getDiscordUser() == null) {

                    SyncTimerTask.addSyncPlayer(new SyncPlayer(player, event.getUser(),event.getChannel()));

                    MessageEmbed embed = Embed.getInfoMessage("Success","Type /sync in minecraft chat " +
                                    "to sync your accounts",
                            new Color(26, 154, 74)).build();

                    event.reply("").addEmbeds(embed).queue();

                    String mcMessage = "&Type &8/&bsync &7to connect your account with&a " + event.getUser().getAsTag();


                    player.sendMessage(ChatUtil.format(mcMessage));
                }else {
                    MessageEmbed embed = Embed.getInfoMessage("Warn","This player is sync!",
                            new Color(213, 156, 71)).build();

                    event.reply("").addEmbeds(embed).queue();
                }
            }else{
                MessageEmbed embed = Embed.getInfoMessage("Error","This player is offline!",
                        new Color(194, 50, 50)).build();

                event.reply("").addEmbeds(embed).queue();
            }
        }
    }
}
