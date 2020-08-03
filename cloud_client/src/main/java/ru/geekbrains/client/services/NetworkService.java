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

    private ClientController clientController;

    private boolean isGetAnswer = false;

    public NetworkService(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect(ClientController controller) throws IOException {
        this.clientController = controller;
        socket = new Socket(host, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        //Убрать после реализации авторизации
        this.clientController.setClientLocalPath(Property.getClientsRootPath()+"/"+1);
        this.clientController.setClientServerPath(Property.getServerRootPath()+"/"+1);
        System.out.println("---------------------------------");
        System.out.println(this.clientController.getClientLocalPath());
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
                            isGetAnswer = true;
                            break;
                        }
                        case NAVIGATE: {

                            System.out.println("[NetworkService]: NAVIGATE");
                            NavigateCommand navigateCommandData = (NavigateCommand) command.getData();
                            clientController.setNavigates(navigateCommandData.getNavigates());
                            System.out.println("Navigates().size():"+clientController.getNavigates().size());
                            isGetAnswer = true;
                            break;
                        }
                        default:
                            isGetAnswer = true;
                            System.err.println("[NetworkService]: Unknown type of command: " + command.getType());
                    }
                } catch (IOException e) {
                    System.out.println("Поток чтения был прерван!");
                    System.out.println(e.getMessage());
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
        isGetAnswer = false;
        out.writeObject(command);
        //out.reset();
    }

    public void PutFile(File f) throws IOException {
        FileUtility.sendFile(socket, f, f.length());
    }

    public boolean isGetAnswer() {
        return isGetAnswer;
    }

    public void GetFile(File f, long len) throws IOException {
        FileUtility.getFile(socket, f, len);
    }
}
