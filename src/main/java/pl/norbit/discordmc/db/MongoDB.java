package pl.norbit.discordmc.db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import pl.norbit.discordmc.server.config.PluginConfig;

public class MongoDB {
    private final static MongoClient client;
    private final static MongoDatabase db;
    private final static MongoCollection<Document> collection;

    static {
        client = MongoClients.create("mongodb://" + PluginConfig.MONGO_HOST + ":" + PluginConfig.MONGO_PORT);
        db = client.getDatabase(PluginConfig.MONGO_DATABASE);
        collection = db.getCollection("players");
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
