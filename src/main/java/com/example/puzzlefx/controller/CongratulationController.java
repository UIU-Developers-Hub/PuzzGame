package com.example.puzzlefx.controller;

import com.example.puzzlefx.PuzzGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class CongratulationController {

    @FXML
    public Button btn_mainMenu;

    @FXML
    public Button btn_playAgain;

    @FXML
    public Label timeLabel; // Injected timeLabel

    @FXML
    void mainMenu(ActionEvent event) {
        try {
            new PuzzGame().menuPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void playAgain(ActionEvent event) {
        new PuzzGame().showGame();

    }

    // Setter method for timeLabel
    public void setTimeLabel(String text) {
        timeLabel.setText(text);
    }
}



