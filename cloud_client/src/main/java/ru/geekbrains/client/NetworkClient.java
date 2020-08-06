package ru.geekbrains.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.geekbrains.client.controller.ClientController;
import ru.geekbrains.common.property.Property;

import java.io.IOException;

public class NetworkClient extends Application  {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            ClientController clientController = new ClientController(Property.getDefaultServerHost(), Property.getDefaultServerPort() );
            clientController.runApplication();

        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to connect to server! Please, check you network settings");
        }
    }
}
