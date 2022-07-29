package pl.norbit.discordmc.serverinfo

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageHistory
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import pl.norbit.discordmc.DiscordMc
import pl.norbit.discordmc.bot.embed.Embed
import pl.norbit.discordmc.server.config.PluginConfig
import pl.norbit.pluginutils.task.TaskBuilder
import pl.norbit.pluginutils.task.TaskType
import pl.norbit.pluginutils.task.TaskUnit
import java.awt.Color
import java.util.*

class InfoUpdater {

    companion object {
        private val maxPlayers = Bukkit.getMaxPlayers()
        private val version = Bukkit.getVersion()
        private var allowEndFormatted: String = "";
        private var allowNetherFormatted: String = "";
        private val allPlayer = Bukkit.getOfflinePlayers().size
        private val stringWorldsList: ArrayList<String> = ArrayList()

        @JvmStatic
        fun start(jda: JDA?, javaPlugin: JavaPlugin) {
            val excludeFolders = PluginConfig.IGNORE_FOLDERS;
            //listOf("cache", "plugins", "logs", "crash-reports", "versions", "libraries", "config")
            TaskBuilder
                .builder()
                .javaPlugin(javaPlugin)
                .period(5)
                .periodUnit(TaskUnit.MINUTES)
                .taskType(TaskType.TIMER)
                .runnable {
                    stringWorldsList.clear()
                    for (listFile in Bukkit.getServer().worldContainer.listFiles()) {
                        if(listFile.isDirectory && !excludeFolders.contains(listFile.name)){
                            stringWorldsList.add(listFile.name)
                        }
                    }
                }
                .build().start()

                val reloadTime = if(PluginConfig.MESSAGE_RELOAD_TIME <= 20){
                    20
                }else{
                    PluginConfig.MESSAGE_RELOAD_TIME
                }

                val allowEnd = Bukkit.getAllowEnd()
                val allowNether = Bukkit.getAllowNether()

                allowEndFormatted = if(allowEnd){
                    PluginConfig.TRUE_INFO
                } else{
                    PluginConfig.FALSE_INFO
                }

                allowNetherFormatted = if(allowNether){
                    PluginConfig.TRUE_INFO
                } else{
                    PluginConfig.FALSE_INFO
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

                    TaskBuilder
                        .builder()
                        .javaPlugin(javaPlugin)
                        .period(reloadTime)
                        .periodUnit(TaskUnit.SECONDS)
                        .taskType(TaskType.TIMER)
                        .runnable {
                            val message = getEmbedConfigMessage(
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
        }

        private fun getEmbedConfigMessage(builder: EmbedBuilder?, lineList: List<String>): EmbedBuilder?{

            val onlinePlayers = Bukkit.getOnlinePlayers().size
            val time = System.currentTimeMillis() - DiscordMc.getTimeServer()

            val formatMinutes: Long
            val seconds = time/1000
            val minutes = seconds/60
            val hours = Integer.parseInt((minutes/60).toString())
            formatMinutes = if(hours != 0) {
                minutes - hours * 60
            } else {
                minutes
            }

            val replacements: HashMap<String, String> = HashMap()

            replacements["{SERVER_ONLINE}"] = "$onlinePlayers"
            replacements["{SERVER_TIME}"] = "$hours h $formatMinutes min"
            replacements["{SERVER_ALL_PLAYERS}"] = "$allPlayer"
            replacements["{SERVER_VERSION}"] = version
            replacements["{SERVER_WORLDS}"] = stringWorldsList.toString()
            replacements["{SERVER_MAX_PLAYERS}"] = "$maxPlayers"
            replacements["{SERVER_ALLOW_NETHER}"] = allowNetherFormatted
            replacements["{SERVER_ALLOW_END}"] = allowEndFormatted

            for (line in lineList) {
                if(line == "{EMPTY_LINE}"){
                    builder?.addBlankField(false)
                }else{
                    val lineArgs = line.split("//")
                    if (lineArgs.size > 1) {
                        var arg1 = lineArgs[0]
                        var arg2 = lineArgs[1]

                        for (replacement in replacements) {

                            arg1 = arg1.replace(replacement.key, replacement.value, true)
                            arg2 = arg2.replace(replacement.key, replacement.value, true)

                        }
                        builder?.addField(arg1, arg2, false)
                    }
                }
            }
            return builder;
        }
    }
}
