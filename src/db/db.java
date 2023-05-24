package db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Vector;

import static utils.macros.log;


public class db  {
    public static boolean loggedIn = false;
    public static boolean databaseSelected = false;
    public static MongoDatabase database;
    public static Vector<MongoCollection<Document>> localCollections = new Vector<>();
    public static MongoClient mongoClient;

    public static boolean login(String username, String password) {
        String uri = "mongodb+srv://" + username + ":" + password + "@inventory0.p0d4fuf.mongodb.net/?retryWrites=true&w=majority";

        // logging in
        try {
            mongoClient = MongoClients.create(uri);
        } catch (Exception e) {
            log("Error: " + e);
            return false;
        }
        loggedIn = true;
        log("Logged in successfully!");
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
        if (!loggedIn) {
            log("Error: You are not logged in!");
            return false;
        }
        if (database == null) {
            log("Error: You have not selected a database!");
            return false;
        }
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
        if (!loggedIn) {
            log("Error: You are not logged in!");
            return;
        }
        if (database == null) {
            log("Error: You have not selected a database!");
            return;
        }
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

    public static MongoCollection<Document> getCollectionFromDatabase(String fruits) {
        if (!loggedIn) {
            log("Error: You are not logged in!");
            return null;
        }
        if (database == null) {
            log("Error: You have not selected a database!");
            return null;
        }
        boolean found_anything = false;
        for (String collection_name : database.listCollectionNames())
            if (collection_name.equals(fruits)) {
                log("Found " + fruits + " collection");
                found_anything = true;
                return database.getCollection(fruits);
            }
        log("Error: Could not find collection: " + fruits);
        return null;
    }
}