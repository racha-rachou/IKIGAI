package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		//Parent root =FXMLLoader.load(getClass().getResource("Login.fxml"));
		//Parent root =FXMLLoader.load(getClass().getResource("Inscription.fxml"));
		//Parent root =FXMLLoader.load(getClass().getResource("ActiviteesDuJour.fxml"));
		//Parent root =FXMLLoader.load(getClass().getResource("ConsulterDevPerso.fxml"));
		//Parent root =FXMLLoader.load(getClass().getResource("InfoApprises.fxml"));
		
		Parent root =FXMLLoader.load(getClass().getResource("Home.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
