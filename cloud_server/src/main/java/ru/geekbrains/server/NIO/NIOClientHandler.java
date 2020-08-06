package ru.geekbrains.server.NIO;


import ru.geekbrains.common.NIO.FileUtility;
import ru.geekbrains.common.property.Property;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClientHandler {
    private  Iterator<SelectionKey> iterator;
    private Selector selector;
    public NIOClientHandler(Iterator<SelectionKey> iterator, Selector selector) {
        this.iterator = iterator;
        this.selector = selector;
    }

    public void run() throws IOException {
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();
            if (key.isAcceptable()) {
                acceptClient((ServerSocketChannel) key.channel());

            }
            if (key.isReadable()) {
                System.out.println( "read key" );
                ByteBuffer buffer = ByteBuffer.allocate( Property.getBufferSize() );
                int count = ((SocketChannel) key.channel()).read( buffer );
                if (count == -1) {
                    key.channel().close();
                    break;

                }
                buffer.flip();
                StringBuilder command = new StringBuilder();
                buffer.rewind();
                while (buffer.hasRemaining()) {
                    buffer.mark();
                    if ( buffer.get() ==  Property.getCommandChar()) {
                        command = readCommand(buffer);
                    }else{
                        buffer.reset();
                    }
                }

                if (!command.toString().isEmpty()) executeCommand(command,buffer, ((SocketChannel) key.channel()) );


            }
        }
    }

    private void executeCommand(StringBuilder command, ByteBuffer buffer, SocketChannel channel) throws IOException {
        StringBuilder listOfFiles = new StringBuilder();
        System.out.println(command);
        String[] str = command.toString().split(Property.getCommandDiv(), 3);
        System.out.println( str[0] );
        System.out.println( str[1] );
        if (str[0].equalsIgnoreCase(   "GetList" )) {
            FileUtility.getFileList(  listOfFiles, str[1] );
            if (listOfFiles.toString().isEmpty()) listOfFiles.append( "#" );
            System.out.println(listOfFiles);
            buffer.clear();
            buffer.flip();


            buffer = ByteBuffer.wrap( new String(listOfFiles).getBytes() );
            while(buffer.hasRemaining()) {
                channel.write( buffer );
            }
            System.out.println("write fileList");
        }else if (str[0].equalsIgnoreCase(   "getFile" )){
            long fileSize = FileUtility.getFileSize(str[1]);
            System.out.println("fileSize: "+fileSize);
            buffer.clear();
            buffer.flip();
            buffer= ByteBuffer.wrap( new String(""+fileSize).getBytes()  );
            while(buffer.hasRemaining()) {
                channel.write( buffer );
            }
            FileUtility.getFileFromServer(str[1],channel);
        }
    }

    private StringBuilder readCommand(ByteBuffer buffer) {
        StringBuilder command = new StringBuilder();
        while(true){
            buffer.mark();
            if ( buffer.get() ==  Property.getCommandChar()) break;
            buffer.reset();
            command.append((char) buffer.get());
        }
        return command;
    }

    private void acceptClient(ServerSocketChannel socketChannel) throws IOException {
        System.out.println( "client accepted" );
        SocketChannel channel = socketChannel.accept();
        channel.configureBlocking( false );
        channel.register( selector, SelectionKey.OP_READ );
    }
}
