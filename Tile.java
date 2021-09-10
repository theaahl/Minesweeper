package minesweeper;

public class Tile {
	
	private char state = 'C'; // if the tile is open, closed or flagged
	private char label = '0'; // if tile is mine
	private boolean isMine; // if the tile is a mine, or the number of mine close to this tile
	private int x;
	private int y;
	
	public Tile(int y, int x) {
		if(y < 0 || x < 0) {
			throw new IllegalArgumentException("Illegal values, cannot be negative");
		}
		this.x = x;
		this.y = y;
	}
	
	public char getState() {
		return state;
	}
	
	public char getLabel() {
		return label;
	}
	
	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}
	
	public boolean getMine() {
		return isMine;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void open() {
		if (state == 'C') {
			this.state = 'O';
		}
		if (getMine()) {
			this.label = 'X';
		}
	}
	
	public void flag() {
		if(state == 'F') {
			this.state = 'C';
		}
		else if (state == 'C') {
			this.state = 'F';
		}
	}
	
	public void setSuroundingMines(int n) {
		if (this.isMine) {
			throw new IllegalStateException("Can't set surrounding mines for a mine as this would overwrite that it is a mine");
		}
		if(n >= 1 & n <= 8) {
			this.label = (char)(n+'0');
		}
		else {
			throw new IllegalArgumentException("Invalid number of surrounding mines");
		}
	}
	
	@Override
	public String toString() {
		return "(" + String.valueOf(x) + "," + String.valueOf(y) + ") state: " + Character.toString(state); 
	}
	
}
