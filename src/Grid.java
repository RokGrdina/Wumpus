import java.util.ArrayList;

enum cellType {
	GOLD, BREEZE, PIT, STENCH, WUMPUS, EXIT, GLITTER, UNKNOWN, EMPTY, EXPLORED
};

class Cell {

	private ArrayList<cellType> types;

	public boolean contains(cellType type) {
		return types.contains(type);
	}

	public void add(cellType type) {

		if (!types.contains(type)) {
			types.add(type);
		}
	}

	public boolean isExplored() {
		return contains(cellType.EXPLORED);
	}

	Cell() {
		types = new ArrayList<cellType>();
	}

	public void remove(cellType type) {
		for (int i = 0; i < types.size(); i++) {
			if (types.get(i).equals(type)) {
				types.remove(i);
				return;
			}
		}
	}
	
	public ArrayList<cellType> getTypes () {
		return types;
	}
	
	public boolean isUnknown () {
		return types.contains(cellType.UNKNOWN);
	}
	
	

}

public class Grid {

	Position exit;

	final int sizeX, sizeY;

	private Cell[][] grid;

	public Position agentStart;

	public void add(int x, int y, cellType type) {
		grid[x][y].add(type);
		grid[x][y].remove(cellType.EMPTY);

		if (grid[x][y].contains(cellType.GOLD)) {
			setNeighboursSense(x, y, cellType.GLITTER);
		} else if (grid[x][y].contains(cellType.WUMPUS)) {
			setNeighboursSense(x, y, cellType.STENCH);
		} else if (grid[x][y].contains(cellType.PIT)) {
			setNeighboursSense(x, y, cellType.BREEZE);
		}
	}

	public void addExit(int x, int y) {
		exit = new Position(x, y);
		add(x, y, cellType.EXIT);
	}

	public Grid(int x, int y) {
		sizeX = x;
		sizeY = y;

		grid = new Cell[x][y];

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j] = new Cell();
				grid[i][j].add(cellType.EMPTY);
			}
		}
	}

	public void setAgentStart(int x, int y) {
		agentStart = new Position(x, y);
	}

	private void setNeighboursSense(int x, int y, cellType type) {
		if (x > 0) {
			grid[x - 1][y].add(type);
		}
		if (y > 0) {
			grid[x][y - 1].add(type);
		}
		if (y + 1 < sizeY) {
			grid[x][y + 1].add(type);
		}
		if (x + 1 < sizeX) {
			grid[x + 1][y].add(type);
		}
	}

	public ArrayList<cellType> getSenses(Position pos) {
		ArrayList<cellType> senses = new ArrayList<cellType>();

		if (grid[pos.x][pos.y].contains(cellType.BREEZE)) {
			senses.add(cellType.BREEZE);
		} else if (grid[pos.x][pos.y].contains(cellType.GLITTER)) {
			senses.add(cellType.GLITTER);
		} else if (grid[pos.x][pos.y].contains(cellType.STENCH)) {
			senses.add(cellType.STENCH);
		}

		return grid[pos.x][pos.y].getTypes();
	}

	public cellType getDeathSituations(Position pos) {
		if (grid[pos.x][pos.y].contains(cellType.WUMPUS)) {
			return cellType.WUMPUS;
		} else if (grid[pos.x][pos.y].contains(cellType.PIT)) {
			return cellType.PIT;
		}
		return null;
	}

	public boolean isExit(Position pos) {
		if (exit.equals(pos)) {
			return true;
		}
		return false;
	}

	public boolean containsGold(Position pos) {
		if (grid[pos.x][pos.y].contains(cellType.GOLD)) {
			return true;
		}
		return false;
	}

	public void removeGold(Position pos) {
		grid[pos.x][pos.y].remove(cellType.GOLD);
	}

}
