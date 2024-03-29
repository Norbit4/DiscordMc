package pl.norbit.discordmc.db;

import org.bukkit.plugin.java.JavaPlugin
import pl.norbit.discordmc.DiscordMc
import pl.norbit.discordmc.config.PluginConfig
import pl.norbit.discordmc.players.DiscordPlayerService
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.*

class MysqlDatabase {

    companion object {

        private var connection: Connection? = null
        private var javaPlugin: JavaPlugin? = null
        private const val tableName: String = "players"
        private var statement: Statement? = null

        @JvmStatic
        fun start(javaPlugin: JavaPlugin) {

            this.javaPlugin = javaPlugin
            val cmd = "CREATE TABLE $tableName (" +
                    "`ID` INT NOT NULL AUTO_INCREMENT,  " +
                    "`${GameUser.UUID.name}` VARCHAR(45) NOT NULL,  " +
                    "`${GameUser.DISCORD_ID.name}` VARCHAR(45) NOT NULL,  " +
                    "PRIMARY KEY (`ID`));"

            //3306
            val ulr =
                "jdbc:mysql://${PluginConfig.DATABASE_HOST}:${PluginConfig.DATABASE_PORT}/" +
                        PluginConfig.DATABASE_NAME +
                        "?useUnicode=yes&characterEncoding=UTF-8&useSSL=${PluginConfig.DATABASE_SSL}" +
                        "&allowPublicKeyRetrieval=true"
            connection = DriverManager.getConnection(ulr, PluginConfig.DATABASE_USER, PluginConfig.DATABASE_PASS)

            statement = connection?.createStatement()

            val resultSet = statement?.executeQuery("SHOW TABLES like '$tableName';")

            var i = 0
            if (resultSet != null) {
                while (resultSet.next()) {
                    i++
                }
            }

            if (i == 0) {
                statement?.executeUpdate(cmd);
                DiscordMc.sendMessage("&a The table &e$tableName &ahas been created!")
            } else {
                DiscordMc.sendMessage("&aThe table &e$tableName &ahas been loaded!")
            }
            DiscordPlayerService.loadPlayers()
        }

        @JvmStatic
        fun close(){
            statement?.close()
            connection?.close()
        }

        @JvmStatic
        fun addUser(playerUUID: String, userID: String){
            deleteUser(playerUUID)

            statement?.executeUpdate(
                "INSERT INTO $tableName(${GameUser.UUID.name}, ${GameUser.DISCORD_ID.name})" +
                        "VALUES('$playerUUID', '$userID');")
        }

        @JvmStatic
        fun getUser(discordID: String):ResultSet?{
            val exist = statement?.executeQuery(
                "SELECT * FROM $tableName WHERE ${GameUser.DISCORD_ID.name} = '$discordID';");

            if (exist != null) {
                if (exist.next()) return exist
            }
            return null
        }
        @JvmStatic
        fun getUser(playerUUID: UUID):ResultSet?{
            val uuidString:String = playerUUID.toString()

            val exist = statement?.executeQuery(
                "SELECT * FROM $tableName WHERE ${GameUser.UUID.name} = '$uuidString';");

                if (exist != null) {
                    if(exist.next()) return exist
                }
            return null
        }

        @JvmStatic
        fun deleteUser(playerUUID: String){
            val exist = statement?.executeQuery("SELECT * FROM $tableName WHERE ${GameUser.UUID.name} = '$playerUUID';");

            if (exist != null) {
                if(exist.next()){
                    statement?.executeUpdate("DELETE FROM $tableName WHERE ${GameUser.UUID.name} = '$playerUUID';");
                }
            }
        }
        @JvmStatic
        fun getDatabaseList():List<ResultSet>{
            val arrayList = ArrayList<ResultSet>()

            val exist = statement?.executeQuery("SELECT * FROM $tableName;");

            if (exist != null) {
                if(exist.next()) arrayList.add(exist)
            }
            return arrayList;
        }
    }
}