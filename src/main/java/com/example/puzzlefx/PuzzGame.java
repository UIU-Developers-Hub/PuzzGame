package com.example.puzzlefx;

import com.example.puzzlefx.controller.CongratulationController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class PuzzGame extends Application {
    public static Stage primaryStage;
    private static int gridSize = 3;
    private static int gridSquare=150;
    private List<Button> buttons = new ArrayList<>();
    private List<Button> mainButtons = new ArrayList<>();
    private Image originalImage,whitePic;
    private String[] imagePath;
    private Timeline timer;
    private int elapsedTimeInSeconds;
    String timeString;
    private int emptyIndex;
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage; //as we have a static Stage variable . we initialize the value as game stage of start method
        menuPage();

        Image icon = new Image(getClass().getResourceAsStream("/icon.png"));

        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Powered By return_3;"); //set the title of the stage
        primaryStage.initStyle(StageStyle.UNDECORATED); //create un decorated style
        primaryStage.show(); //by
    }
    public void menuPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/puzzlefx/menu.fxml"));
        Scene menuScene = new Scene(fxmlLoader.load());
        primaryStage.setScene(menuScene);
    }

    public static void main(String[] args) {
        launch();
    }
    public void showGame(){
        initializeTimer();
        startTimer();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(2);
        gridPane.setVgap(2);

        //LOAD the image
        imagePath= new String[]{"/elephant.png","/tiger.png","/dm.png","/dm2.png","/fn.png","/fn2.png","/httyd.png","/httyd2.png","/kfp.png"};
        Random rand= new Random();
         int index = rand.nextInt(imagePath.length);
        originalImage = new Image(imagePath[index]);
        whitePic = loadImage("/whitepic.png",gridSquare,gridSquare);
        List<Image> imageTiles = splitImage(originalImage);

        //Shuffle image tiles
        Button button;
        int i;
        //add image tiles to the buttons
        for (i = 0; i < imageTiles.size(); i++) {
            ImageView imageView = new ImageView(imageTiles.get(i));
            button = new Button();
            button.setGraphic(imageView);
            button.setPrefSize(gridSquare, gridSquare);
            buttons.add(button);
            mainButtons.add(button);
            gridPane.add(button, i % gridSize, i / gridSize);
        }

        button = new Button();
        ImageView whiteImageView = new ImageView(whitePic);
        button.setGraphic(whiteImageView);
        button.setPrefSize(gridSquare, gridSquare);
        buttons.add(button);
        mainButtons.add(button);
        gridPane.add(button, i % gridSize, i / gridSize);

        //Declare the empty tile;
        emptyIndex = buttons.size() - 1; // last number of index

        //Set action for each button
        for (Button nbutton : buttons) {
            nbutton.setOnAction(event -> {
                int buttonIndex = buttons.indexOf(nbutton);
                if (isValidMove(buttonIndex)) {
                    Collections.swap(buttons, buttonIndex, emptyIndex);
                    emptyIndex = buttonIndex;
                    updateGridPane(gridPane);
                    if (isPuzzleSolved()) {
                        showCongratulationMessage();
                        System.out.println("Congratulation you solved the puzzel.");
                    }
                }
            });
        }
        //create shuffle button
        Button backButton = new Button("Main Menu");
        backButton.setOnAction(event -> {
            try {
                menuPage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //create shuffle button
        Button shuffleButton = new Button("Shuffle");
        shuffleButton.setOnAction(event -> {

            buttons.remove(emptyIndex);
            Collections.shuffle(buttons);
            Button empButton = new Button();
            empButton.setGraphic(whiteImageView);
            empButton.setPrefSize(gridSquare, gridSquare);
            buttons.add(empButton);
            gridPane.add(empButton, buttons.size() - 1 % gridSize, buttons.size() - 1 / gridSize);
            emptyIndex = buttons.size() - 1;
            updateGridPane(gridPane);
        });

        // Create main picture button
        Button mainPictureButton = new Button("Main Picture");
        mainPictureButton.setOnAction(event -> {
            // Clear the buttons list to remove existing buttons
            buttons.clear();

            // Add the main buttons back to the buttons list
            buttons.addAll(mainButtons);

            // Remove the last button (empty button)
            buttons.remove(buttons.size() - 1);

            // Add a new empty button to the end
            Button empButton = new Button();
            empButton.setGraphic(whiteImageView);
            empButton.setPrefSize(gridSquare, gridSquare);
            buttons.add(empButton);

            // Add buttons to the gridPane
            updateGridPane(gridPane);

            // Update emptyIndex
            emptyIndex = buttons.size() - 1;

            // For demonstration purposes, let's just print a message
            System.out.println("Main Picture button clicked!");
        });


        //DESIGN PART
        for (Button nbutton : buttons) {
            nbutton.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 5px;");
            nbutton.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 10, 0, 0, 0);");
        }

        // Button hover animation
        for (Button nbutton : buttons) {
            nbutton.setOnMouseEntered(e -> {
                nbutton.setScaleX(1.05);
                nbutton.setScaleY(1.05);
            });
            nbutton.setOnMouseExited(e -> {
                nbutton.setScaleX(1);
                nbutton.setScaleY(1);
            });
        }

// Button click animation
        for (Button nbutton : buttons) {
            nbutton.setOnMousePressed(e -> {
                nbutton.setScaleX(0.95);
                nbutton.setScaleY(0.95);
            });
            nbutton.setOnMouseReleased(e -> {
                nbutton.setScaleX(1);
                nbutton.setScaleY(1);
            });
        }


// Inside the showGame() method in PuzzGame class
// Allow users to customize the size of the puzzle grid
         //gridSize = 3; // Default grid size
        ComboBox<Integer> gridSizeComboBox = new ComboBox<>();
        gridSizeComboBox.getItems().addAll(3, 4, 5); // Add options for different grid sizes
        gridSizeComboBox.setValue(gridSize); // Set default value
        gridSizeComboBox.setOnAction(event -> {
            gridSize = gridSizeComboBox.getValue(); // Update grid size when selected
            gridSquare=(int)(450/gridSize);
            new PuzzGame().showGame(); // Reset the game with the new grid size
        });



        StackPane root = new StackPane();
        root.getChildren().add(gridPane); // add vbox instead of shuffleButton
        root.getChildren().add(gridSizeComboBox); // Add the combobox to the root
// Set the preferred size of the combobox
        gridSizeComboBox.setPrefSize(100, 30); // Set width to 100 pixels and height to 30 pixels
// Position the combobox at the top-right corner of the StackPane
        StackPane.setAlignment(gridSizeComboBox, Pos.TOP_RIGHT);
        StackPane.setMargin(gridSizeComboBox, new Insets(50, 50, 0, 0)); // 50 pixels up and 50 pixels left
        // Set the preferred size of the buttons
        shuffleButton.setPrefSize(100, 30); // Set width to 100 pixels and height to 30 pixels
        mainPictureButton.setPrefSize(100, 30); // Set width to 100 pixels and height to 30 pixels
        backButton.setPrefSize(100, 30); // Set width to 100 pixels and height to 30 pixels
// Set the background color of the buttons
        shuffleButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Green background color
        mainPictureButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"); // Blue background color
        backButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"); // Blue background color

        // Position the shuffleButton and mainPictureButton at the bottom of the StackPane
        // Position the shuffleButton and mainPictureButton with an offset from the bottom-left and bottom-right corners
        StackPane.setAlignment(shuffleButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(shuffleButton, new Insets(0, 0, 50, 50)); // 20 pixels up and 20 pixels right

        StackPane.setAlignment(mainPictureButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(mainPictureButton, new Insets(0, 50, 50, 0)); // 20 pixels up and 20 pixels left

        StackPane.setAlignment(backButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(backButton, new Insets(0, 0, 0, 0)); // 20 pixels up and 20 pixels left

        root.getChildren().addAll(shuffleButton, mainPictureButton,backButton);
        root.setStyle("-fx-background-color: #f4f0f0;"); // Light gray background color

        // Set scene width and height
        Scene scene = new Scene(root, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Picture Puzzle Game");
        primaryStage.show();
    }

    private List<Image> splitImage(Image image) {
        int width = (int) image.getWidth() / gridSize;
        int height = (int) image.getHeight() / gridSize;

        List<Image> imageTiles = new ArrayList<Image>();
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                //if(y==gridSize-1&&x==gridSize-1) then it will be null. and emp
                if (y == gridSize - 1 && x == gridSize - 1) {
                    break;
                }
                ImageView imageView = new ImageView(image);
                imageView.setViewport(new Rectangle2D(x * width, y * height, width, height));
                imageView.setFitWidth(gridSquare);
                imageView.setFitHeight(gridSquare);
                imageTiles.add(imageView.snapshot(null, null));
            }
        }
        return imageTiles;
    }


    private boolean isValidMove(int buttonIndex) {
        int row = buttonIndex / gridSize;
        int col = buttonIndex % gridSize;
        int emptyRow = emptyIndex / gridSize;
        int emptyCol = emptyIndex % gridSize;
        return Math.abs(row - emptyRow) + Math.abs(col - emptyCol) == 1;
    }

    private void updateGridPane(GridPane gridPane) {
        gridPane.getChildren().clear();
        for (Button nbutton : buttons) {
            gridPane.add(nbutton, buttons.indexOf(nbutton) % gridSize, buttons.indexOf(nbutton) / gridSize);
        }
    }
    public boolean isPuzzleSolved() {
        int state = 0;
        for (int i = 0; i < buttons.size() - 1; i++) {
            ImageView imageView1 = (ImageView) buttons.get(i).getGraphic();
            ImageView imageView2 = (ImageView) mainButtons.get(i).getGraphic();
            if (!imageView1.getImage().equals(imageView2.getImage())) {
                state++;
            }
        }
        return state == 0;
    }
    private void showCongratulationMessage() {
        stopTimer();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/puzzlefx/congratulation.fxml"));
        Scene congratulationScene = null;
        try {
            congratulationScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CongratulationController congratulationController = fxmlLoader.getController();
        congratulationController.setTimeLabel(timeString); // Pass the time to the controller
        primaryStage.setScene(congratulationScene);
    }

    //Timer thing
    private void initializeTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                elapsedTimeInSeconds++;
                updateTimerDisplay();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
    }

    private void startTimer() {
        elapsedTimeInSeconds = 0;
        timer.play();
    }

    private void stopTimer() {
        timer.stop();
    }

    private void updateTimerDisplay() {
        int hours = elapsedTimeInSeconds / 3600;
        int minutes = (elapsedTimeInSeconds % 3600) / 60;
        int seconds = elapsedTimeInSeconds % 60;

         timeString = String.format("You Solved the puzzle within "+"%02d:%02d:%02d", hours, minutes, seconds);
    }

    public  Image loadImage(String imagePath, int width, int height) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)), width, height, true, true);
    }

    public static void exit(){
        Platform.exit();
    }
    public static void exitWithConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Exiting the game will end your current session.");

        // Add "Yes" and "No" buttons to the confirmation dialog
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Custom CSS for the dialog
        alert.getDialogPane().getStylesheets().add(
                PuzzGame.class.getResource("/com/example/puzzlefx/style.css").toExternalForm());
        alert.initStyle(StageStyle.TRANSPARENT); // Transparent style for a sleek look

        // Handle button actions
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                Platform.exit(); // Exit the application
            }
        });
    }
}
