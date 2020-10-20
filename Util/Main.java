package Util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View_Controller/LogInScreen.fxml"));
        primaryStage.setTitle("Appointment Application");
        primaryStage.setScene(new Scene(root, 400, 330));
        primaryStage.show();
       // DBConnector.populateDBFinal();            // NOTE NOTE: this populates the database with cities, countries, users, etc. on initial launch. Will return error if database is already populated due to primary keys. Left commented out for ease of submission.
    }


    public static void main(String[] args) {
        launch(args);
    }
}
