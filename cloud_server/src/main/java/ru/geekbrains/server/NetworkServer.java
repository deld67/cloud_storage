package ru.geekbrains.server;

import ru.geekbrains.common.Command;
import ru.geekbrains.common.IO.FileUtility;
import ru.geekbrains.common.utils.Utility;
import ru.geekbrains.common.property.Property;
import ru.geekbrains.server.client.ClientHandler;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NetworkServer {
    private final int port;

    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public NetworkServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер был успешно запущен на порту " + port);

            while (true) {
                System.out.println("Ожидание клиентского подключения...");
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout( Property.getConnectionTimeout());
                System.out.println("Клиент подлючился");
                createClientHandler(clientSocket);
                checkAndCreateUserFolder(clientSocket);

            }
        }catch (IOException e) {
            System.out.println("Ошибка при работе сервера");
            e.printStackTrace();
        } finally {
            //здесь позже будут прописаны отключения дополнительных сервисов, таких как авторизация, логирование и прочее
        }
    }

    private void checkAndCreateUserFolder(Socket clientSocket) throws IOException {
        System.out.println("[checkAndCreateUserFolder] clientLocalPath:"+Property.getClientsRootPath()+"/"+clientSocket.getPort());
        File file = new File(Property.getClientsRootPath()+"/"+clientSocket.getPort());
        if (!file.exists()) FileUtility.createDirectory(file.getPath());
        file = new File(Property.getServerRootPath()+"/"+clientSocket.getPort());
        System.out.println("[checkAndCreateUserFolder] clientServerPath:"+Property.getServerRootPath()+"/"+clientSocket.getPort());
        if (!file.exists()) FileUtility.createDirectory(file.getPath());
    }

    private void  createClientHandler(Socket clientSocket) {
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        ExecutorService executorService = Executors.newFixedThreadPool( 1 );
        executorService.execute( new Runnable() {
            @Override
            public void run() {
                clientHandler.run();
            }
        } );
        //это позже надо перенести в процесс авторизации
        clients.add(clientHandler);
        if (clients.size() >= Property.getMaxClients() ) clientHandler.closeConnection();

    }

    public List<File> sendDirList(File file) throws IOException {
        if (file.exists()) {
            return   Utility.getFilesList( file);
        }
        return   Utility.getFilesList( new File( Property.getServerRootPath() ));

    }
    public void sendMessage(String receiver, Command commandMessage) throws IOException {
        System.out.println("[NetworkServer] count ClientHandler: "+clients.size());
        for (ClientHandler client : clients) {

           // if (client.getNickname().equals(receiver)) {
                client.sendMessage(commandMessage);
                System.out.println("[NetworkServer] сообщение отправлено ");
            //    logger.log(Level.INFO,"Пользователю "+client.getNickname()+" направлено персональное сообщение");
                break;
           // }
        }

    }

    public void delClientHandler(Socket clientSocket){
        for (ClientHandler client: clients ) {
            if (client.getClientSocket() == clientSocket){
                clients.remove(client);
                return;
            }
        }
    }
}
