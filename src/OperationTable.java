import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class OperationTable extends Application {

    @Override
    public void start(Stage stage) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("TableView.fxml"));
            Scene scene = new Scene(root);
            // scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.setTitle("School Database");
            // stage.initStyle(StageStyle.TRANSPARENT);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
