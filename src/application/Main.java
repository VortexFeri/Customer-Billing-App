package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

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

	private static Stage stg;
	public static void main(String[] args) {
//		connect("jdbc:mysql://localhost:3306/uni_project","user_1", "password_test");
//
//		InventoryList inv;
//		ResultSet products = getTable("products");
//		inv = InventoryItem.loadFromResultSet(products);
//		assert inv != null;
//		inv.forEach((i) -> {
//			i.print();
//			System.out.println();
//		});

		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stg = stage;
		stage.setResizable(false);
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../gui/login.fxml")));
		stage.setTitle("Log in");
		stage.setScene(new javafx.scene.Scene(root, 600, 300));
		stage.show();
	}

	public void loadHome() {
		try {
			Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../gui/main.fxml")));
			stg.setScene(new javafx.scene.Scene(pane, 900, 600));
			stg.setResizable(true);
			stg.setTitle("Home");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
