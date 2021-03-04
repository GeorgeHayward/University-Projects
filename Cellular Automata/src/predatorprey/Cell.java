package predatorprey;

public class Cell {
	String cellType;

	// Constructor to build each cell.
	public Cell(String CT) {
		this.cellType = CT;
	}

	// Return this cell's type. (prey, predator or dead)
	public String getCellType() {
		return this.cellType;
	}
}
