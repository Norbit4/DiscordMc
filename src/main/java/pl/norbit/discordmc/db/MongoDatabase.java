package pl.norbit.discordmc.db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import pl.norbit.discordmc.DiscordMc;
import pl.norbit.discordmc.server.config.PluginConfig;
import pl.norbit.pluginutils.database.MongoDB;

public class MongoDatabase {

    private static MongoCollection<Document> collection;
    private static MongoDB mongoDB;

    public static void start(){

        mongoDB = MongoDB
                .builder()
                .host(PluginConfig.DATABASE_HOST)
                .password(PluginConfig.DATABASE_PASS)
                .port(PluginConfig.DATABASE_PORT)
                .user(PluginConfig.DATABASE_USER)
                .useSSL(PluginConfig.DATABASE_SSL)
                .build();

        mongoDB.openConnection();

        collection = mongoDB.getDatabase(PluginConfig.DATABASE_NAME).getCollection("players");
    }

    public static void deleteUser(String key, String value){
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.append(key, value);

        collection.deleteOne(dbObject);
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
        mongoDB.closeConnection();
    }
}

