package org.homeworksubmission.pages;


import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.homeworksubmission.database.user;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.homeworksubmission.database.userDatabase;

import java.util.List;

public class AdminPageController {
    @FXML
    public TableRow tableRow;
    @FXML
    private TableView<user> userTableView;
    @FXML
    private TableColumn<user, String> usernameColumn;

    @FXML
    private TableColumn<user, String> emailColumn;

    @FXML
    private TableColumn<user, String> roleColumn;

    @FXML
    private TableColumn<user, Void> actionColumn;  // actionColumn type is Void

    // Initialize the TableView with data
    @FXML
    public void initialize() {
        // Initialize the columns with properties
        usernameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUserName()));
        emailColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail()));
        roleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRoleAsString()));

        // Load data from the database and populate the table
        loadData();
    }
    private void loadData() {
        List<user> users = userDatabase.getUsers();
        ObservableList<user> data = FXCollections.observableArrayList(users);
        userTableView.setItems(data);

    }

}
