package minesweeper;

import minesweeper.App;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	
	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		arg0.setTitle("Minesweeper");
		arg0.setScene(new Scene(FXMLLoader.load(App.class.getResource("minesweeper.fxml"))));
		arg0.show();
	}
	
	public static void main(final String[] args) {
		//App.launch(args);
		launch(App.class, args);
	}
}
