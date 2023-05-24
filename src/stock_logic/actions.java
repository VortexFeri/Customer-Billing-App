package stock_logic;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;
import static db.db.database;
import static db.db.isOpen;
import static utils.macros.log;

public class actions {
    public static void sell(Inventory inv, InventoryItem item, int qty) {
        item.setStock(item.getStock() - qty);
        try {
            updateInventory(database.getCollection(inv.getCategory().toString().toLowerCase()), item);
        } catch (MongoException e) {
            log("Error: " + e);
            item.setStock(item.getStock() + qty);
            return;
        }
        log("Sold " + qty + "*"+ item.getUnit().toString() + " " + item.getName() + " for $" + item.getPrice() * qty + ". New stock: " + item.getStock());
    }

    private static void updateInventory(MongoCollection<Document> coll, InventoryItem item) {
        if (!isOpen())
            return;
        if (item == null) {
            log("Error: item is null");
            return;
        }

        Document query = coll.find(eq("_id", new ObjectId(item.getId()))).first();

        Bson updates = Updates.combine(
                Updates.currentTimestamp("lastUpdated"),
                Updates.set("stock", item.getStock())
        );
        UpdateOptions options = new UpdateOptions().upsert(true);
        try {
            assert query != null;
            UpdateResult result = coll.updateOne(query, updates, options);
            System.out.println("Modified document count: " + result.getModifiedCount());
            System.out.println("Upserted id: " + result.getUpsertedId()); // only contains a value when an upsert is performed
            System.out.println("Matched count: " + result.getMatchedCount());
            System.out.println("Acknowledged? " + result.wasAcknowledged());
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
        }
    }
}
