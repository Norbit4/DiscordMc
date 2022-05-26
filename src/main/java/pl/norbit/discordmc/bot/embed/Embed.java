package pl.norbit.discordmc.bot.embed;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Embed {
    private static SimpleDateFormat dateFormat;

    static {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    private static EmbedBuilder getBuilder(String tittle, String desc, Color color, boolean addDate){
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(tittle);
        embedBuilder.setDescription(desc);
        embedBuilder.setColor(color);

        if(addDate) {
            Date date = new Date();
            embedBuilder.setFooter(dateFormat.format(date));
        }

        return embedBuilder;
    }

    public static EmbedBuilder getDiscordMessage(String nick, String message, Color colorEmbed, String field){

        EmbedBuilder embedBuilder = getBuilder(nick, message, colorEmbed, true);
        //embedBuilder.addField("", message, false);

        if(!field.isEmpty()) {
            embedBuilder.addField("", field, true);
        }
        return embedBuilder;
    }

    public static EmbedBuilder getInfoMessage(String tittle, String message, Color color){

        return getBuilder(tittle, message, color, false);
    }
}
