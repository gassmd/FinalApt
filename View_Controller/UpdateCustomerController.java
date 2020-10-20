package View_Controller;

import DAO.AddressDB;
import DAO.CustomerDB;
import Model.Address;
import Model.Customer;
import Util.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

import static View_Controller.CustomerController.updatingCustomer;


public class UpdateCustomerController implements Initializable {

    @FXML private TextField postalField;
    @FXML private Button updateButton;
    @FXML private TextField address2Field;
    @FXML private ChoiceBox cityChoiceBox;
    @FXML private TextField customerNameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;

    ObservableList<String> cityList = FXCollections.observableArrayList("Phoenix, Arizona", "New York, New York", "London, England", "Montreal, Canada");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addressField.setText(updatingCustomer.getAddressId());
        customerNameField.setText(updatingCustomer.getCustomerName());
        phoneField.setText(updatingCustomer.getPhone());
        cityChoiceBox.setItems(cityList);
        cityChoiceBox.setValue("Phoenix, Arizona");
    }
    public void backBtnUpdateCust(ActionEvent event) throws IOException {
        ScreenController customer = new ScreenController();
        customer.showCustomerScreen(event);
    }

    public void userLogout(ActionEvent event) throws IOException {
        ScreenController userLogout = new ScreenController();
        userLogout.userLogout(event);
    }

    public void updateButton(ActionEvent event) throws IOException {
        if(allFieldsValid()) {
            String newName = customerNameField.getText();
            String newAddress = addressField.getText();
            String newAddress2 = address2Field.getText();
            int newCityId = getCityIdByName();
            String newPhone = phoneField.getText();
            String newPostal = postalField.getText();
            int addressId = AddressDB.getAddressIdByCustomer(updatingCustomer.getCustomerId());


            Address updatedAddress = new Address(addressId, newAddress, newAddress2, newCityId, newPostal, newPhone);
            AddressDB.updateAddress(updatedAddress);
            CustomerDB.updateCustomerName(newName, updatingCustomer.getCustomerId());
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Customer Updated", ButtonType.OK);
            confirm.show();

            ScreenController showCustomerScreen = new ScreenController();
            showCustomerScreen.showCustomerScreen(event);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error Updating Customer");
            alert.setHeaderText("Invalid Fields");
            alert.setContentText("Please ensure all fields are filled in");
            alert.showAndWait();
        }
    }

    public boolean allFieldsValid(){
        if(customerNameField.getText().isBlank() || addressField.getText().isBlank() || address2Field.getText().isBlank() || phoneField.getText().isBlank() || postalField.getText().isBlank()){
            return false;
        }
        return true;
    }

    public int getCityIdByName(){
        String newCity = (String) cityChoiceBox.getValue();

        if(newCity.equals("Phoenix, Arizona")){
            return 602;
        }
        if (newCity.equals("New York, New York")){
            return 718;
        }
        if (newCity.equals("London, England")){
            return 200;
        }
        if (newCity.equals("Montreal, Canada")){
            return 514;
        }
        return 0;
    }
}
