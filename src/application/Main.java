package application;

import stock_logic.InventoryItem;
import stock_logic.InventoryList;

import java.sql.ResultSet;
import java.sql.SQLException;

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

	public static void main(String[] args) throws SQLException {
		//login to the database
		connect("jdbc:mysql://localhost:3306/uni_project","user_1", "password_test");

		InventoryList inv;
		ResultSet products = getTable("products");
		inv = InventoryItem.loadFromResultSet(products);
		assert inv != null;
		inv.forEach((i) -> {
			i.print();
			System.out.println();
		});
		updateOne("products", 10, "stock", "1");
		InventoryList updated = InventoryItem.loadFromResultSet(getTable("products"));
		assert updated != null;
		updated.forEach((i) -> {
			i.print();
			System.out.println();
		});
	}
}
