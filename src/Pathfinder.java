import java.util.*;
import javax.swing.*;
import java.awt.*;
public class Pathfinder extends JPanel{
	
	/*
	 * Private class cell that holds all the data for each cell
	 * Its data is used to help out in the various operations
	 */
	private class Cell{
		int x;
		int y;
		String v;
		boolean right = false;
		boolean down = false;
		boolean up = false;
		boolean left = false;
		
		public Cell(int xC, int yC, String value) {
			x = xC;
			y = yC;
			v = value;
		}
		
		public void reset() {
			right = false;
			down = false;
			up = false;
			left = false;
		}
	}
	
	int size;
	//Holds the visited cells
	private ArrayList<Cell> visited;
	private Cell[][] board;
	//Holds the parents of each cell in board. Used to find the shortest path
	private Cell[][] parents;
	//Holds all the cells that make up the shortest path in a linear array
	private ArrayList<Cell> shortest;
	//Default arraysize
	private int defcellPixNum = 24;
	//cell size
	private int cs;  
	//Has the goal been reached
	boolean goal = false;
	//Is the board done being created
	boolean done = false;
	//Is bfs ready to show the shortest path
	boolean shortReady = false;
	//ready to clear the color off the board
	boolean ready = false;
	Random r = new Random();

	//Creates board of specific size
	public Pathfinder(int size) {
		this.size = size;
		init(size);
		visited = new ArrayList<Cell>();
		shortest = new ArrayList<Cell>();
		 cs = getWidth() / board.length;  
		
		
	}
	
	//Default constructor
	public Pathfinder() {
		visited = new ArrayList<Cell>();
		init(defcellPixNum);
		parents = new Cell[defcellPixNum][defcellPixNum];
		shortest = new ArrayList<Cell>();
		 cs = getWidth() / board.length;   
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		cs = getWidth() / board.length;  
		super.paintComponent(g);
		draw(g);
		
		
	}
	//Draws the various colors and grids 
	public void draw(Graphics g) {
		//Show the shortest path
		if(shortest.size() > 0) {
			for(int i = 0; i<shortest.size(); i++) {
				Cell temp = shortest.get(i);
				g.setColor(Color.PINK);
				g.fillRect(temp.x*cs,temp.y*cs,cs,cs);
			}
		}
		
		//Clears the board
		if(ready) {
			for(int i = 0; i < board.length; i++) {
				for(int j = 0; j < board[0].length; j++) {
					Cell c = board[i][j];
					 g.setColor(Color.WHITE);    
					 g.fillRect(c.x * cs, c.y * cs, cs, cs);
				}
			}
		}
		//Colors the maze either red or blue based on if its creating the maze or solving
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				Cell c = board[i][j];
				
				if (visited.contains(c)) {
				   if(done)
					   g.setColor(new Color(0, 0, 255, 100)); //Translucent color
				   else
					   g.setColor(new Color(255, 0, 0, 100));    
				    g.fillRect(c.x * cs, c.y * cs, cs, cs);
				    
				    g.setColor(new Color(0,255,0,100)); 
				    g.fillRect(board[board.length-1][board.length-1].x * cs, board[board.length-1][board.length-1].y * cs, cs, cs);

				}
				
				/*
				 * The next 4 if statements are used to determine what walls to draw
				 * If the boolean variables that indicate the walls are false that means there is a wall there.
				 * It checks all 4 sides of each cell and draw accordingly 
				 */
				if(!c.right) {
					g.setColor(Color.black);
					draw(c.y,c.x + 1,c.y+1,c.x+1, g);
				}
				
				
				if(!c.left) {
					g.setColor(Color.black);
					draw(c.y,c.x,c.y +1,c.x, g);
				}
				
					
				if(!c.up) {
					g.setColor(Color.black);
					draw(c.y,c.x,c.y,c.x +1, g);
				
				}
				
					
				if(!c.down) {
					g.setColor(Color.black);
					draw(c.y +1,c.x,c.y +1,c.x +1, g);
				}
				
			}
			
		}
		
	}
	//Parameterized draw method that draws the lines
	private void draw(int row, int col, int row2, int col2, Graphics g){
		
		g.drawLine(col*cs, row*cs,col2*cs,row2*cs);
	}
	
	//Initializes board 
	private void init(int size){
		board = new Cell[size][size]; 
		parents = new Cell[size][size];
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				board[i][j] = new Cell(j,i,"■");
				if(i == board.length-1 && j ==board.length-1) {
					board[i][j].v = "g";
				}
			}
		}
	}
	
	//Method used to change the size of maze 
	public void changeSize(int s) {
		init(s);
		reset();
	}
	
	//Clears all the color from the maze
	public void clearColor() {
		shortest.clear();
		goal = false;
		ready = true;
		done = false;
		repaint();
		ready = false;
	}
	//Resets the maze completely
	public void reset() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				board[i][j].reset();
				parents[i][j] = null;
			}
		}
		
		visited.clear();
		clearColor();
		done = false;
		goal = false;
		repaint();
	}
	
	/*Helper method for dfs
	 * Gets a random direction from the potential ways you could move from the current cell
	 */
	private String ranDirection(Cell c) {
		ArrayList<String> res = new ArrayList<String>();
		if(vR(c)) {
			res.add("r");
		}
		if(vL(c)) {
			res.add("l");
		}
		if(vU(c)) {
			res.add("u");
		}
		if(vD(c)) {
			res.add("d");
		}
		if(!vR(c) && !vL(c) && !vU(c) && !vD(c) ){
			return "n";
		}
		return res.get(r.nextInt(res.size()));
	}
	
	/*
	 * Make board is used to create a maze via dfs. 
	 * The threads are used to animate the creation
	 */
	public void makeBoard() { 
		
		makeBoard(board[0][0]);
	}
	private void makeBoard(Cell c) {
		if(!visited.contains(c))
			visited.add(c);
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(() -> {
			repaint();
		});
	
		boolean hasN = true;
		while(hasN) {
			String x = ranDirection(c);
			if(x.equals("n")) {
				hasN = false;
				return;
			}
			else {
				switch(x) {
				/*
				 * The cases are decided based on the random direction chosen earlier.
				 * When it moves it opens the wall at the current cell and the opposite wall at the next one
				 * It then recursively calls itself to continue moving
				 */
					case "r":
						c.right = true;
						board[c.y][c.x+1].left = true;
						 makeBoard(board[c.y][c.x + 1]);
						 break;
					case "l":
						c.left = true;
						board[c.y][c.x-1].right = true;
						 makeBoard(board[c.y][c.x - 1]);
						 break;
					case "u":
						c.up = true;
						board[c.y-1][c.x].down = true;
						 makeBoard(board[c.y - 1][c.x]);
						 break;
					case "d":
						c.down = true;
						board[c.y+1][c.x].up = true;
						 makeBoard(board[c.y + 1][c.x]);
						 break;
				}
			}
			
		}
		
		return;
	}
	
	//Same as make board just searches through the maze rather than change it
	 public void dfsPath() {
		 visited.clear();
		 done = true;
		 ready = false;
		 dfsPath(board[0][0]);
	 }
	private void dfsPath(Cell c) {
		if(goal) {
			return;
		}
		if(c.v.equals("g")) {
			goal = true;
			return;
		}
		//if(visited.size() == 24*24)
			//return;
		visited.add(c);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(() -> {
			repaint();
		});
		
		boolean hasN = true;
		while(hasN) {
			String x = ranDirection(c);
			if(x.equals("n")) {
				hasN = false;
				return;
			}
			else if(goal) {
				return;
			}
			else {
				switch(x) {
					case "r":
						 dfsPath(board[c.y][c.x + 1]);
						 break;
					case "l":
						dfsPath(board[c.y][c.x - 1]);
						 break;
					case "u":
						dfsPath(board[c.y - 1][c.x]);
						 break;
					case "d":
						dfsPath(board[c.y + 1][c.x]);
						 break;
				}
			}
			
		}
		return;
	}
	
	/*Basic breadth first search
	 * Except it saves the parents of each cell inorder to find the shortest path 
	 */
	public void bfs() {
		visited.clear();
		parents = new Cell[board.length][board.length];
		Queue<Cell> q = new LinkedList<Cell>();
		done = true;
		bfs(board[0][0], q);
	}
	
	private void bfs(Cell c, Queue<Cell> q) {
		q.add(c);
		parents[0][0] = null;
		while(!q.isEmpty()) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			SwingUtilities.invokeLater(() -> {
				repaint();
			});
			Cell currC = q.remove();
			if(currC.v.equals("g"))
				break;
			visited.add(currC);
			if(vL(currC)){
				parents[currC.y][currC.x-1] = currC;
				q.add(board[currC.y][currC.x-1]);
			}
			if(vD(currC)){
				parents[currC.y+1][currC.x] = currC;
				q.add(board[currC.y+1][currC.x]);
			}
			if(vR(currC)){
				parents[currC.y][currC.x+1] = currC;
				q.add(board[currC.y][currC.x+1]);
			}
			if(vU(currC)){
				parents[currC.y-1][currC.x] = currC;
				q.add(board[currC.y-1][currC.x]);
			}
		}
	}
	
	//Calls bfs first then uses the board and the parents 2d array to follow the parents starting from the goal to get the shortest path
	public void findShortest() {
		bfs();
		parents[0][0] = null;
		Cell curr = null;
		Cell par = null;
		curr = board[board.length-1][board.length-1];
		shortest.add(curr);
		par = parents[curr.y][curr.x];
			while(true) {
				shortest.addFirst(par);
				par = parents[par.y][par.x];
				if(par == null)
					break;
				
			}
			shortReady = true;
			repaint();
	}
	//Checks the validity of the right cell
	private boolean vR(Cell c) {
		 int nx = c.x + 1;
		 int ny = c.y;
		 if (!isValid(ny, nx)) 
			 return false; 
		 if(done && !c.right)
			 return false;
		 return !visited.contains(board[ny][nx]);
	}
	//Checks the validity of the left cell
	private boolean vL(Cell c) {
		 int nx = c.x - 1;
		 int ny = c.y;
		 if (!isValid(ny, nx)) 
			 return false; 
		 if(done && !c.left)
			 return false;
		 return !visited.contains(board[ny][nx]);
	}
	//Checks the validity of the down cell
	private boolean vD(Cell c) {
		 int nx = c.x;
		 int ny = c.y + 1;
		 if (!isValid(ny, nx)) 
		    return false;
		 if(done && !c.down)
			 return false;
		 
		 return !visited.contains(board[ny][nx]);
	}
	//Checks the validity of the up cell
	private boolean vU(Cell c) {
		 int nx = c.x;
		 int ny = c.y - 1;
		 if (!isValid(ny, nx)) 
		    return false;    
		 if(done && !c.up)
			 return false;
		 return !visited.contains(board[ny][nx]);
	}
	//Print method used for testing
	public void printBoard() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j].v);
			}
			System.out.println();
		}
	}
	//Checks if location is a valid in the 2d array
	private boolean isValid(int row, int col) {
		return row < board.length && row >= 0 && col < board[0].length && col >= 0;
	}
	
	
}
