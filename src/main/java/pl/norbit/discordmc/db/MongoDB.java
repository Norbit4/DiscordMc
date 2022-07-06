package pl.norbit.discordmc.db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import pl.norbit.discordmc.server.config.PluginConfig;

public class MongoDB {
    private static MongoClient client;
    private static MongoDatabase db;
    private static MongoCollection<Document> collection;

    public static void start(JavaPlugin javaPlugin){

        System.out.println("start mongodb");
        String mongoURI;

        if(PluginConfig.DATABASE_USER.isEmpty() && PluginConfig.DATABASE_PASS.isEmpty()) {
            mongoURI  = "mongodb://" + PluginConfig.DATABASE_HOST + ":" + PluginConfig.MONGO_PORT;
        } else{
            if(PluginConfig.DATABASE_PASS.isEmpty()) {
                mongoURI  = "mongodb://" + PluginConfig.DATABASE_USER +
                        "@"+ PluginConfig.DATABASE_HOST + ":" + PluginConfig.MONGO_PORT;
            }else {
                mongoURI  ="mongodb://" + PluginConfig.DATABASE_USER + ":"+ PluginConfig.DATABASE_PASS
                        + "@" + PluginConfig.DATABASE_HOST + ":" + PluginConfig.MONGO_PORT;
            }
        }
        if(PluginConfig.MONGO_SSL){
            mongoURI = mongoURI + "&tls=true";
        }

        client = MongoClients.create(mongoURI);

        if(!PluginConfig.DATABASE_NAME.isEmpty()) {
            db = client.getDatabase(PluginConfig.DATABASE_NAME);
            collection = db.getCollection("players");
        }else {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[Error] Set database name!");
            Bukkit.getServer().getPluginManager().disablePlugin(javaPlugin);
        }
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
