import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Wumpus {

	private static Grid grid;

	public static void main(String[] args) throws IOException {
		parse(args);
		
		Agent agent = new Agent(grid);
		
		agent.move();
		
		agent.printActions();
	}

	public static void parse(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(args[0]));

		while (br.ready()) {
			StringTokenizer st = new StringTokenizer(br.readLine());

			String order = st.nextToken();

			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());

			if (order.equals("M")) {
				grid = new Grid(x, y);
			} else if (order.equals("G")) {
				grid.add(x, y, cellType.GOLD);
			} else if (order.equals("P")) {
				grid.add(x, y, cellType.PIT);
			} else if (order.equals("W")) {
				grid.add(x, y, cellType.WUMPUS);
			} else if (order.equals("GO")) {
				grid.addExit(x, y);
			} else if (order.equals("A")) {
				grid.setAgentStart(x, y);
			}
		}
		br.close();
	}

}
