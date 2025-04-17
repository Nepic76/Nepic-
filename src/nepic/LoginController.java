package nepic;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private javafx.scene.control.Label errorLabel;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/nepic";
    private static final String JDBC_USER = "root"; // Your database username
    private static final String JDBC_PASSWORD = "mandip123"; // Your database password

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean isValid = validateUser(username, password);

        if (isValid) {
            // Show a success message when login is successful
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText("Welcome back!");
            alert.setContentText("You are logged in successfully.");
            alert.showAndWait();

            // Load the next scene after login
            MainApp.loadScene("musicdashboard.fxml", "Nepic Music Dashboard");
        } else {
            // Show an error message if login fails
            errorLabel.setVisible(true);
        }
    }

    private boolean validateUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return password.equals(storedPassword); // Insecure, but works for now
            }

            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    public void goToRegister() {
        MainApp.loadScene("register.fxml", "Register - Nepic");
    }

    @FXML
    public void handleForgotPassword() {
        MainApp.loadScene("Forgotpassword.fxml", "Forgot Password");
    }
}
