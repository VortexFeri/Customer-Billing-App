package stock_logic;

import org.jetbrains.annotations.NotNull;
import products.Category;
import products.Product;
import products.UnitType;

import java.sql.ResultSet;
import java.util.Objects;
import java.util.logging.Logger;

import static application.utils.log;

public class InventoryItem extends Product {
	protected String image_url;
	private int stock;
	private final int id;
	public static InventoryList fullInv = new InventoryList();

	public InventoryItem() {
		super("Unknown", Category.OTHER, 0, UnitType.PCS);
		this.stock = 0;
		this.id = -1;
	}

	public InventoryItem(int id, @NotNull Product product, int stock) {
		super(product.getName(), product.getCategory(), product.getPrice(), product.getUnit());
		this.stock = stock;
		this.id = id;
	}
	public InventoryItem(int id, @NotNull Product product) {
		this(id, product, 0);
	}

	public static InventoryItem findItemById(int id) {
		for (InventoryItem item : fullInv) {
			if (item.getId() == id) {
				return item;
			}
		}
		return null;
	}

	public boolean equalsProduct(Product product) {
		return Objects.equals(product.getName(), this.getName()) &&
				product.getCategory() == this.getCategory() &&
				product.getUnit() == this.getUnit() &&
				product.getPrice() == this.getPrice();
	}
	public void print() {
		System.out.println("ID: " + id);
		System.out.println("Stock left: " + stock);
		super.print();
	}

	public static InventoryList loadFromResultSet(ResultSet resultSet) {
		if (resultSet == null) {
			log("Error: ResultSet is null!");
			return null;
		}
		try {
			InventoryList inv = new InventoryList();
			while (resultSet.next()) {
				Category cat;
				UnitType unit;
				try {
					String cString =resultSet.getString("Category").toUpperCase();
					cat = Category.valueOf(cString);
				} catch (IllegalArgumentException e) {
					log("Error: Invalid category! (Defaulting to OTHER)");
					cat = Category.OTHER;
				}
				try {
					unit = UnitType.valueOf(resultSet.getString("Unittype").toUpperCase());
				} catch (IllegalArgumentException e) {
					log("Error: Invalid unit for item" +resultSet.getString("Name") + "! (Defaulting to PIECE)");
					unit = UnitType.PCS;
				}
				Product product = new Product(resultSet.getString("Name"), cat, resultSet.getFloat("Price"), unit);
				inv.add(new InventoryItem(resultSet.getInt("ProductID"), product, resultSet.getInt("Quantity")));
			}
			return inv;
		} catch (Exception e) {
			Logger.getLogger(InventoryItem.class.toString()).severe(e.getMessage());
			log("Error: ResultSet is invalid!");
			return null;
		}
	}

	public int getId() {
		return id;
	}

	//setters
	public void setStock(int newStock) { stock = newStock; }
	public void setImage_url(String image_url) { this.image_url = image_url; }
	//getters
	public int getStock() { return stock; }
	public String getImage_url() { return image_url; }
}
