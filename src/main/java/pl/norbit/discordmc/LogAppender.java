package pl.norbit.discordmc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.embed.Embed;

import java.awt.*;


public class LogAppender extends AbstractAppender {
    private final JDA jda;

    public LogAppender(JDA jda, JavaPlugin javaPlugin) {
        super("LogAppender", null, null);
        this.jda = jda;
    }

    @Override
    public void append(LogEvent event) {

        String formatMessage = event.getMessage().getFormattedMessage().replaceAll("\u001b\\[[;\\d]*m", "");

        String message = "**["+ event.getLevel().toString() + "]**: " + formatMessage + "";

        Color color;

        if(event.getLevel().toString().equals("INFO")){
            color = new Color(27, 155, 201);
        }else if(event.getLevel().toString().equals("ERROR")){
            color = new Color(166, 42, 42);
        }else{
            color = new Color(183, 144, 34);
        }

        MessageEmbed embed = Embed.getConsoleMessage("", message, color).build();

        jda.getTextChannelsByName("console", false).get(0).sendMessageEmbeds(embed).queue();

    }
}
