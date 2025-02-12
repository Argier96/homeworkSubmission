package org.homeworksubmission.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import org.homeworksubmission.database.user;

public class StudentPageController {
    @FXML
    private TableView<user> userTable;
    @FXML
    public void showTable() {
        userTable.setVisible(true);
    }

    @FXML
    public void hideTable() {
        userTable.setVisible(false);
    }

}
