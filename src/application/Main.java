package application;

import stock_logic.Inventory;
import stock_logic.InventoryItem;

import java.util.ArrayList;

import static db.db.login;
import static stock_logic.actions.sell;

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
		ArrayList<Inventory> fullInventory = Inventory.loadAll();

		assert fullInventory != null;
		for(Inventory inventory : fullInventory) {
			inventory.print();
			System.out.println("\n");
		}
		InventoryItem item = fullInventory.get(0).getItemAtId(0);
		sell(fullInventory.get(0), item, 2);
	}
}
