package ru.geekbrains.client;

import ru.geekbrains.client.controller.ClientController;
import ru.geekbrains.common.property.Property;

import java.io.IOException;

public class NetworkClient {
    public static void main(String[] args) {
        try {
            ClientController clientController = new ClientController("localhost", Property.getDefaultServerPort() );
            clientController.runApplication();
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to connect to server! Please, check you network settings");
        }
    }
}
