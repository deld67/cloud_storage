package ru.geekbrains.common.IO;

import ru.geekbrains.common.property.Property;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FileUtility {
    public static void createFile(String fileName) throws IOException {
        //работа сервера на IO
        File file = new File( fileName );
        if (!file.exists()){
            file.createNewFile();
        }
    }

    public static void createDirectory(String dirName) throws IOException {
        //работа сервера на IO
        File file = new File( dirName );
        if (!file.exists()){
            file.mkdir();
        }

    }

    public static void move( File dir, File file, long fileSize) throws IOException {
        String path  = dir.getAbsolutePath() + "\\"+ file.getName();
        createFile( path );
        InputStream is = new FileInputStream( file );
        try(OutputStream os = new FileOutputStream( new File( path ) )) {
            byte[] buffer = new byte[8192];
            while (is.available() > 0) {
                int readBytes = is.read();
                System.out.println(readBytes);
                os.write(buffer, 0, readBytes  );
            }
        }

    }

    public static void sendFile(Socket socket, File file, long fileSize) throws IOException {
        InputStream fis = new FileInputStream( file );

        int count = (int) (fileSize / Property.getBufferSize()) / 10;
        DataOutputStream os = new DataOutputStream( socket.getOutputStream() );
            byte[] buffer = new byte[Property.getBufferSize()];
            if (fileSize < Property.getBufferSize()){
                int readBytes = fis.read(buffer);
                os.write(buffer, 0, readBytes);

            } else {
                for (long i = 0; i < count; i++) {
                    int readBytes = fis.read(buffer);
                    os.write(buffer, 0, readBytes);
                }
            }
    }

    public static void getFile(Socket socket, File file, long fileSize) throws IOException {

        OutputStream fos = new FileOutputStream( file );
        if (!file.exists()) file.createNewFile();
        int count = (int) (fileSize / Property.getBufferSize()) / 10;
        DataInputStream is = new DataInputStream( socket.getInputStream() );



        if (fileSize < Property.getBufferSize()){
            byte[] buffer = new byte[(int) fileSize];
            int readBytes = is.read(buffer);
            fos.write(buffer, 0, readBytes);

        } else {
            byte[] buffer = new byte[Property.getBufferSize()];
            for (long i = 0; i < count; i++) {
                int readBytes = is.read(buffer);
                fos.write(buffer, 0, readBytes);
            }
        }

        fos.close();
    }

}
