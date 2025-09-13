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
import javafx.scene.text.Font;
import javafx.util.Duration;


/**
 * Controller for the main GUI window of the ChatterBox application.
 *
 * <p> This class handles:
 * <ul>
 *  <li>displaying user and bot dialog messages</li>
 *  <li>managing user input and sending it to {@code ChatterBox} backend</li>
 *  <li>graceful application exit when user types {@code bye}</li>
 * </ul>
 * <p>
 */
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

    /**
     * Initializes the main window.
     *
     * <p> Binds the scroll pane's vertical value to the height
     * of the dialog container so that the latest messages
     * are always visible.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        sendButton.setFont(Font.font("Verdana"));
        userInput.setFont(Font.font("Verdana"));
        String greeting = "Welcome! I'm ChatterBox.\nWhat can I do for you?";
        dialogContainer.getChildren().add(
                DialogBox.getChatterBoxDialog(greeting, botImage)
        );
    }

    /** Injects the Duke instance */
    public void setChatterBox(ChatterBox c) {
        chatterBox = c;
        chatterBox.initialize();
    }

    /**
     * Handles user input from the text field when the
     * send button is pressed.
     *
     * <p> This method:
     * <ul>
     *  <li>retrieves the user input and appends a '\n' to mimic CLI input</li>
     *  <li>queries {@code ChatterBox} for a response</li>
     *  <li>and adds the bot's response to the container</li>
     * </ul>
     *
     * If the user enters {@code bye}, a farewell message is displayed
     * and the application exits after a 2 second delay.
     * </p>
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().toLowerCase().trim() + '\n';

        if (input.equalsIgnoreCase("bye")) {
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
