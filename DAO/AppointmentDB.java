package DAO;

import Model.Appointment;
import Util.DBConnector;
import View_Controller.CustomerController;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static View_Controller.LogInController.currentUser;

public class AppointmentDB {

    public static void addAppointment(Appointment appointment, String customerName) {

        String appointmentStart = appointment.getStart();
        String appointmentEnd = appointment.getEnd();
        if (!appointmentTimeConflictCheck(appointmentStart) && !appointmentTimeConflictCheck(appointmentEnd)) {
            try {
                PreparedStatement ps = DBConnector.getConnection().prepareStatement("INSERT INTO appointment(appointmentId, customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)" +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                ps.setInt(1, appointment.getAppointmentId());
                ps.setInt(2, getCustomerIdByName(customerName));
                ps.setInt(3, currentUser.getUserId());
                ps.setString(4, appointment.getTitle());
                ps.setString(5, appointment.getDescription());
                ps.setString(6, appointment.getLocation());
                ps.setString(7, appointment.getContact());
                ps.setString(8, appointment.getType());
                ps.setString(9, appointment.getUrl());
                ps.setString(10, appointment.getStart());
                ps.setString(11, appointment.getEnd());
                ps.setString(12, DBConnector.getCurrentDateTime());
                ps.setString(13, currentUser.getUserName());
                ps.setString(14, DBConnector.getCurrentDateTime());
                ps.setString(15, currentUser.getUserName());
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.NONE);
                alert.setTitle("Error");
                alert.setHeaderText("Error adding appointment");
                alert.setContentText("Check customer name. (Must be valid customer record in database)");
                alert.showAndWait();

            }
        }
    }

    public static void updateAppointment(Appointment appointment) {

        try {
            PreparedStatement ps = DBConnector.getConnection().prepareStatement("UPDATE appointment SET title = ?, description = ?, type = ?, url = ?, start = ?, end = ?, lastUpdate = ?, lastUpdateBy = ? WHERE appointmentId = ?");
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getType());
            ps.setString(4, appointment.getUrl());
            ps.setString(5, appointment.getStart());
            ps.setString(6, appointment.getEnd());
            ps.setString(7, DBConnector.getCurrentDateTime());
            ps.setString(8, currentUser.getUserName());
            ps.setInt(9, appointment.getAppointmentId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean appointmentTimeConflictCheck(String appointmentTime) {
        String appointmentHour = appointmentTime.substring(11, 13);
        int appointmentHourInt = Integer.parseInt(appointmentHour);
        if (appointmentHourInt < 9 || appointmentHourInt > 17) {
            return true;
        }
        return false;
    }

    public static boolean customerInDatabaseCheck(String customer) {
        try {
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT customerName FROM customer");

            while (rs.next()) {
                if (customer.equals(rs.getString("customerName"))) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setTitle("Error");
        alert.setHeaderText("Error adding appointment");
        alert.setContentText("Check customer name. (Must be valid customer record in database)");
        alert.showAndWait();
        return false;
    }

    public static void deleteAppointmentByCustomerId(int customerId) {
        try {
            PreparedStatement ps = DBConnector.getConnection().prepareStatement("DELETE FROM appointment WHERE customerID = ?");
            ps.setInt(1, customerId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getCustomerIdByName(String customerName) {
        int customerId = 0000000;
        try {
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT customerId FROM customer WHERE customerName = '" + customerName + "'");

            if (rs.next()) {
                customerId = rs.getInt("customerId");
            } else {
                System.out.println("Customer doesn't exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerId;
    }

    public static ArrayList<Date> getAllDates(boolean startDates) {
        ArrayList<Date> dates = new ArrayList<>();
        try {
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT start, end FROM appointment");

            while (rs.next()) {
                if (startDates) {
                    Date newDate = stringToDate(rs.getString("start"));
                    dates.add(newDate);
                }
                if (!startDates) {
                    Date newDate = stringToDate(rs.getString("end"));
                    dates.add(newDate);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dates;
    }

    public static Date stringToDate(String date) throws ParseException {

        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        return date1;

    }

    public static int monthlyReport(int month){
        int numOfAppointments = 0;
        try{
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(start) FROM appointment WHERE Month(start) = '" + month + "'");

            if(rs.next()){
                numOfAppointments = rs.getInt("COUNT(start)");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return numOfAppointments;
    }

    public static int userReport(String user){
        int numOfAppointments = 0;
        try{
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(start) FROM appointment WHERE createdBy = '" + user + "'");

            if (rs.next()){
                numOfAppointments = rs.getInt("COUNT(start)");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return numOfAppointments;
    }

    public static String customerReport (String customerName, boolean titleBoo){
        String title="";
        String start="";
        try {
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT title, start FROM appointment INNER JOIN customer ON appointment.customerId = customer.customerId WHERE customer.customerName = '" + customerName + "'");

            while (rs.next()) {
                title = rs.getString("title");
                start = rs.getString("start");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(titleBoo){
            return title;
        }
        else{
            return start;
        }
    }

    public static int customerAppointmentCount(String customerName){
        int appointmentCount = 0;
        int custId = CustomerController.getCustIdByName(customerName);

        try {
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(start) FROM appointment WHERE customerId = '" + custId + "'");

            while (rs.next()){
                appointmentCount = rs.getInt("COUNT(start)");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return appointmentCount;
    }
}
