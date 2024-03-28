package com.example.puzzlefx.controller;



import com.example.puzzlefx.PuzzGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {
    public PuzzGame puzzGame;

    @FXML
    public Button btn_option;

    @FXML
    public Button btn_exit;

    @FXML
    public Button btn_start;

    @FXML
    public void option(ActionEvent event) {

    }

    @FXML
    public void exit(ActionEvent event) {
        PuzzGame.exitWithConfirmation();
    }

    @FXML
    public void start(ActionEvent event) {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/return_3/signUp.fxml"));
//        //loader.setController(new MenuController());
//        Parent root = loader.load();
//        Scene scene = new Scene(root);
//        primaryStage.setScene(scene);
       // TestPuzzGame.gameInstance.showGame();
        new PuzzGame().showGame();


    }

}
