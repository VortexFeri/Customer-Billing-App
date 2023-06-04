package application;

import gui.MainView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
	private static Stage stg;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception  {
		stg = stage;
		stage.setResizable(false);
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../gui/login.fxml")));
		stage.setTitle("Log in");
		stage.setScene(new Scene(root, 600, 300));
		stage.show();
	}

	public void loadHome() {
		try {
			Parent pane = MainView.initLayout();
			stg.setScene(new javafx.scene.Scene(pane, 1020, 600));
			stg.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("../gui/style.css")).toExternalForm());
			stg.setResizable(true);
			stg.setTitle("Home");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
