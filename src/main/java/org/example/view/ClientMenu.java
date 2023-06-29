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
        user = new User(scanner.nextLine());
        try {
            Socket socket = new Socket("localhost", 8080);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            handleInput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void handleInput () {
        String input;
        Matcher matcher;
        while (true) {
            input = scanner.nextLine();
            if ((matcher = Commands.NEW_MAP.getMatcher(input)) != null) {
                user.maps.add(new GameMap(matcher.group("map"), user));
            } else if ((matcher = Commands.SHARE_MAP.getMatcher(input)) != null) {
            
            }
        }
    }
}
