import JavaFX.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.geekbrains.client.controller.ClientController;
import ru.geekbrains.common.property.Property;


public class Main extends Application {
    Controller controller;
    Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception{


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml")); // FXMLLoader.load(getClass().getResource("/views/main.xml.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        scene = new Scene(root);
        primaryStage.setTitle("Cloud storage");
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    @Override
    public void stop() throws Exception {
        super.stop();
        controller.getClientController().disconnectToServer();

    }

    public static void main(String[] args) {

        launch(args);

    }
}
