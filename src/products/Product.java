package products;

public class Product {
	private final String name;
	private final Category category;
	private final float price;
	private final UnitType unit;

	public Product(String name, Category category, float price, UnitType unit) {
		this.name = name;
		this.category = category;
		this.price = price;
		this.unit = unit;
	}
	
//	Alternative constructors
	public Product(String name, float price, UnitType unit) {
		this(name, Category.OTHER, price, unit);
	}
	public Product(String name, Category category, float price) {
		this(name, category, price, UnitType.PCS);
	}
	
	public Product(String name, float price) {
		this(name, Category.OTHER, price, UnitType.PCS);
	}

	public void print() {
		System.out.println("Name: " + name);
		System.out.println("Category: " + category);
		System.out.println("Price: " + price);
		System.out.println("Unit type: " + unit);
	}
//	getters
	public String getName() { return name; }
	public Category getCategory() { return category; }
	public float getPrice() { return price; }
	public UnitType getUnit() { return unit; }
}
