package gui;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import stock_logic.InventoryItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static db.db.*;
import static gui.ProductCard.showProducts;
import static stock_logic.InventoryItem.fullInv;

public class Login {
    private static final PasswordField password_field = new PasswordField();
    private static final TextField username_field = new TextField();

    public static Parent initLayout() {
        StackPane root = new StackPane();
        GridPane pane = new GridPane();
        root.getChildren().add(pane);
        pane.setAlignment(Pos.CENTER);

        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPrefSize(600, 300);
        pane.getStylesheets().add(Objects.requireNonNull(Login.class.getResource("style.css")).toExternalForm());

        username_field.setPromptText("Username");
        username_field.setPrefWidth(200);
        pane.add(username_field, 1, 1);

        password_field.setPromptText("Password");
        password_field.setPrefWidth(200);
        pane.add(password_field, 1, 2);

        // Submit button
        Button submit = new Button("Log in");
        submit.setPrefWidth(200);
        submit.setDefaultButton(true);
        submit.setOnAction(e -> userLogin());
        pane.add(submit, 1, 3);

        return root;
    }

    public static void userLogin() {
        Main m = new Main();
        Dialog<String> dialog = new Dialog<>();
        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(Login.class.getResource("myDialogs.css")).toExternalForm());
        dialog.getDialogPane().getStyleClass().add("myDialog");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        dialog.setTitle("Error");
        try {
            connect("jdbc:sqlserver://warehouseprojectunitbv.database.windows.net:1433;database=warehouse",
                    username_field.getText(),
                    password_field.getText());
        } catch (SQLException e) {
            dialog.setContentText("Error: Access denied. Please check your username and password.");
            dialog.show();
        }
        if (loggedIn) {
            try {
                ResultSet products = getTable("dbo.product");
                fullInv = InventoryItem.loadFromResultSet(products);
                m.loadHome();
                showProducts(fullInv);
            }
            catch (SQLException e) {
                dialog.setContentText(e.getMessage());
                dialog.show();
            }
        }
    }
}
