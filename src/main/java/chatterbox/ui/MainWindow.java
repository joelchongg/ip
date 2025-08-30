package chatterbox.ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private ChatterBox chatterBox;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image botImage = new Image(this.getClass().getResourceAsStream("/images/chatterbox.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Duke instance */
    public void setChatterBox(ChatterBox c) {
        chatterBox = c;
        chatterBox.initialize();
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText() + '\n';

        if (input.trim().equalsIgnoreCase("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            
            dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getChatterBoxDialog("Good day! Closing in 2 seconds.", botImage)
            );

            delay.setOnFinished(event -> Platform.exit());
            delay.play();

            return;
        }

        String response = chatterBox.run(input);

        dialogContainer.getChildren().addAll(
            DialogBox.getUserDialog(input, userImage),
            DialogBox.getChatterBoxDialog(response, botImage)
        );

        userInput.clear();
    }
}
