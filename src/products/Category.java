package products;

public enum Category {
	OTHER("Other"),
	FRUIT("Fruit"),
	VEGETABLE("Vegetable"),
	MEAT("Meat"),
	DAIRY("Dairy");

	private final String name;
	Category(String name) {
		this.name = name;
	}

	@Override
	public String toString(){
		return name;
	}
}