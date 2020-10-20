package View_Controller;

import DAO.AppointmentDB;
import Model.Customer;
import Util.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;




public class CustomerController implements Initializable {
    public Button refreshBtn;
    @FXML
    private TableColumn<Customer, String> phoneCol;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> customerNameCol;
    @FXML
    private TableColumn<Customer, String> addressIdCol;
    @FXML
    public static Customer selectedCustomer = new Customer();
    public static Customer updatingCustomer = new Customer();


    ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT customer.customerId, customer.customerName, address.address,customer.active, address.phone  FROM address INNER JOIN customer ON address.addressId = customer.addressId;");

            while (rs.next()){
                customerList.add(new Customer(rs.getInt("customerId"), rs.getString("customerName"), rs.getString("address"), rs.getInt("active"), rs.getString("phone")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressIdCol.setCellValueFactory(new PropertyValueFactory<>("addressId"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerTable.setItems(customerList);
    }


    public void deleteCustomerBtn(ActionEvent event) {                          // B. Provide the ability to add, update, and delete customer records in the database, including name, address, and phone number.
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Customer Deletion");
        alert.setHeaderText("Confirm Customer Delete");
        alert.setContentText("Delete " + selectedCustomer.getCustomerName() + " from customer records?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try{
                int customerId = selectedCustomer.getCustomerId();
                AppointmentDB.deleteAppointmentByCustomerId(customerId);
                Connection con = DBConnector.getConnection();
                Statement stmt = con.createStatement();
                stmt.executeUpdate("DELETE FROM customer WHERE customerId = " + customerId);
                refreshBtn(event);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void updateCustomerBtn(ActionEvent event) throws IOException {               // B. Provide the ability to add, update, and delete customer records in the database, including name, address, and phone number.
        updatingCustomer = customerTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Update Customer");
        alert.setHeaderText("Confirm Update Customer");
        alert.setContentText("Update Customer " + updatingCustomer.getCustomerName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            ScreenController showUpdateCustomer = new ScreenController();
            showUpdateCustomer.showUpdateCustScreen(event);
        }
    }

    public void addCustomerBtn(ActionEvent event) throws IOException {
        ScreenController addCustomer = new ScreenController();
        addCustomer.showAddCustScreen(event);
    }

    public void backBtnCustomer(ActionEvent event) throws IOException {
        ScreenController customer = new ScreenController();
        customer.showWelcomeScreen(event);
    }

    public void userLogout(ActionEvent event) throws IOException {
        ScreenController userLogout = new ScreenController();
        userLogout.userLogout(event);
    }

    public void refreshBtn(ActionEvent event) throws IOException {
        ScreenController customer = new ScreenController();
        customer.showCustomerScreen(event);
    }

    public static int getCustIdByName(String customerName){
        int customerId = 00000000;
        try{
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT customerId FROM customer WHERE customerName = '" + customerName + "'");

            if(rs.next()){
                customerId = rs.getInt("customerId");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return customerId;
    }

    public static String getNameByCustId(int customerId){
        String customerName = "";
        try{
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT customerName FROM customer WHERE customerId ='" + customerId + "'");

            if (rs.next()){
                customerName = rs.getString("customerName");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return customerName;
    }

    public void showCustomerRecord(ActionEvent event) throws IOException {
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        ScreenController recordScreen = new ScreenController();
        recordScreen.showCustomerRecordScreen(event);
    }
}
