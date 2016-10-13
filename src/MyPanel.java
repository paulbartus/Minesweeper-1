import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 0;
	private static final int GRID_Y = 0;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 9; 
	private static final int MINE = 10;//10 identifies if the array has a mine
	private static final int NUMBER_OF_MINES = 10;//total mines that appear in the game
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public int mineCounter[][] =  new int[TOTAL_COLUMNS][TOTAL_ROWS];//provides information about the cell, mines nearby or if is a mine

	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		
		createTheMines();
		
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //The grid
			for (int y = 0; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
			}
		}
	}
	
	public void createTheMines() {
		
		//initialize the bombs
		mineCounter = new int[TOTAL_COLUMNS][TOTAL_ROWS];
		Random randomColumn = new Random();
		Random randomRow = new Random();
		int col;
		int row;
		
		for (int i = 0; i < NUMBER_OF_MINES; i++) {
			do {
			col = randomColumn.nextInt(TOTAL_COLUMNS);
			row = randomRow.nextInt(TOTAL_ROWS);
			} while(mineCounter[col][row] == MINE);
				mineCounter[col][row] = MINE;
		}
		
		//count bombs near
		for (int x = 0; x < mineCounter.length; x++) {
			for (int y = 0; y < mineCounter[0].length; y++) {
				if(mineCounter[x][y] != MINE) {
					int neighborCount = 0;
					if (x > 0 && y > 0 && mineCounter[x-1][y-1] == MINE) {//up left
						neighborCount++;
					}
					if (y > 0 && mineCounter[x][y-1] == MINE) {//up
						neighborCount++;
					}
					if (x < mineCounter.length - 1 && y > 0 && mineCounter[x+1][y-1] == MINE) {//up right
						neighborCount++;
					}
					if (x > 0 && mineCounter[x-1][y] == MINE) {//left
						neighborCount++;
					}
					if (x < mineCounter.length - 1 && mineCounter[x+1][y] == MINE) {//right
						neighborCount++;
					}
					if (x > 0 && y < mineCounter[0].length - 1 && mineCounter[x-1][y+1] == MINE) {//down right
						neighborCount++;
					}
					if (y < mineCounter[0].length - 1 && mineCounter[x][y+1] == MINE) {//down
						neighborCount++;
					}
					if (x < mineCounter.length - 1 && y < mineCounter[0].length - 1 && mineCounter[x+1][y+1] == MINE) {//down left
						neighborCount++;
					}
					mineCounter[x][y] = neighborCount;
				}
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;

		//By default, the grid will be 9x9 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS)));
		}

		//Paints cell colors and numbers
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				Color c = colorArray[x][y];
				
				g.setColor(c);
				g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
				
				if(c == Color.LIGHT_GRAY){
					Color countColor = null;	
					switch (mineCounter[x][y]) {
						case 0: 
							countColor = Color.LIGHT_GRAY;
							break;
						case 1:
							countColor = Color.BLUE;
							break;
						case 2:
							countColor = Color.GREEN;
							break;
						case 3:
							countColor = Color.RED;
							break;
						case 4:
							countColor = new Color(0, 0, 139); //dark blue
							break;
						case 5:
							countColor = new Color(165, 42, 42); //brown
							break;
						case 6:
							countColor = Color.CYAN;
							break;
						case 7:
							countColor = Color.MAGENTA;
							break;
						case 8:
							countColor = Color.GRAY;
							break;
						case 10:
							countColor =  Color.BLACK;
							break;
						}
					
					g.setColor(countColor);
					g.drawString("" + mineCounter[x][y], x1 + (x * (INNER_CELL_SIZE + 1) + 12), y1 + ((y+1) * (INNER_CELL_SIZE + 1) - 10) ); 
				}	
			}
		}
	}
	
	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
}