package pl.norbit.discordmc.bot.events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.server.config.PluginConfig;

public class OnMessageConsoleEvent extends ListenerAdapter {
    private final JavaPlugin javaPlugin;

    public OnMessageConsoleEvent(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String channelName = message.getChannel().getName();

        if(channelName.equals(PluginConfig.CONSOLE_CHANNEL_NAME)) {

            if(!event.getAuthor().isBot()) {

                javaPlugin.getServer().getScheduler().runTask(javaPlugin, () -> {

                    javaPlugin.getServer().dispatchCommand(javaPlugin.getServer().getConsoleSender(),
                            message.getContentDisplay());
                });

            }
        }
    }
}

