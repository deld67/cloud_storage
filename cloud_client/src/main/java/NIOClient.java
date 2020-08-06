import JavaFX.NIOController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class NIOClient extends Application {
    NIOController controller;
    Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("Client start");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sampleNIO.fxml"));
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
