package nepic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

public class MainApp extends Application {

    private static Stage primaryStage;

    /**
     * The entry point for launching the application.
     * 
     * @param stage The primary stage for the application.
     */
    @Override
    public void start(Stage stage) {
        try {
            primaryStage = stage;
            loadScene("login.fxml", "Nepic Music Player");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Application Startup Error", "Failed to start the application.", e.getMessage(), AlertType.ERROR);
        }
    }

    /**
     * Loads a new scene into the primary stage.
     *
     * @param fxmlFile The FXML file to load.
     * @param title    The title to set for the window.
     */
    public static void loadScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
            Pane root = loader.load();
            Scene scene = new Scene(root, 800, 600);  // Adjust size if needed

            // Load the CSS if available
            String cssPath = "style.css";  // Adjust path if necessary
            if (MainApp.class.getResource(cssPath) != null) {
                scene.getStylesheets().add(MainApp.class.getResource(cssPath).toExternalForm());
            } else {
                System.out.println("⚠️ Warning: style.css not found!");
            }

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading " + fxmlFile);
            e.printStackTrace();
            showAlert("Scene Loading Error", "Failed to load " + fxmlFile, e.getMessage(), AlertType.ERROR);
        }
    }

    /**
     * Displays an alert dialog to the user.
     *
     * @param title   The title of the alert.
     * @param header  The header text for the alert.
     * @param content The content text for the alert.
     * @param type    The type of alert (Error, Information, etc.).
     */
    private static void showAlert(String title, String header, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args Command-line arguments (not used here).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
