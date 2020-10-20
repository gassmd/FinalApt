package View_Controller;

import DAO.AppointmentDB;
import Model.Appointment;
import Model.User;
import Util.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddAppointmentController {
    @FXML
    public TextField customerTextFieldAddAppt;
    @FXML
    public TextField apptDescTextField;
    @FXML
    public TextField apptTypeTextField;
    @FXML
    public TextField titleTextField;
    @FXML
    public TextField contactTextField;
    @FXML
    public TextField urlTextField;
    @FXML
    public TextField dateField;
    @FXML
    public TextField startTimeTextField;
    @FXML
    public TextField endTimeTextField;
    @FXML
    private ChoiceBox startTimeChoiceBox;
    @FXML
    private ChoiceBox endTimeChoiceBox;
    @FXML
    private ChoiceBox officeLocationChoiceBox;
    ObservableList<String> cityList = FXCollections.observableArrayList("Phoenix, Arizona", "New York, New York", "London, England", "Montreal, Canada");
    ObservableList<String> timeList = FXCollections.observableArrayList("AM", "PM");

    // C. Add, Update, Delete appointments

    @FXML
    private void initialize(){officeLocationChoiceBox.setItems(cityList);
    startTimeChoiceBox.setItems(timeList);
    endTimeChoiceBox.setItems(timeList);
    officeLocationChoiceBox.setValue("Phoenix, Arizona");
    startTimeChoiceBox.setValue("AM");
    endTimeChoiceBox.setValue("AM");
    }

    public void backBtnAddAppt(ActionEvent event) throws IOException {
        ScreenController apptScreen = new ScreenController();
        apptScreen.showAppointmentScreen(event);
    }

                                                                 //F. Write exception controls to prevent each of the following:
    public boolean validAppointmentCheck() throws Exception {                   // throws
        String startFormatted = "";
        String endFormatted = "";
        String date;
        try {
            date = dateField.getText();
            startFormatted = timeSdf(date, true);
            endFormatted = timeSdf(date, false);
        }catch(Exception e){                                                    // try-catch blocks
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error Adding Appointment");
            alert.setHeaderText("Incorrect Date Format");
            alert.setContentText("Check Date format (MM/DD/YY) (Include zeroes i.e 01/01/20 for January 1st, 2020");
            alert.showAndWait();
        }
        if (appointmentTimeOverlapCheck(startFormatted, endFormatted)) {                // method handling
            if(AppointmentDB.customerInDatabaseCheck(customerTextFieldAddAppt.getText())){
                if(appointmentTimeConflictCheck(startFormatted) && appointmentTimeConflictCheck(endFormatted)){
                    return true;
                }
            }
        }
        System.out.println("error adding appointment valid AppointmentCheck return false");
        return false;
    }
    public void addBtnAddAppt(ActionEvent event) throws Exception {
        if (allFieldsValid()) {
            if (validAppointmentCheck()) {
                int apptId = AppointmentController.randomId();
                int customerId = CustomerController.getCustIdByName(customerTextFieldAddAppt.getText());
                int userId = LogInController.currentUser.getUserId();
                String title = titleTextField.getText();
                String description = apptDescTextField.getText();
                String location = getCityName();
                String contact = contactTextField.getText();
                String apptType = apptTypeTextField.getText();
                String url = urlTextField.getText();
                String date = dateField.getText();
                String start = (String) startTimeChoiceBox.getValue();
                String end = (String) endTimeChoiceBox.getValue();
                String startFormatted = timeSdf(date, true);
                System.out.println("STARTFORMATTED : " + startFormatted);
                String endFormatted = timeSdf(date, false);
                System.out.println("ENDFORMATTED: " + endFormatted);
                Appointment newAppointment = new Appointment(apptId, customerId, userId, title, description, location, contact, apptType, url, startFormatted, endFormatted);
                String customerName = CustomerController.getNameByCustId(customerId);

                try {
                    AppointmentDB.addAppointment(newAppointment, customerName);                  //F. entering nonexistant or invalid customer data, try/catch
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.NONE);
                    alert.setTitle("Error Adding Appointment");
                    alert.setHeaderText("Customer not present in database");
                    alert.setContentText("Check customer name. (Must be valid customer record in database)");
                    alert.showAndWait();
                }
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Appointment Added", ButtonType.OK);
                confirm.show();
                ScreenController showAppointmentScreen = new ScreenController();
                showAppointmentScreen.showAppointmentScreen(event);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error Adding Appointment");
            alert.setHeaderText("Invalid Fields");
            alert.setContentText("Please ensure all fields are filled in");
            alert.showAndWait();
        }
    }

    public boolean allFieldsValid(){
        if(titleTextField.getText().isBlank() || customerTextFieldAddAppt.getText().isBlank() || apptDescTextField.getText().isBlank() || contactTextField.getText().isBlank() || apptTypeTextField.getText().isBlank() || urlTextField.getText().isBlank() || dateField.getText().isBlank() || startTimeTextField.getText().isBlank() || endTimeTextField.getText().isBlank()){
            return false;
        }
        return true;
    }


    public static boolean appointmentTimeConflictCheck(String appointmentTime){                         // F. scheduling an appointment outside business hours, method handling
        String appointmentHour = appointmentTime.substring(11,13);
        int appointmentHourInt = Integer.parseInt(appointmentHour);
        if (appointmentHourInt < 9 || appointmentHourInt > 17){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error Adding Appointment");
            alert.setHeaderText("Appointment Scheduling Conflict");
            alert.setContentText("Cannot schedule outside operating hours. Open (9AM-5PM)");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean appointmentTimeOverlapCheck(String start1, String end1) throws Exception {        // F. scheduling overlapping appointments, throws, method handling
       System.out.println("overlapCheck String start1: " + start1);
       System.out.println("overlapCheck String end1: " + end1);
        Date start1date = stringToDate(start1);
        Date end1date = stringToDate(end1);
        System.out.println("overlap DATE start1date = " + start1date);
        System.out.println("overlap DATE end1date = " + end1date);
        ArrayList<Date> startDates2 = AppointmentDB.getAllDates(true);
        ArrayList<Date> endDates2 = AppointmentDB.getAllDates(false);


        for (int i = 0; i<startDates2.size(); i++) {
            Date start2date = startDates2.get(i);
            Date end2date = endDates2.get(i);
            if (isOverlapping(start1date, end1date, start2date, end2date)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.NONE);
                alert.setTitle("Error");
                alert.setHeaderText("Appointment Scheduling Conflict");
                alert.setContentText("Appointment is already scheduled within this timeframe. Please change appointment hours");
                alert.showAndWait();
                return false;
            }
        }

        return true;
    }

    public static Date stringToDate(String date) throws ParseException {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);

        return date1;
    }

    public static boolean isOverlapping(Date start1, Date end1, Date start2, Date end2){
        return start1.before(end2) && start2.before(end1);
    }

    public String timeSdf(String date, boolean startTime) {
        String year = "20" + date.substring(6,8) + "-";
        String month = date.substring(0,2)+ "-";
        String day = date.substring(3,5) + " ";
        String time = "00:00:00";
        String startTimeAmPm = (String) startTimeChoiceBox.getValue();
        String endTimeAmPm = (String) endTimeChoiceBox.getValue();
        if(startTime){
            if (startTimeAmPm.equals("AM")) {
                time = hourHandle(startTimeTextField.getText(), false) + ":00";
            }
            if (startTimeAmPm.equals("PM")){
                time = hourHandle(startTimeTextField.getText(), true) + ":00";
            }
        }

        else{
            if (endTimeAmPm.equals("AM")){
                time = hourHandle(endTimeTextField.getText(), false) + ":00";
            }
            if (endTimeAmPm.equals("PM")){
                time = hourHandle(endTimeTextField.getText(), true) + ":00";
            }
        }
        System.out.println(" 212// timeSDF " + year + month + day + time);
        return year + month + day + time;

    }







    public String hourHandle(String hourMinutes, boolean pm){

        if (hourMinutes.length() == 4){
            hourMinutes = "0" + hourMinutes;
        }
        String minutes = hourMinutes.substring(3,5);
        if (pm == true){
            int hourInt = Integer.parseInt(hourMinutes.substring(0,2));
            if (hourInt == 12){
                hourInt = 0;
            }
            hourInt = hourInt + 12;
            hourMinutes = hourInt + ":" + minutes;
        }
        return hourMinutes;
    }
    public String getCityName(){
        if (officeLocationChoiceBox.getValue().equals("Phoenix, Arizona")) {
            return "Phoenix, Arizona";
        }
        if (officeLocationChoiceBox.getValue().equals("New York, New York")){
            return "New York, New York";
        }
        if (officeLocationChoiceBox.getValue().equals("London, England")){
            return "London, England";
        }
        if (officeLocationChoiceBox.getValue().equals("Montreal, Canada")){
            return "Montreal, Canada";
        }
        return "";
    }
}
