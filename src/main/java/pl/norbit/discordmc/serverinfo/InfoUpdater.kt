package pl.norbit.discordmc.serverinfo

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageHistory
import org.bukkit.scheduler.BukkitRunnable
import pl.norbit.discordmc.DiscordMc
import pl.norbit.discordmc.bot.embed.Embed
import pl.norbit.discordmc.config.PluginConfig
import pl.norbit.discordmc.utils.PlaceholderUtil
import java.awt.Color

class InfoUpdater {

    companion object {

        @JvmStatic
        fun start(jda: JDA?) {

            val reloadTime = if(PluginConfig.MESSAGE_RELOAD_TIME <= 20){
                20
            }else{
                PluginConfig.MESSAGE_RELOAD_TIME
            }

            val messageID: String
            val textChannel = jda?.awaitReady()?.getTextChannelById(PluginConfig.CHANNEL_INFO_ID)
            val color = Color(PluginConfig.EMBED_INFO_R, PluginConfig.EMBED_INFO_G, PluginConfig.EMBED_INFO_B)

            if(textChannel == null) return

            val complete = MessageHistory.getHistoryFromBeginning(textChannel).complete()

            val messagesList = complete?.retrievedHistory;

            messagesList?.forEach { message ->
                textChannel.deleteMessageById(message.id).queue()
            }
            val infoMessage = Embed.getInfoMessage("", "Loading...", color)

            textChannel.sendMessage("*").setEmbeds(infoMessage.build()).complete()
            messageID = textChannel.latestMessageId

            object : BukkitRunnable() {
                override fun run() {
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
            }.runTaskTimerAsynchronously(DiscordMc.getInstance(), 20 * 6, reloadTime.toLong() * 20)
        }
    }
}
