package arcoiris;
//ENSINANDO GRAO
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    private static Stage stage;
    private static Scene signUpScene;
    private static Scene signInScene;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent fxmlSignIn = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        signInScene = new Scene(fxmlSignIn);
        Parent fxmlSignUp = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        signUpScene = new Scene(fxmlSignUp);
        
        stage.setScene(signInScene);
        stage.show();
    }
    
    public static void changeScreen(String src){
        switch(src){
            case "SignIn":
                stage.setScene(signInScene);
                break;
            case "SignUp":
                stage.setScene(signUpScene);
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
