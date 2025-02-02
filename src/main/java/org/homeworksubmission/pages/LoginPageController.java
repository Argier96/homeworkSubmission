package org.homeworksubmission.pages;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.w3c.dom.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EventObject;

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
                alert.close();});
            delay.play();
        }else {
            alert.setHeaderText("Login Successful");
            alert.show();
            //alert is closed after 5 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(5));
            delay.setOnFinished(event -> {
                userNameInput.setText("");
                passwordInput.setText("");
                alert.close();});
            delay.play();
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