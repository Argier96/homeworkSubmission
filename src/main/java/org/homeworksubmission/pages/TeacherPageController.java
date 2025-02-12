package org.homeworksubmission.pages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TeacherPageController {
    @FXML
    private Text userText;
    @FXML
    private Button signOutBtn;

    private  String userName;
    @FXML
    public void initialize() {
        userText.setText("welcome "+userName);
        signOutBtn.setOnAction(e -> {
            try {
                onSignOutBtnCLicked();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }
    public void setLoggedUserName(String loggedUserName) {
        this.userName = loggedUserName;
    }

    @FXML
    private void onSignOutBtnCLicked() throws IOException {
        // Get the current stage (window)
        Stage currentStage = (Stage) signOutBtn.getScene().getWindow();

        // Load the login screen FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginScreenView.fxml"));
        Parent root = fxmlLoader.load();

        // Create a new scene for the login screen
        Scene loginScene = new Scene(root, 640, 400);
        Stage loginStage = new Stage();
        loginStage.setTitle("Homework Submission");

        loginStage.initStyle(StageStyle.UNDECORATED);
        loginStage.setScene(loginScene);
        loginStage.show();
        currentStage.close();
    }
}
