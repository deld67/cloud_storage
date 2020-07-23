package ru.geekbrains.client.services;

import ru.geekbrains.client.controller.ClientController;
import ru.geekbrains.common.Command;
import ru.geekbrains.common.IO.FileUtility;
import ru.geekbrains.common.command.ErrorCommand;
import ru.geekbrains.common.command.NavigateCommand;
import ru.geekbrains.common.property.Property;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkService {
    private final String host;
    private final int port;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private ClientController controller;

    public NetworkService(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect(ClientController controller) throws IOException {
        this.controller = controller;
        socket = new Socket(host, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        //Убрать после реализации авторизации
        this.controller.setClientLocalPath(Property.getClientsRootPath()+"/"+socket.getLocalPort());
        this.controller.setClientServerPath(Property.getServerRootPath()+"/"+socket.getLocalPort());
        System.out.println("---------------------------------");
        System.out.println(this.controller.getClientLocalPath());
        System.out.println("---------------------------------");
        runReadThread();
    }
    private void runReadThread() {
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("[runReadThread]: start ");
                    Command command = (Command) in.readObject();
                    System.out.println("[runReadThread]: readObject ");
                    System.out.println("[runReadThread]: command.getType() -> "+command.getType());
                    switch (command.getType()) {
                        case ERROR: {
                            ErrorCommand commandData = (ErrorCommand) command.getData();
                            System.out.println("[NetworkService]: ERROR ");
                            System.out.println(commandData.getErrorMessage());
                            //controller.showErrorMessage(commandData.getErrorMessage());
                            break;
                        }
                        case NAVIGATE: {
                            //UpdateUsersListCommand commandData = (UpdateUsersListCommand) command.getData();
                            //List<String> users = commandData.getUsers();
                          //  controller.updateUsersList(users);
                            System.out.println("[NetworkService]: NAVIGATE");
                            NavigateCommand navigateCommandData = (NavigateCommand) command.getData();
                            controller.setNavigates(navigateCommandData.getNavigates());
                            controller.setStartStep(false); // убрать после реализации интерфейса
                            break;
                        }
                        default:
                            System.err.println("[NetworkService]: Unknown type of command: " + command.getType());
                    }
                } catch (IOException e) {
                    System.out.println("Поток чтения был прерван!");
                    return;
                } catch (ClassNotFoundException e) {
                    System.out.println("[NetworkService]: ClassNotFoundException");
                    e.printStackTrace();
                }
                System.out.println("[runReadThread]: stop ");
            }

        }).start();

    }

    public void sendCommand(Command command) throws IOException {
        out.writeObject(command);
    }

    public void GetFile(File n) throws IOException {
        controller.setStartStep(true);
        FileUtility.getFile(socket, n);
        controller.setStartStep(false);
    }

    public void PutFile(File f) throws IOException {
        controller.setStartStep(true);
        FileUtility.sendFile(socket, f);
        controller.setStartStep(false);
    }
}
