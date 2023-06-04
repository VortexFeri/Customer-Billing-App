package gui;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import stock_logic.InventoryItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static db.db.*;
import static gui.ProductCard.showProducts;
import static stock_logic.InventoryItem.fullInv;

public class Login {
    public Login() {}

    @FXML
    private TextField username_field;
    @FXML
    private PasswordField password_field;

    public void userLogin() throws SQLException {
        Main m = new Main();
        Dialog<String> dialog = new Dialog<>();
        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("myDialogs.css")).toExternalForm());
        dialog.getDialogPane().getStyleClass().add("myDialog");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        dialog.setTitle("Error");
        try {
            connect("jdbc:sqlserver://warehouseprojectunitbv.database.windows.net:1433;database=warehouse",
                    username_field.getText(),
                    password_field.getText());
        } catch (SQLException e) {
            e.printStackTrace();
            dialog.setContentText("Error: Access denied. Please check your username and password.");
            dialog.show();
        }
        if (loggedIn) {
            ResultSet products = getTable("dbo.product");
            fullInv = InventoryItem.loadFromResultSet(products);
            m.loadHome();
            showProducts(fullInv);
        }
    }
}
