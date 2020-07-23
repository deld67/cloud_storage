package ru.geekbrains.server;

import ru.geekbrains.common.property.Property;
import ru.geekbrains.server.NetworkServer;

public class ServerApp {

    public static void main(String[] args) {
        new NetworkServer( Property.getDefaultServerPort() ).start();
    }
}
