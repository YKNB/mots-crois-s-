package yegbe.izere.tp6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainTP6 extends Application {
	@Override 
	public void start(Stage primaryStage) {
		try
		{
	        primaryStage.setTitle("TP6 yegbe-izere");
			FXMLLoader loader = new FXMLLoader() ;
            loader.setLocation(MainTP6.class.getResource("VueTP6.fxml"));
            Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
