package gui;

import application.utils;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import products.Category;
import products.UnitType;
import stock_logic.InventoryItem;
import stock_logic.InventoryList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static db.db.getTable;
import static gui.MainView.*;
import static gui.ProductCard.showProducts;
import static stock_logic.InventoryItem.fullInv;

public class Actions {
    public static void edit() {
        TableModel.TableEntry selectedItem = billTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            InventoryItem item = InventoryItem.findItemById(selectedItem.getId());
            assert item != null;
            Dialog<TableModel.TableEntry> dialog = new Dialog<>();

            dialog.getDialogPane().getStylesheets().add(
                    Objects.requireNonNull(Actions.class.getResource("myDialogs.css")).toExternalForm());
            dialog.getDialogPane().getStyleClass().add("myDialog");

            dialog.setTitle("Edit item");
            dialog.setHeaderText("Edit item");
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            TextField quantity = new TextField();
            quantity.setPromptText("Quantity");
            quantity.setText(String.valueOf(selectedItem.getQuantity()));
            dialogPane.setContent(quantity);
            dialog.setResultConverter((ButtonType button) -> {
                if (button == ButtonType.OK) {
                    int quantityValue = Integer.parseInt(quantity.getText());
                    if (quantityValue > item.getStock() + selectedItem.getQuantity()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error");

                        String u = "";
                        u = item.getUnit().equals(UnitType.PCS) ? " items" : " kg";
                        alert.setContentText(("Not enough items in stock. Only " + item.getStock() + u + " available"));
                        alert.showAndWait();
                        return null;
                    }
                    item.setStock(item.getStock() + selectedItem.getQuantity());
                    item.setStock(item.getStock() - quantityValue);
                    selectedItem.setQuantity(quantityValue);
                    selectedItem.computeTotal();
                    return selectedItem;
                }
                return null;
            });
            dialog.showAndWait();
            billTable.refresh();
            totalLabel.setText("Total: " + computeTotal() + " RON");
        }
    }

    public static void remove() {
        TableModel.TableEntry selectedItem = billTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            InventoryItem item = InventoryItem.findItemById(selectedItem.getId());
            assert item != null;
            item.setStock(item.getStock() + selectedItem.getQuantity());
            MainView.data.remove(selectedItem);
            totalLabel.setText("Total: " + computeTotal() + " RON");
        }
    }

    public static void checkout() throws SQLException {
        if (data.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No items in cart");
            alert.showAndWait();
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(Actions.class.getResource("myDialogs.css")).toExternalForm());
        dialog.getDialogPane().getStyleClass().add("myDialog");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH, ButtonType.CANCEL);
        dialog.setContentText("Total: " + computeTotal() + " RON" + "\nAre you sure you want to checkout?");

        Optional<ButtonType> res = dialog.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.CANCEL) {
            return;
        }
        if (res.isPresent() && res.get() == ButtonType.FINISH) {
            printBill();
        }

        data.forEach(entry -> {
            InventoryItem item = InventoryItem.findItemById(entry.getId());
            assert item != null;
            db.db.updateOne("dbo.product", item.getId(), "Quantity", String.valueOf(item.getStock()));
        });
        data.clear();
        billTable.refresh();
        productsView.getChildren().clear();
        ResultSet products = getTable("dbo.product");
        fullInv = InventoryItem.loadFromResultSet(products);
        totalLabel.setText("Total: 0 RON");
        assert fullInv != null;
        showProducts(fullInv);
    }

    private static void printBill() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if(job != null){

            int orderNumber = (int) Math.floor(Math.random() * 20 + 1);

            VBox image = new VBox();
            TableView<TableModel.TableEntry> tableCopy = new TableView<>();
            TableColumn nameColumn = new TableColumn("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            TableColumn priceColumn = new TableColumn("Price");
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            TableColumn quantityColumn = new TableColumn("Quantity");
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            TableColumn totalColumn = new TableColumn<>("Total");
            totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
            tableCopy.getColumns().addAll(nameColumn, priceColumn, quantityColumn, totalColumn);
            // table column horizontal grow
            nameColumn.prefWidthProperty().bind(tableCopy.widthProperty().multiply(0.4));
            priceColumn.prefWidthProperty().bind(tableCopy.widthProperty().multiply(0.1));
            quantityColumn.prefWidthProperty().bind(tableCopy.widthProperty().multiply(0.2));
            totalColumn.prefWidthProperty().bind(tableCopy.widthProperty().multiply(0.2));

            tableCopy.setItems(data);

            Label total = new Label("Total: " + computeTotal() + " RON");
            total.setPrefWidth(300);
            total.setPrefHeight(50);
            total.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-alignment: left;");

            image.getChildren().addAll(
                    new Label("Feri INC."),
                    new Label("Order number: " + orderNumber),
                    new Label("Date: " + java.time.LocalDate.now()),
                    new Label("Time: " + utils.currentTime()),
                    tableCopy,
                    total
            );

            Dialog<VBox> d = new Dialog<>();
            d.getDialogPane().getStylesheets().add(
                    Objects.requireNonNull(Actions.class.getResource("myDialogs.css")).toExternalForm());
            d.getDialogPane().getStyleClass().add("myDialog");

            d.getDialogPane().setContent(image);
            d.getDialogPane().setPrefWidth(500);
            job.showPrintDialog(null); // Window must be your main Stage
            job.printPage(new ImageView(
                    d.getDialogPane().snapshot(null, null)
            ));
            job.endJob();
        }
    }

    public static float computeTotal() {
        float total = 0;
        for (TableModel.TableEntry entry : data) {
            total += entry.getTotal();
        }
        // round to 2 decimals
        return (float) Math.round(total * 100) / 100;
    }

    public static void addToBill(InventoryItem item) {
        AtomicBoolean found = new AtomicBoolean(false);
        if (item.getStock() <= 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No more items in stock");
            alert.showAndWait();
            return;
        }
        MainView.data.forEach(entry -> {
            if (entry.getId() == item.getId()) {
                entry.setQuantity(entry.getQuantity() + 1);
                entry.computeTotal();
                billTable.refresh();
                found.set(true);
            }
        });
        if (!found.get()) {
            MainView.data.add(new TableModel.TableEntry(
                    item.getId(),
                    item.getName(),
                    item.getPrice(),
                    1
            ));
        }
        item.setStock(item.getStock() - 1);

        totalLabel.setText("Total: " + computeTotal() + " RON");
    }

    public static void filter(String category) {
        productsView.getChildren().clear();
        if (category.equals("All")) {
            showProducts(fullInv);
        }
        else {
            Category.valueOf(category.toUpperCase());
            InventoryList inventoryList = fullInv.filterByCategory(Category.valueOf(category.toUpperCase()));
            showProducts(inventoryList);
        }
    }

    public static void search(String text) {
        if (text.equals("")) {
            showProducts(fullInv);
        }
        else {
            categoriesDropDown.getSelectionModel().select(0);
            InventoryList inventoryList = new InventoryList();
            inventoryList = fullInv.searchByName(text);
            productsView.getChildren().clear();
            showProducts(inventoryList);
        }
    }
}
