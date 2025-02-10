package org.homeworksubmission.pages;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
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
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login");
        alert.setHeaderText("Error");

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
        //styling userName or EmailInput
        userNameOrEmailInput.setStyle(
            "-fx-background-color: transparent;"+
            "-fx-border-width: 0 0 2px 0;" +
            "-fx-focus-color: blue;"+
            "-fx-faint-focus-color: transparent;"+
            "-fx-padding: 5 0 3 0;"+
            "-fx-prompt-text-fill: gray;"+
            "-fx-text-fill: black;"+
            " -fx-border-color: transparent transparent deepskyblue;"

        );

        userNameOrEmailInput.setPrefWidth(300);
        userNameOrEmailInput.setPromptText("Enter your username or email");

        GridPane grid = new GridPane();
        grid.setPrefHeight(50);
        grid.setPrefWidth(500);
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setStyle("-fx-background-color: #ffffff;");
        grid.add(new Label("Enter Username or Email:  "), 0, 0);
        grid.add(userNameOrEmailInput, 1, 0);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.getDialogPane().setContent(grid);
        dialog.setTitle("Forget Password");
// Style the dialog buttons
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);

        okButton.setStyle(
                "-fx-background-color: deepskyblue;" + // blue background
                        "-fx-text-fill: white;" +           // white text
                        "-fx-font-size: 14px;" +            // font size
                        "-fx-padding: 8px 15px;" +          // padding for better button feel
                        "-fx-background-radius: 5px;"       // rounded button corners
        );
        cancelButton.setStyle(
                "-fx-background-color: #f8f9fa;" +  // light background
                        "-fx-text-fill: #495057;" +          // dark text color
                        "-fx-font-size: 14px;" +             // font size
                        "-fx-padding: 8px 15px;" +           // padding for better button feel
                        "-fx-border-radius: 5px;"            // rounded button corners
        );


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
        alert.setContentText(message);
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
            Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
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