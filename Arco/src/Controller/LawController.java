package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class LawController implements Initializable {

    @FXML
    protected void homeOnAction() {
        View.Main.changeScreen("Dashboard");
    }
    @FXML
    protected void calendarOnAction() {
        View.Main.changeScreen("Calendar");
    }
    @FXML
    protected void psychoOnAction() {
        View.Main.changeScreen("Psycho");
    }
    @FXML
    protected void profileOnAction() {
        View.Main.changeScreen("Profile");
    }
    @FXML
    protected void exitOnAction() {
        View.Main.changeScreen("SignIn");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
