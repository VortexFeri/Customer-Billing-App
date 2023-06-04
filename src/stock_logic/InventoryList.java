package stock_logic;

import products.Category;

import java.util.ArrayList;

public class InventoryList extends ArrayList<InventoryItem> {
    public InventoryList() {
        super();
    }
    public InventoryList(ArrayList<InventoryItem> inv) {
        super(inv);
    }
    public InventoryList(InventoryList inv) {
        super(inv);
    }
    public InventoryList clone() {
        return (InventoryList) super.clone();
    }
    public InventoryList searchByName(String name) {
        InventoryList inv = new InventoryList();
        for (InventoryItem i : this) {
            if (i.getName().toLowerCase().contains(name.toLowerCase())) {
                inv.add(i);
            }
        }
        return inv;
    }
    public InventoryList filterByCategory(Category category) {
        InventoryList inv = new InventoryList();
        for (InventoryItem i : this) {
            if (i.getCategory() == category) {
                inv.add(i);
            }
        }
        return inv;
    }

    public InventoryItem getItem(int id) {
        for (InventoryItem i : this) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }
}
