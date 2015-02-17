import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

enum endType {
	WIN, LOOSE
};

public class Agent {
	
	private ArrayList<Action> actions;
	private Position currentPosition;
	
	private ArrayList<Position> unexploredSafe;
	private ArrayList<Position> unexplored;

	private Grid grid;
	private Database database;
	
	private boolean visited[][];
	private boolean end;
	
	private int points;	
	

	// adds the action it performed, to be printed later
	private void addAction(Action action) {
		actions.add(action);
	}

	// print all actions
	public void printActions() {
		for (Action action : actions) {
			action.print();
		}
	}

	public Agent(Grid grid) {
		this.grid = grid;
		database = new Database(grid);
		visited = new boolean[grid.sizeX][grid.sizeY];
		points = 0;
		end = false;
		
		unexploredSafe = new ArrayList<Position>();
		unexplored = new ArrayList<Position>();
		
		currentPosition = grid.agentStart;
		
		actions = new ArrayList<Action>();
	}

	public void move() {
		
		ArrayList<CellPosition> deducted = database.tell(currentPosition, grid.getSenses(currentPosition));
		
		for (CellPosition cellPosition : deducted) {
			addAction(new DeductAction(cellPosition));
		}

		while (!endCondition()) {
			nextMove();
		}
	}


	private void nextMove() {
		Position move = getNextMove();
		
		ArrayList<CellPosition> deducted = database.tell(currentPosition, grid.getSenses(currentPosition));
		
		ArrayList<Position> path = shortestPath(currentPosition, move);
		
		for (Position position : path) {
			currentPosition = position;
			points--;
			addAction(new MoveAction(position));
			

			for (cellType sense : grid.getSenses(position)) {
				
				addAction(new SenseAction(sense));

			}
			
			deducted.addAll(database.tell(position, grid.getSenses(position)));

			for (CellPosition cellPosition : deducted) {
				addAction(new DeductAction(cellPosition));
			}

			if (grid.getDeathSituations(position) != null) {
				points -= 1000;
				addAction(new DeathAction(grid.getDeathSituations(position)));				
				end(endType.LOOSE);
				return;
			} else if (grid.isExit(position)) {
				addAction(new ExitAction());
				end(endType.WIN);
				return;
			} else if (grid.containsGold(position)) {
				points += 1000;
				addAction(new GetGoldAction());			
				grid.removeGold(position);
			}
		}		

	}

	private Position getNextMove() {

		Position nextPosition;
		
		if (!unexploredSafe.isEmpty()) {
			nextPosition = unexploredSafe.get(0);

			for (Position target : unexploredSafe) {
				
				if (distance(nextPosition, currentPosition) > distance(target,
						currentPosition)) {
					nextPosition = target;
				}
			}
		} else {
			nextPosition = unexplored.get(0);
			for (Position target : unexplored) {
				if (distance(nextPosition, currentPosition) > distance(target,
						currentPosition)) {
					nextPosition = target;
				}
			}
		}

		return nextPosition;
	}

	private double distance(Position a, Position b) {
		return Math.sqrt(Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y), 2));
	}

	private boolean endCondition() {

		if (end) {
			return true;
		}

		updateUnexplored();
		
		if (unexplored.isEmpty() && unexploredSafe.isEmpty()) {
			return true;
		}

		return false;

	}

	private void updateUnexplored() {
		unexploredSafe.clear();
		unexplored.clear();
		
		for (int i = 0; i < grid.sizeX; i++) {
			for (int j = 0; j < grid.sizeY; j++) {
				if (database.ask(i, j) == status.SAFE) {
					unexploredSafe.add(new Position(i, j));
				}
				else if (database.ask(i, j) == status.UNSAFE) {
					unexplored.add(new Position(i, j));
				}
			}
		}
	}

	private void end(endType type) {
		
		end = true;		
		addAction(new EndAction(type, points));

	}

	private ArrayList<Position> shortestPath(Position start, Position end) {
		Queue<BFSData> queue = new LinkedList<BFSData>();
		
		queue.add(new BFSData(start));

		for (int i = 0; i < visited.length; i++) {
			for (int j = 0; j < visited[0].length; j++) {
				visited[i][j] = false;
			}
		}
		while (!queue.isEmpty() && !end.equals(queue.peek().position)) {
			queue.addAll(getNeighbours(queue.peek()));
			queue.remove();
		}

		return queue.peek().path;
	}
	
	private Queue<BFSData> getNeighbours(BFSData data) {
		
		Queue<BFSData> queue = new LinkedList<BFSData>();
		
		ArrayList<Position> neighbours = getNeighbours(data.position);
		
		for (Position neighbour : neighbours) {
			queue.add(new BFSData(neighbour, data.path));
		}
		
		return queue;
	}

	private ArrayList<Position> getNeighbours(Position pos) {
		
		ArrayList<Position> safe = new ArrayList<Position>();

		int x = pos.x;
		int y = pos.y;

		visited[x][y] = true;

		if (database.ask(pos) == status.UNSAFE) {
			return safe;
		}

		if (x > 0 && !visited[x - 1][y]) {
			safe.add(new Position(x - 1, y));
		}
		if (y > 0 && !visited[x][y - 1]) {
			safe.add(new Position(x, y - 1));
		}
		if (x + 1 < grid.sizeX && !visited[x + 1][y]) {
			safe.add(new Position(x + 1, y));
		}
		if (y + 1 < grid.sizeY && !visited[x][y + 1]) {
			safe.add(new Position(x, y + 1));
		}

		return safe;
	}

}

class BFSData {

	Position position;

	ArrayList<Position> path;

	BFSData(Position pos) {
		position = pos;
		path = new ArrayList<Position>();
	}

	BFSData(Position position, ArrayList<Position> path) {
		this.path = new ArrayList<Position>();
		this.position = position;
		this.path.addAll(path);
		this.path.add(position);
	}

}
