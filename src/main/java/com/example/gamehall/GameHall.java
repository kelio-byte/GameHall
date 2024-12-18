package com.example.gamehall;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameHall extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game Hall");

        // 创建游戏图标
        Button mineSweeper = createGameButton("马冬梅扫雷", "file:src/main/resources/icons/mineSweeper.png");
        Button gokomu = createGameButton("雷神五子棋", "file:src/main/resources/icons/gokomu.png");
        Button otherGames = createGameButton("刺激的外场", "file:src/main/resources/icons/otherGames.png");

        // 启动游戏
        mineSweeper.setOnMouseClicked(e -> launchGame("马冬梅扫雷"));
        gokomu.setOnMouseClicked(e -> launchGame("雷神五子棋"));
        otherGames.setOnMouseClicked(e -> launchGame("刺激的外场"));

        // 创建垂直布局并添加每一个水平游戏布局
        VBox rootLayout = new VBox(30);
        rootLayout.getChildren().addAll(mineSweeper, gokomu, otherGames);
        rootLayout.setAlignment(javafx.geometry.Pos.CENTER);

        // 添加背景图片
        Image bkg = new Image("file:src/main/resources/icons/background.png");
        BackgroundImage backgroundImage = new BackgroundImage(bkg, null, null, BackgroundPosition.CENTER, null);
        rootLayout.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(rootLayout, bkg.getWidth(), bkg.getWidth());
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:src/main/resources/icons/GameHall.png"));
        primaryStage.show();
    }
    private static ImageView getImageView(String image_path) {
        Image image = new Image(image_path);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        return imageView;
    }
    private Button createGameButton(String gameName, String iconPath) {
        Button button = new Button(gameName);
        ImageView iconView = getImageView(iconPath);
        button.setGraphic(iconView);

        button.setFont(Font.font("Arial", 14));
        button.setStyle("-fx-padding: 10 20 10 20; -fx-background-color: #007ACC; -fx-text-fill: white;");
        button.setMaxSize(200, 80);
        return button;
    }

    private void launchGame(String gameName) {
        switch (gameName) {
            case "马冬梅扫雷" -> startMineSweeper();
            case "雷神五子棋" -> System.out.println("雷神五子棋游戏启动中...");
            case "刺激的外场" -> System.out.println("刺激的外场游戏启动中...");
        }
    }

    private void startMineSweeper() {
        // 创建新的Stage来显示MineSweeper游戏
        MineSweeper mineSweeperGame = new MineSweeper();
        Stage newGameStage = new Stage();
        mineSweeperGame.start(newGameStage); // 启动新游戏
    }


    public static void main(String[] args) {
        launch(args);
    }
}
