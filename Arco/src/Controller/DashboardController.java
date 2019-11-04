/*
 Pendencias:
    - Cada card deve receber as informações de cada evento e noticia por parametro;
    - Se existirem muitos cards, a pagina deve ser capaz de escrolar;
    - Cada card deve ter um link que redireciona para sua respectiva pagina na web;
 */

package Controller;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class DashboardController implements Initializable {

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

    @FXML
    protected void eventOnAction() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.correio24horas.com.br/noticia/nid/gilberto-gil-e-baianasystem-estreiam-os-encontros-tropicais/"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    protected void newsOnAction() {
        try {
            Desktop.getDesktop().browse(new URI("https://globoesporte.globo.com/futebol/mundial-de-clubes/noticia/ceo-do-liverpool-diz-que-recebeu-garantias-que-torcedores-lgbt-serao-bem-vindos-ao-catar.ghtml"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
