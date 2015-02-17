enum actionType {
	MOVE, SENSE, DEATH, GETGOLD, EXIT, END, DEDUCT
};

class Action {
	actionType type;

	void print() {
	}
}

class MoveAction extends Action {
	int x;
	int y;

	MoveAction(int x, int y) {
		this.x = x;
		this.y = y;
		super.type = actionType.MOVE;
	}

	MoveAction(Position pos) {
		this.x = pos.x;
		this.y = pos.y;
		super.type = actionType.MOVE;
	}

	@Override
	void print() {
		System.out.println("Move to cell (" + x + ", " + y + ")");
	}

}

class SenseAction extends Action {
	cellType type;

	SenseAction(cellType celltype) {
		super.type = actionType.SENSE;
		type = celltype;
	}

	@Override
	void print() {
		System.out.println("Sense " + type.toString());
	}

}

class DeathAction extends Action {
	cellType type;

	DeathAction(cellType celltype) {
		super.type = actionType.DEATH;
		type = celltype;
	} 
	
	void print() {
		System.out.println("Death by " + type.toString());
	}
}

class ExitAction extends Action {

	ExitAction() {
		super.type = actionType.EXIT;
	}
	
	void print() {
		System.out.println("EXIT");
	}
}

class GetGoldAction extends Action {

	GetGoldAction() {
		super.type = actionType.GETGOLD;
	}
	
	void print() {
		System.out.println("Got gold");
	}
}

class EndAction extends Action  {
	
	endType type;
	int points;
	
	EndAction (endType type, int points) {
		super.type = actionType.END;
		this.type = type;
		this.points = points;
		
	}
	
	void print() {
		System.out.println(type.toString() + " " + points + " points.");
	}
}

class DeductAction extends Action  {
	
	cellType type;
	Position pos;
	
	DeductAction (cellType type, Position pos) {
		super.type = actionType.DEDUCT;
		this.type = type;
		this.pos = pos;
		
	}
	
	DeductAction (CellPosition cellPosition) {
		super.type = actionType.DEDUCT;
		this.type = cellPosition.type;
		this.pos = cellPosition.pos;
	}
	
	void print() {
		System.out.println("Deducted " + type  + " on postion " + pos.x + ", " + pos.y);
	}
}