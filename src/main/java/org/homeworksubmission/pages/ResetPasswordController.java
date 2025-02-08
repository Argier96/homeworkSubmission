package org.homeworksubmission.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ResetPasswordController {
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
