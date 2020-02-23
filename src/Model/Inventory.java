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
public class Inventory {
    private static ObservableList<Part> allParts =  FXCollections.observableArrayList();
    private static ObservableList<Part> filteredParts =  FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static ObservableList<Product> filteredProducts = FXCollections.observableArrayList();

    
    
    public static void addPart(Part newPart) {
        
        allParts.add(newPart);
        
    }
    
    
    public static void deletePart(Part selectedPart) {
        
        allParts.remove(selectedPart);
        
    }

    
    //  Search part by ID
    public static Part lookupPart(int ID) {
        
        //Part searchedPart;
        
        for (Part searched : Inventory.getAllParts()) {
            if (searched.getID() == ID) {
                return searched;
            }
           
    }
        return null;
    }
   
    //  Search by partName
    public static ObservableList<Part> lookupPart(String partName) {
        
        //  Add parts that contain the input
        for (Part searched : allParts) {
            if (searched.getName().toLowerCase().contains(partName.toLowerCase())) {
                filteredParts.add(searched);
            }
        }
        //  If any part matches the input then return all matching parts
        if (!(filteredParts.isEmpty())){
            
        return filteredParts;
        }
        // allParts returned if no parts match the input
        return allParts;
    }

    
    public static void updatePart(int index, Part selectedPart) {
            
        allParts.set(index, selectedPart);
                
    }
        
    public static ObservableList<Part> getAllParts() {
        
        return allParts;
        
    }
   
    public static void addProduct(Product newProduct) {
        
        allProducts.add(newProduct);
        
    }
    
    public static void deleteProduct(Product selectedProduct) {
        
        allProducts.remove(selectedProduct);
        
    }
    
    public static Product lookupProduct(int ID) {
        
            for (Product searched : Inventory.getAllProducts()) {
            if (searched.getID() == ID) {
                return searched;
            }
           
    }
        return null;

    }
    
    public static ObservableList<Product> lookupProduct(String productName) {
        
        for (Product searched : allProducts) {
            if (searched.getName().toLowerCase().contains(productName.toLowerCase())) {
                filteredProducts.add(searched);
            }
        }
        if (!(filteredProducts.isEmpty())){
            
        return filteredProducts;
        }
        
        return allProducts;
    }

    public static void updateProduct(int index, Product selectedProduct) {
        
        allProducts.set(index, selectedProduct);
        
    }
    
    public static ObservableList<Product> getAllProducts() {
        
        return allProducts;
        
    }
    
    public static ObservableList<Part> getFilteredParts() {
        
        return filteredParts;
    }
    
    public static ObservableList<Product> getFilteredProducts() {
        return filteredProducts;
    }
    
    
}