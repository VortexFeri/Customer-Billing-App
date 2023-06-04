package gui;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class TableModel {
    public static class TableEntry {
        private String name;
        private final SimpleFloatProperty price;
        private final SimpleIntegerProperty quantity;
        private final SimpleFloatProperty total;
        private final int id;

        TableEntry(int id, String name, Float price, int quantity) {
            this.name = name;
            this.price = new SimpleFloatProperty(price);
            this.quantity = new SimpleIntegerProperty(quantity);
            this.total = new SimpleFloatProperty(price * quantity);
            this.id = id;
        }
        public int getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public void setName(String Name) {
            this.name = Name;
        }

        public Float getPrice() {
            return price.get();
        }
        public void setPrice(float newPrice) {
            price.set(newPrice);
        }

        public int getQuantity() {
            return quantity.get();
        }
        public void setQuantity(int newQuantity) {
            quantity.set(newQuantity);
        }
        public float getTotal() {
            return total.get();
        }
        public void computeTotal() {
            float newTotal = price.get() * quantity.get();
            //format to 2 decimals
            newTotal = (float) (Math.floor(newTotal * 100.0) / 100.0);
            total.set(newTotal);
        }

    }
}
