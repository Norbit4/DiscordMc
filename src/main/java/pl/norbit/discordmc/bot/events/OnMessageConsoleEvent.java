package pl.norbit.discordmc.bot.events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.server.config.PluginConfig;

import java.awt.*;
import java.util.List;

public class OnMessageConsoleEvent extends ListenerAdapter {
    private final JavaPlugin javaPlugin;

    public OnMessageConsoleEvent(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String channelID = message.getChannel().getId();

        if(channelID.equals(PluginConfig.CONSOLE_CHANNEL_ID)) {

            if(!event.getAuthor().isBot()) {
                List<String> blockedCommands = PluginConfig.BLOCKED_COMMANDS;

                boolean sendCmd = true;

                for (String blockedCommand : blockedCommands) {
                    if(message.getContentDisplay().equalsIgnoreCase(blockedCommand)) {

                        sendCmd = false;
                        break;
                    }
                }

                if(sendCmd) {
                    javaPlugin.getServer().getScheduler().runTask(javaPlugin, () -> {

                        javaPlugin.getServer().dispatchCommand(javaPlugin.getServer().getConsoleSender(),
                                message.getContentDisplay());
                    });
                }else{
                    Color color = new Color(203, 26, 26);
                    String message1 = "**Command: /" + message.getContentDisplay() + " is blocked!**";

                    MessageEmbed embed = Embed.getConsoleMessage("", message1, color).build();

                    event.getChannel().sendMessageEmbeds(embed).queue();
                }
            }
        }
    }
}

