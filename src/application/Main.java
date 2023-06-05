package application;

import gui.MainView;
import gui.Login;
import javafx.application.Application;
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
	public void start(Stage stage){
		stg = stage;
		stage.setResizable(false);
		Parent pane = Login.initLayout();
		stage.setTitle("Log in");
		stage.setScene(new Scene(pane, 600, 300));
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
