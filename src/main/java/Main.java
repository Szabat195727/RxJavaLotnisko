import apiService.AirportInfoService;
import io.reactivex.Observable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Flight;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by krystian on 07.06.17.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Airport.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("Airport INFO");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
