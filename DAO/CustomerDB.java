package DAO;

import Model.Address;
import Model.Customer;
import Util.DBConnector;

import java.sql.PreparedStatement;

import static View_Controller.CustomerController.updatingCustomer;
import static View_Controller.LogInController.currentUser;

public class CustomerDB {

    public static void addCustomer(Customer customer){                  //B.   Provide the ability to add, update, and delete customer records in the database, including name, address, and phone number.
        try{
            PreparedStatement ps = DBConnector.getConnection().prepareStatement("INSERT INTO customer(customerId, customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)" + "VALUES (?,?,?,?,?,?,?,?)");
            ps.setInt(1, customer.getCustomerId());
            ps.setString(2, customer.getCustomerName());
            ps.setString(3, customer.getAddressId());
            ps.setInt(4, customer.getActive());
            ps.setString(5, DBConnector.getCurrentDateTime());
            ps.setString(6, currentUser.getUserName());
            ps.setString(7, DBConnector.getCurrentDateTime());
            ps.setString(8, currentUser.getUserName());
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateCustomerName(String newName, int customerId){              //B.   Provide the ability to add, update, and delete customer records in the database, including name, address, and phone number.

        try{
            PreparedStatement ps = DBConnector.getConnection().prepareStatement("UPDATE customer SET customerName = ?, lastUpdate = ?, lastUpdateBy = ? WHERE customerId = ?");
            ps.setString(1, newName);
            ps.setString(2, DBConnector.getCurrentDateTime());
            ps.setString(3, currentUser.getUserName());
            ps.setInt(4, customerId);
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteCustomer(Customer customer){
        int customerDeleteId = customer.getCustomerId();
        try{
            PreparedStatement ps = DBConnector.getConnection().prepareStatement("DELETE FROM appointment WHERE customerId = ?");
            ps.setInt(1, customerDeleteId);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            PreparedStatement ps = DBConnector.getConnection().prepareStatement("DELETE FROM customer WHERE customerId = ?");
            ps.setInt(1, customerDeleteId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
