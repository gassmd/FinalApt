package View_Controller;

import DAO.AppointmentDB;
import Model.Customer;
import Model.User;
import Util.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


        // I. Provide the ability to generate each  of the following reports:

public class ReportsController implements Initializable {
    public TextField customerNameTextField;
    @FXML
    private ChoiceBox userChoiceBox;
    @FXML
    private ChoiceBox monthChoiceBox;
    ObservableList<String> monthList = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    ObservableList<String> userList = FXCollections.observableArrayList("test", "Michael", "TestUser", "username");
    public static String selectedUser = new String();
    public static String selectedCustomer = new String();
    public static boolean consultantSchedule;                   // 1 consultant, 2 customer, 3monthly
    public static int whichSchedule;
    public static boolean monthlyReport = false;
    public static int monthInt;
    public static String monthString;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthChoiceBox.setItems(monthList);
        userChoiceBox.setItems(userList);

    }

    public void getAppointmentByName(ActionEvent event) throws IOException {                   // I. one additional report of your choice, get appointments by customer name

        String customerName = customerNameTextField.getText();
        int appointmentCount = AppointmentDB.customerAppointmentCount(customerName);

        selectedCustomer = customerNameTextField.getText();
       // consultantSchedule = false;
        whichSchedule = 2;

        ScreenController showReport = new ScreenController();
        showReport.showFinalReportScreen(event);
        Alert report = new Alert(Alert.AlertType.CONFIRMATION);
        report.initModality(Modality.NONE);
        if (appointmentCount != 0) {
            report.setHeaderText(customerName + "report");
            report.setContentText(customerName + " has " + appointmentCount + " appointment(s) coming up");
        }
        else{
            report.setHeaderText("Error getting report");
            report.setContentText("Customer has no upcoming appointments");
        }
        report.showAndWait();

    }

    public void getMonthlyReport(ActionEvent event) throws IOException {                                   //I.  monthly report

        whichSchedule = 3;
        //monthlyReport = true;
        monthString = (String) monthChoiceBox.getValue();
        int numOfAppointments = AppointmentDB.monthlyReport(getMonthSelection());
        monthInt = getMonthSelection();
        ScreenController showReport = new ScreenController();
        showReport.showFinalReportScreen(event);
        Alert report = new Alert(Alert.AlertType.CONFIRMATION);
        report.initModality(Modality.NONE);
        report.setHeaderText(monthString + " Monthly Report");
        report.setContentText("Appointments scheduled for the month of " + monthString + ": " + numOfAppointments);
        report.showAndWait();

    }

    public void getUserReport(ActionEvent event) throws IOException {                               //I. consultant schedule
        selectedUser = (String) userChoiceBox.getValue();
        //consultantSchedule = true;
        whichSchedule = 1;
     ScreenController showReport = new ScreenController();
     showReport.showFinalReportScreen(event);




       String user = (String) userChoiceBox.getValue();
        int numOfAppointments = AppointmentDB.userReport(user);
        Alert report = new Alert(Alert.AlertType.CONFIRMATION);
        report.initModality(Modality.NONE);
        report.setHeaderText(user + " User Report");
        report.setContentText("Appointments scheduled for user " + user + ": " + numOfAppointments);
        report.showAndWait();
    }

    public void userLogout(ActionEvent event) throws IOException {
        ScreenController userLogout = new ScreenController();
        userLogout.userLogout(event);
    }

    public int getMonthSelection(){
        switch ((String) monthChoiceBox.getValue()){
            case "January":
                return 1;
            case "February":
                return 2;
            case "March":
                return 3;
            case "April":
                return 4;
            case "May":
                return 5;
            case "June":
                return 6;
            case "July":
                return 7;
            case "August":
                return 8;
            case "September":
                return 9;
            case "October":
                return 10;
            case "November":
                return 11;
            case "December":
                return 12;
            default:
                return 1;
        }
    }

    public void backBtn(ActionEvent event) throws IOException {
        ScreenController welcomeScreen = new ScreenController();
        welcomeScreen.showWelcomeScreen(event);
    }
}
