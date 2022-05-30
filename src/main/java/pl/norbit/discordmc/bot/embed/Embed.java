package pl.norbit.discordmc.bot.embed;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Embed {
    private static SimpleDateFormat dateFormat, dateFormatConsole;

    static {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateFormatConsole = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    private static EmbedBuilder getBuilder(String tittle, String desc, Color color, boolean addDate, String module){
        EmbedBuilder embedBuilder = new EmbedBuilder();

        if(!tittle.isEmpty()) {
            embedBuilder.setTitle(tittle);
        }
        embedBuilder.setDescription(desc);
        embedBuilder.setColor(color);

        if(addDate) {
            Date date = new Date();
            if(module.isEmpty()) {
                embedBuilder.setFooter(dateFormat.format(date));
            } else{
                embedBuilder.setFooter(dateFormatConsole.format(date));
            }
        }

        return embedBuilder;
    }

    public static EmbedBuilder getDiscordMessage(String nick, String message, Color colorEmbed, String field){

        EmbedBuilder embedBuilder = getBuilder(nick, message, colorEmbed, true, "");
        //embedBuilder.addField("", message, false);

        if(!field.isEmpty()) {
            embedBuilder.addField("", field, true);
        }
        return embedBuilder;
    }

    public static EmbedBuilder getInfoMessage(String tittle, String message, Color color){

        return getBuilder(tittle, message, color, false, "");
    }

    public static EmbedBuilder getConsoleMessage(String tittle, String message, Color color){

        return getBuilder(tittle, message, color, true, "1");
    }

    public static EmbedBuilder getProfileMessage(String discordMention, String mcName){
        EmbedBuilder embedBuilder = getBuilder("Profile", "*", new Color(180, 115, 208),
                false, "");
        embedBuilder.addField("MC: " + mcName, "**DC:** " + discordMention, true);

        return embedBuilder;
    }
}
