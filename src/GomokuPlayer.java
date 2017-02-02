import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GomokuPlayer {
	
	protected Socket socket;			
    protected PrintWriter output;            
    protected BufferedReader input;
    protected Board board;
    protected Board scores;
    
    private Random rand;
    
    public static final int DEFAULTPORT = 17033;    
    
	public GomokuPlayer(String host, int port) {
			this.rand = new Random(System.currentTimeMillis());
			openSocket(host, port);
			board = this.new Board(0,0);
			board = this.new Board(0,0);
	}
	
	public void openSocket(String host, int port) {
        
		try {
			
            socket = new Socket(host, port);
            System.out.println("Opened port");
            output = new PrintWriter(socket.getOutputStream(), true); 
            System.out.println("setup port output");
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
        } catch (UnknownHostException e) {
            System.err.println("Can't connect to host: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Failed I/O : " + host);
            System.exit(1);
        }
    }
	
	public Board getBoard(int length) throws Exception {
		
		String line = null;
		Board board = new Board(length, length);
		
		for (int x = 0; x < length; x ++ ) {
			  line = input.readLine();
              board.add(line.toCharArray(), x);
        }
		
		System.out.println("Got board");
		return board;
		
	}
	
	public void sendMove(String move) throws Exception {
		
			output.println(move);
	}
	
	public void play () {
		
	}
	public void assess() {
		
	}
	public void think() {
		
	}
	
	public String random() {
		int cols = this.board.cols;
		int rows = this.board.rows;
		int x = 0;
		int y = 0;
		String out = null;
		boolean thinking = true;
		x = this.rand.nextInt(rows);
		y = this.rand.nextInt(rows);
		while (thinking) {
			if(board.isEmpty()) {
				thinking = false;
				out = x + " " + y;
			} else if (this.board.gets(x, y).matches("x|o")){
				x = this.rand.nextInt(rows);
				y = this.rand.nextInt(rows);
				
			} else {
				out = x + " " + y;
				thinking = false;
				
			}
		}
		return out;
	}
	
	class Board {
		
		protected char[][] g;
		protected int rows;
		protected int cols;
		
		public Board(int rows, int cols) {
			this.rows = rows;
			this.cols = cols;
			this.g = new char[rows][cols];
			for (int x = 0; x < rows; x++) {
				for (int y = 0; y < cols; y++) {
					this.g[x][y] = ' ';
				}
			}
		}
		
		public char[] get(int row){
			return this.g[row];
		}
		public String gets(int row) {
			return String.copyValueOf(this.g[row]);
		}
		public String gets(int row, int col) {
			return String.valueOf(this.g[row][col]);
		}
		public boolean add(char[] value, int row) {
			this.g[row] = value;
			return true;
		}
		
		public boolean addcell(char value, int row, int col) {
			this.g[row][col] = value;
			return true;
		}
		
		public boolean isEmpty() {
			for (int x = 0; x < rows; x++) {
				for (int y = 0; y < cols; y++) {
					if (!(this.g[x][y] == ' ')) return false;
				}
			}
			return true;
		}
		
		public int length() {
			return this.cols;
		}
		
		public int height() {
			return this.rows;
		}
	}
	
	
	public static void main(String[] args) {
		
		GomokuPlayer gp = new GomokuPlayer("localhost", DEFAULTPORT);

		boolean start = true;
		String state = null;
		String line = null;
		int length = 0;
		String self = null;
		Scanner scan = new Scanner(System.in);

		while (true) {
			if (start) {
				try {
					state = gp.input.readLine();
					System.out.print("Game state: " + state + "\n");
					
		            if (!state.matches("continuing|win|lose|draw|forfeit-time|forfeit-move")){
		            	System.out.println("Invalid string!");
		            	return;
		            } 
		            
		            if (state.matches("win|lose|draw|forfeit-time|forfeit-move")) {
		            	System.out.println("Game over, man!");
		            	return;
		            }
		            
		            line = gp.input.readLine();
		            length = line.length();
		            
		            gp.board = gp.new Board(length, length);
		            gp.scores = gp.new Board(length, length);
		            
		            gp.board.add(line.toCharArray(), 0);
		            for (int x = 0; x < length-1; x++) {
		            	line = gp.input.readLine();
		            	gp.board.add(line.toCharArray(), x+1);
		            }
		            
		            for (int x = 0; x < length; x++) {
		            	gp.scores.add(gp.board.get(x), x);
		            }
		            
		            System.out.println("Blank board");
		            self = gp.input.readLine();
		            if (!self.matches("o|x")){
		            	System.out.println("Invalid string!");
		            	return;
		            }
		            
		            System.out.println("Score board");
		            for (int x = 0; x < length; x++){
		            	if (x < 10) System.out.print(x + "  ");
		            	else System.out.print(x + " ");
		            	if (x == length-1) {
		            		System.out.println();
		            		
		            	}
		            }

		            for (int x = 0; x < length; x++){
		            	if (x < 10) {
		            		System.out.print(x + " ");
		            	} else {
		            		System.out.print(x);
		            	}
		            	for (String value : gp.scores.gets(x).split("")){
		            		if (value.equals("")){
		            			System.out.print("[ ]");
		               		} else {
		               			System.out.print("["+value+"]");
		               		}
		            	}
		            	System.out.println();
		            }
		            
		            System.out.print("My piece: " + self + "\n");
		            System.out.println("Setup port input buffered reader ");
					System.out.println("Enter move: ");

					//gp.sendMove(gp.random());
					gp.sendMove(scan.nextLine());
					
					System.out.println("--------------");
					
					start = false;
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {

					state = gp.input.readLine();
		            System.out.print("Game state: " + state + "\n");
		            if (!state.matches("continuing|win|lose|draw|forfeit-time|forfeit-move")){
		            	System.out.println("Invalid string!");
		            	return;
		            } 
		            if (state.matches("win|lose|draw|forfeit-time|forfeit-move")) {
		            	System.out.println("Game over, man!");
		            	return;
		            }
		           
		            gp.board = gp.getBoard(length);
		            gp.scores = gp.new Board(length, length);

		            for (int x = 0; x < length; x++) {
		            	gp.scores.add(gp.board.get(x), x);
		            }
		            System.out.print("   ");
		            for (int x = 0; x < length; x++){
		            	if (x < 10) System.out.print(x + "  ");
		            	else System.out.print(x + " ");
		            	if (x == length-1) {
		            		System.out.println();
		            		
		            	}
		            }

		            for (int x = 0; x < length; x++){
		            	if (x < 10) {
		            		System.out.print(x + " ");
		            	} else {
		            		System.out.print(x);
		            	}
		            	for (String value : gp.board.gets(x).split("")){
		            		if (value.equals("")){
		            			System.out.print("[ ]");
		               		} else {
		               			System.out.print("["+value+"]");
		               		}
		            	}
		            	System.out.println();
		            }
		            
		            System.out.println("Score board");
		            for (int x = 0; x < length; x++){
		            	if (x < 10) System.out.print(x + "  ");
		            	else System.out.print(x + " ");
		            	if (x == length-1) {
		            		System.out.println();
		            		
		            	}
		            }

		            for (int x = 0; x < length; x++){
		            	if (x < 10) {
		            		System.out.print(x + " ");
		            	} else {
		            		System.out.print(x);
		            	}
		            	for (String value : gp.scores.gets(x).split("")){
		            		if (value.equals("")){
		            			System.out.print("[ ]");
		               		} else {
		               			System.out.print("["+value+"]");
		               		}
		            	}
		            	System.out.println();
		            }
		            
		            
		            self = gp.input.readLine();
				    if (!self.matches("o|x")){
				         System.out.println("Invalid string!");
				         return;
				    }
				    System.out.print("My piece: " + self + "\n");
				    
					System.out.println("Enter move: ");

					//gp.sendMove(gp.random());
					gp.sendMove(scan.nextLine());
					System.out.println("--------------");
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
				
		}
	}

}
