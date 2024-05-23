package test;


import java.io.IOException;
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

    private void runServer() {
        try{
            ServerSocket server = new ServerSocket(serverPort);
            server.setSoTimeout(1000);
            while (!stop) {
                try{
                    Socket client = server.accept();
                    try {
                        clientHandler.handleClient(client.getInputStream(), client.getOutputStream());
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                }
            }
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start() {
        new Thread(() -> runServer()).start();
    }

    public void close() {
        stop = true;
    }
}
