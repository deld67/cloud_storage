package ru.geekbrains.client.controller;


import javafx.scene.control.ListView;
import ru.geekbrains.common.NIO.FileUtility;
import ru.geekbrains.common.property.Property;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class NIOClientController {
    private InetSocketAddress nioAddress;
    private SocketChannel socketChannel;

    private String clientLocalPath;
    private String clientServerPath;



    public NIOClientController(String serverHost, int serverPort) {
        this.nioAddress = new InetSocketAddress( serverHost, serverPort);
    }

    public void runApplication() throws IOException {
        connectToServer();
    }


    private void connectToServer() throws IOException {
        this.socketChannel = SocketChannel.open(nioAddress);
        this.clientLocalPath = Paths.get( Property.getClientsRootPath()).toString();
        this.clientServerPath = Paths.get( Property.getServerRootPath()).toString();
    }

    public void sendCommand(String command, String path, long size) throws IOException {
        command = new String("$"+command+"#"+path+"#"+size+"$");
        byte[] byteCommand = new String(command).getBytes();
        ByteBuffer buffer = ByteBuffer.wrap( byteCommand );
        socketChannel.write( buffer );
    }

    public StringBuilder readAnswerList() throws IOException {
        System.out.println("readAnswerList");
        StringBuilder listOfFiles = new StringBuilder();
        ByteBuffer buffer = ByteBuffer.allocate(Property.getBufferSize());
        int bytesRead = socketChannel.read(buffer);
        System.out.println(bytesRead);

            buffer.flip();
            while(buffer.hasRemaining()){
                listOfFiles.append((char) buffer.get());
            }
            System.out.println(listOfFiles);
            buffer.clear();

        return listOfFiles;
    }

    public void disconnectToServer() throws IOException {
        socketChannel.close();
    }

    public String getClientLocalPath() {
        return clientLocalPath;
    }

    public String getClientServerPath() {
        return clientServerPath;
    }

    public void refreshListView(ListView<String> localView, String clientPath) throws IOException {
        FileUtility.getListView(  localView, clientPath );
    }

    public void refreshServerList(ListView<String> listView, StringBuilder answerList) {
        for (String s: answerList.toString().split( "#" )) {
            listView.getItems().add(s);
        }

    }

    public void getFromServer(Path fromPath, Path toPath) throws IOException {
        long fileSize;
        sendCommand( "getFile", fromPath.toString(), 0 );

        ByteBuffer buffer = ByteBuffer.allocate(Property.getBufferSize());
        int bytesRead = socketChannel.read(buffer);
        StringBuilder answer = new StringBuilder();
        buffer.flip();
        while(buffer.hasRemaining()){
            answer.append((char) buffer.get());
        }
        System.out.println(answer);
        fileSize = new Long( answer.toString() );
        buffer.clear();
        FileUtility.putFile(socketChannel, toPath.toString(), fileSize);
    }
}
