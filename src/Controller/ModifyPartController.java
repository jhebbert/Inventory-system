package Controller;

import Exceptions.InventoryOutOfBoundsException;
import Model.InHouse;
import static Model.Inventory.updatePart;
import Model.Outsourced;
import Model.Part;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author james
 */
public class ModifyPartController implements Initializable {

    @FXML
    private RadioButton inHouseRdBtn;
    @FXML
    private RadioButton outsourcedRdBtn;
    @FXML
    private TextField partIDFld;
    @FXML
    private TextField partNameFld;
    @FXML
    private TextField partInvFld;
    @FXML
    private TextField partPriceFld;
    @FXML
    private TextField partMaxFld;
    @FXML
    private TextField compNameFld;
    @FXML
    private TextField partMinFld;
    @FXML
    private Button savePartBtn;
    @FXML
    private Button cancelPartBtn;
    
    private Part selectedPart;
    
    private int index;
    @FXML
    private Label sourceLabel;
    
    private ToggleGroup toggleSource;
    @FXML
    private Label exceptionLbl;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        exceptionLbl.setText("");
        
        //  Toggle group for inhouse or outsourced part
        toggleSource = new ToggleGroup();
        this.outsourcedRdBtn.setToggleGroup(toggleSource);
        this.inHouseRdBtn.setToggleGroup(toggleSource);
        //  Set default selection
        inHouseRdBtn.setSelected(true);
        sourceLabel.setText("Machine ID");
        compNameFld.setPromptText("Machine ID");
        
    } 
    
    public void sendIndex(int index) {
        
        this.index = index;
        
    }
    
    public void sendPart(Part selected) {
        
        //  If selected part is an outsourced part
        if (selected instanceof Outsourced) {
        
        selectedPart = selected;
        //  Set toggle to outsourced
        outsourcedRdBtn.setSelected(true);
        //  Auto populate text fields with data from selected part
        partIDFld.setText(String.valueOf(selected.getID()));
        partNameFld.setText(selected.getName());
        partInvFld.setText(String.valueOf(selected.getStock()));
        partPriceFld.setText(String.valueOf(selected.getPrice()));
        partMaxFld.setText(String.valueOf(selected.getMax()));
        partMinFld.setText(String.valueOf(selected.getMin()));
        compNameFld.setText(((Outsourced) selected).getCompanyName());
        
    }
        else {
            
        selectedPart = selected;
        //  Auto populate text fields with data from selected part
        partIDFld.setText(String.valueOf(selected.getID()));
        partNameFld.setText(selected.getName());
        partInvFld.setText(String.valueOf(selected.getStock()));
        partPriceFld.setText(String.valueOf(selected.getPrice()));
        partMaxFld.setText(String.valueOf(selected.getMax()));
        partMinFld.setText(String.valueOf(selected.getMin()));
        compNameFld.setText(String.valueOf(((InHouse) selected).getMachineID()));
        }
    
    
    }

    //  Add part to all parts and return to main screen
    @FXML
    private void onSavePartClick(ActionEvent event) throws Exception {
        
        System.out.println("Saving part");
        
        try {
            //  Get values from text fields
            int id = Integer.parseInt(partIDFld.getText());
            String name = partNameFld.getText(); 
            int stock = Integer.parseInt(partInvFld.getText());
            double price = Double.parseDouble(partPriceFld.getText());
            int max = Integer.parseInt(partMaxFld.getText());
            int min = Integer.parseInt(partMinFld.getText());
        
            //  If stock is within set limits
            if (stock >= min && stock <= max) {
               
                if (selectedPart instanceof Outsourced) {
            
                String compName = compNameFld.getText();

                Outsourced selectedOutsourced = new Outsourced(id, name, price, stock, min, max, compName);

                updatePart(index, selectedOutsourced);
                }
                else {
                    int machineID = Integer.parseInt(compNameFld.getText());

                    InHouse selectedInHouse = new InHouse(id, name, price, stock, max, min, machineID);

                    updatePart(index, selectedInHouse);
            }
                //  Go to main screen       
                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

                window.setScene(mainScreenScene);
                window.show();
            }
            //  If inventory out of bounds throw exception
            else {
                throw(new InventoryOutOfBoundsException());
            }
        }
            
            catch(NumberFormatException ex) {
                exceptionLbl.setText("Invalid data type entered.");
            }
            
            catch(InventoryOutOfBoundsException e) {
                exceptionLbl.setText(e.getMessage());
            }
    
            catch(Exception e) {
            
            exceptionLbl.setText("Please fill in all text fields");
            
        }
        
    }

    //  Return to Main Screen
    @FXML
    private void onCancelPartClick(ActionEvent event) throws Exception {
        
        System.out.println("Canceled");
        
        //  Show alert and confirm cancel
        Alert cancelWarning = new Alert(Alert.AlertType.CONFIRMATION);
        cancelWarning.setTitle("WARNING!");
        cancelWarning.setHeaderText("You are about to cancel your modifications");
        cancelWarning.setContentText("Are you sure you want to procede? All unsaved modifications will be lost.");
        
        Optional<ButtonType> selection = cancelWarning.showAndWait();
        
        //  If confirmed then go back to Main Screen
        if (selection.get() == ButtonType.OK) {
        
        
        Parent MainScreenParent = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
        Scene MainScreenScene = new Scene(MainScreenParent);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(MainScreenScene);
        window.show();
        
        }
        else {
            
        }
        
    }

    //  Update sourceLabel and compNameFld on toggle change.
    @FXML
    private void toggleSource(ActionEvent event) {
        
        if (this.toggleSource.getSelectedToggle().equals(this.inHouseRdBtn)) {
            
            sourceLabel.setText("Machine ID");
            compNameFld.setPromptText("Machine ID");
            
        } 
        else {
            
            sourceLabel.setText("Company Name");
            compNameFld.setPromptText("Company Name");
            
        }
        
    }
    
   
}
