package View_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenController {



    public void showWelcomeScreen(ActionEvent event) throws IOException {
        Parent welcomeScreen = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));            // methods to show different screens
        Scene scene = new Scene(welcomeScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void showAppointmentScreen(ActionEvent event) throws IOException{
        Parent appointmentScreen = FXMLLoader.load(getClass().getResource("AppointmentScreen.fxml"));            // methods to show different screens
        Scene scene = new Scene(appointmentScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void showAddApptScreen(ActionEvent event) throws IOException{
        Parent welcomeScreen = FXMLLoader.load(getClass().getResource("AddAppointmentScreen.fxml"));            // methods to show different screens
        Scene scene = new Scene(welcomeScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void showUpdateApptScreen(ActionEvent event) throws IOException{
        Parent updateScreen = FXMLLoader.load(getClass().getResource("UpdateAppointmentScreen.fxml"));            // methods to show different screens
        Scene scene = new Scene(updateScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void showCustomerScreen(ActionEvent event) throws IOException{
        Parent customerScreen = FXMLLoader.load(getClass().getResource("CustomerScreen.fxml"));
        Scene scene = new Scene(customerScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void showAddCustScreen(ActionEvent event) throws IOException{
        Parent addCustScreen = FXMLLoader.load(getClass().getResource("AddCustomerScreen.fxml"));
        Scene scene = new Scene(addCustScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void showUpdateCustScreen(ActionEvent event) throws IOException {
        Parent updateScreen = FXMLLoader.load(getClass().getResource("UpdateCustomerScreen.fxml"));            // methods to show different screens
        Scene scene = new Scene(updateScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    public void userLogout(ActionEvent event) throws IOException {
        Parent welcomeScreen = FXMLLoader.load(getClass().getResource("LogInController.fxml"));
        Scene scene = new Scene(welcomeScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void showFinalReportScreen(ActionEvent event) throws IOException{
        Parent testScreen = FXMLLoader.load(getClass().getResource("ReportScreen.fxml"));            // methods to show different screens
        Scene scene = new Scene(testScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void showCustomerRecordScreen(ActionEvent event) throws IOException{
        Parent recordScreen = FXMLLoader.load(getClass().getResource("CustomerRecordScreen.fxml"));
        Scene scene = new Scene(recordScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void showApptDetailScreen(ActionEvent event) throws IOException{
        Parent detailScreen = FXMLLoader.load(getClass().getResource("AppointmentRecordController.fxml"));
        Scene scene = new Scene(detailScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void showReportScreen(ActionEvent event) throws IOException{
        Parent reportScreen = FXMLLoader.load(getClass().getResource("ReportsController.fxml"));
        Scene scene = new Scene(reportScreen);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
