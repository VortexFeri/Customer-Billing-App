package stock_logic;

import products.Category;
import products.Product;
import products.UnitType;

import java.util.Objects;

public class InventoryItem extends Inventory {
	private final String id;
	private Product product;
	private int stock;
	
	public InventoryItem(String name, Category category, float price, UnitType unit, int qty, String id) {
		product = new Product(name, category, price, unit);
		stock = qty;
		this.id = id == null ? String.valueOf(this.hashCode()) : id;
	}

	public InventoryItem(String name, Category category, float price, UnitType unit, int qty) {
		this(name, category, price, unit, qty, null);
	}

	public InventoryItem(Product product, Integer stock, String id) {
		this(product.getName(), product.getCategory(), product.getPrice(), product.getUnit(), stock, id);
	}

	public InventoryItem(Product product, Integer stock) {
		this(product, stock, null);
	}

	//	getters
	public String getName() { return product.getName(); }
	public Category getCategory() { return product.getCategory(); }
	public float getPrice() { return product.getPrice(); }
	public UnitType getUnit() { return product.getUnit(); }
	public int getStock() { return stock; }
	public String getId() { return id; }
	public Product getProduct() { return product; }
//	setters
	public void setName(String newName) { product.setName(newName); }
	public void setPrice(float newPrice) { product.setPrice(newPrice); }
	public void setStock(int newQty) { stock = newQty; }

	public boolean equalsProduct(Product product) {
		return Objects.equals(product.getName(), this.getName()) &&
				product.getCategory() == this.getCategory() &&
				product.getUnit() == this.getUnit() &&
				product.getPrice() == this.getPrice();
	}
	public void print() {
		System.out.println("----------" + "ID = " + id + "----------");
		System.out.println("Stock left: " + stock);
		product.print();
	}
}
