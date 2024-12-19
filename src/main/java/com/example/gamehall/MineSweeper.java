package com.example.gamehall;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MineSweeper extends Application {
    private ArrayList<ArrayList<Integer>> distribution;
    private static final int width = 7;
    private static final int height = 7;
    private static int found_landmines = 0;
    private static int landmine_num = 0;
    private boolean game_over = false;
    private boolean game_started = false;
    private Stage primaryStage;
    private boolean win = false;

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        // 展示游戏规则
        showGameRules();
    }
    private void showGameRules() {
        // 创建规则对话框
        Alert ruleAlert = new Alert(AlertType.INFORMATION);
        ruleAlert.setTitle("游戏规则");
        ruleAlert.setHeaderText("欢迎来到马冬梅扫雷！");
        ruleAlert.setContentText("游戏规则：\n1. 左边矩阵中每一个值表示以之为九宫格的中心，该九宫格所包含的地雷数。\n"
                + "2. 如果点击了地雷格子，游戏结束。\n"
                + "3. 如果发现所有无地雷的格子，游戏胜利。\n"
                + "4. 点击右上角的暂停按钮来暂停游戏，继续或退出游戏。\n");
        ruleAlert.setHeight(300);
        ruleAlert.showAndWait();
        //展示规则之后就开始游戏
        startGame();
    }

    // 启动游戏！
    public void startGame() {
        if(game_started) return; //有可能是暂停之后再开始游戏，那就不需要重新启动

        game_started = true;
        // 地雷分布
        distribution = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            distribution.add(new ArrayList<>());
            for (int j = 0; j < width; j++) {
                double rand = Math.random();
                distribution.get(i).add(rand > 0.75 ? 1 : 0);   //大于0.75就设为1，表示这个地方有地雷
                landmine_num += distribution.get(i).get(j);
            }
        }

        GridPane gridL = new GridPane();
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                Button cellButton = new Button();
                cellButton.setText(String.valueOf(count_landmines(i,j)));
                // 设置按钮大小
                cellButton.setPrefSize(7, 7);
                cellButton.setStyle("-fx-padding: 10 11 10 11; -fx-background-color: #007ACC; -fx-text-fill: white;");
                cellButton.setDisable(true);
                gridL.add(cellButton, j, i);
            }
        }
        GridPane gridR = new GridPane();
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                Button cellButton = new Button();
                cellButton.setGraphic(getImageView("file:src/main/resources/icons/dogHead.png"));
                final int x = i, y = j;
                cellButton.setOnAction(e -> handleCellClick(x, y, cellButton));
                gridR.add(cellButton, j, i);
            }
        }

        System.out.println("landmine_num:" +  landmine_num);
        // 创建菜单
        MenuBar menuBar = createMenuBar();

        HBox gameLayout = new HBox(10);
        gameLayout.getChildren().addAll(gridL, gridR);
        VBox allLayout = new VBox(20);// 10为按钮之间的间距
        gameLayout.setAlignment(javafx.geometry.Pos.CENTER);
        allLayout.getChildren().addAll(menuBar, gameLayout);

        Scene gameScene = new Scene(allLayout, 535, 300);
        primaryStage.setTitle("马冬梅扫雷");
        primaryStage.getIcons().add(new Image("file:src/main/resources/icons/mineSweeper.png"));
        primaryStage.setScene(gameScene);
        primaryStage.show();
        game_started = true;
    }

    private static ImageView getImageView(String imagePath) {
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        return imageView;
    }
    //该函数用来计算以某一处为九宫格中心，该九宫格有多少地雷
    private int count_landmines(int x, int y) {
        int count = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < height && j >= 0 && j < width && distribution.get(i).get(j) == 1) {
                    count++;
                }
            }
        }
        return count;
    }

    private MenuBar createMenuBar() {
        Menu gameMenu = new Menu("马冬梅扫雷--菜单");

        MenuItem pauseButton = new MenuItem("暂停");
        pauseButton.setOnAction(e -> pauseGame());

        MenuItem exitButton = new MenuItem("退出");
        exitButton.setOnAction(e -> {
            Alert exitAlert = new Alert(AlertType.INFORMATION);
            exitAlert.setTitle("是否退出游戏");
            exitAlert.setHeaderText("游戏已暂停，请确定是否真的退出。");
            ButtonType confirmButtonType = new ButtonType("确定退出");
            ButtonType cancelButtonType = new ButtonType("取消");
            exitAlert.getButtonTypes().setAll(confirmButtonType, cancelButtonType);
            exitAlert.showAndWait().ifPresent(type -> {
                if(type == confirmButtonType){
                    primaryStage.close();
                }
            });
        });

        MenuItem restartButton = new MenuItem("重新开始");
        restartButton.setOnAction(e -> resetGame());

        MenuItem ruleButton = new MenuItem("游戏规则");
        ruleButton.setOnAction(e -> showGameRules());

        gameMenu.getItems().addAll(pauseButton, exitButton, restartButton, ruleButton);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(gameMenu);
        return menuBar;
    }

    private void pauseGame() {
        if (game_over) return;

        Alert pauseAlert = new Alert(AlertType.INFORMATION);
        pauseAlert.setTitle("游戏暂停");
        pauseAlert.setHeaderText("游戏已暂停，请确定继续游戏或退出游戏。");
        ButtonType continueButton = new ButtonType("继续游戏");
        ButtonType exitButton = new ButtonType("退出游戏");
        pauseAlert.getButtonTypes().setAll(continueButton, exitButton);
        pauseAlert.setContentText("请选择操作：");
        pauseAlert.showAndWait().ifPresent(response -> {
            if (response == continueButton) {
                // 继续游戏
            } else if (response == exitButton) {
                // 退出游戏
                primaryStage.close();
            }
        });
    }

    private void handleCellClick(int x, int y, Button cellButton){
        if(game_over) return;

        if(distribution.get(x).get(y) == 1){ // 踩到地雷
            cellButton.setGraphic(getImageView("file:src/main/resources/icons/landmine.png"));
            showGameOver("很遗憾你踩到地雷了！游戏结束！");
        }else{
            cellButton.setGraphic(getImageView("file:src/main/resources/icons/smile.png"));
            cellButton.setDisable(true);
            found_landmines++;
            System.out.println("found_landmines:" + found_landmines);
            Check_win();
        }
    }

    public void Check_win(){
        if(found_landmines == height * width - landmine_num){
            win = true;
            showGameOver("恭喜你！找到了所有无地雷的格子！");
        }
    }

    private void showGameOver(String message) {
        game_over = true;

        Alert gameOverAlert = new Alert(AlertType.INFORMATION);
        gameOverAlert.setTitle("游戏结束");
        gameOverAlert.setHeaderText(null);
        gameOverAlert.setContentText(message + "\n\n请选择接下来的操作：");
        gameOverAlert.setHeight(200);
        if(win){
            gameOverAlert.setGraphic(getImageView("file:src/main/resources/icons/cheer.png"));
        }else{
            gameOverAlert.setGraphic(getImageView("file:src/main/resources/icons/cry.png"));
        }
        ButtonType continueButton = new ButtonType("再来一轮");
        ButtonType exitButton = new ButtonType("不想玩了");
        gameOverAlert.getButtonTypes().setAll(continueButton, exitButton);
        gameOverAlert.showAndWait().ifPresent(response -> {
            if (response == continueButton) {
                resetGame();
            } else if (response == exitButton) {
                primaryStage.close();
            }
        });
    }

    private void resetGame(){
        game_over = false;
        found_landmines = 0;
        landmine_num = 0;
        game_started = false;
        win = false;
        startGame();
    }

    public static void main(String[] args) {
            launch(args);
    }
}
