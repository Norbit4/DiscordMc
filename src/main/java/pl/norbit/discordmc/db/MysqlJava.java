package pl.norbit.discordmc.db;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlJava {

    public static void connect(JavaPlugin javaPlugin) throws SQLException {


        javaPlugin.getServer().getScheduler().runTaskLaterAsynchronously(javaPlugin, () -> {
            String ulr = "jdbc:mysql://localhost:3306/test?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=false";

            try {
                Connection connection = DriverManager.getConnection(ulr, "norbit", "1234");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("xd");
        },1);

    }
}
