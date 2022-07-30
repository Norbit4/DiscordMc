package pl.norbit.discordmc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.server.config.PluginConfig;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class LogAppender extends AbstractAppender{
    private final JDA jda;
    private final Queue<MessageEmbed> messageQueue;

    public LogAppender(JDA jda, JavaPlugin javaPlugin){
        super("LogAppender", null, null);
        this.jda = jda;
        messageQueue = new LinkedList<>();
        messageTask(javaPlugin);

        Color color = new Color(35, 201, 86);
        String message = "**Successfully connected to the console!**";

        MessageEmbed embed = Embed.getConsoleMessage("", message, color).build();

        try {
            jda.awaitReady().getTextChannelById(PluginConfig.CONSOLE_CHANNEL_ID).sendMessageEmbeds(embed).queue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void append(LogEvent event) {
        Color color;

        boolean sendMessage = true;

        switch (event.getLevel().toString()) {
            case "INFO":
                color = new Color(27, 155, 201);
                break;
            case "ERROR":
                color = new Color(166, 42, 42);

                if (PluginConfig.BLOCK_ERROR_MESSAGES) {
                    sendMessage = false;
                }
                break;
            case "WARN":
                color = new Color(183, 144, 34);

                if (PluginConfig.BLOCK_WARN_MESSAGES) {
                    sendMessage = false;
                }
                break;
            default:
                color = new Color(84, 81, 81);
                break;
        }

        if(sendMessage) {
            String[] colorCodes = {"\\u00A70", "\\u00A71", "\\u00A72", "\\u00A73", "\\u00A74", "\\u00A75", "\\u00A76",
                    "\\u00A77", "\\u00A78", "\\u00A79", "\\u00A7a", "\\u00A7b", "\\u00A7c", "\\u00A7d", "\\u00A7e",
                    "\\u00A7f"};

            String message = event.getMessage().getFormattedMessage();

            for (String s : colorCodes) {
            message = message.replaceAll(s, "");
            }

            String endMessage = "**["+ event.getLevel().toString() + "]**: " + message + "";

            MessageEmbed embed = Embed.getConsoleMessage("", endMessage, color).build();

            messageQueue.offer(embed);
        }
    }

    private void messageTask(JavaPlugin javaPlugin){

        javaPlugin.getServer().getScheduler().runTaskTimer(javaPlugin, () -> {

            if(!messageQueue.isEmpty()) {
                MessageEmbed embed = messageQueue.poll();
                try {
                    jda.awaitReady().getTextChannelById(PluginConfig.CONSOLE_CHANNEL_ID)
                            .sendMessageEmbeds(embed).queue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, 0,30);

    }
}
