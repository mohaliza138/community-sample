package org.example.view;

import org.example.model.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMenu implements Runnable {
    @Override
    public void run () {
        while (true) {
            try {
                ServerSocket serverSocket = new ServerSocket(8080);
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket);
                connection.run();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
