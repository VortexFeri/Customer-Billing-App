package gui;

import application.Main;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import stock_logic.InventoryItem;

import java.sql.ResultSet;
import java.sql.SQLException;

import static db.db.*;
import static stock_logic.InventoryItem.fullInv;

public class Login {
    public Login() {}

    @FXML
    private TextField username_field;
    @FXML
    private TextField password_field;

    public void userLogin() throws SQLException {
        Main m = new Main();
        Dialog<String> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        dialog.setTitle("Error");
        try {
            connect("jdbc:mysql://localhost:3306/uni_project",username_field.getText(), password_field.getText());
        } catch (CommunicationsException e) {
            dialog.setContentText("Error: Connection to database failed. The server might be offline");
            dialog.show();
        } catch (SQLException e) {
            dialog.setContentText("Error: Access denied. Please check your username and password.");
            dialog.show();
        }
        if (loggedIn) {
            ResultSet products = getTable("products");
            fullInv = InventoryItem.loadFromResultSet(products);
            m.loadHome();
        }
    }
}
