package pl.norbit.discordmc.utils

import me.clip.placeholderapi.PlaceholderAPI
import net.dv8tion.jda.api.EmbedBuilder
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import pl.norbit.discordmc.DiscordMc
import pl.norbit.discordmc.config.PluginConfig
import pl.norbit.discordmc.discord.DiscordUserUpdateTask
import pl.norbit.discordmc.players.DiscordPlayerService
import pl.norbit.pluginutils.task.TaskBuilder
import pl.norbit.pluginutils.task.TaskType
import pl.norbit.pluginutils.task.TaskUnit

class PlaceholderUtil {

    companion object {

        private val maxPlayers = Bukkit.getMaxPlayers()
        private val version = Bukkit.getVersion()
        private var allowEndFormatted: String = ""
        private var allowNetherFormatted: String = ""
        private val allPlayer = Bukkit.getOfflinePlayers().size

        @JvmStatic
        fun start(){
            val javaPlugin = DiscordMc.getInstance();

            //check PlaceholderAPI exist
            if(javaPlugin.server.pluginManager.getPlugin("PlaceholderAPI") != null){
                PluginConfig.PLACEHOLDER_API_EXIST = true
                DiscordMc.sendMessage("&aHooked to PlaceholderAPI!")
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
        }

        fun getInfoFormat(builder: EmbedBuilder?, lineList: List<String>): EmbedBuilder? {

            val onlinePlayers = Bukkit.getOnlinePlayers().size
            val time = System.currentTimeMillis() - DiscordMc.getTimeServer()

            val formatMinutes: Long
            val seconds = time / 1000
            val minutes = seconds / 60

            val hours = Integer.parseInt((minutes / 60).toString())
            formatMinutes = if (hours != 0) {
                minutes - hours * 60
            } else {
                minutes
            }

            val replacements: HashMap<String, String> = HashMap()

            replacements["{SERVER_ONLINE}"] = "$onlinePlayers"
            replacements["{SERVER_TIME}"] = "$hours h $formatMinutes m"
            replacements["{SERVER_ALL_PLAYERS}"] = "$allPlayer"
            replacements["{SERVER_VERSION}"] = version
            replacements["{SERVER_MAX_PLAYERS}"] = "$maxPlayers"
            replacements["{SERVER_ALLOW_NETHER}"] = allowNetherFormatted
            replacements["{SERVER_ALLOW_END}"] = allowEndFormatted

            for (line in lineList) {
                if (line == "{EMPTY_LINE}") {
                    builder?.addBlankField(false)
                } else {

                    val placeholderFormat = if (PluginConfig.PLACEHOLDER_API_EXIST) {
                        PlaceholderAPI.setPlaceholders(null, line)
                    }else{
                        line;
                    }

                    val lineArgs = placeholderFormat .split("//")

                    if (lineArgs.size < 2) continue

                    var arg1 = lineArgs[0]
                    var arg2 = lineArgs[1]

                    for (replacement in replacements) {
                        arg1 = arg1.replace(replacement.key, replacement.value, true)
                        arg2 = arg2.replace(replacement.key, replacement.value, true)

                    }
                    builder?.addField(arg1, arg2, false)
                }
            }
            return builder;
        }
    }
}