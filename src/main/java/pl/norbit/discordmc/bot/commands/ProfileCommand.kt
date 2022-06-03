package pl.norbit.discordmc.bot.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import pl.norbit.discordmc.bot.embed.Embed
import pl.norbit.discordmc.db.PluginDBManager
import pl.norbit.discordmc.server.config.PluginConfig
import java.awt.Color

class ProfileCommand: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {

        if(event.name == "profile"){
            val id: String
            val message: String

            if(event.options.isNotEmpty()) {
                val mention = event.getOption(CmdOptions.MENTION.name.lowercase())!!.asMentionable
                id = mention.id

                message = "This user is not synchronized!"
            }else {

                id = event.user.id
                message = "You are not synchronized! Use **/sync** to sync your accounts!"
            }

            val databaseRecord = PluginDBManager.getUser(id)

            if(databaseRecord != null){

                val userName = databaseRecord.user.asMention
                val mcName = databaseRecord.player.name
                val embed = Embed.getProfileMessage(userName, mcName);

                event.reply("").addEmbeds(embed.build()).queue()
            }else {
                val embed = Embed.getInfoMessage(
                    PluginConfig.ERROR_TITTLE, message,
                    Color(PluginConfig.EMBED_ERROR_R, PluginConfig.EMBED_ERROR_G, PluginConfig.EMBED_ERROR_B)).build()

                event.reply("").addEmbeds(embed).queue()
            }
        }
    }
}
