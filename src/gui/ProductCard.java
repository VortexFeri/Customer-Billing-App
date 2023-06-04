package gui;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import products.UnitType;
import stock_logic.InventoryItem;
import stock_logic.InventoryList;

import javax.imageio.ImageIO;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;

import static application.utils.toTitleCase;
import static gui.MainView.productsView;
import static application.utils.log;

public class ProductCard {

    public static void showProducts(InventoryList inv) {
        inv.forEach(ProductCard::showProduct);
    }
    public static void showProduct(InventoryItem item) {
        VBox card = new VBox();
        card.getStyleClass().add("card");
        card.setPrefWidth(150);
        // allow to grow vertically
        card.setMaxHeight(Double.MAX_VALUE);

        ImageView img = new ImageView();
        img.getStyleClass().add("card-img");

        File f = new File("\\assets\\" + item.getName() + ".png");
        try {
            img.setImage(new Image(f.toString()));
        }
        catch (Exception e) {
            //get links from file
            String link = null;
            JSONParser parser = new JSONParser();
            try{
                Object obj = parser.parse(new FileReader("src\\assets\\image_links.json"));
                JSONObject jsonObject = (JSONObject)obj;
                link = jsonObject.get(item.getName()).toString();
            } catch(Exception e2) {
                log("Error: " + e2.getMessage());
            }
            Image downloadedImg = null;
            File file = new File("src\\assets\\" + item.getName() + ".png");
            boolean success = true;
            try {
                downloadedImg = new Image(link);
                BufferedImage bImage = new BufferedImage((int) downloadedImg.getWidth(), (int) downloadedImg.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = bImage.createGraphics();
                graphics.drawImage(SwingFXUtils.fromFXImage(downloadedImg, null), 0, 0, null);
                graphics.dispose();
                ImageIO.write(bImage, "png", file);
            }
            catch (Exception e1) {
                success = false;
                log("Error: " + e1.getMessage());
            }
            if (success)
                img.setImage(downloadedImg);
            else {
            f = new File("\\assets\\default.png");
            img.setImage(new Image(f.toString()));
            }
        }

        img.setFitWidth(125);
        img.setFitHeight(125);
//        img.setPreserveRatio(true);
        img.setSmooth(true);
        img.setCache(true);
        card.getChildren().add(img);

        Label nameLabel = new Label(toTitleCase(item.getName()));
        card.getChildren().add(nameLabel);
        nameLabel.getStyleClass().add("card-title");
        nameLabel.setWrapText(true);

        Label priceLabel = new Label("RON " + item.getPrice() + "/" + item.getUnit());
        priceLabel.getStyleClass().add("card-price");

        VBox stockBox = new VBox();

        Label stockLabel = new Label();
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox.setVgrow(stockBox, Priority.ALWAYS);
        stockBox.getChildren().addAll(spacer, priceLabel, stockLabel);
        //top margin
        AnchorPane.setTopAnchor(nameLabel, 10.0);

        AnchorPane.setBottomAnchor(stockBox, 0.0);
        stockBox.setMaxHeight(Double.MAX_VALUE);

        if (item.getStock() <= 0) {
            stockLabel.setText("• Out of stock!");
            stockLabel.setStyle("-fx-text-color: red");
            stockLabel.getStyleClass().add("card-stock-out");
        }
        else if (item.getStock() < 10) {
            if (item.getUnit() == UnitType.PCS)
                stockLabel.setText("• Limited stock! " + (int)item.getStock() + " left");
            else
                stockLabel.setText("• Limited stock! " + item.getStock() + "kg left");
            stockLabel.getStyleClass().add("card-stock-limited");
        }
        else {
            stockLabel.setText("• In stock");
            stockLabel.getStyleClass().add("card-stock");
        }
        card.getChildren().add(stockBox);
        if (item.getStock() <= 0)
            card.setDisable(true);

        card.setOnMouseClicked(e -> Actions.addToBill(item));
        productsView.getChildren().add(card);
    }
}
