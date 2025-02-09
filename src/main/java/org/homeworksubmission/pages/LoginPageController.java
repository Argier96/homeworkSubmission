package org.homeworksubmission.pages;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.homeworksubmission.database.userDatabase;

import javax.swing.text.html.ImageView;
import java.io.IOException;

import static org.homeworksubmission.database.userDatabase.checkLogin;
import static org.homeworksubmission.database.userDatabase.userExists;

public class LoginPageController {

    @FXML
    public TextField userNameInput;
    @FXML
    public PasswordField passwordInput;

    //close application
    @FXML
    protected void closeApplication() {
        closeCurrentStage();
    }


    @FXML
    protected void onLoginButtonClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login");

        // Validate input fields
        if (userNameInput.getText().isEmpty() || passwordInput.getText().isEmpty()) {
            showAlert(alert, "Username and Password are Required");
            clearInputFields();
            return;
        }

        // Check user credentials
        String userRole = checkLogin(userNameInput.getText(), passwordInput.getText());

        // Redirect based on user role
        switch (userRole) {
            case "student" -> redirectToPage("studentPage.fxml", "Student Dashboard");
            case "teacher" -> redirectToPage("teacherPage.fxml", "Teacher Dashboard");
            case "admin" -> redirectToPage("adminPage.fxml", "Admin Dashboard");
            default -> showAlert(alert, "Username or password incorrect");
        }

        clearInputFields();
    }

    @FXML
    public void onForgetButtonClick() {
        // Create a dialog for password reset
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        TextField userNameOrEmailInput = new TextField();
        //styling userNameorEmailInput
        userNameOrEmailInput.setStyle(
                "-fx-background-color: transparent;"+
        "-fx-border-color: transparent transparent deepskyblue;"+
        "-fx-border-width: 0 0 2px 0;" +
        "-fx-focus-color: blue;"+
        "-fx-faint-focus-color: transparent;"+
        "-fx-padding: 5 0 3 0;"+
        "-fx-prompt-text-fill: gray;"+
        "-fx-text-fill: black;"
        );

        userNameOrEmailInput.setPrefWidth(300);
        userNameOrEmailInput.setPromptText("Enter your username or email");

        GridPane grid = new GridPane();
        grid.setPrefHeight(50);
        grid.setPrefWidth(500);
        grid.setHgap(20);
        grid.setVgap(20);
        grid.add(new Label("Enter Username or Email:"), 0, 0);
        grid.add(userNameOrEmailInput, 1, 0);
        dialog.getDialogPane().getStylesheets().add(LoginPageController.class.getResource("style.css").toExternalForm());

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();

        // Handle dialog result
        if (dialog.getResult() == ButtonType.OK) {
            if (userNameOrEmailInput.getText().isEmpty()) {
                dialog.setTitle("Username or email is Required!!!");
                dialog.showAndWait();
            } else if (userExists(userNameOrEmailInput.getText())) {
                redirectToPage("resetPasswordView.fxml", "Reset Password");
                closeCurrentStage();
            }
        }
    }

    // Helper method to show alerts
    private void showAlert(Alert alert, String message) {
        alert.setHeaderText(message);
        alert.getDialogPane().getStylesheets().add(LoginPageController.class.getResource("style.css").toExternalForm());
        alert.show();

        // Close alert after 5 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> alert.close());
        delay.play();
    }

    // Helper method to clear input fields
    private void clearInputFields() {
        userNameInput.clear();
        passwordInput.clear();
    }

    // Helper method to redirect to a new page
    private void redirectToPage(String fxmlFile, String title) {
        try {
            Stage currentStage = (Stage) userNameInput.getScene().getWindow();
            currentStage.close();

            Stage newStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
            newStage.setTitle(title);
            // Remove window decorations (minimize, maximize, close buttons)
            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML file: " + fxmlFile, e);
        }
    }

    // Helper method to close the current stage
    private void closeCurrentStage() {
        Stage currentStage = (Stage) userNameInput.getScene().getWindow();
        if (currentStage != null) {
            currentStage.close();
        }
    }


}