package org.homeworksubmission.pages;


import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.homeworksubmission.database.user;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.homeworksubmission.database.userDatabase;

import java.util.List;

public class AdminPageController {
    @FXML
    public TableRow tableRow;
    public Button addUserButton;
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
    @FXML
    private void onUserAddButtonClicked() {
        // alert dialogue is created
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add User");
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Create input fields for the dialog
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Username");

        TextField emailInput = new TextField();
        emailInput.setPromptText("Email");

        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        ComboBox<String> roleInput = new ComboBox<>();
        roleInput.getItems().addAll( "Teacher", "Student");
        roleInput.setPromptText("Role");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameInput, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailInput, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordInput, 1, 2);
        grid.add(new Label("Role:"), 0, 3);
        grid.add(roleInput, 2, 3);
        dialog.getDialogPane().setContent(grid);
// Convert the result when the "Add" button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                // Process the user data (this is where you could save to a database or a list)
                String username = usernameInput.getText();
                String email = emailInput.getText();
                String password = passwordInput.getText();
                String role = roleInput.getValue();
                // Convert the role to numeric values
                int roleValue = 0;
                if ("Teacher".equals(role)) {
                    roleValue = 2;  // Teacher has value 2
                } else if ("Student".equals(role)) {
                    roleValue = 3;  // Student has value 3
                }
                userDatabase.addUser(username,password,email,roleValue);
            }
            return null;
        });

        // Show the dialog and wait for the user interaction
        dialog.showAndWait();
    }

}
