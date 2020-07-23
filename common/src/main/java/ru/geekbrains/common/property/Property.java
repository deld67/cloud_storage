package ru.geekbrains.common.property;

public class Property {
    private static final int CONNECTION_TIMEOUT = 120000;
    private static final int DEFAULT_SERVER_PORT = 8189;
    private static final String SERVER_ROOT_PATH = "C://GeekBrains/cloud/server/client";
    private static final String CLIENTS_ROOT_PATH = "C://GeekBrains/cloud/client";
    private static final int MAX_CLIENTS = 3;

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
}
