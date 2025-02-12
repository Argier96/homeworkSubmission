package org.homeworksubmission.pages;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class TeacherPageController {
    @FXML
    private Text userText;

    private  String userName;
    @FXML
    public void initialize() {
        userText.setText("welcome "+userName);


    }
    public void setLoggedUserName(String loggedUserName) {
        this.userName = loggedUserName;
    }

}
