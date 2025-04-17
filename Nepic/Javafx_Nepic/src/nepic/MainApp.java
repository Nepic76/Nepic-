package nepic;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Nepic Music App");
        // Creating MainView as an inner class
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Inner MainView class
    public class MainView extends StackPane {
        public MainView() {
            Label label = new Label("Welcome to Nepic Music App!");
            getChildren().add(label);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
