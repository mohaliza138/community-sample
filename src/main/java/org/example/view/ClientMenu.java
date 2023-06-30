package org.example.view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.Commands;
import org.example.model.GameMap;
import org.example.model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;

public class ClientMenu extends Application implements Runnable {
    public VBox myMaps;
    public VBox sharedMaps;
    public VBox availableMaps;
    Scanner scanner = new Scanner(System.in);
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private User user;
    
    public static void main (String[] args) {
        launch(args);
//        (new ClientMenu()).run();
    }
    
    @Override
    public void run () {
        System.out.println("Enter your username:");
        user = new User(scanner.nextLine());
        user.maps.add(new GameMap("map1", user));
        user.maps.add(new GameMap("map4", user));
        user.maps.add(new GameMap("map6", user));
        try {
            Socket socket = new Socket("localhost", 8080);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream.writeUTF(user.toJson());
//            handleInput();
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
    
    @Override
    public void start (Stage stage) throws Exception {
        URL url = ClientMenu.class.getResource("/FXML/ClientCommunityMenu.fxml");
        Pane pane = FXMLLoader.load(url);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
    
    private void refresh() {
        //todo implement based on server
    }
    
    @FXML
    public void initialize () {
        this.run();
        for (GameMap map : user.maps) {
            ImageView imageView =
                    new ImageView(ClientMenu.class.getResource("/Images/" + map.name + ".png").toString());
            imageView.setOnMouseClicked(mouseEvent -> {
                try {
                    outputStream.writeUTF(map.toJson());
                    System.out.println(inputStream.readUTF());
//                    if (inputStream.readUTF().equals("Success!")) refresh();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            myMaps.getChildren().add(imageView);
        }
    }
}
