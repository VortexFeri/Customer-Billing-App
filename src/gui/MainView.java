package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class MainView {
    public static VBox categoryButtons = new VBox();
    public static TilePane productsView = new TilePane();
    public static TableView<TableColumn<String, String>> billView = new TableView<>();

    public static Parent initLayout() {
        BorderPane root = new BorderPane();

//        category buttons
        {
            Button vegButton = new Button("Vegetables");
            Button fruitButton = new Button("Fruits");
            Button meatButton = new Button("Meat");
            Button dairyButton = new Button("Dairy");
            Button otherButton = new Button("Other");
            categoryButtons.getChildren().addAll(vegButton, fruitButton, meatButton, dairyButton, otherButton);
            categoryButtons.setSpacing(0);
            categoryButtons.setAlignment(Pos.TOP_LEFT);
            categoryButtons.setPrefWidth(230);
            categoryButtons.getChildren().forEach(button -> button.setLayoutX(10));
            categoryButtons.getChildren().forEach((node) -> {
                if (node instanceof Button) {
                    ((Button) node).setMinWidth(categoryButtons.getPrefWidth() - 2);
                    ((Button) node).setPrefHeight(50);
                }
            });
        }
//        products view
        {
            productsView.setPadding(new Insets(10, 10, 10, 10));
            productsView.setHgap(10);
            productsView.setVgap(10);
            productsView.setPrefColumns(4);
        }
//        bill view
        {
            TableColumn nameColumn = new TableColumn<>("Name");
            TableColumn priceColumn = new TableColumn<>("Price");
            TableColumn quantityColumn = new TableColumn<>("Quantity");
            TableColumn totalColumn = new TableColumn<>("Total");
            billView.getColumns().addAll(nameColumn, priceColumn, quantityColumn, totalColumn);

            billView.setPrefWidth(300);
            billView.setEditable(false);
            billView.setSortPolicy((param) -> false);

            billView.getColumns().forEach((column) -> column.setStyle("-fx-alignment: CENTER-LEFT;"));
            billView.getColumns().forEach((column) -> column.setMinWidth(75));
        }

        AnchorPane.setRightAnchor(billView, 0d);
        AnchorPane.setBottomAnchor(billView, 0d);
        AnchorPane.setLeftAnchor(categoryButtons, 0d);
        AnchorPane.setTopAnchor(categoryButtons, 0d);
        root.setLeft(categoryButtons);
        root.setCenter(productsView);
        root.setRight(billView);
        return root;
    }
}
