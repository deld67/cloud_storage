package ru.geekbrains.client.controller;

import ru.geekbrains.client.services.NetworkService;
import ru.geekbrains.common.IO.FileUtility;
import ru.geekbrains.common.property.Property;

import static ru.geekbrains.common.Command.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class ClientController {
    private final NetworkService networkService;
    private List<File> navigates = new CopyOnWriteArrayList<>();
    private boolean startStep = false;
    private String ClientLocalPath;
    private String ClientServerPath;

    public ClientController(String serverHost, int serverPort) {
        this.networkService = new NetworkService(serverHost, serverPort);
    }

    public void runApplication() throws IOException, InterruptedException {
        connectToServer();
        runTestProgram(); // позднее вместо определенных действий будем поднимать форму
    }

    private void runTestProgram() throws IOException, InterruptedException {
        System.out.println("[runTestProgram]: NavigateCommand  запросили список файлов из корня сервера");
        startStep = true;
        List<File> files = new LinkedList<>();
        files.add(new File(Property.getServerRootPath()));
        networkService.sendCommand(NavigateCommand(files));

        while (startStep){
            TimeUnit.SECONDS.sleep(1);
        }
        for (File n: navigates ) {
            if (!n.isDirectory()){
                System.out.println(n.getName()+" "+n.getPath());
                //читаем файлы с сервера и пишем на локал
                networkService.sendCommand(GetFileCommand(n));
                networkService.GetFile(new File(getClientLocalPath()+"/"+n.getName()));
                while (startStep){
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        }
        //теперь просто читаем наши локальные файлы и передаем их на сервер
        for(File f: new File(getClientLocalPath()).listFiles()){
            if (!f.isDirectory()) {
                System.out.println(f.getName() + " " + f.getPath()+" "+getClientServerPath());
                networkService.sendCommand(PutFileCommand(new File(getClientServerPath()+"/"+f.getName())));
                networkService.PutFile(f);
                while (startStep){
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        }


        System.out.println("[runTestProgram]: endCommand");
        networkService.sendCommand( endCommand() );

    }

    public List<File> getNavigates() {
        return navigates;
    }

    public void setNavigates(List<File> navigates) {
        this.navigates = navigates;
    }

    public boolean isStartStep() {
        return startStep;
    }

    public void setStartStep(boolean startStep) {
        this.startStep = startStep;
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

}
