package application;

import products.Category;
import products.Product;
import products.UnitType;
import stock_logic.Inventory;

public class Main {

	public static void main(String[] args) {
		Inventory fruits = new Inventory(Category.FRUITS);
		
		Product apple = new Product("Apple", Category.FRUITS, 0.99f, UnitType.KG);
		Product orange = new Product("Orange", 1.29f);
		
		fruits.addProduct(apple);
		fruits.addProduct(orange);
//		fruits.setStock(orange, 4);
		fruits.addProduct(apple,2);
		fruits.addProduct("Pineapple", 0.29f, UnitType.KG);
		fruits.addProduct(apple);
		fruits.setStock(orange, 5);
		
		fruits.print();
	}

}
