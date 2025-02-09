package org.homeworksubmission.pages;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.homeworksubmission.database.user;
import org.homeworksubmission.database.userDatabase;

import java.io.IOException;
import java.util.List;

public class AdminPageController {

    @FXML
    public TableView<user> userTableView;
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
    private TableColumn<user, Void> actionColumn;  // For edit buttons

    @FXML
    public Button addUserButton;
    @FXML
    public Button signOutButton;

    @FXML
    public void initialize() {
        // Bind columns to user properties
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoleAsString()));

        // Set up the action column with an "Edit" button
        actionColumn.setCellFactory(param -> {
            Button editButton = new Button("Edit");
            editButton.setOnAction(event -> {
                user selectedUser = userTableView.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    openEditUserDialog(selectedUser);
                }
            });
            return new TableCell<>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : editButton);
                }
            };
        });

        // Load data into the table
        loadData();
    }

    // Load user data from the database
    private void loadData() {
        List<user> users = userDatabase.getUsers();
        ObservableList<user> data = FXCollections.observableArrayList(users);
        userTableView.setItems(data);
    }

    // Handle "Add User" button click
    @FXML
    private void onUserAddButtonClicked() {
        Dialog<ButtonType> dialog = createUserDialog("Add User", null);
        dialog.showAndWait();
    }

    // Open the edit dialog for a selected user
    private void openEditUserDialog(user selectedUser) {
        Dialog<ButtonType> dialog = createUserDialog("Edit User", selectedUser);
        dialog.showAndWait();
    }

    // Create a dialog for adding or editing a user
    private Dialog<ButtonType> createUserDialog(String title, user selectedUser) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Create input fields
        TextField firstNameInput = new TextField(selectedUser != null ? selectedUser.getFirstName() : "");
        TextField lastNameInput = new TextField(selectedUser != null ? selectedUser.getLastName() : "");
        TextField usernameInput = new TextField(selectedUser != null ? selectedUser.getUserName() : "");
        TextField emailInput = new TextField(selectedUser != null ? selectedUser.getEmail() : "");
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("New Password (optional)");

        ComboBox<String> roleInput = new ComboBox<>();
        roleInput.getItems().addAll("Teacher", "Student");
        roleInput.setValue(selectedUser != null ? selectedUser.getRoleAsString() : "Student");

        // Add fields to a grid layout
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

        // Handle dialog result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String firstName = firstNameInput.getText();
                String lastName = lastNameInput.getText();
                String username = usernameInput.getText();
                String email = emailInput.getText();
                String password = passwordInput.getText();
                String role = roleInput.getValue();

                int roleValue = "Teacher".equals(role) ? 2 : 3; // Convert role to numeric value

                if (selectedUser == null) {
                    // Add new user
                    userDatabase.addUser(firstName, lastName, username, password, email, roleValue);
                } else {
                    // Update existing user
                    userDatabase.updateUser(selectedUser.getUserName(), firstName, lastName, username,
                            password.isEmpty() ? null : password, email, roleValue);
                }

                loadData(); // Refresh the table
            }
            return null;
        });

        return dialog;
    }

    // Handle "Sign Out" button click
    @FXML
    public void onSignOutButtonClicked() {
        Stage currentStage = (Stage) signOutButton.getScene().getWindow();
        currentStage.close();

        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("loginScreenView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("Login");
            // Remove window decorations (minimize, maximize, close buttons)
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load login screen", e);
        }
    }
}