package com.example.gamehall;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class test extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建一个Label组件作为场景中的内容
        Label label = new Label("Hello, JavaFX!");

        // 创建一个Scene，并将Label添加到其中
        Scene scene = new Scene(label, 300, 250);

        // 设置Stage的图标
        primaryStage.getIcons().add(new Image("file:path/to/your/icon.png"));

        // 设置窗口标题
        primaryStage.setTitle("JavaFX Application");

        // 设置场景到Stage
        primaryStage.setScene(scene);

        // 显示Stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}