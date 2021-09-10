package minesweeper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GameResultHandler implements FileHandler {

	private List<ArrayList<Integer>> results = new ArrayList<ArrayList<Integer>>();
	private int winPercentage;
	private int bestTime;
	private int gamesPlayed;
	
	@Override
	public void save(String fileName, int counter, boolean isGameWon) {
		//FileWriter so it does not overwrite		
		try(PrintWriter writer = new PrintWriter(new FileWriter(FileHandler.getFilePath(fileName), true))){
			writer.println("Counter: " + counter + " , Win: " + isGameWon);
		}catch (FileNotFoundException e) {
			
			System.out.println("FileNotFound exception");
		}catch (Exception e) {
			
			System.out.println("Unknown exception");
		}
	}
	


	@Override
	public void load(String fileName) {
		// TODO Auto-generated method stub
		try (Scanner scanner = new Scanner(new File(FileHandler.getFilePath(fileName)))){
			results.clear();
			// Resets values to zero, in case of manual changes to the file
			bestTime = 0;
			winPercentage = 0;
			gamesPlayed = 0;
			while(scanner.hasNextLine()) {
				// saves relevant values from file to variables
				scanner.next();
				int currentCounter = scanner.nextInt();
				scanner.next();
				scanner.next();
				boolean currentResult = scanner.nextBoolean();
		
				scanner.nextLine();
				
				// Transformes boolean to int, 0 -> false, 1 -> true
				int currentResultToInt = currentResult ? 1:0;
				results.add(new ArrayList<Integer>(Arrays.asList(currentCounter, currentResultToInt)));
				calculateWinPercentage();
				calculateBestTime();
				calculateGamesPlayed();
				
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException occured");
		}
		catch (Exception e) {
			System.out.println("Unknown exception, check if format of file is correct");
		}
	}
	
	
	// Private help-function with calculations
	private void calculateGamesPlayed() {
		this.gamesPlayed = results.size();
	}
	
	private void calculateWinPercentage() {
		int sumOfGamesWon = 0;
		for (ArrayList<Integer> i : results) {
			int isGameWon = i.get(1);
			sumOfGamesWon += isGameWon;
		}
		if (results.size() != 0) {
			this.winPercentage = (100*sumOfGamesWon/results.size());
		}
	}
	
	private void calculateBestTime() {
		for (ArrayList<Integer> i: results) {
			if(i.get(1) == 1) { // if boolean win result = true (game won)
					
				if (bestTime == 0) {  // if no best time has been set yet
					this.bestTime = i.get(0);  // sets best time to current counter
				}
				else {
					if (bestTime > i.get(0)) { // if new time is better than current best counter time
						this.bestTime = i.get(0);
					}
				}
			}
		}
	}
	
	public int getWinPercentage() {
		return winPercentage;
	}

	public int getBestTime() {
		return bestTime;
	}


	public int getGamesPlayed() {
		return gamesPlayed;
	}

}
