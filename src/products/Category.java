package products;

public enum Category {
	MISC("Miscellaneous"),
	FRUITS("Fruits"),
	VEGETABLES("Vegetables"),
	MEAT("Meat"),
	DAIRY("Dairy"),
	COSMETICS("Cosmetics");

	private String name;
	private Category(String name) {
		this.name = name;
	}

	@Override
	public String toString(){
		return name;
	}
}