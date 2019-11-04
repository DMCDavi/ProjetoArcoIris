/*
Pendencias:
    - Conectar as ChoiceBoxes ao banco para pegar as informacoes;
    - Adicionar data e horario no calendario;
    - Nao deixar o usuario marcar numa data anterior;
    - Nao deixar o usuario marcar no mesmo dia e horario de outra consulta ja marcada;
    - Colocar os horarios na choice box;
*/

package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class PsychoController implements Initializable {

    @FXML
    ChoiceBox institution, psycho, hour, minute;
    
    @FXML
    DatePicker date;

    @FXML
    protected void homeOnAction() {
        View.Main.changeScreen("Dashboard");
    }

    @FXML
    protected void lawOnAction() {
        View.Main.changeScreen("Law");
    }

    @FXML
    protected void calendarOnAction() {
        View.Main.changeScreen("Calendar");
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
        DropShadow ds = new DropShadow();
        ds.setOffsetY(4.0f);
        ds.setColor(Color.color(0.6f, 0.6f, 0.6f));
        institution.setEffect(ds);
        psycho.setEffect(ds);
        hour.setEffect(ds);
        minute.setEffect(ds);
        date.setEffect(ds);

    }

}
