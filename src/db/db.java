package db;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Vector;

import static utils.macros.log;


public class db  {
    public static boolean loggedIn = false;
    public static boolean databaseSelected = false;
    public static MongoDatabase database;
    public static Vector<MongoCollection<Document>> localCollections = new Vector<>();
    public static MongoClient mongoClient;

    public static boolean isOpen() {
        if (!loggedIn) {
            log("Error: You are not logged in!");
            return false;
        }
        if (database == null) {
            log("Error: You have not selected a database!");
            return false;
        }
        return true;
    }

    public static boolean login(String username, String password) {
        String uri = "mongodb+srv://" + username + ":" + password + "@inventory0.p0d4fuf.mongodb.net/?retryWrites=true&w=majority";

        // logging in
        try {
            mongoClient = MongoClients.create(uri);
            MongoDatabase database = mongoClient.getDatabase("admin");
            try {
                // Send a ping to confirm a successful connection
                Bson command = new BsonDocument("ping", new BsonInt64(1));
                Document commandResult = database.runCommand(command);
                log("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                log(e.toString());
                return false;
            }
        }
        catch (MongoException e) {
            log("Error: " + e);
            return false;
        }
        loggedIn = true;
        log("Logged in successfully as: " + username);
        return true;
    }

    @org.jetbrains.annotations.Nullable
    public static ArrayList<String> getDatabaseNames() {
        if (!loggedIn) {
            log("Error: You are not logged in!");
            return null;
        }
        return mongoClient.listDatabaseNames().into(new ArrayList<>());
    }

    public static boolean selectDatabase(String name) {
        if (!loggedIn) {
            log("Error: You are not logged in!");
            return false;
        }
        try {
            database = mongoClient.getDatabase(name.strip().toLowerCase());
        } catch (Exception e) {
            log("Error: " + e);
            database = null;
            return false;
        }
        if (database.listCollectionNames().into(new ArrayList<>()).isEmpty()) {
            log("Error: Database " + name + " not found!");
            database = null;
            return false;
        }
        log("Database selected: " + name);
        return true;
    }

    public static boolean storeCollections() {
        if (!isOpen())
            return false;
        try {
            for (String name : database.listCollectionNames()) {
                localCollections.add(database.getCollection(name));
            }
        } catch (Exception e) {
            log("Error: " + e);
            return false;
        }
        log("Collections stored locally!");
        return true;
    }

    public static void logout() {
        mongoClient.close();
        loggedIn = false;
        database = null;
        localCollections.clear();
        log("Logged out");
    }

    public static void printLocalCollection(String name) {
        if (localCollections.isEmpty()) {
            log("Error: You have not stored any collections locally!");
            return;
        }
        boolean found_anything = false;
        for (MongoCollection<Document> collection : localCollections)
            if (collection.getNamespace().getCollectionName().equals(name)) {
                log("Printing " + name + " collection");
                for (Document document : collection.find())
                    System.out.println(document.toJson());
                found_anything = true;
            }
        if (!found_anything)
            log("Error: Could not find collection " + name);
    }

    public static void printCollectionFromDatabase(String name) {
        if (!isOpen())
            return;
        boolean found_anything = false;
        for (String collection_name : database.listCollectionNames())
            if (collection_name.equals(name)) {
                log("Printing " + name + " collection");
                for (Document document : database.getCollection(name).find())
                    System.out.println(document.toJson());
                found_anything = true;
            }
        if (!found_anything)
            log("Error: Could not find collection: " + name);
    }

    public static MongoCollection<Document> getCollectionFromDatabase(String name) {
        if (!isOpen())
            return null;
        boolean found_anything = false;
        for (String collection_name : database.listCollectionNames())
            if (collection_name.equals(name)) {
                log("Found \"" + name + "\" collection " + "in database \"" + database.getName() + "\" (containing " + database.getCollection(name).countDocuments() + " entries)");
                found_anything = true;
                return database.getCollection(name);
            }
        log("Error: Could not find collection: " + name);
        return null;
    }

    public static void addCollection(String name) {
        try {
            database.createCollection(name);
        } catch (Exception e) {
            log("Error: " + e);
            return;
        }
        log("Created collection: " + name);
    }
    public static void addItemToCollection(String collection_name, Document document) {
        if (!isOpen())
            return;
        if (getCollectionFromDatabase(collection_name) == null) {
            log("Error: Could not find collection: " + collection_name);
            return;
        }
        try {
            database.getCollection(collection_name).insertOne(document);
        } catch (Exception e) {
            log("Error: " + e);
            return;
        }
    }
}