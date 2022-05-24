package pl.norbit.discordmc.bot.embed;

import net.dv8tion.jda.api.EmbedBuilder;

import javax.annotation.Nullable;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Embed {
    public static EmbedBuilder getDiscordMessage(String nick, String message, Color colorEmbed, String field){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(nick);
        embedBuilder.setDescription(message);
        //embedBuilder.addField("", message, false);
        embedBuilder.setColor(colorEmbed);
        embedBuilder.setFooter(dateFormat.format(date));
        if(!field.isEmpty()) {
            embedBuilder.addField("", field, true);
        }
        return embedBuilder;
    }
}
