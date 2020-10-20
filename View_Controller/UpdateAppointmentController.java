package View_Controller;

import DAO.AppointmentDB;
import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static View_Controller.AppointmentController.updatingAppointment;
import static View_Controller.LogInController.currentUser;
public class UpdateAppointmentController implements Initializable {

    @FXML public TextField newDescTextFieldUpdateAppt;
    @FXML public TextField custNameTextFieldUpdateAppt;
    @FXML public TextField newApptTypeTextFieldUpdateAppt;
    @FXML public TextField newTitleTextFieldUpdateAppt;
    @FXML public TextField newUrlTextFieldUpdateAppt;
    @FXML public ChoiceBox newStartTimeChoiceBox;
    @FXML public ChoiceBox newEndTimeChoiceBox;
    @FXML public TextField newDateTextField;
    @FXML public TextField startTimeTextField;
    @FXML public TextField endTimeTextField;

    ObservableList<String> timeList = FXCollections.observableArrayList( "AM", "PM");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newTitleTextFieldUpdateAppt.setText(updatingAppointment.getTitle());
        custNameTextFieldUpdateAppt.setText(CustomerController.getNameByCustId(updatingAppointment.getCustomerId()));
        newDescTextFieldUpdateAppt.setText(updatingAppointment.getDescription());
        newApptTypeTextFieldUpdateAppt.setText(updatingAppointment.getType());
        newUrlTextFieldUpdateAppt.setText(updatingAppointment.getUrl());
        newStartTimeChoiceBox.setItems(timeList);
        newEndTimeChoiceBox.setItems(timeList);
        newStartTimeChoiceBox.setValue("AM");
        newEndTimeChoiceBox.setValue("PM");

    }

    public void backBtnUpdateAppt(ActionEvent event) throws IOException {
        ScreenController apptScreen = new ScreenController();
        apptScreen.showAppointmentScreen(event);
    }

    public void userLogout(ActionEvent event) throws IOException {
        ScreenController logout = new ScreenController();
        logout.userLogout(event);
    }

    public void updateBtnUpdateAppt(ActionEvent event) throws Exception {
        if (allFieldsValid()) {
            if (validAppointmentCheck()) {
                int apptId = updatingAppointment.getAppointmentId();
                int customerId = updatingAppointment.getCustomerId();
                int userId = currentUser.getUserId();
                String newTitle = newTitleTextFieldUpdateAppt.getText();
                String newDescription = newDescTextFieldUpdateAppt.getText();
                String newLocation = updatingAppointment.getLocation();
                String newContact = updatingAppointment.getContact();
                String newApptType = newApptTypeTextFieldUpdateAppt.getText();
                String newUrl = newUrlTextFieldUpdateAppt.getText();
                String newDate = newDateTextField.getText();
                String startFormatted = timeSdf(newDate, true);
                String endFormatted = timeSdf(newDate, false);
                Appointment updatedAppointment = new Appointment(apptId, customerId, userId, newTitle, newDescription, newLocation, newContact, newApptType, newUrl, startFormatted, endFormatted);
                AppointmentDB.updateAppointment(updatedAppointment);
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
        if(newTitleTextFieldUpdateAppt.getText().isBlank() || custNameTextFieldUpdateAppt.getText().isBlank() || newApptTypeTextFieldUpdateAppt.getText().isBlank() || newDescTextFieldUpdateAppt.getText().isBlank() || newUrlTextFieldUpdateAppt.getText().isBlank() || newDateTextField.getText().isBlank() || startTimeTextField.getText().isEmpty() || endTimeTextField.getText().isEmpty()){
            return false;
        }
        return true;
    }

    public boolean validAppointmentCheck() throws Exception {
        String startFormatted = "";
        String endFormatted = "";
        String date;
        try {
            date = newDateTextField.getText();
            startFormatted = timeSdf(date, true);
            endFormatted = timeSdf(date, false);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error Adding Appointment");
            alert.setHeaderText("Incorrect Date Format");
            alert.setContentText("Check Date format (MM/DD/YY) (Include zeroes i.e 01/01/20 for January 1st, 2020");
            alert.showAndWait();
        }
        if (AddAppointmentController.appointmentTimeOverlapCheck(startFormatted, endFormatted)) {
            if (AppointmentDB.customerInDatabaseCheck(custNameTextFieldUpdateAppt.getText())) {
                if (AddAppointmentController.appointmentTimeConflictCheck(startFormatted) && AddAppointmentController.appointmentTimeConflictCheck(endFormatted)) {
                    return true;
                }
            }
        }
        System.out.println("error adding appointment valid AppointmentCheck return false");
        return false;
    }

    public String timeSdf(String date, boolean startTime) {
        String year = "20" + date.substring(6,8) + "-";
        String month = date.substring(0,2)+ "-";
        String day = date.substring(3,5) + " ";
        String time = "00:00:00";
        String startTimeAmPm = (String) newStartTimeChoiceBox.getValue();
        String endTimeAmPm = (String) newEndTimeChoiceBox.getValue();
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
}
