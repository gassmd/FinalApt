package View_Controller;

import Model.Appointment;
import Model.Customer;
import Util.DBConnector;
import javafx.beans.Observable;
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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class AppointmentController implements Initializable {
    public Button refreshBtn;
    @FXML
    public Label timeZoneLabel;
    @FXML
    private ChoiceBox timezoneChoiceBox;
    @FXML
    private Button tempAppAccessBtn;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<String, String> customerNameCol;
    @FXML
    private TableColumn<Appointment, String> titleCol;
    @FXML
    private TableColumn<Appointment, String> descriptionCol;
    @FXML
    private TableColumn<Appointment, String> locationCol;
    @FXML
    private TableColumn<Appointment, String> contactCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, String> urlCol;
    @FXML
    private TableColumn<Appointment, String> startCol;
    @FXML
    private TableColumn<Appointment, String> endCol;
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    public static Appointment updatingAppointment = new Appointment();
    public static Appointment selectedAppointment = new Appointment();
    private ZoneId newzid = ZoneId.systemDefault();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    ObservableList<String> timezoneList = FXCollections.observableArrayList("PST (GMT-7)", "MST (GMT-6)", "CST (GMT-5)", "EST (GMT-4)", "BST (GMT-1)");



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        timezoneChoiceBox.setItems(timezoneList);
        //timezoneChoiceBox.setValue("UTC");
       //timeZoneLabel.setText("Currently UTC");

        try {

            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT appointment.appointmentId, appointment.customerId, appointment.userId, customer.customerName, appointment.title, appointment.description, appointment.location, appointment.contact, appointment.type, appointment.url, appointment.start, appointment.end FROM appointment INNER JOIN customer ON appointment.customerId = customer.customerId;");
            while (rs.next()){
                int apptId = rs.getInt("appointment.appointmentId");

                //ZonedDateTime newzdtStart = newStart.toLocalDateTime().atZone(ZoneId.of("UTC"));

                String newStartString = rs.getString("appointment.start");
               // System.out.println("newStartString: " + newStartString);

              /*  Timestamp newStart = rs.getTimestamp("appointment.start");
                System.out.println("Timestamp newStart: " + newStart);*/

                LocalDateTime startDateTime = LocalDateTime.parse(newStartString, formatter);
                //System.out.println("LocalDateTime dateTime: " + startDateTime);

                ZonedDateTime newZdtStart = startDateTime.atZone(ZoneId.of("UTC"));
               // System.out.println("ZonedDateTime newzdtstart: " + newZdtStart);

                ZonedDateTime newLocalStart = newZdtStart.withZoneSameInstant(newzid);
               // System.out.println("ZDT newLocalStart: " + newLocalStart);
                
                //Timestamp timestampEnd = rs.getTimestamp("appointment.end");
                String newEndString = rs.getString("appointment.end");
                LocalDateTime endDateTime = LocalDateTime.parse(newEndString, formatter);
                ZonedDateTime newZdtEnd = endDateTime.atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalEnd = newZdtEnd.withZoneSameInstant(newzid);
               // ZonedDateTime newzdtEnd = timestampEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
               // ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(ZoneId.systemDefault());

                String tTitle = rs.getString("appointment.title");
                String tType = rs.getString("appointment.description");

                appointmentList.add(new Appointment(apptId, rs.getInt("appointment.customerId"), rs.getInt("appointment.userId"), tTitle, rs.getString("appointment.description"), rs.getString("appointment.location"), rs.getString("appointment.contact"), tType, rs.getString("appointment.url"), newLocalStart.format(timeDTF), newLocalEnd.format(timeDTF)));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));

        appointmentTable.setItems(appointmentList);

    }

    public static int randomId(){
        Random random = new Random();
        int rand = random.nextInt(9999999-1000000)+1000000;
        return rand;
    }

  /*  public ZoneId getZoneId() {
        String zoneId;
        zoneId = (String) timezoneChoiceBox.getValue();
        switch (zoneId){
            case "PST (GMT-7)":
                timeZoneLabel.setText("Currently PST");
                return ZoneId.of("America/Los_Angeles");
            case "MST (GMT-6)":
                timeZoneLabel.setText("Currently MST");
                return ZoneId.of("-07:00");
            case "CST (GMT-5)":
                timeZoneLabel.setText("Currently CST");
                return ZoneId.of("America/Chicago");
            case "EST (GMT-4)":
                timeZoneLabel.setText("Currently EST");
                return ZoneId.of("-05:00");
            case "BST (GMT-1)":
                timeZoneLabel.setText("Currently BST");
                return ZoneId.of("Asia/Dhaka");
            default:
                timeZoneLabel.setText("NEWUTC");
                return ZoneId.of("UTC");

        }
    }

    public void getNewList(){
        System.out.println(getZoneId());

        appointmentList.clear();
    }*/


    public void deleteAppointmentBtn(ActionEvent event) {                   // C. Add, Update, Delete appointments
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Appointment Deletion");
        alert.setHeaderText("Confirm Appointment Delete");
        alert.setContentText("Delete " + appointment.getTitle() + " from appointment records?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try{
                int appointmentId = appointment.getAppointmentId();
                Connection con = DBConnector.getConnection();
                Statement stmt = con.createStatement();
                stmt.executeUpdate("DELETE FROM appointment WHERE appointmentId = " + appointmentId);
                refreshBtn(event);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void updateAppointmentBtn(ActionEvent event) throws IOException {                // C. Add, Update, Delete appointments
        updatingAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Update Customer");
        alert.setHeaderText("Confirm Update Customer");
        alert.setContentText("Update Appointment " + updatingAppointment.getTitle() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            ScreenController updateAppointment = new ScreenController();
            updateAppointment.showUpdateApptScreen(event);
        }
    }

    public static Date stringToDate(String date) throws ParseException {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);

        return date1;
    }

    public void addAppointmentBtn(ActionEvent event) throws IOException {
        ScreenController addAppointment = new ScreenController();
        addAppointment.showAddApptScreen(event);
    }

    public void backBtnAppointment(ActionEvent event) throws IOException {
        ScreenController customer = new ScreenController();
        customer.showWelcomeScreen(event);
    }

    public void userLogout(ActionEvent event) throws IOException {
        ScreenController userLogout = new ScreenController();
        userLogout.userLogout(event);
    }

    public void refreshBtn(ActionEvent event) throws IOException {
        ScreenController appointment = new ScreenController();
        appointment.showAppointmentScreen(event);
        //getNewList();
    }

    public void showCustomerRecord(ActionEvent event) throws IOException{
        selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        ScreenController recordScreen = new ScreenController();
        recordScreen.showCustomerRecordScreen(event);
    }

    public void apptDetailsBtn(ActionEvent event) throws IOException{
        selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        ScreenController detailScreen = new ScreenController();
        detailScreen.showApptDetailScreen(event);
    }
}
