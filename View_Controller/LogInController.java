package View_Controller;

import Model.User;
import Util.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML
    public Button enterButton;

    @FXML public TextField LogInPasswordField;

    @FXML public TextField LogInUsernameField;

    @FXML
    public ChoiceBox cityChoiceBox;
    @FXML
    public Label usernameLabel;
    @FXML
    public Label passwordLabel;
    @FXML
    public Label welcomeLabel;
    @FXML
    public Label userPassLabel;
    Locale userLocale;
    ResourceBundle rb;
    public static User currentUser = new User();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userLocale = Locale.getDefault();
        this.rb = ResourceBundle.getBundle("LoginFields", this.userLocale); // gets user Locale to handle login messages in two different languages.

        usernameLabel.setText(this.rb.getString("username"));
        passwordLabel.setText(this.rb.getString("password"));
        enterButton.setText(this.rb.getString("enter"));
        welcomeLabel.setText(this.rb.getString("welcome"));
        userPassLabel.setText(this.rb.getString("userPass"));

    }

    public void attemptLogin(ActionEvent event) throws Exception {  // logUser() logs user in logger.txt, logInUser attempts login.
        logUser();
        logInUser(event);

    }
    public String getLogInUsername() {
        String username = LogInUsernameField.getText();
        return username;
    }

    public String getLogInPassword() {
        String password = LogInPasswordField.getText();
        return password;

    }


    public void logInUser(ActionEvent event) throws Exception {                     // handles login
        String username = getLogInUsername();
        String password = getLogInPassword();
        int userId = getUserId(username);
        try {                                                                               // F. entering an incorrect username or password, try/catch
            if (passwordCheck(userId, password)) {
                currentUser.setUserId(userId);
                currentUser.setUserName(username);
                ScreenController welcomeScreen = new ScreenController();                    // A. Determines user location and translates log-in/error control messages into two languages
                welcomeScreen.showWelcomeScreen(event);
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText(this.rb.getString("loginFailHeader"));
                a.setContentText(this.rb.getString("loginFailText"));
                a.showAndWait();
            }
        }catch (NullPointerException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Login Error");
            a.setContentText("Please select office location");
            a.showAndWait();
        }
    }

    public boolean passwordCheck(int userId, String password) throws Exception {                // checks if userId, username, password are valid
        Statement stmt = DBConnector.getConnection().createStatement();

        String sqlStmt = "SELECT password FROM user WHERE userId = '" + userId + "'";

        ResultSet result = stmt.executeQuery(sqlStmt);

        while (result.next()){
            if(result.getString("password").equals(password)){
                return true;
            }
        }
        return false;
    }

    public int getUserId(String username) throws Exception {
        int userId = -1;

        Statement stmt = DBConnector.getConnection().createStatement();

        String sqlStmt = "SELECT userId FROM user WHERE userName ='" + username + "'";

        ResultSet result = stmt.executeQuery(sqlStmt);

        while(result.next()){
            userId = result.getInt("userId");
        }
        return userId;
    }

    public String getDateTime() throws IOException {
        String currentDateTime;
        DateTimeFormatter datetime = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        currentDateTime = datetime.format(now);
        return currentDateTime;
    }

    public void logUser() throws Exception{                      // J. Logs user log in attempts in text filed titled "newLog.txt"
        String file = "newLog";
        BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
        out.append("Access Date: " + getDateTime() + " Username: " + getLogInUsername() + " Password: " + getLogInPassword() + "\n");
        out.close();
    }
}
