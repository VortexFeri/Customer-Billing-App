package application;

import db.db;
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
		db.login("user_1", "password_test");
		db.selectDatabase("inventory");
	}
}
