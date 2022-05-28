package pl.norbit.discordmc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.server.config.PluginConfig;

import java.awt.*;


public class LogAppender extends AbstractAppender {
    private final JDA jda;

    public LogAppender(JDA jda, JavaPlugin javaPlugin) {
        super("LogAppender", null, null);
        this.jda = jda;

        Color color = new Color(35, 201, 86);
        String message = "**Successfully connected to the console!**";

        MessageEmbed embed = Embed.getConsoleMessage("", message, color).build();

        jda.getTextChannelsByName(PluginConfig.CONSOLE_CHANNEL_NAME, false).get(0)
                .sendMessageEmbeds(embed).queue();
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
            String[] replace = {"§9","§8","§7","§6","§5","§4","§3", "§2", "§1", "§0","§a", "§b", "§c","§d", "§e", "§f"};

            String formatMessage = event.getMessage().getFormattedMessage().replaceAll("\u001b\\[[;\\d]*m", "");

            for (String s : replace) {
                formatMessage = formatMessage.replaceAll(s, "");
            }

            String message = "**["+ event.getLevel().toString() + "]**: " + formatMessage + "";

            MessageEmbed embed = Embed.getConsoleMessage("", message, color).build();

            jda.getTextChannelsByName(PluginConfig.CONSOLE_CHANNEL_NAME, false).get(0)
                    .sendMessageEmbeds(embed).queue();
        }
    }
}
