/*
Pendencias:
    - 
*/

package arcoiris;

import java.net.URL;

import java.util.ResourceBundle;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;


public class SignInController implements Initializable {
    
    @FXML
    TextField mail, password;
    
    @FXML
    protected void signInMouseReleased(){
        Main.changeScreen("SignUp");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(4.0f);
        ds.setColor(Color.color(0.6f, 0.6f, 0.6f));
        password.setEffect(ds);
        mail.setEffect(ds);
    }
}
