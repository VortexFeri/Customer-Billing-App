package application;

import products.UnitType;
import stock_logic.Inventory;

import static db.db.*;

public class Main {

	//TODO add a menu
	//TODO VAT and total price
	//TODO company saving system
	//TODO user system
	//TODO search by name
	//TODO receipt system

	//connecting to database
	//https://www.youtube.com/watch?v=akW6bzoRcZo

	public static void main(String[] args) {
		//login to the database
		login("user_1", "password_test");
		selectDatabase("inventory");
		storeCollections();
		Inventory fruits = new Inventory();
		fruits.initFromCollection(getCollectionFromDatabase("fruits"));
		fruits.addProduct("Apple", 1.5f, UnitType.KG, 5);
	}
}
