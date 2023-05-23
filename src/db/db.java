package db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Vector;


public class db  {
    public static boolean loggedIn = false;
    public static MongoDatabase database;
    public static Vector<MongoCollection<Document>> localCollections = new Vector<>();
    public static MongoClient mongoClient;
    public static boolean login(String username, String password) {
        String uri = "mongodb+srv://" + username + ":" + password + "@inventory0.p0d4fuf.mongodb.net/?retryWrites=true&w=majority";

        // logging in
        try {
            mongoClient = MongoClients.create(uri);
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
        loggedIn = true;
        System.out.printf("[%s] Logged in as user: %s%n", java.time.LocalTime.now().truncatedTo(ChronoUnit.SECONDS), username);
        return true;
    }

    public static ArrayList<String> getDatabaseNames() {
        if (!loggedIn) {
            System.out.println("Error: You are not logged in!");
            return null;
        }
        return mongoClient.listDatabaseNames().into(new ArrayList<>());
    }

    public static boolean selectDatabase(String name) {
        if (!loggedIn) {
            System.out.println("Error: You are not logged in!");
            return false;
        }
        try {
            database = mongoClient.getDatabase(name.strip().toLowerCase());
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
        System.out.printf("[%s] Database selected: %s%n", java.time.LocalTime.now().truncatedTo(ChronoUnit.SECONDS), name);
        return true;
    }

    public static boolean storeCollections() {
        if (!loggedIn) {
            System.out.println("Error: You are not logged in!");
            return false;
        }
        if (database == null) {
            System.out.println("Error: You have not selected a database!");
            return false;
        }
        try {
            for (String name : database.listCollectionNames()) {
                localCollections.add(database.getCollection(name));
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
        System.out.printf("[%s] Collections stored locally!", java.time.LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
        return true;
    }

    public static void logout() {
        mongoClient.close();
        loggedIn = false;
        database = null;
        localCollections.clear();
        System.out.printf("[%s] Logged out!n", java.time.LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    public static void printLocalCollection(String name) {
        for (MongoCollection<Document> collection : localCollections)
            if (collection.getNamespace().getCollectionName().equals(name))
                for (Document document : collection.find())
                    System.out.println(document.toJson());
    }
    public static void printCollection(String name) {
        if (!loggedIn) {
            System.out.println("Error: You are not logged in!");
            return;
        }
        if (database == null) {
            System.out.println("Error: You have not selected a database!");
            return;
        }
        for (Document document : database.getCollection(name).find())
            System.out.println(document.toJson());
    }
}