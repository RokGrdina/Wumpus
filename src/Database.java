import java.util.ArrayList;

public class Database {

	private Cell[][] cell;

	public Database(Grid grid) {

		cell = new Cell[grid.sizeX][grid.sizeY];

		for (int i = 0; i < cell.length; i++) {
			for (int j = 0; j < cell[0].length; j++) {
				cell[i][j] = new Cell();
				cell[i][j].add(cellType.UNKNOWN);
			}
		}
		
		cell[grid.exit.x][grid.exit.y].add(cellType.EXIT);

	}

	public ArrayList<CellPosition> tell(Position pos, ArrayList<cellType> senses) {
		
		//cell[pos.x][pos.y].remove(cellType.UNKNOWN);

		cell[pos.x][pos.y].add(cellType.EXPLORED);

		for (cellType sense : senses) {
			cell[pos.x][pos.y].add(sense);
		}

		return deductAll();
	}

	public status ask(int x, int y) {
		return ask(new Position(x, y));
	}

	public status ask(Position pos) {

		if (cell[pos.x][pos.y].contains(cellType.EXPLORED)) {
			return status.EXPLORED;
		} else if (cell[pos.x][pos.y].contains(cellType.EMPTY)
				|| cell[pos.x][pos.y].contains(cellType.GOLD)) {
			return status.SAFE;
		} else {
			return status.UNSAFE;
		}

	}

	private ArrayList<CellPosition> deductAll() {
		boolean deducting = true;

		ArrayList<CellPosition> deducted = new ArrayList<CellPosition>();

		while (deducting) {

			deducting = false;

			ArrayList<CellPosition> tmp = new ArrayList<CellPosition>();

			for (int i = 0; i < cell.length; i++) {
				for (int j = 0; j < cell[0].length; j++) {

					if (cell[i][j].isUnknown()) {
						cellType currentCell = deduct(i, j);

						if (currentCell != cellType.UNKNOWN) {
							tmp.add(new CellPosition(currentCell, new Position(
									i, j)));
						}
					}

				}
			}

			if (!tmp.isEmpty()) {
				deducting = true;
			}
			

			deducted.addAll(tmp);

		}

		ArrayList<CellPosition> realDeducted = new ArrayList<CellPosition>();

		for (CellPosition cell : deducted) {

			if (cell.type != cellType.EMPTY) {
				realDeducted.add(cell);
			}
		}

		return deducted;
	}

	private cellType deduct(Position pos) {

		int x = pos.x;
		int y = pos.y;

		int goldOccurence = 0;
		int pitOccurence = 0;
		int wumpusOccurence = 0;

		
		if (x > 0) {

			if (cell[x - 1][y].contains(cellType.GLITTER)) {
				goldOccurence++;
			}
			else if (cell[x - 1][y].contains(cellType.BREEZE)) {
				pitOccurence++;
			}
			else if (cell[x - 1][y].contains(cellType.STENCH)) {
				wumpusOccurence++;
			}
			else if (cell[x - 1][y].contains(cellType.EXPLORED)) {
				cell[pos.x][pos.y].remove(cellType.UNKNOWN);
				cell[pos.x][pos.y].add(cellType.EMPTY);
				return cellType.EMPTY;
			}
		}
		if (y > 0) {
			if (cell[x][y - 1].contains(cellType.GLITTER)) {
				goldOccurence++;
			}
			else if (cell[x][y - 1].contains(cellType.BREEZE)) {
				pitOccurence++;
			}
			else if (cell[x][y - 1].contains(cellType.STENCH)) {
				wumpusOccurence++;
			}
			else if (cell[x][y - 1].contains(cellType.EXPLORED)) {
				cell[pos.x][pos.y].remove(cellType.UNKNOWN);
				cell[pos.x][pos.y].add(cellType.EMPTY);
				return cellType.EMPTY;
			}
		}
		if (x + 1 < cell.length) {
			
			if (cell[x + 1][y].contains(cellType.GLITTER)) {
				goldOccurence++;
			}
			else if (cell[x + 1][y].contains(cellType.BREEZE)) {
				pitOccurence++;
			}
			else if (cell[x + 1][y].contains(cellType.STENCH)) {
				wumpusOccurence++;
			}
			else if (cell[x + 1][y].contains(cellType.EXPLORED)) {
				cell[pos.x][pos.y].remove(cellType.UNKNOWN);
				cell[pos.x][pos.y].add(cellType.EMPTY);
				return cellType.EMPTY;
			}
		}
		if (y + 1 < cell[0].length) {
			
			if (cell[x][y + 1].contains(cellType.GLITTER)) {
				goldOccurence++;
			}
			else if (cell[x][y + 1].contains(cellType.BREEZE)) {
				pitOccurence++;
			}
			else if (cell[x][y + 1].contains(cellType.STENCH)) {
				wumpusOccurence++;
			}
			else if (cell[x][y + 1].contains(cellType.EXPLORED)) {
				cell[pos.x][pos.y].remove(cellType.UNKNOWN);
				cell[pos.x][pos.y].add(cellType.EMPTY);
				return cellType.EMPTY;
			}
		}
		
		
		if (goldOccurence > 2) {
			cell[pos.x][pos.y].remove(cellType.UNKNOWN);
			cell[pos.x][pos.y].add(cellType.GOLD);
			return cellType.GOLD;
		}
		if (pitOccurence > 2) {
			cell[pos.x][pos.y].remove(cellType.UNKNOWN);
			cell[pos.x][pos.y].add(cellType.PIT);
			return cellType.PIT;
		}
		if (wumpusOccurence > 2) {
			cell[pos.x][pos.y].remove(cellType.UNKNOWN);
			cell[pos.x][pos.y].add(cellType.WUMPUS);
			return cellType.WUMPUS;
		}

		return cellType.UNKNOWN;

	}

	private cellType deduct(int x, int y) {
		return deduct(new Position(x, y));
	}

}

enum status {
	SAFE, UNSAFE, EXPLORED
};

class CellPosition {
	cellType type;
	Position pos;

	CellPosition(cellType type, Position pos) {
		this.type = type;
		this.pos = pos;
	}
}
