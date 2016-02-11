package application;
	
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class Main extends Application {
	
	public static Stage stage;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		stage = primaryStage;
		
		Pane mainPane = FXMLLoader.load(getClass().getResource("/Sample.fxml"));
		primaryStage.setScene(new Scene(mainPane));
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
