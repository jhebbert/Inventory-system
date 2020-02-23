package Controller;

import Exceptions.InventoryOutOfBoundsException;
import static Model.Inventory.addPart;
import Model.Outsourced;
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
public class Add_PartController implements Initializable {

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
    
    private ToggleGroup toggleSource;
    @FXML
    private Label sourceLabel;
    @FXML
    private Label exceptionLbl;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        exceptionLbl.setText("");
        
        //  Toggle group to select whether the part is an inhouse or outsourced 
        //  part
        toggleSource = new ToggleGroup();
        this.inHouseRdBtn.setToggleGroup(toggleSource);
        this.outsourcedRdBtn.setToggleGroup(toggleSource);
        //  Set default toggle selection
        inHouseRdBtn.setSelected(true);
        sourceLabel.setText("Machine ID");
        compNameFld.setPromptText("Machine ID");
    }    

    //  
    @FXML
    private void onSavePartClick(ActionEvent event) throws Exception {
        
        System.out.println("Saving part");
        
        try {
            try{
        
        int ID = Integer.parseInt(partIDFld.getText());
        
        String name = partNameFld.getText();
        
        int stock = Integer.parseInt(partInvFld.getText());
        
        double price = Double.parseDouble(partPriceFld.getText());
        
        int max = Integer.parseInt(partMaxFld.getText());
        
        int min = Integer.parseInt(partMinFld.getText());
        
        String compName = compNameFld.getText();
        
        try {
            //  If stock is within the set bounds then create a new Part object
            //  and add it to allParts list
            if (stock >= min && stock <= max) {

                Outsourced newPart = new Outsourced(ID, name, price, stock, min, max, compName);

                addPart(newPart);

                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

                window.setScene(mainScreenScene);
                window.show();
            }
        else {
            throw(new InventoryOutOfBoundsException());
        }
        }
        catch(InventoryOutOfBoundsException e) {
                exceptionLbl.setText(e.getMessage());
                }
        
            }
            catch(NumberFormatException ex) {
                exceptionLbl.setText("Invalid data type entered.");
            }
        }
        catch(Exception e) {
         
            exceptionLbl.setText("Please fill in all text fields");
            
        }
  
    }

    //  Return to MainScreen
    @FXML
    private void onCancelPartClick(ActionEvent event) throws Exception {
        
        System.out.println("Canceled");
        
        //  Show alert and confirm cancel
        Alert cancelWarning = new Alert(Alert.AlertType.CONFIRMATION);
        cancelWarning.setTitle("WARNING!");
        cancelWarning.setHeaderText("You are about to cancel your entry!");
        cancelWarning.setContentText("Are you sure you want to procede? All unsaved data will be lost.");
        
        Optional<ButtonType> selection = cancelWarning.showAndWait();
        
        if (selection.get() == ButtonType.OK) {
        
        //  If confirmed, return to MainScreen
        Parent MainScreenParent = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
        Scene MainScreenScene = new Scene(MainScreenParent);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(MainScreenScene);
        window.show();
        
        }
        else {
            
        }
    }

    //  On toggle change, set sourceLable and compNameFld text
    @FXML
    private void partSourceToggle() {
        
        if (this.toggleSource.getSelectedToggle().equals(this.inHouseRdBtn)) {
            sourceLabel.setText("Machine ID");
            compNameFld.setPromptText("Machine ID");
        } else {
            sourceLabel.setText("Company Name");
            compNameFld.setPromptText("Company Name");
        }
        
    }
    
}
