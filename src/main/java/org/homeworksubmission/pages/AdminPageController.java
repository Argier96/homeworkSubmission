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
    private TableColumn<user, String> firstNameColumn;

    @FXML
    private TableColumn<user, String> lastNameColumn;

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
        firstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName()));
        usernameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUserName()));
        emailColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail()));
        roleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRoleAsString()));

        // Load data from the database and populate the table
        loadData();
        // Set up the action column with an "Edit" button
        actionColumn.setCellFactory(param -> {
            // Create a button for each row
            Button editButton = new Button("Edit");
            editButton.setOnAction(_ -> {
                user selectedUser = userTableView.getSelectionModel().getSelectedItem();
                System.out.println(selectedUser);
                if (selectedUser != null) {
                    System.out.println("Edit button clicked for user: " + selectedUser.getUserName()); // Add this line
                    openEditUserDialog(selectedUser);
                }
            });

            return new TableCell<user, Void>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        setGraphic(editButton); // Add the button to the table cell
                    } else {
                        setGraphic(null);
                    }
                }
            };
        });

    }
    private void loadData() {
        List<user> users = userDatabase.getUsers();
        ObservableList<user> data = FXCollections.observableArrayList(users);
        // Debugging: Print usernames to check if they are loaded correctly
        for (user u : users) {
            System.out.println("Loaded user: " + u.getUserName());
        }
        userTableView.setItems(data);

    }
    @FXML
    private void onUserAddButtonClicked() {
        // alert dialogue is created
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add User");
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Create input fields for the dialog
        TextField firstNameInput = new TextField();
        firstNameInput.setPromptText("First Name");

        TextField lastNameInput = new TextField();
        lastNameInput.setPromptText("Last Name");

        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Username");


        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");

        TextField emailInput = new TextField();
        emailInput.setPromptText("Email");

        ComboBox<String> roleInput = new ComboBox<>();
        roleInput.getItems().addAll( "Teacher", "Student");
        roleInput.setPromptText("Role");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("First Name"), 0, 0);
        grid.add(firstNameInput, 1, 0);
        grid.add(new Label("Last Name"), 0, 1);
        grid.add(lastNameInput, 1, 1);
        grid.add(new Label("Username:"), 0, 2);
        grid.add(usernameInput, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailInput, 1, 3);
        grid.add(new Label("Password:"), 0, 4);
        grid.add(passwordInput, 1, 4);
        grid.add(new Label("Role:"), 0, 5);
        grid.add(roleInput, 1, 5);
        dialog.getDialogPane().setContent(grid);
// Convert the result when the "Add" button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                // Process the user data (this is where you could save to a database or a list)
                String firstName = firstNameInput.getText();
                String lastName = lastNameInput.getText();
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
                // Add the user to the database
                boolean userAdded = userDatabase.addUser(firstName, lastName, username, password, email, roleValue);

                // If the user was successfully added, reload the data and update the table
                if (userAdded) {
                    loadData(); // Reload data to reflect the newly added user
                }
            }
            return null;
        });

        // Show the dialog and wait for the user interaction
        dialog.showAndWait();
    }

    private void openEditUserDialog(user selectedUser) {
        // Debugging: Check the selected user and their username
        System.out.println("Selected User: " + selectedUser);
        System.out.println("Username: " + selectedUser.getUserName());

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit User");
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Check if the username is null and handle it
        String userName = selectedUser.getUserName() != null ? selectedUser.getUserName() : "No Username";

        TextField firstNameInput = new TextField(selectedUser.getFirstName());
        TextField lastNameInput = new TextField(selectedUser.getLastName());
        TextField usernameInput = new TextField(userName);  // Set the username correctly
        TextField emailInput = new TextField(selectedUser.getEmail());
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("New Password (optional)");

        ComboBox<String> roleInput = new ComboBox<>();
        roleInput.getItems().addAll("Teacher", "Student");
        roleInput.setPromptText("Role");
        roleInput.setValue(selectedUser.getRoleAsString());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("First Name"), 0, 0);
        grid.add(firstNameInput, 1, 0);
        grid.add(new Label("Last Name"), 0, 1);
        grid.add(lastNameInput, 1, 1);
        grid.add(new Label("Username:"), 0, 2);
        grid.add(usernameInput, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailInput, 1, 3);
        grid.add(new Label("New Password (optional):"), 0, 4);
        grid.add(passwordInput, 1, 4);
        grid.add(new Label("Role:"), 0, 5);
        grid.add(roleInput, 1, 5);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String firstName = firstNameInput.getText();
                String lastName = lastNameInput.getText();
                String username = usernameInput.getText();
                String email = emailInput.getText();
                String password = passwordInput.getText();
                String role = roleInput.getValue();

                int roleValue = 0;
                if ("Teacher".equals(role)) {
                    roleValue = 2;
                } else if ("Student".equals(role)) {
                    roleValue = 3;
                }

                if (password.isEmpty()) {
                    password = null;
                }

                boolean userUpdated = userDatabase.updateUser(selectedUser.getUserName(), firstName, lastName, username, password, email, roleValue);
                if (userUpdated) {
                    loadData();  // Reload data to reflect the updated user
                }
            }
            return null;
        });

        dialog.showAndWait();
    }


}
