import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        URL url = new File("D:/Workspace/GitHub/MOTTP/src/main/java/mainWindow.fxml").toURL();
        Parent root = FXMLLoader.load(url);
//        Parent root = FXMLLoader.load(getClass().getResource("D:/Workspace/GitHub/MOTTP/src/main/java/mainWindow.fxml"));//.getClassLoader()
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
