package client;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.fxml.FXMLLoader.load;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = load(getClass().getResource("/Main.fxml"));
        primaryStage.setTitle("BrainsChat Client");
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
