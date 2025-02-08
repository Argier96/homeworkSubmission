package org.homeworksubmission.pages;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.homeworksubmission.database.*;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.util.Duration;
import javafx.stage.Stage;

import java.io.IOException;

import static org.homeworksubmission.database.userDatabase.checkLogin;
import static org.homeworksubmission.database.userDatabase.userExists;


public class LoginPageController {
    @FXML
    public TextField userNameInput;
    public PasswordField passwordInput;

    @FXML
    protected void onLoginButtonClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login");
        if (userNameInput.getText().isEmpty() || passwordInput.getText().isEmpty()) {
            alert.setHeaderText("Username and Password are Required");
            userNameInput.setText("");
            passwordInput.setText("");
            alert.showAndWait();
            //alert is closed after 5 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(5));
            delay.setOnFinished(event -> {

                });
            alert.close();
            delay.play();

        }else {
            String User = checkLogin(userNameInput.getText(),passwordInput.getText());

            //jump to new scene based on roles
            Stage stage = new Stage();

            switch (User) {
                case "student" -> {
                    //close current window & open new window
                    Stage currentStage = (Stage) userNameInput.getScene().getWindow();
                    currentStage.close();
                    Scene scene = null ;
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("studentPage.fxml"));
                    try {
                        scene = new Scene(fxmlLoader.load(), 1000, 1000);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle(User);
                    stage.setScene(scene);
                    stage.show();
                    break;
                }
                case "teacher" -> {
                    //close current window & open new window
                    Stage currentStage = (Stage) userNameInput.getScene().getWindow();
                    currentStage.close();
                    Scene scene = null ;
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("teacherPage.fxml"));
                    try {
                        scene = new Scene(fxmlLoader.load(), 1000, 1000);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle(User);
                    stage.setScene(scene);
                    stage.show();
                    break;
                }
                case "admin" -> {
                    //close current window & open new window
                    Stage currentStage = (Stage) userNameInput.getScene().getWindow();
                    currentStage.close();
                    Scene scene = null ;
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("adminPage.fxml"));
                    try {
                        scene = new Scene(fxmlLoader.load(), 1000, 1000);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle(User);
                    stage.setScene(scene);
                    stage.show();
                    break;
                }
                default -> {
                    alert.setHeaderText("Username or password incorrect");
                    alert.show();
                }
            }
            userNameInput.setText("");
            passwordInput.setText("");
        }
    }

    public void onForgetButtonClick() {
        // first, a dialog appears, and only when the correct username or email is entered, goes to reset the password
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Forgot Password??");
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        TextField userNameInput = new TextField();
        userNameInput.setPrefWidth(300);
        userNameInput.setPromptText("Enter your username or email");

        GridPane grid = new GridPane();
        // setting gridPane size
        grid.setPrefHeight(50);
        grid.setPrefWidth(500);
        grid.setHgap(20);
        grid.setVgap(20);
        grid.add(new Label("Enter Username or email:"), 0, 0);
        grid.add(userNameInput, 1, 0);
        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();

        if (dialog.getResult() == ButtonType.OK) {
            if (userNameInput.getText().isEmpty()) {
                dialog.setTitle("Username or email is Required!!!");
                dialog.showAndWait();
            } else {
                if (userExists(userNameInput.getText())) {
                    Stage stage = new Stage();

                    // Create the new scene after the dialog is closed and check if the input is valid
                    Scene scene = null;
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("resetPasswordView.fxml"));
                    try {
                        scene = new Scene(fxmlLoader.load(), 640, 400);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle("Forgot Password");
                    stage.setScene(scene);
                    stage.show();

                    // Close the current stage (Login Page) after opening the new one
                    Stage currentStage = (Stage) userNameInput.getScene().getWindow();
                    if (currentStage != null) {
                        currentStage.close();
                    }
                }
            }
        }
    }

}