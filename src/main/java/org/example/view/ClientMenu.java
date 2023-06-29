package org.example.view;

import org.example.model.Commands;
import org.example.model.GameMap;
import org.example.model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;

public class ClientMenu implements Runnable {
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private User user;
    Scanner scanner = new Scanner(System.in);
    public static void main (String[] args) {
        (new ClientMenu()).run();
    }
    @Override
    public void run () {
        System.out.println("Enter your username:");
        user = new User(scanner.nextLine());
        try {
            Socket socket = new Socket("localhost", 8080);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream.writeUTF(user.toJson());
            handleInput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void handleInput () throws IOException {
        String input;
        Matcher matcher;
        while (true) {
            System.out.println("********************\nEnter your command:\n");
            input = scanner.nextLine();
            if ((matcher = Commands.NEW_MAP.getMatcher(input)) != null) {
                user.maps.add(new GameMap(matcher.group("map"), user));
                continue;
            } else if ((matcher = Commands.SHARE_MAP.getMatcher(input)) != null) {
                GameMap sharingMap = user.getMapByName(matcher.group("map"));
                if (sharingMap != null) outputStream.writeUTF(sharingMap.toJson());
                else {
                    System.out.println("Invalid map name.");
                    continue;
                }
            } else if (Commands.USER_MAPS.getMatcher(input) != null) {
                for (GameMap map : user.maps) {
                    System.out.println(map.name);
                }
            } else if (Commands.SERVER_MAPS.getMatcher(input) != null) {
                outputStream.writeUTF("info");
                System.out.println(inputStream.readUTF());
            }
        }
    }
}
