package simulation;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * App class that extends the JavaFX Application class to start the Gym Motivation Simulation.
 * This class is responsible for loading the main menu FXML, applying the CSS styles, 
 * and configuring the main stage.
 * 
 * <p>This simulation models how gym motivation spreads through social networks and
 * how different states of gym involvement affect others.</p>
 * 
 * <p>Author: Juan Carlos Quintero</p>
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/simulation/mainMenu.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1280, 720);

        URL cssUrl = getClass().getResource("/simulation/styles/mainMenu.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.out.println("Could not load CSS file.");
        }

        stage.setTitle("Gym Motivation Simulation");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}