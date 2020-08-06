package ru.geekbrains.common.NIO;

import javafx.scene.control.ListView;
import ru.geekbrains.common.property.Property;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class FileUtility {

    public static void createFile(String fileName) throws IOException {
        Path path = Paths.get( fileName );
        if (!Files.exists( path )) {
            Files.createFile( path );
        }
    }

    public static void createDirectory(String dirName) throws IOException {
        Path path = Paths.get( dirName );
        if (!Files.exists( path )) {
            Files.createDirectory( path );
        }
    }


    public static void getListView(ListView<String> localView, String dir) throws IOException {
        Path clPath = Paths.get( dir );
        Files.walkFileTree( Paths.get( dir ), new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                String fileString = file.toAbsolutePath().toString();
                System.out.println( "pathString = " + fileString + " " + clPath.toString() + "\\" + file.getFileName() );
                if (fileString.equals( clPath.toString() + "\\" + file.getFileName() )) {
                    localView.getItems().add( file.getFileName().toString() );
                }

                return FileVisitResult.CONTINUE;
            }
        } );

    }

    public static void getFileList(StringBuilder listOfFiles, String dir) throws IOException {
        Path clPath = Paths.get( dir );
        Files.walkFileTree( Paths.get( dir ), new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                String fileString = file.toAbsolutePath().toString();
                System.out.println( "pathString = " + fileString + " " + clPath.toString() + "\\" + file.getFileName() );
                if (fileString.equals( clPath.toString() + "\\" + file.getFileName() )) {
                    listOfFiles.append( file.getFileName().toString() + "#" );
                }

                return FileVisitResult.CONTINUE;
            }
        } );
    }

    public static Long getFileSize(String filePath) {
        Path file = Paths.get( filePath );
        return file.toFile().length();
    }

    public static void getFileFromServer(String filePath, SocketChannel channel) throws IOException {
        RandomAccessFile file = new RandomAccessFile( filePath, "r" );
        FileChannel inChannel = file.getChannel();
        ByteBuffer buf = ByteBuffer.allocate( Property.getBufferSize() );
        buf.clear();
        int bytesRead = inChannel.read( buf );
        while (buf.hasRemaining()) {
            channel.write( buf );
        }
        inChannel.close();


    }

    public static void putFile(SocketChannel socketChannel, String filePath, long fileSize) throws IOException {
        long offset = 0;
        System.out.println("start putFile");
        System.out.println("filePath:"+filePath);
        System.out.println("fileSize"+fileSize);

        createFile(filePath);
        System.out.println("createFile");
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open( Paths.get( filePath ), StandardOpenOption.WRITE );
        System.out.println("open fileChannel ");
        while (true) {
            System.out.println(offset +"=="+ fileSize);
            if (offset == fileSize) {
                break;
            }
            System.out.println("receiveByteArray");
            byte[] response = receiveByteArray( socketChannel );
            int bytesRead = offset + response.length >= fileSize ? (int) (fileSize - offset) : response.length;
            System.out.println("fileChannel.write");
            fileChannel.write( ByteBuffer.wrap( Arrays.copyOfRange( response, 0, bytesRead ) ), offset );
            offset += bytesRead;
            System.out.println( socketChannel.socket().getRemoteSocketAddress() + " >>> " + bytesRead + " bytes" );

        }
        fileChannel.close();

    }

    private static byte[] receiveByteArray(SocketChannel server) throws IOException {
        ByteBuffer readBuffer = ByteBuffer.allocate( Property.getBufferSize() ); //size = 1460
        server.read( readBuffer ); //label 1
        readBuffer.flip();
        return readBuffer.array();

    }
}


