package org.homeworksubmission.pages;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    private TableColumn<user, Number> hashColumn;
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
    private Button signOutButton;

    // Define a custom button type for "Delete"
    private static final ButtonType DELETE_BUTTON = new ButtonType("Delete", ButtonBar.ButtonData.LEFT);



    @FXML
    public void initialize() {
        // Bind columns to user properties
        hashColumn.setCellValueFactory(cellData -> {
            int rowIndex = userTableView.getItems().indexOf(cellData.getValue());
            return new SimpleIntegerProperty(rowIndex+1);
        });
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoleAsString()));

        // Set up the action column with an "Edit" button
        actionColumn.setCellFactory(param -> {
            Button editButton = new Button("Edit");
            editButton.setStyle("-fx-background-color: transparent;" +
                    "-fx-text-fill: black;"+"-fx-border-width: 0 0 2px 0;"+ "-fx-border-color: black;");

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

        userTableView.setRowFactory(tv -> {
            TableRow<user> row = new TableRow<>();

            // Listen to selection changes
            row.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    row.setStyle("-fx-border-color: blue; -fx-background-color: blue");  // Set the background color when selected
                } else {
                    row.setStyle("");  // Reset to default when deselected
                }
            });
            return row;
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

        // Add the "Delete" button to the dialog
        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL, DELETE_BUTTON);

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
                    if(!firstName.isEmpty() && !lastName.isEmpty() && !username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                        // Add new user
                        userDatabase.addUser(firstName, lastName, username, password, email, roleValue);
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error!!");
                        alert.setContentText("Required Fields are empty");
                        alert.showAndWait();
                    }

                } else {
                    // Update existing user
                    userDatabase.updateUser(selectedUser.getUserName(), firstName, lastName, username,
                            password.isEmpty() ? null : password, email, roleValue);
                }
                loadData();
            } else if (dialogButton == DELETE_BUTTON) {
                if (selectedUser != null) {
                    // Delete the user
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Delete User");
                    if (selectedUser.getUserName() != null) {
                        if(userDatabase.deleteUser(selectedUser.getUserName())) {
                            alert.setHeaderText("User deleted");
                            alert.showAndWait();
                            loadData();
                        }

                    }

                    loadData();
                }
            }
            return null;
        });
        return dialog;
    }

    // Handle "Sign Out" button click
    @FXML
    private void onSignOutButtonClicked() throws IOException {
        // Get the current stage (window)
        Stage currentStage = (Stage) signOutButton.getScene().getWindow();

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

    @FXML
    private void onUserButtonClicked(){
        userTableView.setVisible(true);
        initialize();
    }
    @FXML
    private void  onHomeButtonClicked(){
        userTableView.setVisible(false);
    }

}
