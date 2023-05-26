package products;

public enum UnitType {
	PIECE("piece"),
	KG("kg"),
	GRAMS_100("100g");

	private final String name;
	UnitType(String name) {
		this.name = name;
	}

	@Override
	public String toString(){
		return name;
	}
}

