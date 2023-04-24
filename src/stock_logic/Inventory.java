package stock_logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import products.Category;
import products.Product;
import products.UnitType;

public class Inventory {
	private Category category;
	protected ArrayList<InventoryItem> products;
	
	public Inventory(Category category) {
		this.category = category;
		if (category == null) this.category = Category.MISC;
		products = new ArrayList<>();
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
		products.add(item);
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
		getItemAtId(id).setQty(newQty);
	}
	public int getStock(Product product) {
		int id = getId(product);
		if (id == -1) return 0;
		return getItemAtId(id).getQty();
	}
	public int getId(Product product) {
	     int id = 0;
	     if (products.size() > 0)
		     for (InventoryItem item : products) {
		       if (item.equalsProduct(product)) return id;
		       id++;
		     }
	     return -1;
	}
	public InventoryItem getItemAtId(int id) {
		return products.get(id);
	}
	public boolean alreadyHasProduct(Product product) {
		if (getId(product) == -1) return false;
		return true;
	}
	public void print() {
		for (InventoryItem item : products) {
			item.print();
		}
	}
}
