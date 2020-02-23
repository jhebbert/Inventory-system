/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Part;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author james
 */
public class Product {
    
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int ID;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    
    public Product() {
        
    }

    public Product(int ID, String name, double price, int stock, int min, int max) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
    
    public void addAssociatedPart(Part part) {
        
        associatedParts.add(part);
        
    }
    
    public void deleteAssociatedPart(Part part) {
        
        associatedParts.remove(part);
        
    }
    
    
}
