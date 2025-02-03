package org.homeworksubmission.pages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ForgotPageController {
    @FXML
    public Button forgotBtn;
    public TextField userNameInput;

    @FXML
    public void onforgetBtnClicked() throws IOException {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Forgot Password");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to reset your password?");
            Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            //move back to login page after closing current window
            Stage stage = new Stage();
            Stage currentStage = (Stage) userNameInput.getScene().getWindow();
            currentStage.close();
            Scene scene = null ;
            FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("loginScreenView.fxml"));
            scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("Forgot Password");
            stage.setScene(scene);
            stage.show();
            }else {
                System.out.println("Password reset cancelled");
            }
    }
}
