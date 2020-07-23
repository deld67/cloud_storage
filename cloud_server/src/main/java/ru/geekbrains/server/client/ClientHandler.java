package ru.geekbrains.server.client;


import ru.geekbrains.common.Command;
import ru.geekbrains.common.IO.FileUtility;
import ru.geekbrains.common.command.ErrorCommand;
import ru.geekbrains.common.command.GetFileCommand;
import ru.geekbrains.common.command.NavigateCommand;
import ru.geekbrains.common.command.PutFileCommand;
import ru.geekbrains.server.NetworkServer;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {
    private final NetworkServer networkServer;
    private final Socket clientSocket;
    private final int clientId = 0;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(NetworkServer networkServer, Socket socket) {
        this.networkServer = networkServer;
        this.clientSocket = socket;
    }

    public void run() {
        doHandle(clientSocket);
    }

    private void doHandle(Socket socket) {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            new Thread(() -> {
                try {
                    readMessages();
                } catch (IOException e) {
                    System.out.println("Соединение с клиентом  было закрыто!");

                } finally {
                    closeConnection();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessages() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            switch (command.getType()) {
                case END:
                    System.out.println("Received 'END' command");
                    return;
                case ERROR:
                    System.out.println("Received 'Error' command");
                    ErrorCommand errorCommand = (ErrorCommand) command.getData();
                    System.out.println("Error message:" + errorCommand.getErrorMessage() );
                    networkServer.sendMessage("321", Command.errorCommand(errorCommand.getErrorMessage()));
                    break;
                case NAVIGATE:
                    System.out.println("Received 'NAVIGATE' command");
                    NavigateCommand navigateCommand = (NavigateCommand) command.getData();
                    networkServer.sendMessage("123", Command.NavigateCommand(networkServer.sendDirList(navigateCommand.getNavigates().get(0))));
                    break;
                case GET_FILE:
                    System.out.println("Received 'GET_FILE' command");
                    GetFileCommand getFileCommand = (GetFileCommand) command.getData();
                    System.out.println(getFileCommand.getFile().getPath()+" "+getFileCommand.getFile().getName());
                    FileUtility.sendFile(clientSocket, getFileCommand.getFile());
                    break;
                case PUT_FILE:
                    System.out.println("Received 'PUT_FILE' command");
                    PutFileCommand putFileCommand = (PutFileCommand) command.getData();
                    System.out.println(putFileCommand.getFile().getPath()+" "+putFileCommand.getFile().getName());
                    FileUtility.sendFile(clientSocket, putFileCommand.getFile());
                    break;
                default:
                    System.err.println("[ClientHandler]: Unknown type of command : " + command.getType());
            }
        }
    }


    private Command readCommand() throws IOException {
        try {
            return (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Unknown type of object from client!";
            System.err.println(errorMessage);
            e.printStackTrace();
            return null;
        }
    }

    public void sendMessage(Command command) throws IOException {
        out.writeObject(command);
    }

    public void closeConnection() {
        try {
            networkServer.delClientHandler(clientSocket);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
