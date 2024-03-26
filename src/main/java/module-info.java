module com.example.puzzlefx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.puzzlefx to javafx.fxml;
    opens com.example.puzzlefx.controller to javafx.fxml;

    exports com.example.puzzlefx;
    exports com.example.puzzlefx.controller;
}