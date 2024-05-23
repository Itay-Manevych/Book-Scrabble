package test;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {
    int serverPort;
    ClientHandler clientHandler;
    private  volatile boolean stop;
    public MyServer(int serverPort, ClientHandler clientHandler) {
        this.serverPort = serverPort;
        this.clientHandler = clientHandler;
        stop = false;
    }

    private void runServerLogic() {
        try{
            ServerSocket server = new ServerSocket(serverPort);
            server.setSoTimeout(1000);
            while (!stop) {
                try{
                    Socket client = server.accept();
                    clientHandler.handleClient(client.getInputStream(), client.getOutputStream());
                    clientHandler.close();
                    client.close();
                } catch (SocketTimeoutException e) {}
            }
            server.close();
        } catch (IOException e) {}
    }
    public void start() {
        new Thread(this::runServerLogic).start();
    }

    public void close() {
        stop = true;
    }
}
