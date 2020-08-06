package ru.geekbrains.server.NIO;

import ru.geekbrains.common.NIO.FileUtility;
import ru.geekbrains.common.property.Property;
import ru.geekbrains.server.client.ClientHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer implements Runnable{
    private ServerSocketChannel server;
    private Selector selector;
    private int cnt_clients = 0;

    public NIOServer(int port) throws IOException {
        server = ServerSocketChannel.open();
        server.socket().bind( new InetSocketAddress( port ) );
        server.configureBlocking( false );
        selector = Selector.open();
        server.register( selector, SelectionKey.OP_ACCEPT );

    }


    @Override
    public void run() {

        try {
            System.out.println( "NIO server started" );
            while (server.isOpen()) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                NIOClientHandler nioClientHandler = new NIOClientHandler(iterator, selector);
                nioClientHandler.run();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
