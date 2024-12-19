package com.example.gamehall;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;

public class OtherGames extends Application {

    private Stage primaryStage;
    private boolean game_over = false;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // 创建菜单栏
        MenuBar menuBar = createMenuBar();

        // 创建在线游戏按钮，点击后打开网页
        Button game1Button = createOnlineGameButton("4399", "https://www.4399.com/");
        Button game2Button = createOnlineGameButton("7k7k", "https://www.7k7k.com/");
        Button game3Button = createOnlineGameButton("MOOC", "https://www.icourse163.org/channel/2001.htm");

        // 创建布局并添加按钮
        VBox onlineLayout = new VBox(15);
        onlineLayout.getChildren().addAll(game1Button, game2Button, game3Button);
        onlineLayout.setAlignment(javafx.geometry.Pos.CENTER);

        // 创建主布局
        VBox allLayout = new VBox(20);
        allLayout.getChildren().addAll(menuBar, onlineLayout);
        allLayout.setAlignment(javafx.geometry.Pos.CENTER);

        // 创建场景并显示
        Scene scene = new Scene(allLayout, 300, 250);
        primaryStage.setTitle("在线游戏");
        primaryStage.getIcons().add(new Image("file:src/main/resources/icons/OnlineGames.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        Menu gameMenu = new Menu("在线游戏--菜单");

        MenuItem exitButton = new MenuItem("回到GameHall");
        exitButton.setOnAction(e -> showExitConfirmation());

        gameMenu.getItems().addAll(exitButton);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(gameMenu);
        return menuBar;
    }

    private Button createOnlineGameButton(String gameName, String url) {
        Button button = new Button(gameName);
        button.setFont(Font.font("Arial", 14));
        button.setStyle("-fx-padding: 10 20 10 20; -fx-background-color: #007ACC; -fx-text-fill: white;");
        button.setMaxSize(200, 50);
        button.setOnAction(e -> openWebsite(url));
        return button;
    }

    private void openWebsite(String url) {
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showExitConfirmation() {
        if (game_over) return;

        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("确认退出");
        exitAlert.setHeaderText("确定要退出在线游戏吗?");
        exitAlert.setContentText("如果退出，将关闭游戏窗口。");

        exitAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                primaryStage.close();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
