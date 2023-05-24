package stock_logic;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import products.Category;
import products.Product;
import products.UnitType;

import java.util.ArrayList;

import static utils.macros.log;

public class Inventory {
	private Category category;
	protected ArrayList<InventoryItem> items;

	public boolean initFromCollection(MongoCollection<Document> coll) {
		if (coll == null) {
			log("Error: collection is null");
			return false;
		}

		try {
			this.category = Category.valueOf((String) coll.getNamespace().getCollectionName().toUpperCase());
		} catch (IllegalArgumentException e) {
			log("Warning: " + e + " (defaulting to MISC)");
			this.category = Category.MISC;
		}

		items = new ArrayList<>();
		for (Document doc : coll.find()) {
			UnitType unit;
			try {
				unit = UnitType.valueOf(doc.getString("unit").toUpperCase());
			} catch (IllegalArgumentException e) {
				log("Warning: " + e + " (defaulting to PIECE)");
				unit = UnitType.PIECE;
			}
			Product product = new Product(doc.getString("name"), category, doc.get("price", Number.class).floatValue(), unit);
			InventoryItem item = new InventoryItem(product, doc.getInteger("stock"), doc.getObjectId("_id").toString());
			items.add(item);
		}
		return true;
	}
	
	public Inventory(Category category) {
		this.category = category;
		if (category == null) this.category = Category.MISC;
		items = new ArrayList<>();
	}
	public Inventory() {
		this(Category.MISC);
	}
	public void addProduct(Product product, int qty) {
//		if the item already exists add the stock up
		if (alreadyHasProduct(product)) {
			setStock(product, getStock(product)+qty);

			return;
		}
		InventoryItem item = new InventoryItem(product.getName(), product.getCategory(), product.getPrice(), product.getUnit(), qty);
		items.add(item);
	}
	public void addProduct(Product product) {
		addProduct(product, 1);
	}
	public void addProduct(String name, float price, UnitType unit, int qty) {
		Product product = new Product(name, category, price, unit);
		addProduct(product, qty);
	}
	public void addProduct(String name, float price, UnitType unit) {
		addProduct(name, price, unit, 1);
	}
	
	public void setStock(Product product, int newQty) {
		int id = getId(product);
		if (id == -1) return;
		getItemAtId(id).setStock(newQty);
	}
	public int getStock(Product product) {
		int id = getId(product);
		if (id == -1) return 0;
		return getItemAtId(id).getStock();
	}
	public int getId(Product product) {
	     int id = 0;
	     if (items.size() > 0)
		     for (InventoryItem item : items) {
		       if (item.equalsProduct(product)) return id;
		       id++;
		     }
	     return -1;
	}
	public InventoryItem getItemAtId(int id) {
		return items.get(id);
	}
	public boolean alreadyHasProduct(Product product) {
		return getId(product) != -1;
	}
	public void print() {
		for (InventoryItem item : items) {
			item.print();
		}
	}
}
