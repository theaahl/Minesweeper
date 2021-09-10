package minesweeper;

import java.util.ArrayList;


public class Minesweeper {
	
	private int height;
	private int width;
	private int mines;
	private Tile[][] board;
	
	private boolean gameOver = false;
	private boolean gameWon = false;
	private int openedTiles;

	public Minesweeper(int height, int width, int mines) {
		if(height <= 0 || width <= 0 || mines <= 0) {
			throw new IllegalArgumentException("Must create a board with a positive number of tiles and mines");
		}
		this.height = height;
		this.width = width;
		this.mines= mines;
		
		// creates the board
		this.board = new Tile[height][width];
		
		// Makes tiles for each board element
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				board[y][x] = new Tile(y, x);
			}
		}
		setRandomMines(mines);
	}
	
	private void setRandomMines(int mines) {
		int placedMines = 0;
		
		if (mines >= this.height * this.width) {
			throw new IllegalArgumentException("To many mines for size of board");
			}
		if(mines <= 0) {
			throw new IllegalArgumentException("Can't have negative or zero number of mines");
		}
		while(placedMines < mines) {
			int randomTileHeight = (int)(Math.random()*height); // Finds random numbers in range height and width
			int randomTileWidth = (int)(Math.random()*width);
			if (!this.board[randomTileHeight][randomTileWidth].getMine()) {
				this.board[randomTileHeight][randomTileWidth].setMine(true);
				placedMines++;
				// Cannot place a mine on an existing mine, whileloop ensures that the right amount of mines are placed.
			}
		}
	}
	
	// Returns the number of surrounding mines
	private Integer countMines(ArrayList<Tile> surroundingTiles) {
		int surroundingMines = 0;
		for (Tile tile : surroundingTiles) {
			if(tile.getMine()) {
				surroundingMines++;
			}
		}
		return surroundingMines;
	}
	
	// Returns the surrounding tiles (so we later can see how many mines there are surrounding this tile)
	/*public ArrayList<Tile> surroundingTiles(int a, int b){
		Tile tile = board[a][b];*/
	private ArrayList<Tile> surroundingTiles(Tile tile){
		ArrayList<Tile> surroundingTiles = new ArrayList<Tile>();
		// Finds the surrounding tiles by getting position of tile and adding -1,0 or 1 on index,
		// returns a list of 3, 5 or 8 elements (depending on the position of the tile), consisting of the surrounding tiles. 
		
		
		if (tile.getX() > width || tile.getX() < 0 
				|| tile.getY() > height || tile.getY() < 0) {
			throw new IllegalArgumentException("The tile is not on the board");
		}
		
		for (int y = -1; y < 2; y++) {
			for (int x = -1; x < 2; x++) {
				if (y == 0 && x == 0) {
					continue;	// When element is the input tile, just continue, don't add to list
				}
				
				if(!((tile.getY() + y) < 0  // do not count tiles outside the edges, because these do not exist
						|| (tile.getX() + x) < 0  
						|| (tile.getY() + y) > (this.height-1) 
						|| (tile.getX() + x) > (this.width-1)))  {
					Tile surrounding = this.board[tile.getY() + y][tile.getX() + x];
					surroundingTiles.add(surrounding);
				}
			}
			
		}
		return surroundingTiles;
	}
	
	public void openTile(Tile tile) {
		// Opens the tile
		if (tile.getState() == 'C') {
			tile.open();
			if(tile.getMine()) {	// if the tile has a mine behind it, it alerts you that you lost, and sets gameOver to true!
				gameOver = true;
				System.out.println("BOOM! You lost");
			}
			else {  // if there is no mine behind the tile, increase number of opened tiles
									// count number of mines in surrounding tiles
				openedTiles++;
				int nrMines = countMines(surroundingTiles(tile));
				if (nrMines > 0) {
					tile.setSuroundingMines(nrMines);  // if there are bombs around the tile, it sets the tiles label to the 
													   // number of surrounding mines
				}else if (nrMines == 0) {
					for (Tile a:surroundingTiles(tile)) {	// if the tile has no surrounding mines, it recursively 
														    // opens its surrounding tiles, until it finds a nearby mine.
						openTile(a);
					}
				}
				if (openedTiles == getWidth()*getHeight()-getMines()) { // if every mine-free tile is opened, you have won!!
					System.out.println("You won!");
					gameWon = true;
				}
			}
		}
	}
	
	public int getHeight() {
		return this.height;
	}

	public int getMines() {
		return this.mines;
	}

	public int getWidth() {
		return this.width;
	}
	
	public Tile[][] getTiles(){
		return board;
	}
	
	public boolean isGameWon() {
		return gameWon;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}

	public int getOpenedTiles() {
		return openedTiles;
	}

	public void setOpenedTiles(int openedTiles) {
		this.openedTiles = openedTiles;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void setGameWon(boolean gameWon) {
		this.gameWon = gameWon;
	}
}
