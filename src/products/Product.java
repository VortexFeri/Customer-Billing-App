package products;

public class Product {
	private int id;
	private String name;
	private Categories category;
	private float price;
	private UnitType unit;
	private int qty;
	private String imgLink;
	
	public Product(int id, String name, Categories category, float price, UnitType unit, int qty, String imgLink) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.price = price;
		this.unit = unit;
		this.qty = qty;
		this.imgLink = imgLink;
	}
	
	public Product(int id, String name, float price, UnitType unit, int qty, String imgLink) {
		this(id, name, Categories.MISC, price, unit, qty, imgLink);
	}
	
	public Product(int id, String name, float price, UnitType unit, int qty) {
		this(id, name, Categories.MISC, price, unit, qty, null);
	}
}
