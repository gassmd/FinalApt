package View_Controller;

import Util.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import static View_Controller.AppointmentController.selectedAppointment;
import static View_Controller.CustomerController.selectedCustomer;

public class CustomerRecordController implements Initializable {


    @FXML public TextField custNameFieldCustRec;
    @FXML public TextField addrFieldCustRec;
    @FXML public TextField phoneFieldCustRec;
    @FXML public TextField addr2FieldCustRec;
    @FXML public TextField postalFieldCustRec;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        custNameFieldCustRec.setText(CustomerController.getNameByCustId((selectedAppointment.getCustomerId())));
        addrFieldCustRec.setText(getAddressByAddrId(getAddrIdByCustId(selectedAppointment.getCustomerId()), 1));
        addr2FieldCustRec.setText(getAddressByAddrId(getAddrIdByCustId(selectedAppointment.getCustomerId()), 2));
        postalFieldCustRec.setText(getAddressByAddrId(getAddrIdByCustId(selectedAppointment.getCustomerId()), 3));
        phoneFieldCustRec.setText(getAddressByAddrId(getAddrIdByCustId(selectedAppointment.getCustomerId()), 4));

        custNameFieldCustRec.setText(CustomerController.getNameByCustId((selectedCustomer.getCustomerId())));
        addrFieldCustRec.setText(getAddressByAddrId(getAddrIdByCustId(selectedCustomer.getCustomerId()), 1));
        addr2FieldCustRec.setText(getAddressByAddrId(getAddrIdByCustId(selectedCustomer.getCustomerId()), 2));
        postalFieldCustRec.setText(getAddressByAddrId(getAddrIdByCustId(selectedCustomer.getCustomerId()), 3));
        phoneFieldCustRec.setText(getAddressByAddrId(getAddrIdByCustId(selectedCustomer.getCustomerId()), 4));


    }

    public void backCustRec(ActionEvent event) throws IOException {
        ScreenController customer = new ScreenController();
        customer.showCustomerScreen(event);
    }

    public void userLogout(ActionEvent event) throws IOException {
        ScreenController userLogout = new ScreenController();
        userLogout.userLogout(event);
    }

    public String getAddressByAddrId(int addressId, int addrType){
        String address = "";
        try{
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT address, address2, postalCode, phone FROM address WHERE addressId = " + addressId);
            if (addrType == 1){
                if (rs.next()){
                    address = rs.getString("address");
                }
            }
            if (addrType == 2){
                if (rs.next()){
                    address = rs.getString("address2");
                }
            }
            if (addrType == 3){
                if (rs.next()){
                    address = rs.getString("postalCode");
                }
            }
            if (addrType == 4){
                if (rs.next()){
                    address = rs.getString("phone");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return address;
    }

    public int getAddrIdByCustId(int customerId){
        int addressId = 0000000;
        try{
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT addressId FROM customer WHERE customerId = '" + customerId + "'");

            if (rs.next()){
                addressId = rs.getInt("addressId");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return addressId;
    }


    public void backApptRec(ActionEvent event) throws IOException {
        ScreenController apptRec = new ScreenController();
        apptRec.showAppointmentScreen(event);
    }
}
