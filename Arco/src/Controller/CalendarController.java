/*
 Pendencias:
 - A choice box deve filtrar os cards entre eventos e consultas;
 - Cada card deve receber as informações de cada evento por parametro;
 */
package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class CalendarController implements Initializable {

    @FXML
    ChoiceBox chEvents;

    @FXML
    protected void homeOnAction() {
        View.Main.changeScreen("Dashboard");
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

    private void eventsBox() {
        List<String> choiceList = new ArrayList<String>();
        Iterator<String> it = choiceList.iterator();
        choiceList.add("Consultas");
        choiceList.add("Eventos");

        choiceList.stream().forEach((choice) -> {
            chEvents.getItems().add(choice);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        eventsBox();
        DropShadow ds = new DropShadow();
        ds.setOffsetY(4.0f);
        ds.setColor(Color.color(0.6f, 0.6f, 0.6f));
        chEvents.setEffect(ds);
    }

}
