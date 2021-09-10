package minesweeper;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class Controller {
	private Minesweeper minesweeper;
	private GameResultHandler gameResultHandler = new GameResultHandler();
	private int counter;
	private String level;
	
	@FXML Pane board;
	Text winLoseText = new Text();
	@FXML Label count;
	Timeline timeline;
	@FXML TextFlow flow;
	
	
	@FXML
	public void initialize() {
		level = "Easy";
		createBoard(10, 10, 10);
		timer();
		showAllLevelStats();
	}


	public void createBoard(int height, int width, int mines) { // Minesweeper already throws exception if values are wrong
		board.setTranslateX(50);
		board.getChildren().clear();
		minesweeper = new Minesweeper(height, width, mines);
		for (int y = 0; y < minesweeper.getHeight(); y++) {
			for (int x = 0; x < minesweeper.getWidth(); x++) {
				Button tile = new Button();
				tile.setTranslateX(x*30);
				tile.setTranslateY(y*30);
				tile.setPrefWidth(30);
				tile.setPrefHeight(30);
				tile.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
				board.getChildren().add(tile);
				
				final int xcopy = x;
				final int ycopy = y;
				
				
				tile.setOnMouseClicked(e -> {
					if (e.getButton() == MouseButton.PRIMARY) { // Execute handleClick if left click
						handleClick(minesweeper.getTiles()[ycopy][xcopy]);
					}else if (e.getButton() == MouseButton.SECONDARY) {  //Execute handleSideClick if right click 
						handleSideClick(minesweeper.getTiles()[ycopy][xcopy]);
					}
					//https://stackoverflow.com/questions/1515547/right-click-in-javafx
				});
			}
		}
		
	}
	
	public void handleClick(Tile clickedTile) {
		openTiles(clickedTile);
		colorTile(clickedTile);
	}
	
	public void handleSideClick(Tile clickedTile) {
		clickedTile.flag();
		colorTile(clickedTile);
	}
	
	
	public String setAppearance(Tile tile) { // Returns the colors of the tiles, mine (black), open (white and numbers if nearby tile), flagged (red)
		if (tile.getLabel() == 'X') {
			return "-fx-color: black;";
		}
		if (tile.getState() == 'F') {
			
			return "-fx-color: red;";
		}
		else if (tile.getState() == 'O') {
			setLabelFXML(tile);
			return "-fx-text-fill:"+labelColor(String.valueOf(tile.getLabel()))+"; -fx-color: white; -fx-font-size: 1.5em;";
		} 
		else if (tile.getState() == 'C') {
			return "-fx-border-color: black; -fx-border-width: 1px;";
		}
		throw new IllegalStateException("Something went wrong with setting appearance");
	}
	
	
	public void setLabelFXML(Tile tile) {
		Button thisTile =  (Button) board.getChildren().get(tile.getY()*minesweeper.getWidth() + tile.getX());
		int surrMines = Character.getNumericValue(tile.getLabel());
		if (surrMines != 0) {
			thisTile.setText(String.valueOf(surrMines));
			//thisTile.setStyle("-fx-text-fill:"+labelColor(String.valueOf(surrMines)));
		}
		
	}
	
	public void colorTile(Tile tile) {
		board.getChildren().get(tile.getY()*minesweeper.getWidth() + tile.getX()).setStyle(setAppearance(tile));
	}
	
	public String labelColor(String label) {	
		if (label.equals("1")) {
			return "blue;";
		} else if (label.equals("2")) {
			return "green;";
		}else if (label.equals("3")) {
			return "red;";
		}else if (label.equals("4")) {
			return "purple;";
		}else if (label.equals("5")) {
			return "maroon;";
		}else if (label.equals("6")) {
			return "turquoise;";
		}else if (label.equals("7")) {
			return "black;";
		}else if (label.equals("8")) {
			return "gray;";
		}return null;
		
	}
	
	public void openTiles(Tile tile) {
		if(minesweeper.isGameOver() || minesweeper.isGameWon()) {
		} // Checks if game is already ended when you click on tile, if it is, do nothing (aka lock the game)
		else {	
			minesweeper.openTile(tile);
			for (int y = 0; y < minesweeper.getHeight(); y++) {
				for (int x = 0; x < minesweeper.getWidth(); x++) {
					Tile oneTile = minesweeper.getTiles()[y][x];
					colorTile(oneTile);
				}
			}
			if(minesweeper.isGameOver() || minesweeper.isGameWon()) {
				gameEnded();
			}
		}
	}
	
	public void gameEnded() {
		timeline.stop();
	
		winLoseText.setStyle("-fx-font-size: 20px");
		winLoseText.setFill(Color.BLACK);
		winLoseText.setTranslateX(10.0);
		winLoseText.setTranslateY(minesweeper.getHeight()*30+30); 
		
		if(minesweeper.isGameOver()) {
			for (int y = 0; y < minesweeper.getHeight(); y++) {
				for (int x = 0; x < minesweeper.getWidth(); x++) {
					Tile tile = minesweeper.getTiles()[y][x];
					tile.open();
					colorTile(tile);
				}
			}
			winLoseText.setText("Click on desired level to start over");
			board.getChildren().add(winLoseText);
		}
		else if(minesweeper.isGameWon()) {
			winLoseText.setText("You won!");
			board.getChildren().add(winLoseText);
		}
		handleSave(level);
		showAllLevelStats();
	}
	
	public void handleSave(String fileName) {
		gameResultHandler.save(fileName, counter, minesweeper.isGameWon());
	}
	
	public void handleLoad(String fileName){
		gameResultHandler.load(fileName);
	}
	
	@FXML
	public void handleReset(ActionEvent e) {
		
		minesweeper.setOpenedTiles(0);
		minesweeper.setGameOver(false);
		minesweeper.setGameWon(false);
		timeline.stop();
		
		Button button = (Button) e.getSource();
		// Starter nytt spill avhengig av level
		
		if (button.getText().equals("Easy")){
			createBoard(10, 10, 10);
			level = "Easy";
			flow.setTranslateY(0);
			
		}
		else if(button.getText().equals("Medium")){
			createBoard(13, 13, 30);
			level = "Medium";
			flow.setTranslateY(80);
			
		}
		else if(button.getText().equals("Hard")) {
			createBoard(13, 20, 70);
			level = "Hard";
			flow.setTranslateY(80);
		}
		else {
		 throw new IllegalArgumentException("Something went wrong with clicking on the button");
		}
		board.getScene().getWindow().setWidth(minesweeper.getWidth()*30+100);
		board.getScene().getWindow().setHeight(minesweeper.getHeight()*30+328);
		timer();
		//showStats();
	}
	
	public void showStats(String fileName, Color color){
		handleLoad(fileName);
		int bestTime = gameResultHandler.getBestTime();
		int winPercentage = gameResultHandler.getWinPercentage();
		int gamesPlayed = gameResultHandler.getGamesPlayed();
		
		Text level = new Text(fileName);
		level.setFont(Font.font("Apple Casual", FontWeight.BOLD, 17));
		level.setFill(color);
		
		Text gamesPlayedText = new Text("\nGames played: ");
		Text gamesPlayedNr = new Text(Integer.toString(gamesPlayed));
		gamesPlayedNr.setFont(Font.font("Apple Casual", FontWeight.BOLD, 13));
		gamesPlayedNr.setFill(Color.BLUE);

		
		Text winPercentageText = new Text("\tWin percentage: ");
		Text winPercentageNr = new Text(Integer.toString(winPercentage)+ "% ");
		winPercentageNr.setFont(Font.font("Apple Casual", FontWeight.BOLD, 13));
		winPercentageNr.setFill(Color.BLUE);
		
		Text bestTimeText = new Text("\tBest time: ");
		Text bestTimeNr = new Text(Integer.toString(bestTime)+"s\n\n");
		bestTimeNr.setFont(Font.font("Apple Casual", FontWeight.BOLD, 13));
		bestTimeNr.setFill(Color.BLUE);
		
		flow.getChildren().add(level);
		flow.getChildren().add(gamesPlayedText);
		flow.getChildren().add(gamesPlayedNr);
		flow.getChildren().add(winPercentageText);
		flow.getChildren().add(winPercentageNr);
		flow.getChildren().add(bestTimeText);
		flow.getChildren().add(bestTimeNr);
	}
	
	public void showAllLevelStats() {
		flow.getChildren().clear();
		showStats("Easy", Color.GREEN);
		showStats("Medium", Color.ORANGE);
		showStats("Hard", Color.RED);
	}
	
	public void timer() {	
		counter = 0;
		timeline = new Timeline(
	            new KeyFrame(Duration.seconds(0),
	                e -> updateTimer()),
	            new KeyFrame(Duration.seconds(1)));
	    timeline.setCycleCount(Animation.INDEFINITE);
	    timeline.play();
	}
	
	public void updateTimer() {
		counter++;
		count.setText(Integer.toString(counter));
	}
}
