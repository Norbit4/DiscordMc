package pl.norbit.discordmc.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.builder.BotBuilder;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    private final JDABuilder jdaBuilder;
    private JDA jda;
    private final JavaPlugin javaPlugin;

    public DiscordBot(String token, JavaPlugin javaPlugin) {
        this.jdaBuilder = BotBuilder.getBuilder(token);
        this.javaPlugin = javaPlugin;
    }

    public void start(){
        try {
            jda = jdaBuilder.build();
        } catch (LoginException e) {
            //e.printStackTrace();
            javaPlugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[ERROR] Wrong token!");
        }
    }

    public JDABuilder getJdaBuilder() {
        return jdaBuilder;
    }

    public JDA getJda() {
        return jda;
    }

    public void close(){
        jda.shutdown();
    }
}
