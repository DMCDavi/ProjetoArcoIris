/*
Pendencias:
    - Obrigar o usuario a preencher todos os campos;
    - Dar um limite de caracteres para os text fields;
    - Verificar se eh um email valido ou se ja esta cadastrado;
    - Verificar se as senhas sao iguais;
    - Salvar todos os dados em suas respectivas classes e num banco ao clicar no botao salvar;
    - Exibir mensagens de erro;
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
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class SignUpController implements Initializable {

    @FXML
    ChoiceBox chSex, chGender;
    
    @FXML
    TextField name, mail, password, confPassword;

    @FXML
    ImageView iconRainbowHeart;
    
    @FXML
    protected void signUpMouseReleased(){
        View.Main.changeScreen("SignIn");
    }

    private void sexBox() {
        List<String> sexList = new ArrayList<String>();
        Iterator<String> it = sexList.iterator();
        sexList.add("Masculino");
        sexList.add("Feminino");
        sexList.add("Intersexo");
        sexList.add("Outro");
        sexList.add("Prefiro não informar");
        sexList.stream().forEach((sex) -> {
            chSex.getItems().add(sex);
        });
    }
    
    private void genderBox() {
        List<String> genderList = new ArrayList<String>();
        Iterator<String> it = genderList.iterator();
        genderList.add("Heterossexual");
        genderList.add("Lésbica");
        genderList.add("Gay");
        genderList.add("Bissexual");
        genderList.add("Transgênero");
        genderList.add("Two-Spirit");
        genderList.add("Queer");
        genderList.add("Assexuado");
        genderList.add("Panssexual");
        genderList.add("Pangênero");
        genderList.add("Bigênero");
        genderList.add("Variante");
        genderList.add("Sem gênero");
        genderList.add("Não sei");
        genderList.add("Outro");
        genderList.add("Prefiro não informar");
        genderList.stream().forEach((gender) -> {
            chGender.getItems().add(gender);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sexBox();
        genderBox();
        DropShadow ds = new DropShadow();
        ds.setOffsetY(4.0f);
        ds.setColor(Color.color(0.6f, 0.6f, 0.6f));
        name.setEffect(ds);
        mail.setEffect(ds);
        chSex.setEffect(ds);
        chGender.setEffect(ds);
        password.setEffect(ds);
        confPassword.setEffect(ds);
    }
}
