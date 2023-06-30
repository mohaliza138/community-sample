package org.example.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.model.Commands;
import org.example.model.GameMap;
import org.example.model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    private Timeline refreshTimeline;
    
    public static void main (String[] args) {
        launch(args);
//        (new ClientMenu()).run();
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
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    
    private void refreshSharedMaps () throws IOException {
        outputStream.writeUTF("info");
        String allMaps = inputStream.readUTF();
        String[] maps = allMaps.split("\n");
        sharedMaps.getChildren().clear();
        if (maps[0].isBlank()) return;
        for (String map : maps) {
            ImageView imageView = new ImageView(ClientMenu.class.getResource("/images/" + map + ".png").toString());
            imageView.setOnMouseClicked(mouseEvent -> {
                try {
                    outputStream.writeUTF("clone " + map);
                    String received = inputStream.readUTF();
                    if (received.equals("fail")) return;
                    GameMap gameMap = GameMap.jsonToGameMap(received);
                    if (user.getMapByName(gameMap.name) != null) return;
                    user.getCustomMaps().add(gameMap);
                    user.getReceivedMapsNames().add(gameMap.name);
                    refreshMyMaps();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            sharedMaps.getChildren().add(imageView);
        }
    }
    
    private void refreshMyMaps () {
        refreshShop();
        myMaps.getChildren().clear();
        for (GameMap map : user.getCustomMaps()) {
            ImageView imageView =
                    new ImageView(ClientMenu.class.getResource("/images/" + map.name + ".png").toString());
            if (user.getReceivedMapsNames().contains(map.name)) {
                ColorAdjust monochrome = new ColorAdjust();
                monochrome.setSaturation(-1.0);
                imageView.setEffect(monochrome);
            }
            imageView.setOnMouseClicked(mouseEvent -> {
                try {
                    outputStream.writeUTF(map.toJson());
                    if (inputStream.readUTF().equals("Success!")) refreshSharedMaps();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            myMaps.getChildren().add(imageView);
        }
    }
    
    private void refreshShop () {
        ArrayList<String> mapNames = new ArrayList<>(List.of(new String[] {"map1", "map2", "map4", "map5", "map6"}));
        for (GameMap map : user.getCustomMaps()) {
            mapNames.remove(map.name);
        }
        availableMaps.getChildren().clear();
        for (String map : mapNames) {
            ImageView imageView = new ImageView(ClientMenu.class.getResource("/images/" + map + ".png").toString());
            imageView.setOnMouseClicked(mouseEvent -> {
                user.getCustomMaps().add(new GameMap(map));
                refreshMyMaps();
            });
            availableMaps.getChildren().add(imageView);
        }
        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> {
            try {
                refreshSharedMaps();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        refreshTimeline.setCycleCount(-1);
        refreshTimeline.play();
    }
    
    @FXML
    public void initialize () {
        this.run();
        refreshMyMaps();
        try {
            refreshSharedMaps();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
