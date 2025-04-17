package nepic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ForgotPasswordController {

    @FXML
    private TextField emailField; // Make sure this matches the fx:id in the FXML

    // Method to handle the Reset Password button click
    public void handleResetPassword(ActionEvent event) {
        String email = emailField.getText();

        if (email.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Email field cannot be empty.");
        } else {
            // Perform password reset logic here, e.g., send an email with a reset link
            showAlert(AlertType.INFORMATION, "Success", "Password reset link sent to: " + email);
        }
    }

    // Method to handle the Back to Login button click
    public void handleBackToLogin(ActionEvent event) {
        // Ensure you're passing both the FXML file and the scene title
        MainApp.loadScene("Login.fxml", "Nepic Music Player"); // Pass the title along with the FXML
    }

    // Helper method to show alerts
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
