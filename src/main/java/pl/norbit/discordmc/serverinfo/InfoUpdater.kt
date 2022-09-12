package pl.norbit.discordmc.serverinfo

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageHistory
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import pl.norbit.discordmc.DiscordMc
import pl.norbit.discordmc.bot.embed.Embed
import pl.norbit.discordmc.server.config.PluginConfig
import pl.norbit.discordmc.utils.PlaceholderUtil
import pl.norbit.pluginutils.task.TaskBuilder
import pl.norbit.pluginutils.task.TaskType
import pl.norbit.pluginutils.task.TaskUnit
import java.awt.Color
import java.util.*

class InfoUpdater {

    companion object {

        @JvmStatic
        fun start(jda: JDA?, javaPlugin: JavaPlugin) {

                val reloadTime = if(PluginConfig.MESSAGE_RELOAD_TIME <= 20){
                    20
                }else{
                    PluginConfig.MESSAGE_RELOAD_TIME
                }

                val messageID: String
                val textChannel = jda?.awaitReady()?.getTextChannelById(PluginConfig.CHANNEL_INFO_ID)
                val color = Color(PluginConfig.EMBED_INFO_R, PluginConfig.EMBED_INFO_G, PluginConfig.EMBED_INFO_B)

                if (textChannel != null) {
                    val complete = MessageHistory.getHistoryFromBeginning(textChannel).complete()

                    val messagesList = complete?.retrievedHistory;

                    messagesList?.forEach { message ->
                        textChannel.deleteMessageById(message.id).queue()
                    }

                    val infoMessage = Embed.getInfoMessage("", "Loading...",color)

                    textChannel.sendMessage("*").setEmbeds(infoMessage.build()).complete()
                    messageID = textChannel.latestMessageId

                    //wait 5 seconds
                    TaskBuilder
                        .builder()
                        .javaPlugin(javaPlugin)
                        .delay(5)
                        .taskType(TaskType.LATER)
                        .delayUnit(TaskUnit.SECONDS)
                        .runnable {

                            //run info update task
                            TaskBuilder
                                .builder()
                                .javaPlugin(javaPlugin)
                                .period(reloadTime)
                                .periodUnit(TaskUnit.SECONDS)
                                .taskType(TaskType.TIMER)
                                .runnable {
                                    val message = PlaceholderUtil.getInfoFormat(
                                        Embed.getInfoMessage(
                                            PluginConfig.EMBED_INFO_TITTLE,
                                            PluginConfig.EMBED_INFO_DESC,
                                            color
                                        ),
                                        PluginConfig.EMBED_INFO_ARGS
                                    )

                                    textChannel.editMessageById(messageID, "*").setEmbeds(message?.build()).queue()
                                }
                                .build().start()
                        }
                        .build().start()
                }
        }
    }
}
