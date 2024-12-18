module com.example.gamehall {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.gamehall to javafx.fxml;
    exports com.example.gamehall;
}