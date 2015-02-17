
public class Position {
	int x;
	int y;
	
	Position (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals (Position pos) {
		return pos.x == x && pos.y == y;
	}
}
