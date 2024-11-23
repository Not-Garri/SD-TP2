package Comunication.Server;

import java.io.Serializable;
import java.util.ArrayList;

public class Product {

    private final String name;
    private float price;
    private String store;
    private String username;
    private String date;

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public String getStore() {
        return store;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Product(String name, float price, String store, String username, String date) {
        this.store = store;
        this.price = price;
        this.name = name;
        this.username = username;
        this.date = date;
    }

    public boolean compare(Product p) {
        return name.equals(p.name) && store.equals(p.store);
    }

    public boolean compare(String productName, String productStore) {
        return name.equals(productName) && store.equals(productStore);
    }

    public String toString(){
        return String.format("Nome: %s - Preco: %.2f - Loja: %s - Adicionado por: %s - Data: %s", name, price, store, username, date);
    }

    public String toFileString() {
        return (name + ";" + price + ";" + store + ";" + username+ ";" + date);
    }

    public static ArrayList<Product> listFromFile(String[] list) {

        ArrayList<Product> productList = new ArrayList<>();

        for (String s : list) {
            String[] parts = s.split(";");
            productList.add(new Product(parts[0], Float.parseFloat(parts[1]), parts[2], parts[3], parts[4]));
        }

        return productList;
    }

    public static Product[] arrayFromFile(String[] list) {

        Product[] productList = new Product[list.length];

        for (int i = 0; i < list.length; i++) {
            String[] parts = list[i].split(";");
            productList[i] = new Product(parts[0], Float.parseFloat(parts[1]), parts[2], parts[3], parts[4]);
        }

        return productList;
    }
}
