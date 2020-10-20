package View_Controller;

import Model.Appointment;
import Util.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

import static View_Controller.ReportsController.selectedUser;
import static View_Controller.ReportsController.selectedCustomer;
import static View_Controller.ReportsController.consultantSchedule;
import static View_Controller.ReportsController.monthlyReport;
import static View_Controller.ReportsController.monthInt;
import static View_Controller.ReportsController.whichSchedule;
import static View_Controller.ReportsController.monthString;

public class ReportScreen implements Initializable {
    @FXML
    public TableColumn titleCol;
    @FXML
    public TableColumn startCol;
    @FXML
    public TableColumn endCol;
    @FXML
    public TableColumn typeCol;
    @FXML public TableView reportTable;
    private final ZoneId newzid = ZoneId.systemDefault();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    public Label reportLabel;
    ObservableList<Appointment> reportList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reportList.clear();
        if (whichSchedule == 1){
            showConsultantSchedule();
            reportLabel.setText("Consultant Schedule");
        }
        else if(whichSchedule == 2){
            showCustomerReport();
            reportLabel.setText("Customer Report");
        }
        else if (whichSchedule == 3){
            showMonthlyReport(monthInt);
            reportLabel.setText("Monthly Appointment Type Report for " + monthString);
        }
    }

    public void showConsultantSchedule(){
        try{
            Connection con = DBConnector.getConnection();

            ResultSet rs = con.createStatement().executeQuery("SELECT appointmentId, customerId, userId, title, description, location, contact, type, url, start, end FROM appointment WHERE userId = " + getUserId(selectedUser));

            while (rs.next()){

                int apptId = rs.getInt("appointmentId");
                Timestamp newStart = rs.getTimestamp("start");
                ZonedDateTime newzdtStart = newStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);

                Timestamp timestampEnd = rs.getTimestamp("end");
                ZonedDateTime newzdtEnd = timestampEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(newzid);

                String tTitle = rs.getString("title");
                String tType = rs.getString("description");

                reportList.add(new Appointment(rs.getInt("appointmentId"), rs.getInt("customerId"), rs.getInt("userId"), tTitle, rs.getString("description"), rs.getString("location"), rs.getString("contact"), tType, rs.getString("url"), newLocalStart.format(timeDTF), newLocalEnd.format(timeDTF)));

            }
        }catch(Exception e){
            e.printStackTrace();
        }

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));

        reportTable.setItems(reportList);
    }

    public void showMonthlyReport(int month) {
        System.out.println("SHOW MONTHLY SHOW MONTHLY");
        try {
            Connection con = DBConnector.getConnection();

            ResultSet rs = con.createStatement().executeQuery("SELECT appointmentId, customerId, userId, title, description, location, contact, type, url, start, end FROM appointment WHERE Month(start) = '" + month + "' ORDER BY type");

            while (rs.next()) {
                Timestamp newStart = rs.getTimestamp("start");
                ZonedDateTime newzdtStart = newStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);

                Timestamp timestampEnd = rs.getTimestamp("end");
                ZonedDateTime newzdtEnd = timestampEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(newzid);

                String tTitle = rs.getString("title");
                String tType = rs.getString("description");

                reportList.add(new Appointment(rs.getInt("appointmentId"), rs.getInt("customerId"), rs.getInt("userId"), tTitle, rs.getString("description"), rs.getString("location"), rs.getString("contact"), tType, rs.getString("url"), newLocalStart.format(timeDTF), newLocalEnd.format(timeDTF)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));

        reportTable.setItems(reportList);
    }

    public void showCustomerReport(){
        try{
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT appointmentId, customerId, userId, title, description, location, contact, type, url, start, end FROM appointment WHERE customerId = " + CustomerController.getCustIdByName(selectedCustomer));

            while (rs.next()){
                int apptId = rs.getInt("appointmentId");
                Timestamp newStart = rs.getTimestamp("start");
                ZonedDateTime newzdtStart = newStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);

                Timestamp timestampEnd = rs.getTimestamp("end");
                ZonedDateTime newzdtEnd = timestampEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(newzid);

                String tTitle = rs.getString("title");
                String tType = rs.getString("description");

                reportList.add(new Appointment(rs.getInt("appointmentId"), rs.getInt("customerId"), rs.getInt("userId"), tTitle, rs.getString("description"), rs.getString("location"), rs.getString("contact"), tType, rs.getString("url"), newLocalStart.format(timeDTF), newLocalEnd.format(timeDTF)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));

        reportTable.setItems(reportList);
    }


    public int getUserId(String user){
        switch(user){
            case "test":
                return 1111;
            case "Michael":
                return 4182;
            case "TestUser":
                return 4444;
            case "username":
                return 7777;
            default:
                return 1111;
        }
    }

    public void logoutBtn(ActionEvent event) throws IOException {
        ScreenController userLogout = new ScreenController();
        userLogout.userLogout(event);
    }

    public void backBtn(ActionEvent event) throws IOException {
        reportList.clear();
        ScreenController backBtn = new ScreenController();
        backBtn.showReportScreen(event);
    }
}
