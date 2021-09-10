package minesweeper;

import java.io.File;

public interface FileHandler {
	
	public final static String save_folder = "src/main/java/minesweeper/saves/";
	public void save(String fileName, int counter, boolean isGameWon);
	public void load(String fileName);
	
	public static String getFilePath(String fileName) {
		String rootPath = new File("").getAbsolutePath();
		String filePath = rootPath + "/" + save_folder + fileName +".txt";
		return filePath;
	}

}
