package pl.norbit.discordmc.db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.server.config.PluginConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDB {
    private final static MongoClient client;
    private static MongoDatabase db;
    private static MongoCollection<Document> collection;
    private static JavaPlugin javaPlugin;

    public static void init(JavaPlugin javaPlugin){
        MongoDB.javaPlugin = javaPlugin;
    }


    static {
        String mongoURI;

        if(PluginConfig.MONGO_USER.isEmpty() && PluginConfig.MONGO_PASS.isEmpty()) {
            mongoURI  = "mongodb://" + PluginConfig.MONGO_HOST + ":" + PluginConfig.MONGO_PORT;
        } else{
            if(PluginConfig.MONGO_PASS.isEmpty()) {
                mongoURI  = "mongodb://" + PluginConfig.MONGO_USER +
                        "@"+ PluginConfig.MONGO_HOST + ":" + PluginConfig.MONGO_PORT;
            }else {
                mongoURI  ="mongodb://" + PluginConfig.MONGO_USER + ":"+ PluginConfig.MONGO_PASS
                        + "@" + PluginConfig.MONGO_HOST + ":" + PluginConfig.MONGO_PORT;
            }
        }
        if(PluginConfig.MONGO_SSL){
            mongoURI = mongoURI + "&tls=true";
        }


        client = MongoClients.create(mongoURI);

        if(!PluginConfig.MONGO_DATABASE.isEmpty()) {
            db = client.getDatabase(PluginConfig.MONGO_DATABASE);
            collection = db.getCollection("players");
        }else {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[Error] Set database name!");
            Bukkit.getServer().getPluginManager().disablePlugin(javaPlugin);
        }

//        Logger logger = Logger.getLogger("org.mongodb.driver");
//        logger.setLevel(Level.SEVERE);
    }

    public static Document getUser(String key, String value){
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put(key, value);
        return collection.find(dbObject).first();
    }

    public static void addUser(Document document){
        collection.insertOne(document);
    }

    public static void close(){
        client.close();
    }

}
