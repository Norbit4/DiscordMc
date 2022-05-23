package pl.norbit.discordmc.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import pl.norbit.discordmc.bot.builder.BotBuilder;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    private final JDABuilder jdaBuilder;
    private JDA jda;

    public DiscordBot(String token) {
        jdaBuilder = BotBuilder.getBuilder(token);
    }

    public void start(){
        try {
            jda = jdaBuilder.build();
        } catch (LoginException e) {
            e.printStackTrace();
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
