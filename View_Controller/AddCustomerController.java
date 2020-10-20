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
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AddCustomerController {

    public TextField addr2FieldAddCust;
    public TextField postalFieldAddCust;
    ObservableList<String> cityList = FXCollections.observableArrayList("Phoenix, Arizona", "New York, New York", "London, England", "Montreal, Canada");
    public TextField custNameFieldAddCust;
    public TextField addrFieldAddCust;
    public TextField phoneFieldAddCust;
    public Button addBtnAddCust;
    public ArrayList<TextField> textFields;

    @FXML
    private ChoiceBox cityChoiceBox;

    @FXML
    private void initialize(){
        cityChoiceBox.setItems(cityList);
        cityChoiceBox.setValue("Phoenix, Arizona");
    }


    public void backBtnAddCust(ActionEvent event) throws IOException {
        ScreenController customer = new ScreenController();
        customer.showCustomerScreen(event);
    }

    public void userLogout(ActionEvent event) throws IOException {
        ScreenController userLogout = new ScreenController();
        userLogout.userLogout(event);
    }

    public void addBtnAddCust(ActionEvent event) throws IOException {
        if (allFieldsValid()) {
            addAddress();
            addCustomer();
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Customer Added", ButtonType.OK);
            confirm.show();
            ScreenController showCustomerScreen = new ScreenController();
            showCustomerScreen.showCustomerScreen(event);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error Adding Customer");
            alert.setHeaderText("Invalid Fields");
            alert.setContentText("Please ensure all fields are filled in");
            alert.showAndWait();
        }
    }

    public boolean allFieldsValid(){
        if (custNameFieldAddCust.getText().isBlank() || addrFieldAddCust.getText().isBlank() || addr2FieldAddCust.getText().isBlank() || postalFieldAddCust.getText().isBlank() || phoneFieldAddCust.getText().isBlank()){
                return false;

        }
        return true;
    }

    public void addAddress(){
        try {
            int newId = DBConnector.randomId();
            String address = addrFieldAddCust.getText();
            String address2 = addr2FieldAddCust.getText();
            String postal = postalFieldAddCust.getText();
            String phone = phoneFieldAddCust.getText();
            Address newAddress = new Address(newId, address, address2, getCityId(), postal, phone);
            AddressDB.addAddress(newAddress);
        }catch(Exception e){
            e.printStackTrace();
        }
    }





    public int getCityId(){
        if (cityChoiceBox.getValue().equals("Phoenix, Arizona")) {
            return 602;
        }
        if (cityChoiceBox.getValue().equals("New York, New York")){
            return 718;
        }
        if (cityChoiceBox.getValue().equals("London, England")){
            return 200;
        }
        if (cityChoiceBox.getValue().equals("Montreal, Canada")){
            return 514;
        }
        return 0;
    }

    public String searchAddressTest(){              //checks if address already exists
        String address = addrFieldAddCust.getText();
        int addressId = DBConnector.randomId();
        try{
            PreparedStatement ps = DBConnector.getConnection().prepareStatement("SELECT addressId FROM address WHERE address = + ?");
            ps.setString(1, address);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                addressId = rs.getInt("addressId");
                return Integer.toString(addressId);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return Integer.toString(addressId);
    }

    public void addCustomer(){                      // adds new customer to DB
        int newId = DBConnector.randomId();
        String name = "";
        String phone = "";
        try {
             name = custNameFieldAddCust.getText();
             phone = phoneFieldAddCust.getText();
        }catch(Exception e){
            e.printStackTrace();
        }
        String addressId = searchAddressTest();
        int active = 1;

        Customer newCustomer = new Customer(newId, name, addressId, active, phone);
        CustomerDB.addCustomer(newCustomer);

    }
}
