package View;

import Model.DAO.Conexao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;
    private static Scene signUpScene;
    private static Scene signInScene;
    private static Scene dashboardScene;
    private static Scene profileScene;
    private static Scene psychoScene;
    private static Scene calendarScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent fxmlSignIn = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        signInScene = new Scene(fxmlSignIn);
        Parent fxmlSignUp = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        signUpScene = new Scene(fxmlSignUp);
        Parent fxmlDashboard = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        dashboardScene = new Scene(fxmlDashboard);
        Parent fxmlProfile = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        profileScene = new Scene(fxmlProfile);
        Parent fxmlPsycho = FXMLLoader.load(getClass().getResource("Psycho.fxml"));
        psychoScene = new Scene(fxmlPsycho);
        Parent fxmlCalendar = FXMLLoader.load(getClass().getResource("Calendar.fxml"));
        calendarScene = new Scene(fxmlCalendar);

        stage.setScene(signInScene);
        stage.show();
    }

    public static void changeScreen(String src) {
        switch (src) {
            case "SignIn":
                stage.setScene(signInScene);
                break;
            case "SignUp":
                stage.setScene(signUpScene);
                break;
            case "Dashboard":
                stage.setScene(dashboardScene);
                break;
            case "Profile":
                stage.setScene(profileScene);
                break;
            case "Calendar":
                stage.setScene(calendarScene);
                break;
            case "Psycho":
                stage.setScene(psychoScene);
                break;
        }
    }

    public static void main(String[] args) {
        Conexao conexao = new Conexao();
        conexao.conectar();
        launch(args);
    }

}
