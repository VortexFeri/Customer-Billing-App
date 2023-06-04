package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainView {
    public static VBox mainView = new VBox();
    public static TilePane productsView = new TilePane();
    public static TableView<TableModel.TableEntry> billTable = new TableView<>();
    public static VBox billView = new VBox();
    public static HBox nav = new HBox();

    public static HBox billButtons = new HBox();
    public static HBox filterButtons = new HBox();
    public static Label totalLabel = new Label("Total: 0");
    public static TextField searchField = new TextField();
    public static BorderPane root = new BorderPane();
    public static ComboBox<String> categoriesDropDown = new ComboBox<String>();



    public static final ObservableList<TableModel.TableEntry> data = FXCollections.observableArrayList();
    public static Parent initLayout() {
//      filter buttons
        {
            categoriesDropDown.setPromptText("Category");
            ResultSet categories = db.db.getDistinct("product", "category");
            categoriesDropDown.getItems().add("All");
            try {
                while (true) {
                    assert categories != null;
                    if (!categories.next()) break;
                    categoriesDropDown.getItems().add(categories.getString("category"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            categoriesDropDown.getSelectionModel().selectFirst();
            categoriesDropDown.setOnAction(e -> Actions.filter(categoriesDropDown.getValue()));
            categoriesDropDown.setPrefHeight(30);
            filterButtons.setSpacing(10);
            filterButtons.getChildren().addAll(categoriesDropDown, searchField);
        }

//        search bar
        {
            searchField.setPromptText("Search");
            searchField.setOnKeyReleased(e -> Actions.search(searchField.getText()));
            searchField.setPrefHeight(30);
        }

//        products view
        {
            productsView.setPadding(new Insets(10, 10, 10, 10));
            productsView.setHgap(10);
            productsView.setVgap(10);
            productsView.setPrefColumns(4);
            // make it scrollable
            ScrollPane scrollPane = new ScrollPane(productsView);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            mainView.getChildren().add(scrollPane);
        }
//        bill view
        {
            TableColumn nameColumn = new TableColumn("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn priceColumn = new TableColumn("Price");
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            TableColumn quantityColumn = new TableColumn("Quantity");
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            TableColumn totalColumn = new TableColumn<>("Total");
            totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
            billTable.setItems(data);
            billTable.getColumns().addAll(nameColumn, priceColumn, quantityColumn, totalColumn);

            billTable.setPrefWidth(325);
            billTable.setEditable(true);
            billTable.setSortPolicy((param) -> false);

            billTable.getColumns().forEach((column) -> column.setStyle("-fx-alignment: CENTER-LEFT;"));
            billTable.getColumns().forEach((column) -> column.setMinWidth(75));
            billTable.setPlaceholder(new Label("No items selected"));
            VBox.setVgrow(billTable, Priority.ALWAYS);

            billView.getChildren().addAll(billTable, totalLabel);
            billView.setSpacing(10);
            billView.setPadding(new Insets(10, 10, 10, 10));
            billView.setAlignment(Pos.BOTTOM_RIGHT);


            totalLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        }
        // bill table action buttons
        {
            Button removeButton = new Button("Remove");
            removeButton.setOnAction(e -> Actions.remove());
            Button edit = new Button("Edit");
            edit.setOnAction(e -> Actions.edit());
            Button checkout = new Button("Checkout");
            checkout.setOnAction(e -> {
                try {
                    Actions.checkout();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            removeButton.setPrefHeight(30);
            edit.setPrefHeight(30);
            checkout.setPrefHeight(30);

            billButtons.getChildren().addAll(removeButton, edit, checkout);
            billButtons.setSpacing(10);
            billButtons.setAlignment(Pos.TOP_RIGHT);
            billButtons.setPrefWidth(320);
        }

        nav.getChildren().addAll(filterButtons, billButtons);
        HBox.setHgrow(billButtons, Priority.ALWAYS);
        nav.setPadding(new Insets(10, 10, 10, 10));
        nav.setAlignment(Pos.BOTTOM_RIGHT);
        nav.setStyle("-fx-background-color: #e0e0e0;");

        root.setTop(nav);
        root.setCenter(mainView);
        root.setRight(billView);
        return root;
    }
}
