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
