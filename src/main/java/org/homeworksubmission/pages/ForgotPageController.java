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
    public TextField emailInput;

    @FXML
    public void onForgetBtnClicked() throws IOException {
        String email = emailInput.getText();
    }

    public void sendEmail(String email) {

    }
}
