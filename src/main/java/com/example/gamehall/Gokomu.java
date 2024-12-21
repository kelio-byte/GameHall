package com.example.gamehall;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Gokomu extends Application {

    private Integer chessPaneWidth = 900;
    private Integer chessPaneHeight = 900;
    private Integer chessPaneUnitWidth = 60;
    private Integer chessPaneUnitHeight = 60;
    private Integer startPosition = 60;

    private AnchorPane anchorPane;
    private Boolean  isBlack = false;
    private Boolean  isSuccess = false;

    private Integer[][]  data = new Integer[15][15];


    private String chessPaneLineColor = "#000000";
    private Stage primaryStage;

    private static int computerSide = Chess.BLACK;// 默认机器持黑
    private static int humanSide = Chess.WHITE;
    private static Board bd;// 棋盘，重要
    private static Brain br1;//人工智能
    private static boolean isGameOver = true;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showGameRules();
        run();
    }
    private void run(){
        this.anchorPane = new AnchorPane();
        Canvas canvas = new Canvas(chessPaneWidth,chessPaneHeight);
        anchorPane.getChildren().add(canvas);
        initData();

        this.drawChessPane(canvas);

        Scene scene = new Scene(anchorPane);
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                calculateMouseClickPosition(event.getSceneX(),event.getSceneY() );
            }
        });

        Button button = new Button("重新开始");
        button.setLayoutY(15);
        button.setLayoutX(60);
        anchorPane.getChildren().add(button);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isBlack = false;
                isSuccess = false;
                data = new Integer[15][15];
                initData();
                anchorPane.getChildren().removeAll(anchorPane.getChildren());
                anchorPane.getChildren().add(canvas);
                anchorPane.getChildren().add(button);
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("javafx版本简易五子棋");
        primaryStage.setWidth(960);
        primaryStage.setHeight(980);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    private void showGameRules() {
        // 创建规则对话框
        Alert ruleAlert = new Alert(Alert.AlertType.INFORMATION);
        ruleAlert.setTitle("游戏规则");
        ruleAlert.setHeaderText("欢迎来到雷神五子棋！");
        ruleAlert.setContentText("游戏规则：任意一方棋子连成五个即为胜利\n");
        ruleAlert.setHeight(300);
        ruleAlert.showAndWait();
        //展示规则之后就开始游戏
    }

    public void initData(){
        bd = new Board();
        bd.reset();
        br1 = new Brain(bd, 1, 1);
        bd.start();
        drawBlackChessUnit(anchorPane,8, 8);
    }

    //存储数据

    //展示胜利的弹框
    public void showSuccess(Stage primaryStage, String text ){

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(hBox);

        Label label = new Label();
        label.setText(text);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(Font.font(20));
        label.setTextFill(Paint.valueOf("#333"));
        hBox.getChildren().add(label);

        stage.setScene(scene);
        stage.setWidth(300);
        stage.setHeight(200);
        //获取primaryStage的屏幕位置
        primaryStage.getX();
        primaryStage.getY();
        stage.setX( primaryStage.getX()+primaryStage.getWidth()/2-stage.getWidth()/2 );
        stage.setY( primaryStage.getY()+primaryStage.getHeight()/2-stage.getHeight()/2 );
        stage.show();

    }


    //检测鼠标点击位置
    public void calculateMouseClickPosition( double x, double y ){

        if ( isSuccess ){
            return;
        }

        //有效点击位置计算
        Integer availableMinX = chessPaneUnitWidth/2;
        Integer availableMinY = chessPaneUnitHeight/2;
        Integer availableMaxX = chessPaneWidth + chessPaneUnitWidth/2;
        Integer availabelMaxY = chessPaneHeight + chessPaneUnitHeight/2;

        //判断是否在有效位置上
        if ( availableMinX <= x && x <= availableMaxX && availableMinY <= y && y <= availabelMaxY ){

            //计算出其所在行，所在列
            double translateX = x -30;
            double translateY = y -30;
            Integer i =  (int)Math.ceil(translateX/60+0.000001);
            Integer j =  (int)Math.ceil(translateY/60+0.000001);

            if ( !isBlack ){
                drawWhiteChessUnit(anchorPane, i, j);
                putChess(i-1,j-1);
                Robat();
            }
        }else {

            System.out.println("无效点击位置");
        }

    }
    public void Robat(){
        int[] bestStep = br1.findTreeBestStep();// 估值函数+搜索树AI
        drawBlackChessUnit(anchorPane,bestStep[0]+1,bestStep[1]+1);
        putChess(bestStep[0], bestStep[1]);
    }

    //计算是否胜利

    private boolean putChess(int x, int y) {
        if (bd.putChess(x, y)) {
            int winSide = bd.isGameOver();// 判断终局
            if (winSide > 0) {
                if (winSide == humanSide) {
                    isBlack = true;
                    isSuccess = true;
                    showSuccess(primaryStage, (isBlack?"黑色":"白色") + "获得胜利");
                    System.out.println("白方赢了！");
                } else if (winSide == computerSide) {
                    isBlack = true;
                    isSuccess = true;
                    showSuccess(primaryStage, (isBlack?"黑色":"白色") + "获得胜利");
                    System.out.println("黑方赢了！");
                } else {
                    isSuccess = true;
                    showSuccess(primaryStage, ( "平手"));
                }
                // 清除
                bd.reset();
                isGameOver = true;
                return false;
            }
            return true;
        }
        return false;
    }

    public void drawWhiteChessUnit( AnchorPane anchorPane, Integer i, Integer j ){

        Button button =  new Button();
        anchorPane.getChildren().add(button);
        button.setStyle("-fx-background-radius: 30; -fx-effect:dropshadow(three-pass-box, #72b9da, 8.0,0, 0, 0); ");
        drawChessUnit(button, i, j);
    }


    public void drawBlackChessUnit( AnchorPane anchorPane, Integer i, Integer j ){

        Button button =  new Button();
        anchorPane.getChildren().add(button);
        button.setStyle("-fx-background-color: #000;-fx-background-radius: 30; -fx-effect:dropshadow(three-pass-box, #72b9da, 8.0,0, 0, 0);  ");
        drawChessUnit(button, i, j);
    }

    public void drawChessUnit( Button button, Integer i, Integer j ) {

        double width = chessPaneUnitWidth/1.4;
        button.setFocusTraversable(false);
        button.setPrefSize(width, width);
        button.setLayoutX(i*chessPaneUnitWidth-width/2);
        button.setLayoutY(j*chessPaneUnitWidth-width/2);
    }



    public void drawChessPane( Canvas canvas ){

        GraphicsContext gc = canvas.getGraphicsContext2D();

        for ( int i=0; i<15; i++ ){
            gc.setStroke(Paint.valueOf(chessPaneLineColor));
            gc.setLineWidth(1);
            gc.strokeLine(startPosition, (i+1)*chessPaneUnitHeight, chessPaneWidth,(i+1)*chessPaneUnitHeight);
            gc.strokeLine((i+1)*chessPaneUnitWidth, startPosition, (i+1)*chessPaneUnitWidth,chessPaneHeight);
        }
    }


    public static void main(String[] args) {
//主方法设置
        launch(Gokomu.class,args);
    }
}
