package ru.geekbrains.server;

import ru.geekbrains.common.property.Property;
import ru.geekbrains.server.NIO.NIOServer;

import java.io.IOException;

public class ServerApp {

    public static void main(String[] args) throws IOException {
        if (Property.getServerType().equals( "NIO" ) ){
            new Thread(new NIOServer(Property.getDefaultServerPort())).start();
        } else {
            new NetworkServer( Property.getDefaultServerPort() ).start();
        }
    }
}
