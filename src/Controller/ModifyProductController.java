/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Exceptions.InventoryOutOfBoundsException;
import Model.Inventory;
import static Model.Inventory.getAllParts;
import static Model.Inventory.lookupPart;
import static Model.Inventory.updateProduct;
import Model.Part;
import Model.Product;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author james
 */
public class ModifyProductController implements Initializable {

    @FXML
    private TextField prodIDFld;
    @FXML
    private TextField prodNameFld;
    @FXML
    private TextField prodInvFld;
    @FXML
    private TextField prodPriceFld;
    @FXML
    private TextField prodMaxFld;
    @FXML
    private TextField prodMinFld;
    @FXML
    private TableView<Part> addAssocPartTable;
    @FXML
    private TableColumn<Part, Integer> addPartIDCol;
    @FXML
    private TableColumn<Part, String> addPartNameCol;
    @FXML
    private TableColumn<Part, Integer> addInvLvlCol;
    @FXML
    private TableColumn<Part, Double> addPriceCol;
    @FXML
    private TableColumn<Part, Integer> deletePartIDCol;
    @FXML
    private TableColumn<Part, String> deletePartNameCol;
    @FXML
    private TableColumn<Part, Integer> deleteInvLvlCol;
    @FXML
    private TableColumn<Part, Double> deletePriceCol;
    @FXML
    private Button addAssocPartBtn;
    @FXML
    private Button deleteAssocPartBtn;
    @FXML
    private Button saveProdBtn;
    @FXML
    private Button cancelProdBtn;
    @FXML
    private Button searchAssocPartToAddBtn;
    @FXML
    private TextField searchAssocPartToAddFld;
    @FXML
    private TableView<Part> assocPartTable;
    
    private Product selectedProduct;
    
    private int index;
    
    //Holds associatedParts until saved, so that the changes can be discarded if
    //canceled.
    private ObservableList<Part> tempAssocPartsList = FXCollections.observableArrayList();
    
    private Part selectedPart;
    
    Product temp = new Product();
    @FXML
    private Label exceptionLbl;
    
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        exceptionLbl.setText("");
        //  Show all parts in table
        addAssocPartTable.setItems(Inventory.getAllParts());
        //  Set cell values
        addPartIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        addPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        //  Set cell values
        deletePartIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        deletePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        deleteInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        deletePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }  
    
    //  Used to send selected product from MainScreenController to ModifyProductController
    public void sendProduct(Product selected) {
        
        this.selectedProduct = selected;
        //  Auto populate text fields with data from selected product
        prodIDFld.setText(String.valueOf(selected.getID()));
        prodNameFld.setText(selected.getName());
        prodInvFld.setText(String.valueOf(selected.getStock()));
        prodPriceFld.setText(String.valueOf(selected.getPrice()));
        prodMaxFld.setText(String.valueOf(selected.getMax()));
        prodMinFld.setText(String.valueOf(selected.getMin()));
        
        selected.getAllAssociatedParts().forEach((part) -> {
            tempAssocPartsList.add(part);
        });
        assocPartTable.setItems(selected.getAllAssociatedParts());
        
    }
     
    public void sendIndex(int index) {
        
        this.index = index;
        
    }

    //  Add part to associated parts
    @FXML
    private void onAddAssocPartClick(ActionEvent event) {
        
        selectedPart = addAssocPartTable.getSelectionModel().getSelectedItem();
        selectedProduct.addAssociatedPart(selectedPart);
    }

    //  remove part from associated parts
    @FXML
    private void onDeleteAssocPartClick(ActionEvent event) {
        //  Show alert and confirm delete
        Alert deleteWarning = new Alert(Alert.AlertType.CONFIRMATION);
        deleteWarning.setTitle("WARNING!");
        deleteWarning.setHeaderText("You are about to delete a Part!");
        deleteWarning.setContentText("Are you sure you want to procede?");
        
        Optional<ButtonType> selection = deleteWarning.showAndWait();
        
        //  If confirmed then remove part from associated parts
        if (selection.get() == ButtonType.OK) {
        
        Part selectedPart = (Part)assocPartTable.getSelectionModel().getSelectedItem();
        selectedProduct.deleteAssociatedPart(selectedPart);
        
        }
        else {
            
        }
    }

    //  Save changes and return to Main Screen
    @FXML
    private void onSaveProdClick(ActionEvent event) throws Exception {
        
        System.out.println("Saving product modifications");
        
        try {
            
             
                //  Get values from text fields
                int id = Integer.parseInt(prodIDFld.getText());
                String name = prodNameFld.getText();
                int stock = Integer.parseInt(prodInvFld.getText());
                double price = Double.parseDouble(prodPriceFld.getText());
                int max = Integer.parseInt(prodMaxFld.getText());
                int min = Integer.parseInt(prodMinFld.getText());
                tempAssocPartsList = selectedProduct.getAllAssociatedParts();
        
        
            //  If stock is within bounds
            if (stock >= min && stock <= max) {
        
                Product selectedProd = new Product(id, name, price, stock, min, max);
                //  Add associated part to product
                selectedProduct.getAllAssociatedParts().forEach((assocPart) -> {
                    selectedProd.addAssociatedPart(assocPart);
                    });
                //  Set the new values for the product
                updateProduct(index, selectedProd);

                //  Go to main screen
                Parent MainScreenParent = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
                Scene MainScreenScene = new Scene(MainScreenParent);
                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

                window.setScene(MainScreenScene);
                window.show();
            }
        
            else {
                throw(new InventoryOutOfBoundsException());
            }
        }
        catch(InventoryOutOfBoundsException e) {
            exceptionLbl.setText(e.getMessage());
                }
        
             
             catch(NumberFormatException ex) {
                 exceptionLbl.setText("Invalid data type entered.");
             }
        catch(Exception e) {
            
            exceptionLbl.setText("Please fill in all text fields");
            
        }
        
    }

    //  Return to Main Screen
    @FXML
    private void onCancelProdClick(ActionEvent event) throws Exception {
        
        System.out.println("Canceling product modifications");
        
        //  Show alert and confirm cancel
        Alert cancelWarning = new Alert(Alert.AlertType.CONFIRMATION);
        cancelWarning.setTitle("WARNING!");
        cancelWarning.setHeaderText("You are about to cancel your modifications");
        cancelWarning.setContentText("Are you sure you want to procede? All unsaved modifications will be lost.");
        
        Optional<ButtonType> selection = cancelWarning.showAndWait();
        
        if (selection.get() == ButtonType.OK) {
            
            selectedProduct.getAllAssociatedParts().clear();
        
         //  Set associatedParts back to what it was before changes
         for (Part tempPart : tempAssocPartsList) {
            
            selectedProduct.addAssociatedPart(tempPart);
        }
        
        //  Go to Main Screen
        Parent MainScreenParent = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
        Scene MainScreenScene = new Scene(MainScreenParent);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(MainScreenScene);
        window.show();
        
        }
        else {
            
        }
        
    }

    //  Search by ID or name
    @FXML
    private void onSearchPartToAddClick(ActionEvent event) {
        
        addAssocPartTable.setItems(getAllParts());
        
        String searchText = searchAssocPartToAddFld.getText();
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

                addAssocPartTable.getSelectionModel().select(lookupPart(id));
                Part selectedPartToAdd = (Part) addAssocPartTable.getSelectionModel().getSelectedItem();
                }
            //  If input is not numeric search by name
            else {

                if (!(Inventory.getFilteredParts().isEmpty())) {

                    Inventory.getFilteredParts().clear();
                }
             
                addAssocPartTable.setItems(lookupPart(searchText));
                
        }
        
    }
    
}
