package ru.geekbrains.client.controller;

import JavaFX.Controller;
import ru.geekbrains.client.services.NetworkService;

import static ru.geekbrains.common.Command.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientController {
    private final NetworkService networkService;
    private List<File> navigates = new CopyOnWriteArrayList<>();
    private String ClientLocalPath;
    private String ClientServerPath;
    private Controller controllerFX;

    public ClientController(String serverHost, int serverPort) {
        this.networkService = new NetworkService(serverHost, serverPort);
    }

    public void runApplication() throws IOException, InterruptedException {
        connectToServer();
        //runTestProgram(); // позднее вместо определенных действий будем поднимать форму
    }

    public List<File> getNavigates() {
        return navigates;
    }

    public void setNavigates(List<File> navigates) {
        this.navigates = navigates;
    }

    public void setClientLocalPath(String clientLocalPath) {
        ClientLocalPath = clientLocalPath;
    }

    public String getClientLocalPath() {
        return ClientLocalPath;
    }

    public String getClientServerPath() {
        return ClientServerPath;
    }

    public void setClientServerPath(String clientServerPath) {
        ClientServerPath = clientServerPath;
    }

    private void connectToServer() throws IOException {
        try {
            networkService.connect(this);
        } catch (IOException e) {
            System.err.println("Failed to establish server connection");
            throw e;
        }
    }
     public void disconnectToServer() throws IOException {
         System.out.println("[runTestProgram]: endCommand");
         networkService.sendCommand( endCommand() );
     }


    public void sendNavigateCommand(File dir) throws IOException {
        List<File> files = new LinkedList<>();
        files.add(dir);
        networkService.sendCommand(NavigateCommand(files));

    }
    public boolean networkServiceIsGetAnswer(){
        return networkService.isGetAnswer();
    }

    public void sendToServer(String srcfilename, String dstfilename) throws IOException {
        System.out.println("[sendToServer]: SRC: "+srcfilename);
        System.out.println("[sendToServer]: DST: "+dstfilename);
        networkService.sendCommand(PutFileCommand(new File(srcfilename), new File(dstfilename)));
        networkService.PutFile(new File(srcfilename));
    }

    public void GetFromServer(String srcfilename, String dstfilename) throws IOException {
        System.out.println("[sendToServer]: SRC: "+srcfilename);
        System.out.println("[sendToServer]: DST: "+dstfilename);
        networkService.sendCommand(GetFileCommand(new File(srcfilename), new File(dstfilename)));
        networkService.GetFile(new File(dstfilename), 0);
    }
}
