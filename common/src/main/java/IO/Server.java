package IO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final static int SERVER_PORT = 8189;
    private final static int BUFFER_SIZE = 8192;
    DataInputStream is;
    DataOutputStream os;
    ServerSocket server;

    public Server() throws IOException {
        server = new ServerSocket(SERVER_PORT);
        Socket socket = server.accept();
        System.out.println("Client accepted");
        is = new DataInputStream( socket.getInputStream() );
        os = new DataOutputStream( socket.getOutputStream() );
        String filename = is.readUTF();
        System.out.println("filename:"+filename);
        File file = new File( "./common/server/"+filename );
        file.createNewFile();
        try(FileOutputStream os = new FileOutputStream( file )){
            byte [] buffer = new byte[BUFFER_SIZE];
            while(true){
                int readSize = is.read(buffer);
                if (readSize == -1) break; 
                os.write( buffer, 0, readSize );
            }
        }
        System.out.println("File uploaded!");
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }
}
