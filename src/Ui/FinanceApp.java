package Ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FinanceApp extends Application{
	public static void main(String[] args) {
		launch(args);	
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Finance Tracker Application");
		FinanceGUI gui = new FinanceGUI();
		Scene scene = new Scene(gui, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
