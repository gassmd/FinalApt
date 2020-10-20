package DAO;

import Model.Address;
import Util.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static View_Controller.LogInController.currentUser;

public class AddressDB {

    public static void addAddress(Address address) {

        try {
            PreparedStatement ps = DBConnector.getConnection().prepareStatement("INSERT INTO address(addressId, address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)" + "VALUES(?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, address.getAddressId());
            ps.setString(2, address.getAddress());
            ps.setString(3, address.getAddress2());
            ps.setInt(4, address.getCityId());
            ps.setString(5, address.getPostalCode());
            ps.setString(6, address.getPhone());
            ps.setString(7, DBConnector.getCurrentDateTime());
            ps.setString(8, currentUser.getUserName());
            ps.setString(9, DBConnector.getCurrentDateTime());
            ps.setString(10, currentUser.getUserName());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateAddress(Address updatedAddress) {

        try {
            PreparedStatement ps = DBConnector.getConnection().prepareStatement("UPDATE address SET addressId = ?, address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdate = ?, lastUpdateBy = ? WHERE addressId = ?");
            ps.setInt(1, updatedAddress.getAddressId());
            ps.setString(2, updatedAddress.getAddress());
            ps.setString(3, updatedAddress.getAddress2());
            ps.setInt(4, updatedAddress.getCityId());
            ps.setString(5, updatedAddress.getPostalCode());
            ps.setString(6, updatedAddress.getPhone());
            ps.setString(7, DBConnector.getCurrentDateTime());
            ps.setString(8, currentUser.getUserName());
            ps.setInt(9, updatedAddress.getAddressId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getAddressIdByCustomer(int customerId) {
        int addressId = 0000000;
        try {
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT addressId FROM customer WHERE customerId = '" + customerId + "'");

            if (rs.next()){
                addressId = rs.getInt("addressId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addressId;
    }
}
