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
    Scanner scanner = new Scanner(System.in);
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private User user;
    
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
            } else if ((matcher = Commands.SHARE_MAP.getMatcher(input)) != null) {
                GameMap sharingMap = user.getMapByName(matcher.group("map"));
                if (sharingMap != null) {
                    outputStream.writeUTF(sharingMap.toJson());
                    System.out.println(inputStream.readUTF());
                } else {
                    System.out.println("Invalid map name.");
                }
            } else if (Commands.USER_MAPS.getMatcher(input) != null) {
                for (GameMap map : user.maps) {
                    System.out.println(map.name);
                }
            } else if (Commands.SERVER_MAPS.getMatcher(input) != null) {
                outputStream.writeUTF("info");
                System.out.println(inputStream.readUTF());
            } else if ((matcher = Commands.CLONE_MAP.getMatcher(input)) != null) {
                if (user.getMapByName(matcher.group("map")) != null) {
                    System.out.println("You already have a map with this name!");
                }
                outputStream.writeUTF(input);
                String received = inputStream.readUTF();
                if (received.equals("fail")) {
                    System.out.println("No map with the name given!");
                    continue;
                }
                user.maps.add(GameMap.jsonToGameMap(received));
            }
        }
    }
}
