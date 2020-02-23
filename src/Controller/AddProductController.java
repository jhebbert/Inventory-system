
package Controller;

import Exceptions.InventoryOutOfBoundsException;
import Model.Inventory;
import static Model.Inventory.addProduct;
import static Model.Inventory.getAllParts;
import static Model.Inventory.lookupPart;
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
public class AddProductController implements Initializable {

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
    private Button searchPartToAddBtn;
    @FXML
    private TextField searchPartToAddFld;
    @FXML
    private TableView<Part> searchPartToAddTable;
    @FXML
    private TableView<Part> assocPartTable;
    
    private Part selectedPartToAdd;
    
    private Part selectedPartToDelete;
    
    private ObservableList<Part> tempAssocPartList = FXCollections.observableArrayList();
    @FXML
    private Label exceptionLbl;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        exceptionLbl.setText("");
        
        //  Set table to show all parts
        searchPartToAddTable.setItems(Inventory.getAllParts());
        
        //  Shows parts associated with the selected product
        assocPartTable.setItems(tempAssocPartList);
       
        //  Assign values to cells 
        addPartIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        addPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        deletePartIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        deletePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        deleteInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        deletePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }    

    //  Adds a part to the associated parts for the product
    @FXML
    private void onAddAssocPartClick(ActionEvent event) {
        
        selectedPartToAdd = searchPartToAddTable.getSelectionModel().getSelectedItem();
        
        tempAssocPartList.add(selectedPartToAdd);
        
    }

    //  Remove a part from the associated parts list
    @FXML
    private void onDeleteAssocPartClick(ActionEvent event) {
        
        //  Show alert and confirm removal
        Alert deleteWarning = new Alert(Alert.AlertType.CONFIRMATION);
        deleteWarning.setTitle("WARNING!");
        deleteWarning.setHeaderText("You are about to delete a Part!");
        deleteWarning.setContentText("Are you sure you want to procede?");
        
        Optional<ButtonType> selection = deleteWarning.showAndWait();
        
        if (selection.get() == ButtonType.OK) {

        
        selectedPartToDelete = assocPartTable.getSelectionModel().getSelectedItem();
        
        tempAssocPartList.remove(selectedPartToDelete);
        
        }
        else {
            
        }
        
    }

    //  
    @FXML
    private void onSaveProdClick(ActionEvent event) throws Exception {
        
        System.out.println("Saving Product");
        
        try {
            try {
        int ID = Integer.parseInt(prodIDFld.getText());
        String name = prodNameFld.getText();
        int stock = Integer.parseInt(prodInvFld.getText());
        double price = Double.parseDouble(prodPriceFld.getText());
        int min = Integer.parseInt(prodMinFld.getText());
        int max = Integer.parseInt(prodMaxFld.getText());
        
        Product newProduct = new Product(ID, name, price, stock, min, max);
        
        try {
            if (stock >= min && stock <= max) {
                
                //Takes parts from tempAssocPartList and moves them to associatedParts when
                //when the Product is saved.
                for (Part newPart : tempAssocPartList) {
                    newProduct.addAssociatedPart(newPart);
                }

                addProduct(newProduct);
        
        
      
        
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
        //  Custom exception to ensure inventory levels are within set bounds
        catch(InventoryOutOfBoundsException e) {
            exceptionLbl.setText(e.getMessage());
        }
            }
        
        catch(NumberFormatException ex) {
            exceptionLbl.setText("Invalid data type entered");
                    }
        
        }
        catch(Exception e) {
            exceptionLbl.setText("Please fill in all text fields");
        }
        
    
    }

    //  Return to Main Screen
    @FXML
    private void onCancelProdClick(ActionEvent event) throws Exception {
        
        //System.out.println("Canceled Product");
        
        //  Show alert and confirm cancel
        Alert cancelWarning = new Alert(Alert.AlertType.CONFIRMATION);
        cancelWarning.setTitle("WARNING!");
        cancelWarning.setHeaderText("You are about to cancel your entry");
        cancelWarning.setContentText("Are you sure you want to procede? All unsaved data will be lost.");
        
        Optional<ButtonType> selection = cancelWarning.showAndWait();
        
        if (selection.get() == ButtonType.OK) {
        
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
    
    //  Search for part by id
    @FXML
    private void onSearchPartToAddClick(ActionEvent event) {
        
        System.out.println("Searching for Part");
        
        searchPartToAddTable.setItems(getAllParts());
        
        String searchText = searchPartToAddFld.getText();
        boolean isNumeric = true;
        int id = 0;
        
        try {
        id = Integer.parseInt(searchText);
        }
        catch (NumberFormatException e) {
            isNumeric = false;
        }
        //  If the textfield contains an integer then select for the part with 
        //  a matching id
        if (isNumeric) {
            
        searchPartToAddTable.getSelectionModel().select(lookupPart(id));
        selectedPartToAdd = (Part) searchPartToAddTable.getSelectionModel().getSelectedItem();
        }
        //  Else search by part name
        else {
            
            if (!(Inventory.getFilteredParts().isEmpty())) {
                
                Inventory.getFilteredParts().clear();
            }
             
            searchPartToAddTable.setItems(lookupPart(searchText));
        
        }
        
    }
    
}
