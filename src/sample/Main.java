package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller controller = null;

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Beat Namer");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        controller = Controller.getInstance();
        controller.setStage(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
