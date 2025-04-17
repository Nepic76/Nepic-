package nepic;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/nepic"; // Change to your database name
    private static final String JDBC_USER = "root"; // Replace with your MySQL username
    private static final String JDBC_PASSWORD = "mandip123"; // Replace with your MySQL password

    @FXML
    public void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            if (password.equals(confirmPassword)) {
                try {
                    registerUser(username, email, password);
                    showAlert("Registration successful!", AlertType.INFORMATION);
                    MainApp.loadScene("login.fxml", "Nepic Music Player");
                } catch (SQLException e) {
                    showAlert("Registration failed: " + e.getMessage(), AlertType.ERROR);
                    e.printStackTrace();
                }
            } else {
                showAlert("Passwords do not match.", AlertType.ERROR);
            }
        } else {
            showAlert("Please enter all fields.", AlertType.ERROR);
        }
    }

    @FXML
    public void goToLogin() {
        MainApp.loadScene("login.fxml", "Nepic Music Player");
    }

    private void showAlert(String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Registration Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void registerUser(String username, String email, String password) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (username, email, password) VALUES (?, ?, ?)")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password); // Store hashed password here

            preparedStatement.executeUpdate();
        }
    }
}