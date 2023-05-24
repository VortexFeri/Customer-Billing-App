package application;

import stock_logic.Inventory;

import java.util.ArrayList;
import java.util.Objects;

import static db.db.*;

public class Main {

	//TODO add a menu
	//TODO add a way to save the inventory
	//TODO add a way to load the inventory
	//TODO VAT and total price
	//TODO company saving system
	//TODO connect to a database
	//TODO user system
	//TODO search by name
	//TODO receipt system

	//connecting to database
	//https://www.youtube.com/watch?v=akW6bzoRcZo

	public static void main(String[] args) {
		//login to the database
		login("user_1", "password_test");

		ArrayList<Inventory> inventories = new ArrayList<>();
		if (getDatabaseNames() != null)
			for(String name : Objects.requireNonNull(getDatabaseNames())) {
				if (name.equals("admin") || name.equals("local")) continue;
				selectDatabase(name);
				for (String collectionName : Objects.requireNonNull(database.listCollectionNames().into(new ArrayList<>()))) {
					Inventory inventory = new Inventory();
					inventory.initFromCollection(getCollectionFromDatabase(collectionName));
					inventories.add(inventory);
					inventory.print();
					System.out.println("\n");
				}
			}
	}
}
