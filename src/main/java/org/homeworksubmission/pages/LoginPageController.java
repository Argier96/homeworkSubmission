package org.homeworksubmission.pages;
import org.homeworksubmission.database.*;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import javafx.stage.Stage;
import org.homeworksubmission.database.user;

import java.io.IOException;

import static org.homeworksubmission.database.userDatabase.checkLogin;


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
            alert.show();
            //alert is closed after 5 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(5));
            delay.setOnFinished(event -> {
                userNameInput.setText("");
                passwordInput.setText("");
                });
            alert.close();
            delay.play();

        }else {
            String User = checkLogin(userNameInput.getText(),passwordInput.getText());

            //jump to new scene based on roles
            Stage stage = new Stage();
            //close current window & open new window
            Stage currentStage = (Stage) userNameInput.getScene().getWindow();
            currentStage.close();
            Scene scene = null ;
            switch (User) {
                case "student" -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("studentPage.fxml"));
                    try {
                        scene = new Scene(fxmlLoader.load(), 1000, 1000);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle(User);
                    stage.setScene(scene);
                    stage.show();
                }
                case "teacher" -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("teacherPage.fxml"));
                    try {
                        scene = new Scene(fxmlLoader.load(), 1000, 1000);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle(User);
                    stage.setScene(scene);
                    stage.show();

                }
                case "admin" -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("adminPage.fxml"));
                    try {
                        scene = new Scene(fxmlLoader.load(), 1000, 1000);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    stage.setTitle(User);
                    stage.setScene(scene);
                    stage.show();

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

        Stage stage = new Stage();

        //close current window & open new window
        Stage currentStage = (Stage) userNameInput.getScene().getWindow();
        currentStage.close();

        Scene scene = null ;
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("forgotPasswordView.fxml"));
        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Forgot Password");
        stage.setScene(scene);
        stage.show();



    }
}