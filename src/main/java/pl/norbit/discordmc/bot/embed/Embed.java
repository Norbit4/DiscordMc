package pl.norbit.discordmc.bot.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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
        EmbedBuilder embedBuilder = getBuilder("Profile", "", new Color(180, 115, 208),
                false, "");
        embedBuilder.addField("nick:", mcName, false);
        embedBuilder.addField("user:", discordMention, false);

        Player p = Bukkit.getPlayer(mcName);

        String playerStatus;

        if(p != null){
            playerStatus = "online";
        }else{
            playerStatus = "offline";
        }

        embedBuilder.addField("status:", playerStatus, false);

        if(p != null){
            String world = "{WORLD}".replace("{WORLD}", "" + p.getWorld().getName());

            embedBuilder.addField("world:", world, false);

            String loc = "X: {X} Y: {Y} Z: {Z}"
                    .replace("{X}", String.valueOf((int) p.getLocation().getX()))
                    .replace("{Y}", String.valueOf((int) p.getLocation().getY()))
                    .replace("{Z}", String.valueOf((int) p.getLocation().getZ()));

            embedBuilder.addField("loc:", loc, false);
        }
        try {
            if(isUsernamePremium(mcName)) {
                embedBuilder.addField("namemc:", "https://namemc.com/profile/" + mcName + ".1", false);
            }else {
                embedBuilder.addField("namemc:", "player is non-premium", false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return embedBuilder;
    }
    public static boolean isUsernamePremium(String username) throws IOException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+ username);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = in.readLine())!=null){
            result.append(line);
        }
        return !result.toString().equals("");
    }
}
