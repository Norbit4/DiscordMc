package pl.norbit.discordmc;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.bot.embed.Embed;
import pl.norbit.discordmc.server.config.PluginConfig;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class LogAppender extends AbstractAppender{
    private final JDA jda;
    private final Queue<String> messagesQueue;
    private final JavaPlugin javaPlugin;

    private static class QueueEmbed{
        private String text;
        private final Color color;

        public QueueEmbed() {
            this.text = "";
            this.color = new Color(58, 145, 175);
        }
        public void addMessage(String text) {
            this.text = this.text + "\n" + text;
        }

        public boolean size(String s){
            if(text.length() + s.length() >= 4096){
                return false;
            }
            return true;
        }

        public MessageEmbed getEmbed(){
            return Embed.getConsoleMessage("", text, color).build();
        }
    }

    public LogAppender(JDA jda, JavaPlugin javaPlugin){
        super("LogAppender", null, null);
        this.jda = jda;
        this.javaPlugin = javaPlugin;

        messagesQueue = new LinkedList<>();
        messageTask(javaPlugin);

        Color color = new Color(35, 201, 86);
        String message = "**Successfully connected to the console!**";

        MessageEmbed embed = Embed.getConsoleMessage("", message, color).build();

        try {
            jda.awaitReady().getTextChannelById(PluginConfig.CONSOLE_CHANNEL_ID).sendMessageEmbeds(embed).queue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void append(LogEvent event) {

        boolean sendMessage = true;

        String messageType = event.getLevel().toString();

        switch (messageType) {
            case "ERROR":

                if (PluginConfig.BLOCK_ERROR_MESSAGES) {
                    sendMessage = false;
                }
                break;
            case "WARN":

                if (PluginConfig.BLOCK_WARN_MESSAGES) {
                    sendMessage = false;
                }
                break;
        }

        if(sendMessage) {
            String[] colorCodes = {"\\u00A70", "\\u00A71", "\\u00A72", "\\u00A73", "\\u00A74", "\\u00A75", "\\u00A76",
                    "\\u00A77", "\\u00A78", "\\u00A79", "\\u00A7a", "\\u00A7b", "\\u00A7c", "\\u00A7d", "\\u00A7e",
                    "\\u00A7f", "\\u00A7k", "\\u00A7l", "\\u00A7m", "\\u00A7n", "\\u00A7o", "\\u00A7r","\u001B","\\[m"};

            String message = event.getMessage().getFormattedMessage();

            for (String s : colorCodes) {
                message = message.replaceAll(s, "");
            }

            String endMessage = "**["+ event.getLevel().toString() + "]**: " + message + "";

            messagesQueue.offer(endMessage);
        }
    }

    private void messageTask(JavaPlugin javaPlugin){

        javaPlugin.getServer().getScheduler().runTaskTimer(javaPlugin, () -> {

            if(!messagesQueue.isEmpty()) {
                QueueEmbed queueEmbed = new QueueEmbed();

                while (true){
                    if(messagesQueue.isEmpty()){
                        break;
                    }else if(!queueEmbed.size(messagesQueue.peek())){
                        break;
                    }
                    queueEmbed.addMessage(messagesQueue.poll());
                }

                try {
                    jda.awaitReady().getTextChannelById(PluginConfig.CONSOLE_CHANNEL_ID)
                            .sendMessageEmbeds(queueEmbed.getEmbed())
                            .queue();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0,30);
    }
}
