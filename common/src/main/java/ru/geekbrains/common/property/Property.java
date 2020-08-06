package ru.geekbrains.common.property;

public class Property {
    private static final int CONNECTION_TIMEOUT = 120000;
    private static final int DEFAULT_SERVER_PORT = 8189;
    private static final String DEFAULT_SERVER_HOST = "localhost";
    private static final String SERVER_ROOT_PATH = "C://GeekBrains/cloud/server/client";
    private static final String CLIENTS_ROOT_PATH = "C://GeekBrains/cloud/client";
    private static final int MAX_CLIENTS = 3;
    private static final int BUFFER_SIZE = 1024;
    private static final String SERVER_TYPE = "NIO";
    private static final byte COMMAND_CHAR = (byte)'$';
    private static final String COMMAND_DIV = "#";

    public static int getConnectionTimeout() {
        return CONNECTION_TIMEOUT;
    }

    public static int getDefaultServerPort() {
        return DEFAULT_SERVER_PORT;
    }

    public static int getMaxClients() {
        return MAX_CLIENTS;
    }

    public static String getServerRootPath() {
        return SERVER_ROOT_PATH;
    }
    public static String getClientsRootPath() {
        return CLIENTS_ROOT_PATH;
    }

    public static String getDefaultServerHost() {
        return DEFAULT_SERVER_HOST;
    }

    public static int getBufferSize() {
        return BUFFER_SIZE;
    }

    public static String getServerType() {
        return SERVER_TYPE;
    }

    public static byte getCommandChar() {
        return COMMAND_CHAR;
    }

    public static String getCommandDiv() {
        return COMMAND_DIV;
    }
}
