package pl.norbit.discordmc.db

import net.dv8tion.jda.api.entities.User
import org.bukkit.OfflinePlayer

class DatabaseRecord constructor(val player: OfflinePlayer?,  val user: User?){
}