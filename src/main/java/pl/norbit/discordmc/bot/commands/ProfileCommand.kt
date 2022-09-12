package pl.norbit.discordmc.bot.commands

import me.clip.placeholderapi.PlaceholderAPI
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import pl.norbit.discordmc.bot.embed.Embed
import pl.norbit.discordmc.db.PluginDBManager
import pl.norbit.discordmc.server.config.PluginConfig
import java.awt.Color
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import kotlin.collections.HashMap

class ProfileCommand: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {

        if(event.name == "profile"){
            val id: String
            val message: String

            if(event.options.isNotEmpty()) {
                val mention = event.getOption(CmdOptions.MENTION.name.lowercase())!!.asMentionable
                id = mention.id

                message = PluginConfig.ARG_USER_IS_NOT_SYNC
            }else {

                id = event.user.id
                message = PluginConfig.USER_IS_NOT_SYNC
            }

            val databaseRecord = PluginDBManager.getUser(id)
            val lineList:List<String> = PluginConfig.EMBED_PROFILE

            //checks player exist in database
            if(databaseRecord != null){

                val userName = databaseRecord.user?.asMention
                val mcName = databaseRecord.player?.name
                val p = databaseRecord.player?.player

                val builder = Embed.getProfileMessage(userName, mcName);
                val replacements: HashMap<String, String> = HashMap()
                val status: String
                val replacementsOnline = arrayOf("{X}", "{Y}", "{Z}", "{WORLD}")

                //checks player online status
                if(p != null) {
                    val x = p.location.x.toInt()
                    val y = p.location.y.toInt()
                    val z = p.location.z.toInt()
                    val world = p.world.name

                    replacements["{X}"] = "$x"
                    replacements["{Y}"] = "$y"
                    replacements["{Z}"] = "$z"
                    replacements["{WORLD}"] = world
                    builder.setColor(Color(62, 249, 36))

                    status = PluginConfig.PLAYER_ONLINE_STATUS
                }else{
                    builder.setColor(Color(212, 29, 25))

                    status = PluginConfig.PLAYER_OFFLINE_STATUS
                }

                replacements["{NICK}"] = "$mcName"
                replacements["{USER}"] = "$userName"
                replacements["{STATUS}"] = status

                //checks player is premium
                if(isUsernamePremium(mcName)) {
                    replacements["{NAME_MC}"] = "https://namemc.com/profile/$mcName.1"
                }else{
                    replacements["{NAME_MC}"] = PluginConfig.NAME_MC_NON_PREMIUM
                }

                //replaces placements
                for (line in lineList) {
                    if(line == "{EMPTY_LINE}"){
                        builder?.addBlankField(false)
                    }else{

                        val formattedPlaceholder: String = if(PluginConfig.PLACEHOLDER_API_EXIST) {
                            if (p != null) {

                                PlaceholderAPI.setPlaceholders(p, line)
                            } else {

                                val offlineP: OfflinePlayer? = databaseRecord.player

                                PlaceholderAPI.setPlaceholders(offlineP, line)
                            }
                        }else{
                            line;
                        }

                        val lineArgs = formattedPlaceholder.split("//")
                        if (lineArgs.size > 1) {
                            var arg1 = lineArgs[0]
                            var arg2 = lineArgs[1]

                            for (replacement in replacements) {

                                arg1 = arg1.replace(replacement.key, replacement.value, true)
                                arg2 = arg2.replace(replacement.key, replacement.value, true)
                            }
                            var build = true;
                            for(ro in replacementsOnline) {
                                if(arg1.contains(ro) || arg2.contains(ro)){
                                    if(p == null){
                                        build = false
                                    }
                                }
                            }
                            if(build){
                                builder?.addField(arg1, arg2, false)
                            }
                        }
                    }
                }

                event.reply("").addEmbeds(builder.build()).queue()
            }else {
                val embed = Embed.getInfoMessage(
                    PluginConfig.ERROR_TITTLE, message,
                    Color(PluginConfig.EMBED_ERROR_R, PluginConfig.EMBED_ERROR_G, PluginConfig.EMBED_ERROR_B)).build()

                event.reply("").addEmbeds(embed).queue()
            }
        }
    }
    private fun isUsernamePremium(username: String?): Boolean {
        val url = URL("https://api.mojang.com/users/profiles/minecraft/$username")
        val `in` = BufferedReader(InputStreamReader(url.openStream()))
        var line: String?
        val result = StringBuilder()
        while (`in`.readLine().also { line = it } != null) {
            result.append(line)
        }
        return result.toString() != ""
    }
}
