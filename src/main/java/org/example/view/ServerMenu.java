package org.example.view;

import org.example.model.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMenu implements Runnable {
    public static void main (String[] args) {
        (new ServerMenu()).run();
    }
    @Override
    public void run () {
        while (true) {
            try {
                ServerSocket serverSocket = new ServerSocket(8080);
                while (true) {
                    Socket socket = serverSocket.accept();
                    Connection connection = new Connection(socket);
                    connection.start();
                }
            } catch (IOException e) {
//                throw new RuntimeException(e);
            }
        }
    }
}
