package pl.norbit.discordmc.db

import org.bukkit.plugin.java.JavaPlugin
import pl.norbit.discordmc.server.config.PluginConfig
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class Mysql {

    companion object {

        private var connection: Connection? = null
        private const val tableName: String = "players"

        @JvmStatic
        fun start(javaPlugin: JavaPlugin) {

            println("start sql")

            //Class.forName("com.mysql.jdbc.Driver")
            val cmd = "CREATE TABLE `${PluginConfig.DATABASE_NAME}`.`$tableName` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT,  " +
                    "`${GameUser.UUID.name}` VARCHAR(45) NOT NULL,  " +
                    "`${GameUser.DISCORD_ID.name}` VARCHAR(45) NOT NULL,  " +
                    "PRIMARY KEY (`id`));"

            //val ulr = "jdbc:mysql://" + PluginConfig.DATABASE_HOST + ":3306" + "/" +  PluginConfig.DATABASE_NAME
            //val ulr = "jdbc:mysql://localhost:3306/test?useUnicode=yes&characterEncoding=UTF-8&useSSL=false"
            //connection = DriverManager.getConnection(ulr, "norbit", "1234")
            javaPlugin.server.scheduler.runTaskLater(javaPlugin, {
//                val ulr =
//                    "jdbc:mysql://localhost:3306/test?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=false"
                val ulr =
                    "jdbc:mysql://localhost:3306/test?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=false"
                connection = DriverManager.getConnection(ulr, "norbit", "1234")
                val statement = connection?.createStatement()

//                val resultSet = statement?.executeQuery("SHOW TABLES like $tableName;")
//
//                println(resultSet)
//                if (resultSet == null) {
//                    statement?.executeUpdate(cmd);
//                    println("xd")
//                } else {
//                    println("xd8")
//                }
//                statement?.close()

            }, 1)
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("SHOW TABLES like $tableName;")
            println(resultSet)
            if (resultSet == null) {
                    statement?.executeUpdate(cmd);
                    println("xd")
                } else {
                    println("xd8")
                }
                statement?.close()

        }



        @JvmStatic
        fun addUser(playerUUID: String, userID: String){
            val statement = connection?.createStatement()

            val exist = statement?.executeQuery("SELECT * FROM $tableName WHERE ${GameUser.UUID.name} = $playerUUID;");

            if (exist != null) {
                if(exist.next()){
                    statement?.executeUpdate("DELETE FROM $tableName WHERE ${GameUser.UUID.name} = $playerUUID;");
                }
            }

            statement?.executeUpdate(
                "INSERT INTO $tableName(${GameUser.UUID.name}, ${GameUser.DISCORD_ID.name})" +
                        "VALUES('$playerUUID', $userID);")

            statement?.close()
        }

        @JvmStatic
        fun getUser(discordID: String):ResultSet?{
            val statement = connection?.createStatement()
            val exist = statement?.executeQuery(
                "SELECT * FROM $tableName WHERE ${GameUser.DISCORD_ID.name} = $discordID;");

            if (exist != null) {
                if(exist.next()){

                    return exist
                }
            }

            return null
        }
    }
}