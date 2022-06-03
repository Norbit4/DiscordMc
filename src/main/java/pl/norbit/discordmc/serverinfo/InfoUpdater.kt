package pl.norbit.discordmc.serverinfo

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageHistory
import org.bukkit.Bukkit
import pl.norbit.discordmc.DiscordMc

class InfoUpdater {

    companion object {

        @JvmStatic
        fun start(jda: JDA?) {

            DiscordMc.getExecutorService().submit{

                val messageID: String
                val textChannel = jda?.awaitReady()?.getTextChannelById("980891496368713818")
                val online = Bukkit.getOnlinePlayers().size
                //val history = textChannel?.history

                if (textChannel != null) {
                    val complete = MessageHistory.getHistoryFromBeginning(textChannel).complete()

                    val messagesList = complete?.retrievedHistory;

                    messagesList?.forEach { message ->
                        textChannel.deleteMessageById(message.id).queue()
                    }

                    textChannel.sendMessage("Online Players: $online").complete()
                    messageID = textChannel.latestMessageId

                    while (true) {

                        val onlinePlayers = Bukkit.getOnlinePlayers().size

                        textChannel.editMessageById(messageID, "Online Players: $onlinePlayers").queue()

                        Thread.sleep(5000)
                    }
                }

            }
        }

    }
}
