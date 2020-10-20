package View_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static View_Controller.AppointmentController.selectedAppointment;

public class AppointmentRecordController implements Initializable {

   @FXML public TextField descTextField;
    @FXML public TextField nameTextField;
    @FXML public TextField apptTypeTextField;
    @FXML public TextField titleTextField;
    @FXML public TextField urlTextField;
    @FXML public TextField dateTextField;
    @FXML public TextField startTextField;
    @FXML public TextField endTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleTextField.setText(selectedAppointment.getTitle());
        nameTextField.setText(CustomerController.getNameByCustId(selectedAppointment.getCustomerId()));
        descTextField.setText(selectedAppointment.getDescription());
        apptTypeTextField.setText(selectedAppointment.getType());
        urlTextField.setText(selectedAppointment.getUrl());
        dateTextField.setText(selectedAppointment.getStart().substring(0,7));
        startTextField.setText(selectedAppointment.getStart().substring(8,15));
        endTextField.setText(selectedAppointment.getEnd().substring(8,15));

    }
    public void backBtn(ActionEvent event) throws IOException{
        ScreenController apptScreen = new ScreenController();
        apptScreen.showAppointmentScreen(event);
    }

    public void userLogout(ActionEvent event) throws IOException {
        ScreenController logout = new ScreenController();
        logout.userLogout(event);
    }


}
