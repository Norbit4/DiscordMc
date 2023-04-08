package pl.norbit.discordmc.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import pl.norbit.discordmc.bot.builder.BotBuilder;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    private final JDABuilder jdaBuilder;
    private JDA jda;

    public DiscordBot(String token) {
        this.jdaBuilder = BotBuilder.getBuilder(token);
    }

    public boolean start(){
        try {
            jda = jdaBuilder.build();
            return true;
        } catch (LoginException e) {
            return false;
        }
    }

    public JDABuilder getJdaBuilder() {
        return jdaBuilder;
    }

    public JDA getJda() {
        return jda;
    }

    public void close(){
        if(jda != null) {
            jda.shutdownNow();
        }
    }
}
