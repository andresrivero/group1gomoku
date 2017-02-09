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
    protected String self;
    protected int[][] scored;
    private Random rand;
    
    public static final int DEFAULTPORT = 17033;    
    
	public GomokuPlayer(String host, int port) {
			this.rand = new Random(System.currentTimeMillis());
			openSocket(host, port);
			board = this.new Board(0,0);
			board = this.new Board(0,0);
			self = new String();
			
			
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
	
	public void scoreParse (Board board, char mine) {
		char enemy = 'x';
		if (mine == 'o') {
			
		} else {
			enemy = 'o';
		}
		this.scored = new int[board.height()][board.length()];
		for (int x = 0; x< board.height(); x++){
			for (int y = 0; y < board.length(); y++) {
				this.scored[x][y] = 0;
			}
		}
		//char gp.scored = new Board(board.height(), board.length());
		for (int row = 0; row< board.height()-1; row++) {
			for (int col = 0; col < board.length()-1; col++) {
				if (board.gets(row, col).matches("o|x")) {
					if (board.gets(row, col).matches("o")) scored[row][col] = -1000;
					else scored[row][col] = 1000;
				} else {
					if (row == 0) {
						
						if (col == 0) {
							if (board.g[0][1] == mine)  {
								scored[row][col]++;
							} else if (board.g[0][1] == enemy) {
								scored[row][col]--;
							}
							if (board.g[1][1] == mine)  {
								scored[row][col]++;
							} else if (board.g[1][1] == enemy) {
								scored[row][col]--;
							}
							if (board.g[1][0] == mine)  {
								scored[row][col]++;
							} else if (board.g[1][0] == enemy) {
								scored[row][col]--;
							}
							
							
						} else if (col == board.length()) {
							if (board.g[0][board.length()-1] == mine)  {
								scored[row][col]++;
							} else if (board.g[0][board.length()-1] == enemy) {
								scored[row][col]--;
							}
							if (board.g[1][board.length()-1] == mine)  {
								scored[row][col]++;
							} else if (board.g[1][board.length()-1] == enemy) {
								scored[row][col]--;
							}
							if (board.g[1][board.length()] == mine)  {
								scored[row][col]++;
							} else if (board.g[1][board.length()] == enemy) {
								scored[row][col]--;
							}
							
						} else {
							//if (col < board.length()-1) {
								if (board.g[row][col-1] == mine) {
									scored[row][col]++;
								} else if (board.g[row][col-1] == enemy) {
									scored[row][col]--;
								}
								if (board.g[row+1][col-1] == mine) {
									scored[row][col]++;
								} else if (board.g[row+1][col-1] == enemy) {
									scored[row][col]--;
								}
								if (board.g[row+1][col] == mine) {
									scored[row][col]++;
								} else if (board.g[row+1][col] == enemy) {
									scored[row][col]--;
								}

								if (board.g[row+1][col+1] == mine) {
									scored[row][col]++;
								} else if (board.g[row+1][col+1] == enemy) {
									scored[row][col]--;
								}
								if (board.g[row][col+1] == mine) {
									scored[row][col]++;
								} else if (board.g[row][col+1] == enemy) {
									scored[row][col]--;
								}
								
							//}
							
						}
						
					} else if (row == board.height()) {
						if (col == 0) {
							if (board.g[board.height()-1][0] == mine)  {
								scored[row][col]++;
							} else if (board.g[board.height()-1][0] == enemy) {
								scored[row][col]--;
							}
							if (board.g[board.height()-1][1] == mine)  {
								scored[row][col]++;
							} else if (board.g[board.height()-1][1] == enemy) {
								scored[row][col]--;
							}
							if (board.g[board.height()][1] == mine)  {
								scored[row][col]++;
							} else if (board.g[board.height()][1] == enemy) {
								scored[row][col]--;
							}
							
						} else if (col == board.length()) {
							if (board.g[board.height()][board.length()-1] == mine)  {
								scored[row][col]++;
							} else if (board.g[board.height()][board.length()-1] == enemy) {
								scored[row][col]--;
							}
							if (board.g[board.height()-1][board.length()-1] == mine)  {
								scored[row][col]++;
							} else if (board.g[board.height()-1][board.length()-1] == enemy) {
								scored[row][col]--;
							}
							if (board.g[board.height()-1][board.length()] == mine)  {
								scored[row][col]++;
							} else if (board.g[board.height()-1][board.length()] == enemy) {
								scored[row][col]--;
							}
							
							
						} else {
							//

							if (board.g[row][col-1] == mine) {
								scored[row][col]++;
							} else if (board.g[row][col-1] == enemy) {
								scored[row][col]--;
							}
							if (board.g[row-1][col-1] == mine) {
								scored[row][col]++;
							} else if (board.g[row-1][col-1] == enemy) {
								scored[row][col]--;
							}
							if (board.g[row-1][col] == mine) {
								scored[row][col]++;
							} else if (board.g[row-1][col] == enemy) {
								scored[row][col]--;
							}

							if (board.g[row-1][col+1] == mine) {
								scored[row][col]++;
							} else if (board.g[row-1][col+1] == enemy) {
								scored[row][col]--;
							}
							if (board.g[row][col+1] == mine) {
								scored[row][col]++;
							} else if (board.g[row][col+1] == enemy) {
								scored[row][col]--;
							}
						}
					} else if (col == 0 && (row > 0 || row < board.height())) {
						//sides
						if (board.g[row-1][col] == mine) {
							scored[row][col]++;
						} else if (board.g[row-1][col] == enemy) {
							scored[row][col]--;
						}
						if (board.g[row-1][col+1] == mine) {
							scored[row][col]++;
						} else if (board.g[row-1][col+1] == enemy) {
							scored[row][col]--;
						}
						if (board.g[row][col+1] == mine) {
							scored[row][col]++;
						} else if (board.g[row][col+1] == enemy) {
							scored[row][col]--;
						}

						if (board.g[row+1][col+1] == mine) {
							scored[row][col]++;
						} else if (board.g[row+1][col+1] == enemy) {
							scored[row][col]--;
						}
						if (board.g[row+1][col] == mine) {
							scored[row][col]++;
						} else if (board.g[row+1][col] == enemy) {
							scored[row][col]--;
						}
					} else if (col == board.length() && (row > 0 || row < board.height())){
						if (board.g[row-1][col] == mine) {
							scored[row][col]++;
						} else if (board.g[row-1][col] == enemy) {
							scored[row][col]--;
						}
						if (board.g[row-1][col-1] == mine) {
							scored[row][col]++;
						} else if (board.g[row-1][col-1] == enemy) {
							scored[row][col]--;
						}
						if (board.g[row][col-1] == mine) {
							scored[row][col]++;
						} else if (board.g[row][col-1] == enemy) {
							scored[row][col]--;
						}

						if (board.g[row+1][col-1] == mine) {
							scored[row][col]++;
						} else if (board.g[row+1][col-1] == enemy) {
							scored[row][col]--;
						}
						if (board.g[row][col+1] == mine) {
							scored[row][col]++;
						} else if (board.g[row][col+1] == enemy) {
							scored[row][col]--;
						}
					} else {
						
						//inner
						
						if (board.g[row-1][col-1] == mine) {
							scored[row][col]++;
						} else if (board.g[row-1][col-1] == enemy) {
							scored[row][col]--;
						}
						if (board.g[row-1][col] == mine) {
							scored[row][col]++;
						} else if (board.g[row-1][col] == enemy) {
							scored[row][col]--;
						}
						if (board.g[row-1][col+1] == mine) {
							scored[row][col]++;
						} else if (board.g[row-1][col+1] == enemy) {
							scored[row][col]--;
						}
						if (board.g[row][col+1] == mine) {
							scored[row][col]++;
						} else if (board.g[row][col+1] == enemy) {
							scored[row][col]--;
						}

						if (board.g[row+1][col+1] == mine) {
							scored[row][col]++;
						} else if (board.g[row+1][col+1] == enemy) {
							scored[row][col]--;
						}
						if (board.g[row+1][col] == mine) {
							scored[row][col]++;
						} else if (board.g[row+1][col] == enemy) {
							scored[row][col]--;
						}
						if (board.g[row+1][col-1] == mine) {
							scored[row][col]++;
						} else if (board.g[row+1][col-1] == enemy) {
							scored[row][col]--;
						}
						if (board.g[row][col-1] == mine) {
							scored[row][col]++;
						} else if (board.g[row][col-1] == enemy) {
							scored[row][col]--;
						}
						
					}
					
				}
			}
		}
		
	}
	public static void main(String[] args) {
		
		GomokuPlayer gp = new GomokuPlayer("localhost", DEFAULTPORT);
		gp.self = null;
		boolean start = true;
		String state = null;
		String line = null;
		int length = 0;
		
		Scanner scan = new Scanner(System.in);

        gp.scores = gp.new Board(length, length);
        
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
		            
		            gp.board.add(line.toCharArray(), 0);
		            for (int x = 0; x < length-1; x++) {
		            	line = gp.input.readLine();
		            	gp.board.add(line.toCharArray(), x+1);
		            }
		            
		           
		            
		            System.out.println("Blank board");
		            gp.self = gp.input.readLine();
		            if (!gp.self.matches("o|x")){
		            	System.out.println("Invalid string!");
		            	return;
		            }
		            
		           
		            gp.scoreParse(gp.board, gp.self.charAt(0));
		            
//		            for (int x = 0; x < length; x++) {
//		            	gp.scores.add(gp.board.get(x), x);
//		            }
//		            
		            System.out.println("Score board");
		            for (int x = 0; x < length; x++){
		            	if (x < 10) System.out.print(x + "  ");
		            	else System.out.print(x + " ");
		            	if (x == length-1) {
		            		System.out.println();
		            		
		            	}
		            }
		            for (int x = 0; x < length; x++) {
		            	if (x < 10) {
		            		System.out.print(x + " ");
		            	} else {
		            		System.out.print(x);
		            	}
		            	for (int y = 0; y< length; y++) {
		            		System.out.println("["+gp.scored[x][y]+"]");
		            	}

		            	System.out.println();
		            }
		            	
//		            for (int x = 0; x < length; x++){
//		            	
//		            	for (String value : gp.scores.gets(x).split("")){
//		            		if (value.equals("")){
//		            			System.out.print("[ ]");
//		               		} else {
//		               			System.out.print("["+value+"]");
//		               		}
//		            	}
//		            	System.out.println();
//		            }
		            
		            System.out.print("My piece: " + gp.self + "\n");
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
		            //gp.scores = gp.new Board(length, length);

//		            for (int x = 0; x < length; x++) {
//		            	gp.scores.add(gp.board.get(x), x);
//		            }
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
		            
//		            System.out.println("Score board");
//		            for (int x = 0; x < length; x++){
//		            	if (x < 10) System.out.print(x + "  ");
//		            	else System.out.print(x + " ");
//		            	if (x == length-1) {
//		            		System.out.println();
//		            		
//		            	}
//		            }
//
//		            for (int x = 0; x < length; x++){
//		            	if (x < 10) {
//		            		System.out.print(x + " ");
//		            	} else {
//		            		System.out.print(x);
//		            	}
//		            	for (String value : gp.scores.gets(x).split("")){
//		            		if (value.equals("")){
//		            			System.out.print("[ ]");
//		               		} else {
//		               			System.out.print("["+value+"]");
//		               		}
//		            	}
//		            	System.out.println();
//		            }
		            gp.scoreParse(gp.board, gp.self.charAt(0));
		            
//		            for (int x = 0; x < length; x++) {
//		            	gp.scores.add(gp.board.get(x), x);
//		            }
//		            
		            System.out.println("Score board");
		            for (int x = 0; x < length; x++){
		            	if (x < 10) System.out.print(x + "  ");
		            	else System.out.print(x + "  ");
		            	if (x == length-1) {
		            		System.out.println();
		            		
		            	}
		            }
		            for (int x = 0; x < length; x++) {
		            	if (x < 10) {
		            		System.out.print(x + " ");
		            	} else {	
		            		System.out.print(x);
		            	}
		            	for (int y = 0; y< length; y++) {
		            		if (gp.scored[x][y] == -1000 || gp.scored[x][y] == 1000) {
			            		System.out.print("[ "+gp.board.g[x][y] + " ]");

		            		} else {
		            			if (gp.scored[x][y] <= -10 || gp.scored[x][y] >= 100) {
				            		System.out.print("["+gp.scored[x][y]+"]");

		            			} else if (gp.scored[x][y] == 0) {
				            		System.out.print("[  0]");

		            			}else if (gp.scored[x][y] < 0) {
				            		System.out.print("[ "+gp.scored[x][y]+"]");

		            			}else if (gp.scored[x][y] > 0) {
				            		System.out.print("[  "+gp.scored[x][y]+"]");

		            			}

		            		}
		            	}

		            	System.out.println();
		            }
		            
		            gp.self = gp.input.readLine();
				    if (!gp.self.matches("o|x")){
				         System.out.println("Invalid string!");
				         return;
				    }
				    System.out.print("My piece: " + gp.self + "\n");
				    
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
