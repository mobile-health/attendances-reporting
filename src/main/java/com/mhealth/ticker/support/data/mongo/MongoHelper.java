package com.mhealth.ticker.support.data.mongo;

import com.google.gson.Gson;
import com.mhealth.ticker.support.data.Config;
import com.mhealth.ticker.support.data.MongoObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;
import java.util.logging.Logger;

public class MongoHelper {

    private static Logger logger = Logger.getLogger(MongoHelper.class.getName());

    private static MongoDatabase database;

    private static final Object lock = new Object();

    private MongoHelper() {

    }

    private static MongoDatabase getMongoDatabase() {
        if (database == null) {
            synchronized (lock) {
                MongoClient mongoClient = new MongoClient(Config.getInstance().getMongo());
                database = mongoClient.getDatabase(Config.getInstance().getMongoName());
            }
        }
        return database;
    }

    public static <T extends MongoObject> void insert(T obj, Class<T> clazz) {
        MongoCollection<Document> collection = getCollection(clazz.getSimpleName());
        Document document = Document.parse(new Gson().toJson(obj));
        long nowL = System.currentTimeMillis();
        Date now = new Date(nowL);
        document.put(MongoObject.CREATED_AT, now);
        document.put(MongoObject.CREATED_AT + "_l", nowL);
        document.put(MongoObject.MODIFIED_AT, now);
        document.put(MongoObject.MODIFIED_AT + "_l", nowL);
        collection.insertOne(Document.parse(new Gson().toJson(obj)));
    }

    public static <T extends MongoObject> T getLatest(Class<T> clazz) {
        MongoCollection<Document> collection = getCollection(clazz.getSimpleName());
        Document document = collection.find().limit(1).first();
        if (document != null) {
            return new Gson().fromJson(document.toJson(), clazz);
        }
        return null;
    }

    public static <T extends MongoObject> T convert(Document document, Class<T> clazz) {
        //logger.info("Convert document to object " + document.toJson());
        if (document == null) return null;
        return new Gson().fromJson(document.toJson(), clazz);
    }

    public static MongoCollection<Document> getCollection(String name) {
        return getMongoDatabase().getCollection(name);
    }

    public static MongoCollection<Document> getCollection(Class<? extends MongoObject> clazz) {
        return getCollection(clazz.getSimpleName());
    }
}
