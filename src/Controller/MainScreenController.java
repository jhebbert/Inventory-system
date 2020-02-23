/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;


import Model.InHouse;
import Model.Inventory;
import static Model.Inventory.deletePart;
import static Model.Inventory.deleteProduct;
import static Model.Inventory.getAllParts;
import static Model.Inventory.getAllProducts;
import static Model.Inventory.lookupPart;
import static Model.Inventory.lookupProduct;
import Model.Outsourced;
import Model.Part;
import Model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author james
 */
public class MainScreenController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    public TableView partTable;
    @FXML
    private TableColumn<Part, Integer> partIDCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInventoryLvlCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    @FXML
    private Button partSearchBtn;
    @FXML
    private TextField partSearchFld;
    @FXML
    private Button partAddBtn;
    @FXML
    private Button partModifyBtn;
    @FXML
    private Button partDeleteBtn;
    @FXML
    private TableView productTable;
    @FXML
    private TableColumn<Product, Integer> prodIDCol;
    @FXML
    private TableColumn<Product, String> prodNameCol;
    @FXML
    private TableColumn<Product, Integer> prodInventoryCol;
    @FXML
    private TableColumn<Product, Double> prodPriceCol;
    @FXML
    private TextField prodSearchFld;
    @FXML
    private Button prodSearchBtn;
    @FXML
    private Button prodAddBtn;
    @FXML
    private Button prodModifyBtn;
    @FXML
    private Button prodDeleteBtn;
    @FXML
    private Button exitBtn;
    
    private Part selectedPart;
    private Product selectedProduct;
    
    InHouse sample1 = new InHouse(123, "One", 1.0, 5, 10, 1, 321);
    InHouse sample2 = new InHouse(456, "two", 2.0, 2, 115, 2, 654);
    Outsourced sample3 = new Outsourced(789, "three", 3.0, 12, 20, 2, "comp");
    
    
    ObservableList<Part> tableSource;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //  Set partTable to show all parts
        partTable.setItems(Inventory.getAllParts());
       
        //  Set cell values
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        //  Set productTable to show all products
        productTable.setItems(getAllProducts());
        
        //  Set cell values
        prodIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        
    }    

    //  Search parts table by ID or by part name
    @FXML
    private void onPartSearchClick(ActionEvent event) throws Exception {
        System.out.println("Searching for Part");
        
        partTable.setItems(getAllParts());
        
        String searchText = partSearchFld.getText();
        boolean isNumeric = true;
        int id = 0;
        
        try {
        id = Integer.parseInt(searchText);
        }
        catch (NumberFormatException e) {
            isNumeric = false;
        }
        
        //  input in textfield in an integer then search by ID
        if (isNumeric) {
            
        partTable.getSelectionModel().select(lookupPart(id));
        selectedPart = (Part) partTable.getSelectionModel().getSelectedItem();
        }
        //  If input is not numeric then search by part name
        else {
            
            if (!(Inventory.getFilteredParts().isEmpty())) {
                
                Inventory.getFilteredParts().clear();
            }
             
            partTable.setItems(lookupPart(searchText));
        
        }
        
    }

    //  Go to add part screen
    @FXML
    private void onPartAddClick(ActionEvent event) throws IOException {
        System.out.println("Going to add part screen");
        
        
        Parent addPartParent = FXMLLoader.load(getClass().getResource("/View/Add_Part.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(addPartScene);
        window.show();
        
    }

    //  Go to modify part screen
    @FXML
    private void onPartModifyClick(ActionEvent event) throws Exception {
        System.out.println("Going to modify part screen");
        
        int selectedIndex = partTable.getSelectionModel().getSelectedIndex();
        Part selectedPart = (Part) partTable.getSelectionModel().getSelectedItem();
             
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/ModifyPart.fxml"));
        loader.load();
        
        ModifyPartController MPController = loader.getController();
        
        if (selectedPart == null) {
            System.out.println("Please select a part from the table.");
        }
        //  If there is a part selected then send selected part to modify part 
        //  controller
        else {    
        MPController.sendPart(selectedPart);
        MPController.sendIndex(selectedIndex);
        
        //  Go to modify part screen
        Parent addPartScene = loader.getRoot();
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(new Scene(addPartScene));
        window.show();
        }
        
    }
    
    
    //  Remove part from allParts
    @FXML
    private void onPartDeleteClick(ActionEvent event) {
        System.out.println("Deleting Part");
        
        //  Show alert and confirm delete
        Alert deleteWarning = new Alert(AlertType.CONFIRMATION);
        deleteWarning.setTitle("WARNING!");
        deleteWarning.setHeaderText("You are about to delete a Part!");
        deleteWarning.setContentText("Are you sure you want to procede?");
        
        Optional<ButtonType> selection = deleteWarning.showAndWait();
        
        //  If confirmed then delete selected part
        if (selection.get() == ButtonType.OK) {
        
        selectedPart = (Part)partTable.getSelectionModel().getSelectedItem();
        deletePart(selectedPart);
        
        }
        else {
            
        }
        
    }

    //  Seach Products by ID or by name
    @FXML
    private void onProdSearchClick(ActionEvent event) {
        System.out.println("Searching product");
        
        productTable.setItems(getAllProducts());
        
        String searchText = prodSearchFld.getText();
        boolean isNumeric = true;
        int id = 0;
        
        try {
        id = Integer.parseInt(searchText);
        }
        catch (NumberFormatException e) {
            isNumeric = false;
        }
        
        //  If input is numeric search by ID
        if (isNumeric) {
            
        productTable.getSelectionModel().select(lookupProduct(id));
        selectedProduct = (Product) productTable.getSelectionModel().getSelectedItem();
        }
        
        //  If input is not numeric then search by name
        else {
            
            if (!(Inventory.getFilteredProducts().isEmpty())) {
                
                Inventory.getFilteredProducts().clear();
            }
                             
            productTable.setItems(lookupProduct(searchText));
                               
        }
    }

    //  Go to add Product screen
    @FXML
    private void onProdAddClick(ActionEvent event) throws Exception {
        System.out.println("Going to add product screen");
        
        Parent addProdParent = FXMLLoader.load(getClass().getResource("/View/AddProduct.fxml"));
        Scene addProdScene = new Scene(addProdParent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(addProdScene);
        window.show();
        
    }

    //  Go to modify product screen
    @FXML
    private void onProdModifyClick(ActionEvent event) throws Exception {
        
        System.out.println("Going to modify product screen");
        
        int selectedIndex = productTable.getSelectionModel().getSelectedIndex();
        Product selectedProduct = (Product) productTable.getSelectionModel().getSelectedItem();
             
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/ModifyProduct.fxml"));
        loader.load();
        
        ModifyProductController MPController = loader.getController();
        
        if (selectedProduct == null) {
            System.out.println("Please select a part from the table.");
        }
        //  If there is a select part then send it to the modify parts controller
        else {    
        MPController.sendProduct(selectedProduct);
        MPController.sendIndex(selectedIndex);
        
        //  Go to modify part screen
        Parent addPartScene = loader.getRoot();
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(addPartScene));
        window.show();
        }     
    }

    //  Delete product from allProducts
    @FXML
    private void onProdDeleteClick(ActionEvent event) {
        System.out.println("Deleting product");
        
        //  Show alert and confirm delete
        Alert deleteWarning = new Alert(AlertType.CONFIRMATION);
        deleteWarning.setTitle("WARNING!");
        deleteWarning.setHeaderText("You are about to delete a product!");
        deleteWarning.setContentText("Are you sure you want to procede?");
        
        Optional<ButtonType> selection = deleteWarning.showAndWait();
        
        //  If confirmed then delete selected product
        if (selection.get() == ButtonType.OK) {
        
        selectedProduct = (Product)productTable.getSelectionModel().getSelectedItem();
        deleteProduct(selectedProduct);
        
        }
        else {
            
        }
        
    }

    //  Close program
    @FXML
    private void onExitClick(ActionEvent event) {
        System.out.println("Exiting");
        
        Stage window = (Stage)exitBtn.getScene().getWindow();
        window.close();
    }
    
}
