package predatorprey;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class Cellularautomata implements Animatable {

	public static final String PREDATOR = "PREDATOR";
	public static final String PREY = "PREY";
	public static final String DEAD = "DEAD";
	public static final int BOARD_WIDTH = 100;
	public static final int BOARD_HEIGHT = 100;
	public static final int CELL_SIZE = 5;
	private Cell[][] currentTable = new Cell[BOARD_WIDTH][BOARD_HEIGHT];
	private Cell[][] nextTable = new Cell[BOARD_WIDTH][BOARD_HEIGHT];

	public Cellularautomata() {
		initialise();
	}

	// This method is the starting point for all cells.
	public void initialise() {
		resetTable(currentTable);
		resetTable(nextTable);
		for (int i = 0; i < currentTable.length; i++) {
			for (int j = 0; j < currentTable.length; j++) {
				String probability = getProbability();
				// PreyCell
				if (probability == PREY) {
					currentTable[i][j] = new Cell(PREY);
				}
				// PredatorCell
				else if (probability == PREDATOR) {
					currentTable[i][j] = new Cell(PREDATOR);
				}
				// DeadCell
				else if (probability == DEAD) {
					currentTable[i][j] = new Cell(DEAD);
				}
			}
		}
	}

	// Generates a cell type based on the probabilities specified
	public String getProbability() {
		Random rand = new Random();
		int prob = rand.nextInt(150) + 1;
		String cellType = "";
		// PreyCell
		if (prob >= 1 && prob <= 50) {
			cellType = PREY;
		}
		// PredatorCell
		if (prob > 50 && prob <= 100) {
			cellType = PREDATOR;
		}
		// DeadCell
		if (prob > 100) {
			cellType = DEAD;
		}
		return cellType;
	}

	// Reset the table to make all cells dead.
	public void resetTable(Cell[][] insert) {
		for (int i = 0; i < insert.length; i++) {
			for (int j = 0; j < insert.length; j++) {
				insert[i][j] = new Cell(DEAD);
			}
		}
	}

	// This class will determine the next table, based on the current table.
	@Override
	public synchronized void step() {
		// Iterate through the currentTable
		for (int currentTableX = 0; currentTableX < currentTable.length; currentTableX++) {
			for (int currentTableY = 0; currentTableY < currentTable[0].length; currentTableY++) {

				// If selected cell is PREY
				if (currentTable[currentTableX][currentTableY].getCellType() == PREY) {
					// Make the corresponding cell in the next Table PREY.
					nextTable[currentTableX][currentTableY] = new Cell(PREY);

					/*
					 * Fill neighbouring dead cells with probability prey,
					 * otherwise leave dead.
					 */

					// Use getNeighbouringCells method to retrieve the points of
					// all the neighbouring cells.
					ArrayList<Point> neighbours = getNeighbouringCells(currentTableX, currentTableY);

					// Cycle through all neighbouring cell coordinates which
					// have been stored.
					for (int neighbourItter = 0; neighbourItter < neighbours.size(); neighbourItter++) {
						int x = (int) neighbours.get(neighbourItter).getX();
						int y = (int) neighbours.get(neighbourItter).getY();
						// If a neighboring cell is dead.
						if (currentTable[x][y].getCellType() == DEAD) {
							// If probability returns with prey then
							if (getProbability() == PREY) {
								// Make the corresponding neighbour cell a PREY
								// cell in the next table.
								nextTable[x][y] = new Cell(PREY);
							}
							// Otherwise keep it as a Dead cell.
							else
								nextTable[x][y] = new Cell(DEAD);
						}
					}
				}
			}
		}
		for (int currentTableX = 0; currentTableX < currentTable.length; currentTableX++) {
			for (int currentTableY = 0; currentTableY < currentTable[0].length; currentTableY++) {
				// If selected cell is PREDATOR
				if (currentTable[currentTableX][currentTableY].getCellType() == PREDATOR) {
					// Corresponding cell = PREDATOR
					nextTable[currentTableX][currentTableY] = new Cell(PREDATOR);
					// Fill empty neighbouring cells with probability PREDATOR.
					// Get all neighbouring cells
					ArrayList<Point> cellNeighbours = getNeighbouringCells(currentTableX, currentTableY);
					// Cycle through all neighbouring cell coordinates
					for (int neighbourItter = 0; neighbourItter < cellNeighbours.size(); neighbourItter++) {
						int x = (int) cellNeighbours.get(neighbourItter).getX();
						int y = (int) cellNeighbours.get(neighbourItter).getY();
						// If neighbouring Cell is PREY
						if (currentTable[x][y].getCellType() == PREY) {
							// If probability returns with PREDATOR then
							if (getProbability() == PREDATOR) {
								// Make the corresponding neighbour cell
								// PREDATOR
								nextTable[x][y] = new Cell(PREDATOR);
							}
						}
					}
				}
			}
		}

		// Search through nextTable after first two rules have been applied to
		// nextTable  in the above.
		for (int nextTableX = 0; nextTableX < nextTable.length; nextTableX++) {
			for (int nextTableY = 0; nextTableY < nextTable[0].length; nextTableY++) {
				// If the selected cell in the nextTable is a PREDATOR then
				if (nextTable[nextTableX][nextTableY].getCellType() == PREDATOR) {
					// Create preyCounter variable to determine if there are
					// prey's near the PREDATOR.
					int preyCounter = 0;
					// Get surrounding neighbours
					ArrayList<Point> cellNeighbours = getNeighbouringCells(nextTableX, nextTableY);
					for (int i = 0; i < cellNeighbours.size(); i++) {
						int x = (int) cellNeighbours.get(i).getX();
						int y = (int) cellNeighbours.get(i).getY();
						// If neighbouring Cell is PREY
						if (nextTable[x][y].getCellType() == PREY) {
							// Add 1 to the counter
							preyCounter++;
						}
					}
					// If there is no prey cells surrounding the predator cell
					// then the nextTable predator is now dead.
					if (preyCounter == 0) {
						nextTable[nextTableX][nextTableY] = new Cell(DEAD);
					}
				}
			}
		}
		
		// Make the current table the next table so it can be drawn on the
		// board.
		currentTable = nextTable;
	}

	/*
	 * This method is used to return an array of coordinates on the board, these
	 * coordinates represent the neighbouring cells of the inputted cells
	 * coordinates It takes into account the border so that neighbours arent
	 * selected outside of the playing board
	 */
	public ArrayList<Point> getNeighbouringCells(int x, int y) {
		ArrayList<Point> neighbours = new ArrayList<Point>(0);

		// Cases if X is equal to 0 and Y is equal to 0.
		if (x == 0 && y == 0) {
			neighbours.add(new Point(x, y + 1)); // Add Bottom
			neighbours.add(new Point(x + 1, y)); // Add Right
			neighbours.add(new Point(x + 1, y + 1)); // Add Bottom Right
		}
		// Case if X is equal to 0 and Y is equal to 99.
		else if (x == 0 && y == 99) {
			neighbours.add(new Point(x, y - 1)); // Add Top
			neighbours.add(new Point(x + 1, y)); // Add Right
			neighbours.add(new Point(x + 1, y - 1)); // Add Top Right

		}
		// Case if X is equal to 0, but y isn't near border.
		else if (x == 0 && (y != 99 || y != 0)) {
			neighbours.add(new Point(x, y + 1)); // Add Bottom
			neighbours.add(new Point(x, y - 1)); // Add Top
			neighbours.add(new Point(x + 1, y + 1)); // Add Bottom Right
			neighbours.add(new Point(x + 1, y)); // Add Right
			neighbours.add(new Point(x + 1, y - 1)); // Add Top Right
		}
		// Case if X is equal to 99 and Y is equal to 0.
		else if (x == 99 && y == 0) {
			neighbours.add(new Point(x - 1, y)); // Add Left
			neighbours.add(new Point(x - 1, y + 1)); // Add Bottom Left
			neighbours.add(new Point(x, y + 1)); // Add Bottom
		}
		// Case if X is equal to 99 and Y is equal to 99.
		else if (x == 99 && y == 99) {
			neighbours.add(new Point(x - 1, y - 1)); // Add Top Left
			neighbours.add(new Point(x - 1, y)); // Add Left
			neighbours.add(new Point(x, y - 1)); // Add Top
		}
		// Case if X is equal to 99, but y isn't near border.
		else if (x == 99 && (y != 99 || y != 0)) {
			neighbours.add(new Point(x - 1, y - 1)); // Add Top Left
			neighbours.add(new Point(x - 1, y + 1)); // Add Bottom Left
			neighbours.add(new Point(x - 1, y)); // Add Left
			neighbours.add(new Point(x, y - 1)); // Add Top
			neighbours.add(new Point(x, y + 1)); // Add Bottom
		}

		// Case if Y is equal to 0, but x isn't near border.
		else if (y == 0 && (x != 99 || x != 0)) {
			neighbours.add(new Point(x + 1, y + 1)); // Add Bottom Right
			neighbours.add(new Point(x + 1, y)); // Add Right
			neighbours.add(new Point(x, y + 1)); // Add Bottom
			neighbours.add(new Point(x - 1, y + 1)); // Add Bottom Left
			neighbours.add(new Point(x - 1, y)); // Add Left
		}
		// Case if Y is equal to 99, but x isn't near border.
		else if (y == 99 && (x != 99 || x != 0)) {
			neighbours.add(new Point(x - 1, y - 1)); // Add Top Left
			neighbours.add(new Point(x - 1, y)); // Add Left
			neighbours.add(new Point(x, y - 1)); // Add Top
			neighbours.add(new Point(x + 1, y - 1)); // Add Top Right
			neighbours.add(new Point(x + 1, y)); // Add Right
		}

		// If the cell isn't near the borders then just calculate surrounding
		// cells normally. (8 directions)
		else {
			neighbours.add(new Point(x - 1, y - 1)); // Add Top Left
			neighbours.add(new Point(x - 1, y + 1)); // Add Bottom Left
			neighbours.add(new Point(x - 1, y)); // Add Left
			neighbours.add(new Point(x, y - 1)); // Add Top
			neighbours.add(new Point(x, y + 1)); // Add Bottom
			neighbours.add(new Point(x + 1, y - 1)); // Add Top Right
			neighbours.add(new Point(x + 1, y + 1)); // Add Bottom Right
			neighbours.add(new Point(x + 1, y)); // Add Right
		}
		return neighbours;
	}

	@Override
	public synchronized void paint(Graphics pGraphics) {
		// Define the shapes to be drawn
		Graphics2D graphSettings = (Graphics2D) pGraphics;
		// Antialiasing cleans up jagged lines and defines render
		graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// Go through the entire table.
		for (int i = 0; i < currentTable.length; i++) {
			for (int j = 0; j < currentTable[0].length; j++) {
				// Set locations for particular cells.
				int x1 = i * CELL_SIZE;
				int y1 = j * CELL_SIZE;
				int x2 = x1 + CELL_SIZE;
				int y2 = y1 + CELL_SIZE;
				// If the cell is Predator then make the cell RED
				if (currentTable[i][j].getCellType() == PREDATOR) {
					graphSettings.setPaint(Color.RED);
					Shape aShape = drawRectangle(x1, y1, x2, y2);
					graphSettings.draw(aShape);
					graphSettings.fill(aShape);
				}
				// If the cell is Prey then make the cell BLUE.
				else if (currentTable[i][j].getCellType() == PREY) {
					graphSettings.setPaint(Color.BLUE);
					Shape aShape = drawRectangle(x1, y1, x2, y2);
					graphSettings.draw(aShape);
					graphSettings.fill(aShape);
				}
				// If the cell is Dead then make the cell WHITE.
				else
					graphSettings.setPaint(Color.WHITE);
				Shape aShape = drawRectangle(x1, y1, x2, y2);
				graphSettings.draw(aShape);
				graphSettings.fill(aShape);
			}
		}
	}

	// Takes in a rectangles 4 corners and returns the rectangle which is
	// printed to specific x/y coordinates and are width'd height'd properly.
	private Rectangle2D.Float drawRectangle(int x1, int y1, int x2, int y2) {
		// Get top left hand corner of rectangle
		int x = x1;
		int y = y1;
		// Get difference between the two coordinates
		int width = CELL_SIZE;
		int height = CELL_SIZE;

		return new Rectangle2D.Float(x, y, width, height);
	}

	// Returns the windows width.
	@Override
	public int getWidth() {
		int width;
		width = CELL_SIZE * BOARD_WIDTH;
		return width;
	}

	// Returns the windows height.
	@Override
	public int getHeight() {
		int height;
		height = CELL_SIZE * BOARD_HEIGHT;
		return height;
	}
}
