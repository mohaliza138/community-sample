package org.example.model;

import com.google.gson.JsonSyntaxException;
import org.example.controller.ServerHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection extends Thread {
    private final Socket socket;
    private User user;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    
    public Connection (Socket socket) throws IOException {
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }
    
    @Override
    public void run () {
        try {
            user = ServerHandler.serverHandler.referenceToNewlyAddedUser(getUserFromInputStream());
            while (true) {
                if (inputStream.available() != 0) {
                    String data = inputStream.readUTF();
                    if (data.equals("info")) {
                        String result = "";
                        for (GameMap map : ServerHandler.serverHandler.getMaps()) {
                            result += map.name;
                            result += "\n";
                        }
                        outputStream.writeUTF(result);
                    } else {
                        //todo: check map existence
                        outputStream.writeUTF(ServerHandler.serverHandler.addNewMap(GameMap.jsonToGameMap(data)).text);
                    }//TODO: command handling here...
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private User getUserFromInputStream () throws IOException {
        while (true) {
            try {
                String register = inputStream.readUTF();
                return User.jsonToUser(register);
            } catch (JsonSyntaxException ignored) {
            }
        }
    }
}
