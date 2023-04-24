package stock_logic;

import products.Category;
import products.Product;
import products.UnitType;

public class InventoryItem extends Inventory {
	private int id;
	private Product product;
	private int qty;
	
	public InventoryItem(String name, Category category, float price, UnitType unit, int qty) {
		product = new Product(name, category, price, unit);
		this.qty = qty;
		id = product.hashCode();
	}

//	getters
	public String getName() { return product.getName(); }
	public Category getCategory() { return product.getCategory(); }
	public float getPrice() { return product.getPrice(); }
	public UnitType getUnit() { return product.getUnit(); }
	public int getQty() { return qty; }
	public int getId() { return id; }
	public Product getProduct() { return product; }
//	setters
	public void setName(String newName) { product.setName(newName); }
	public void setPrice(float newPrice) { product.setPrice(newPrice); }
	public void setQty(int newQty) { qty = newQty; }

	public boolean equalsProduct(Product product) {
		if (product.getName() == this.getName() &&
				product.getCategory() == this.getCategory() &&
				product.getUnit() == this.getUnit() &&
				product.getPrice() == this.getPrice()
				) 
			return true;
		return false;
	}
	public void print() {
		System.out.println("----------" + "ID = " + id + "----------");
		System.out.println("Stock left: " + qty);
		product.print();
	}
	
}
