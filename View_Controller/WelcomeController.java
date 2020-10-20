package View_Controller;

import Model.Appointment;
import Util.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class WelcomeController implements Initializable {
    private final ZoneId newzid = ZoneId.systemDefault();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    ObservableList<String> monthList = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    @FXML
    private TextField weekTextField;
    @FXML
    private ChoiceBox monthlyChoiceBox;
    @FXML
    private TableView calendar;
    @FXML
    private TableColumn<Appointment, String> titleCol;
    @FXML
    private TableColumn<Appointment, String> startTimeCol;
    @FXML
    private TableColumn<Appointment, String> locationCol;

    public static List<Date> getDatesBetween(Date startDate, Date endDate) {
        List<Date> datesBetween = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesBetween.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return datesBetween;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthlyChoiceBox.setValue("January");
        monthlyChoiceBox.setItems(monthList);

        try {
            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT start FROM appointment");

            while (rs.next()) {
                ArrayList<Timestamp> appointmentTimes = new ArrayList<Timestamp>();
                appointmentTimes.add(rs.getTimestamp("start"));
                appointmentTimes.forEach((time) -> {
                    appointment15minCheck(time);
                });                                                                      // G. lambda expression to accelerate checking each appointment
                                                                                        // time with current time to ensure that no appointment was within 15 mins.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showReports(ActionEvent event) throws IOException {
        ScreenController reports = new ScreenController();
        reports.showReportScreen(event);
    }

    public void showAppointmentScreen(ActionEvent event) throws IOException {
        ScreenController appointment = new ScreenController();
        appointment.showAppointmentScreen(event);
    }

    public void showCustomerScreen(ActionEvent event) throws IOException {
        ScreenController customer = new ScreenController();
        customer.showCustomerScreen(event);
    }

    public void userLogout(ActionEvent event) throws IOException {
        ScreenController userLogout = new ScreenController();
        userLogout.userLogout(event);

    }

    public void appointment15minCheck(Timestamp appointmentTime) {             // H. provide alert if appointment within 15 mins
        String currentTime = DBConnector.getCurrentDateTime();
        System.out.println("Current time: " + currentTime);
        String currentTimeConcat = currentTime.substring(11, 13) + currentTime.substring(14, 16);
        System.out.println("Current time concat:    " + currentTimeConcat);

        String apptTimeString = appointmentTime.toString();

        String appointmentTimeConcat = apptTimeString.substring(11, 13) + apptTimeString.substring(14, 16);
        System.out.println("Appointment time concat:    " + appointmentTimeConcat);
        System.out.println("___________________________________________");
        int currentTimeInt = Integer.parseInt(currentTimeConcat);
        int appointmentTimeInt = Integer.parseInt(appointmentTimeConcat);
        if (currentTime.substring(0, 10).equals(apptTimeString.substring(0, 10))) {        // if dates same
            if (appointmentTimeInt - currentTimeInt <= 15 && appointmentTimeInt > currentTimeInt) {
                System.out.println(appointmentTimeInt - currentTimeInt);
                Alert warning = new Alert(Alert.AlertType.WARNING, "Appointment Soon", ButtonType.OK);
                warning.show();
            }
        }
    }


    public int getMonthSelection() {
        switch ((String) monthlyChoiceBox.getValue()) {
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

    public void refreshCalendarMonthly() {

        calendar.getItems().clear();
        try {

            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT appointmentId, customerId, userId, title, description, location, contact, type, url, start, end FROM appointment WHERE Month(start) = " + getMonthSelection());

            while (rs.next()) {

                Timestamp newStart = rs.getTimestamp("appointment.start");
                ZonedDateTime newzdtStart = newStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);

                Timestamp timestampEnd = rs.getTimestamp("appointment.end");
                ZonedDateTime newzdtEnd = timestampEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(newzid);


                appointmentList.add(new Appointment(rs.getInt("appointmentId"), rs.getInt("customerId"), rs.getInt("userId"), rs.getString("title"), rs.getString("description"), rs.getString("location"), rs.getString("contact"), rs.getString("type"), rs.getString("url"), newLocalStart.format(timeDTF), newLocalEnd.format(timeDTF)));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        calendar.setItems(appointmentList);
    }

    public void refreshCalendarWeekly(ActionEvent event) throws ParseException {
        getWeeklyCalendar();
        calendar.setItems(appointmentList);
    }

    public Date weeklyStringToDate(String date) throws ParseException {
        Date date1 = new SimpleDateFormat("MM/dd/yy").parse(date);
        return date1;
    }

    public void getWeeklyCalendar() throws ParseException {
        String week = weekTextField.getText();
        Date weekStart = weeklyStringToDate(week);
        Calendar weekCalendar = Calendar.getInstance();

        weekCalendar.setTime(weekStart);
        weekCalendar.add(Calendar.DAY_OF_MONTH, 7);
        Date weekEnd = weekCalendar.getTime();

        calendar.getItems().clear();

        List<Date> listOfDates = (getDatesBetween(weekStart, weekEnd));
        listOfDates.forEach(date -> dateLocalDate(date));            // G. lambda expression to accelerate to converting of each date into localDate to add into Database
    }

    public void dateLocalDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int yearInt = localDate.getYear();
        int monthInt = localDate.getMonthValue();
        int dayInt = localDate.getDayOfMonth();
        addDateInDB(yearInt, monthInt, dayInt);
    }

    public void addDateInDB(int year, int month, int day) {

        try {

            Connection con = DBConnector.getConnection();
            ResultSet rs = con.createStatement().executeQuery("SELECT appointmentId, customerId, userId, title, description, location, contact, type, url, start, end FROM appointment WHERE YEAR(start) = '" + year + "'" + "AND MONTH(start) ='" + month + "'" + "AND DAY(start) = '" + day + "'");

            while (rs.next()) {

                Timestamp newStart = rs.getTimestamp("appointment.start");
                ZonedDateTime newzdtStart = newStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);

                Timestamp timestampEnd = rs.getTimestamp("appointment.end");
                ZonedDateTime newzdtEnd = timestampEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(newzid);

                appointmentList.add(new Appointment(rs.getInt("appointmentId"), rs.getInt("customerId"), rs.getInt("userId"), rs.getString("title"), rs.getString("description"), rs.getString("location"), rs.getString("contact"), rs.getString("type"), rs.getString("url"), newLocalStart.format(timeDTF), newLocalEnd.format(timeDTF)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
    }
}
