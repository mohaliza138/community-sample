package org.example.model;

import com.google.gson.JsonSyntaxException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection implements Runnable {
    private final Socket socket;
    private final User user;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    
    public Connection (Socket socket, User user) throws IOException {
        this.socket = socket;
        this.user = user;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }
    
    @Override
    public void run () {
        while (true) {
            try {
                if (inputStream.available() != 0) {
                    String data = inputStream.readUTF();
                    //TODO: command handling here...
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private User getUserFromInputStream () throws IOException {
        while (true){
            try {
                String register = inputStream.readUTF();
            } catch (JsonSyntaxException ignored) {
            }
            //todo
        }
    }
}
