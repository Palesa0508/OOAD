import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            System.out.println("Starting Pale's Bank System...");
            
            // Load FXML with controller specified in the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            
            primaryStage.setTitle("Pale's Bank System");
            Scene scene = new Scene(root, 900, 600);
            
            try {
                scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
                System.out.println("CSS loaded successfully");
            } catch (Exception e) {
                System.out.println("CSS not loaded: " + e.getMessage());
            }
            
            primaryStage.setScene(scene);
            primaryStage.show();
            System.out.println("Application started successfully");
            
        } catch (Exception e) {
            System.out.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}