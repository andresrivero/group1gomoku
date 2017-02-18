import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* GomokuPlayer for Lab 2
 * Requires ABPrune and Point
 * 
Elijah Anderson-Justis, Samuel Kuhlwein, Andres Rivero
Southern Oregon University
andersoe4@sou.edu, kuhlweins@sou.edu, riveroa@sou.edu
 */


public class GomokuPlayer {
	
	protected Socket socket;			
    protected PrintWriter output;            
    protected BufferedReader input;
    protected Board board;
    protected Board scores;
    protected String self;
    protected char mine;
    protected char enemy;
    protected int[][] scored;
    private Random rand;
    protected boolean fourInaRow;
    protected boolean enemyFourInaRow;
    protected boolean twoByTwo;
    public static final int DEFAULTPORT = 17033;    
    
	public GomokuPlayer(String host, int port) {
			this.rand = new Random(System.currentTimeMillis());
			openSocket(host, port);
			board = this.new Board(0,0);
			self = new String();
			this.fourInaRow = false;
			this.enemyFourInaRow = false;
			this.twoByTwo = false;
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
		public String getsHorizontal(int getrow) {
			String builder = null;
			for (int x = 0; x < this.cols; x++) {
				builder += this.gets(getrow, x);
			}
			return builder;
		}
		public String getsVertical(int getcol) {
			String builder = new String();
			for (int x = 0; x < this.rows; x++) {
				builder += this.gets(x, getcol);
			}
			return builder;
		}

		public String getHorizontal(int x) {
			// TODO Auto-generated method stub
			return null;
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
	
	public void scoreParse (Board board, char mine, boolean first) {
		char enemy = 'x';
		if (mine == 'o') {
			
		} else {
			enemy = 'o';
		}
		if (first) {
			this.scored = new int[board.height()][board.length()];
			for (int x = 0; x< board.height(); x++){
				for (int y = 0; y < board.length(); y++) {
					this.scored[x][y] = 0;
				}
			}
		} else {
			
		}
		
		//char gp.scored = new Board(board.height(), board.length());
		int bh = board.height()-1;
		int bl = board.length()-1;
		for (int row = 0; row<= bh; row++) {
			
			for (int col = 0; col <= bl; col++) {
				
				if (board.gets(row, col).matches("o|x")) {
					if (board.gets(row, col).matches("o")) {
						
						scored[row][col] = -1000000;
						
					}else if (board.gets(row, col).matches("x")) {
					
						 scored[row][col] = 1000000;
						 
					}
					
				} else {
					
					if (row == 0) {
						
						if (col == 0) {
							//top left corner
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
							
							
						} else if (col == bl) {
							//top right corner
							if (board.g[0][bl-1] == mine)  {
								scored[row][col]++;
							} else if (board.g[0][bl-1] == enemy) {
								scored[row][col]--;
							}
							
							if (board.g[1][bl-1] == mine)  {
								scored[row][col]++;
							} else if (board.g[1][bl-1] == enemy) {
								scored[row][col]--;
							}
							
							if (board.g[1][bl] == mine)  {
								scored[row][col]++;
							} else if (board.g[1][bl] == enemy) {
								scored[row][col]--;
							}
							
						} else {
							if (col < bl) {
								//top middle portion
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
								
							}
							
						}
						
					} else if (row == bh) {
						//bottom
						
						if (col == 0) {
							//bottom left corner
							
							if (board.g[bh-1][0] == mine)  {
								scored[row][col]++;
							} else if (board.g[bh-1][0] == enemy) {
								scored[row][col]--;
							}
							
							if (board.g[bh][1] == mine)  {
								scored[row][col]++;
							} else if (board.g[bh][1] == enemy) {
								scored[row][col]--;
							}
							
							if (board.g[bh-1][1] == mine)  {
								scored[row][col]++;
							} else if (board.g[bh-1][1] == enemy) {
								scored[row][col]--;
							}
							
						} else if (col == bl) {
							//bottom right corner
							if (board.g[bh][bl-1] == mine)  {
								scored[row][col]++;
							} else if (board.g[bh][bl-1] == enemy) {
								scored[row][col]--;
							}
							if (board.g[bh-1][bl-1] == mine)  {
								scored[row][col]++;
							} else if (board.g[bh-1][bl-1] == enemy) {
								scored[row][col]--;
							}
							if (board.g[bh-1][bl] == mine)  {
								scored[row][col]++;
							} else if (board.g[bh-1][bl] == enemy) {
								scored[row][col]--;
							}
							
							
						} else {
							//last middle portion

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
					} else if (col == 0 && (row > 0 || row < bh)) {
						
						//left side vertical
						
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
						
					} else if (col == bl && (row > 0 || row < bh)){
						//right side vertical
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
						
						if (board.g[row+1][col] == mine) {
							scored[row][col]++;
						} else if (board.g[row+1][col] == enemy) {
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
		
		this.findVertical();
		this.findHorizontal();
		
	}
	class Inarow {
		
		public int value;
		public int length;
		public String direction;
		public int startx;
		public int starty;
		
	}
	public void findDgDown() {
		
		for (int x = 0; x < board.length()-4; x++) {
			
			for (int count = 0; count < board.length()-4; count++) {
				
				if (this.board.g[x][count] == this.mine && this.board.g[x+1][count+1] == this.mine && this.board.g[x+2][count+2] == this.mine && this.board.g[x+3][count+3] == this.mine && this.board.g[x+4][count+4] == ' ')
				{
					this.scored[x+4][count+4]+= 9000;
					this.fourInaRow = true;
					
				} else if (this.board.g[x][count] == ' ' && this.board.g[x+1][count+1] == this.mine && this.board.g[x+2][count+2] == this.mine && this.board.g[x+3][count+3] == this.mine && this.board.g[x+4][count+4] == this.mine)
				{
					this.scored[x][count]+= 9000;
					this.fourInaRow = true;
					
				} else if (this.board.g[x][count] == this.mine && this.board.g[x+1][count+1] == this.mine && this.board.g[x+2][count+2] == ' ' && this.board.g[x+3][count+3] == this.mine && this.board.g[x+4][count+4] == this.mine)
				{
					this.scored[x+2][count]+= 9000;
					this.fourInaRow = true;
					
				}
				
			}
			
		}
		
	}
	
	
	public void findDgUp() {
		
		for (int x = board.length()-1; x > 3; x--) {
			
			for (int count = 0; count < board.length()-4; count++) {
				
				if (this.board.g[x][count] == this.mine && this.board.g[x-1][count+1] == this.mine && this.board.g[x-2][count+2] == this.mine && this.board.g[x-3][count+3] == this.mine && this.board.g[x-4][count+4] == ' ')
				{
					this.scored[x-4][count+4]+= 9000;
					this.fourInaRow = true;
					
				} else if (this.board.g[x][count] == ' ' && this.board.g[x-1][count+1] == this.mine && this.board.g[x-2][count+2] == this.mine && this.board.g[x-3][count+3] == this.mine && this.board.g[x-4][count+4] == this.mine)
				{
					this.scored[x][count]+= 9000;
					this.fourInaRow = true;
					
				} else if (this.board.g[x][count] == this.mine && this.board.g[x-1][count+1] == this.mine && this.board.g[x-2][count+2] == ' ' && this.board.g[x-3][count+3] == this.mine && this.board.g[x-4][count+4] == this.mine)
				{
					this.scored[x-2][count]+= 9000;
					this.fourInaRow = true;
					
				}
				
			}
			
		}
		
	}
	public void findVertical() {


		for (int x = 0; x < board.length(); x++) {
			String line = board.getsVertical(x);
			for (int count = 0; count < line.length(); count++) {
				
				if (count == 0) {
					if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == ' ') {
						this.scored[2][x]+= 100;
					} else if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == this.mine && line.toCharArray()[3] == ' ') {
						this.scored[3][x]= 200;
					} else if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == this.mine && line.toCharArray()[3] == this.mine && line.toCharArray()[4] == ' ') {
						this.scored[4][x]= 600;
						this.fourInaRow = true;
					} else if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == ' ' && line.toCharArray()[3] == this.mine && line.toCharArray()[4] == this.mine) {
						this.scored[2][x]+= 9000;
						this.fourInaRow = true;
						this.twoByTwo = true;
					}
					
				} else if (count > 0 && count < line.length()-5) {
					if (line.toCharArray()[count-1] == ' ' && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == ' ') {
						this.scored[count-1][x]+= 100;
						this.scored[count+2][x]+= 100;
					} else if (line.toCharArray()[count-1] == ' ' && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == this.mine && line.toCharArray()[count+3] == ' ') {
						this.scored[count-1][x]= 200;
						this.scored[count+3][x]= 200;
					} else if (line.toCharArray()[count-1] == ' ' && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == this.mine && line.toCharArray()[count+3] == this.mine && line.toCharArray()[count+4] == ' ') {
						this.scored[count-1][x]+= 1000;
						this.scored[count+4][x]+= 1000;
						this.fourInaRow = true;
					} else if (line.toCharArray()[count-1] == this.enemy && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == this.mine && line.toCharArray()[count+3] == this.mine && line.toCharArray()[count+4] == ' ') {
						this.scored[count+4][x]+= 1000;

						this.fourInaRow = true;
					} else if (line.toCharArray()[count-1] == ' ' && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == this.mine && line.toCharArray()[count+3] == this.mine && line.toCharArray()[count+4] == this.enemy) {
						this.scored[count-1][x]+= 1000;

						this.fourInaRow = true;
					} else if (line.toCharArray()[count] == this.mine &&  line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == ' ' && line.toCharArray()[count+3] == this.mine && line.toCharArray()[count+4] == this.mine) {
						this.scored[count+2][x]+= 9000;
						this.twoByTwo = true;
						this.fourInaRow = true;
					}
					
				} else if (count == line.length()-1) {

					if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == ' ') {
						this.scored[line.length()-3][x]+= 100;
					} else if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == this.mine && line.toCharArray()[line.length()-4] == ' ') {
						this.scored[line.length()-4][x]= 200;
					} else if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == this.mine && line.toCharArray()[line.length()-4] == this.mine && line.toCharArray()[line.length()-5] == ' ') {
						this.scored[line.length()-5][x]+= 600;
						this.fourInaRow = true;
					}else if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == ' ' && line.toCharArray()[line.length()-4] == this.mine && line.toCharArray()[line.length()-5] == this.mine) {
						this.scored[line.length()-3][x]+= 9000;
						this.twoByTwo = true;
						this.fourInaRow = true;
					}
					
				} 

				if (count == 0) {
					if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == this.mine && line.toCharArray()[3] == ' ') {
						this.scored[3][x]-= 200;
					} else if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == this.mine && line.toCharArray()[3] == this.mine && line.toCharArray()[4] == ' ') {
						this.scored[4][x]-= 600;
					}else if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == ' ' && line.toCharArray()[3] == this.mine && line.toCharArray()[4] == this.mine) {
						this.scored[2][x]-= 2000;
						this.enemyFourInaRow = true;
					}
					
				} else if (count > 0 && count < line.length()-5) {
					if (line.toCharArray()[count-1] == ' ' && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == this.mine && line.toCharArray()[count+3] == ' ') {
						this.scored[count-1][x]-= 200;
						this.scored[count+3][x]-= 200;
					} else if (line.toCharArray()[count-1] == ' ' && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == this.mine && line.toCharArray()[count+3] == this.mine && line.toCharArray()[count+4] == ' ') {
						this.scored[count-1][x]-= 1000;
						this.scored[count+4][x]-= 1000;
						this.enemyFourInaRow = true;
					} else if (line.toCharArray()[count] == this.mine &&  line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == ' ' && line.toCharArray()[count+3] == this.mine && line.toCharArray()[count+4] == this.mine) {
						this.scored[count+2][x]-= 2000;
						
						this.enemyFourInaRow = true;
					}
					
				} else if (count == line.length()-1) {

					if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == this.mine && line.toCharArray()[line.length()-4] == ' ') {
						this.scored[line.length()-4][x]-= 200;
					} else if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == this.mine && line.toCharArray()[line.length()-4] == this.mine && line.toCharArray()[line.length()-5] == ' ') {
						this.scored[line.length()-5][x]-= 600;
					}else if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == ' ' && line.toCharArray()[line.length()-4] == this.mine && line.toCharArray()[line.length()-5] == this.mine) {
						this.scored[line.length()-3][x]-= 2000;
						this.enemyFourInaRow = true;
					}
					
				} 
			
			}
			
			
			
		}

		
	}
	public void findHorizontal() {


		for (int x = 0; x < board.length(); x++) {
			String line = board.gets(x);
			for (int count = 0; count < line.length(); count++) {
				if (count == 0) {
					if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == this.mine && line.toCharArray()[3] == ' ') {
						this.scored[x][3]-= 200;
					} else if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == this.mine && line.toCharArray()[3] == this.mine && line.toCharArray()[4] == ' ') {
						this.scored[x][4]= 500;
					}else if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == ' ' && line.toCharArray()[3] == this.mine && line.toCharArray()[4] == this.mine) {
						this.scored[x][2]-= 2000;
						this.enemyFourInaRow = true;
					}
					
				} else if (count > 0 && count < line.length()-5) {
					if (line.toCharArray()[count-1] == ' ' && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == this.mine && line.toCharArray()[count+3] == ' ') {
						this.scored[x][count-1]-= 200;
						this.scored[x][count+3]-= 200;
					} else if (line.toCharArray()[count-1] == ' ' && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == this.mine && line.toCharArray()[count+3] == this.mine && line.toCharArray()[count+4] == ' ') {
						this.scored[x][count-1]-= 1000;
						this.scored[x][count+4]-= 1000;
						this.enemyFourInaRow = true;
					} else if (line.toCharArray()[count] == this.mine &&  line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == ' ' && line.toCharArray()[count+3] == this.mine && line.toCharArray()[count+4] == this.mine) {
						this.scored[x][count+2]-= 5000;
						
						this.enemyFourInaRow = true;
					}
					
				} else if (count == line.length()-1) {
					if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == this.mine && line.toCharArray()[line.length()-4] == ' ') {
						this.scored[x][line.length()-4]-= 200;
					} else if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == this.mine && line.toCharArray()[line.length()-4] == this.mine && line.toCharArray()[line.length()-5] == ' ') {
						this.scored[x][line.length()-5]-= 500;
					} else if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == ' ' && line.toCharArray()[line.length()-4] == this.mine && line.toCharArray()[line.length()-5] == this.mine) {
						this.scored[x][line.length()-3]-= 2000;
						this.enemyFourInaRow = true;
					}
					
				} 
				if (count == 0) {
					if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == ' ') {
						this.scored[x][2]+= 100;
					} else if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == this.mine && line.toCharArray()[3] == ' ') {
						this.scored[x][3]+= 200;
					} else if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == this.mine && line.toCharArray()[3] == this.mine && line.toCharArray()[4] == ' ') {
						this.scored[x][4]+= 500;
						this.fourInaRow = true;
					}else if (line.toCharArray()[0] == this.mine && line.toCharArray()[1] == this.mine && line.toCharArray()[2] == ' ' && line.toCharArray()[3] == this.mine && line.toCharArray()[4] == this.mine) {
						this.scored[x][2]+= 2000;
						this.fourInaRow = true;
					}
					
				} else if (count > 0 && count < line.length()-5) {
					if (line.toCharArray()[count-1] == ' ' && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == ' ') {
						this.scored[x][count-1]+= 100;
						this.scored[x][count+2]+= 100;
					} else if (line.toCharArray()[count-1] == ' ' && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == this.mine && line.toCharArray()[count+3] == ' ') {
						this.scored[x][count-1]= 200;
						this.scored[x][count+3]= 200;
					} else if (line.toCharArray()[count-1] == ' ' && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == this.mine && line.toCharArray()[count+3] == this.mine && line.toCharArray()[count+4] == ' ') {
						this.scored[x][count-1]+= 1000;
						this.scored[x][count+4]+= 1000;
						this.fourInaRow = true;
					} else if (line.toCharArray()[count-1] == this.enemy && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == this.mine && line.toCharArray()[count+3] == this.mine && line.toCharArray()[count+4] == ' ') {
						this.scored[x][count+4]+= 1000;

						this.fourInaRow = true;
					} else if (line.toCharArray()[count-1] == ' ' && line.toCharArray()[count] == this.mine && line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == this.mine && line.toCharArray()[count+3] == this.mine && line.toCharArray()[count+4] == this.enemy) {
						this.scored[x][count-1]+= 1000;

						this.fourInaRow = true;
					} else if (line.toCharArray()[count] == this.mine &&  line.toCharArray()[count+1] == this.mine && line.toCharArray()[count+2] == ' ' && line.toCharArray()[count+3] == this.mine && line.toCharArray()[count+4] == this.mine) {
						this.scored[x][count+2]+= 9000;
						this.twoByTwo = true;
						this.fourInaRow = true;
					}
					
				} else if (count == line.length()-1) {
					if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == ' ') {
						this.scored[x][line.length()-3]+= 100;
					} else if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == this.mine && line.toCharArray()[line.length()-4] == ' ') {
						this.scored[x][line.length()-4]= 200;
					} else if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == this.mine && line.toCharArray()[line.length()-4] == this.mine && line.toCharArray()[line.length()-5] == ' ') {
						this.scored[x][line.length()-5]+= 500;
						this.fourInaRow = true;
					} else if (line.toCharArray()[line.length()-1] == this.mine && line.toCharArray()[line.length()-2] == this.mine && line.toCharArray()[line.length()-3] == ' ' && line.toCharArray()[line.length()-4] == this.mine && line.toCharArray()[line.length()-5] == this.mine) {
						this.scored[x][line.length()-3]+= 9000;
						this.fourInaRow = true;
						this.twoByTwo = true;
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
        ABPrune prune = null;
        ArrayList<Point> pointList = new ArrayList<Point>();
        
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
		            
		            gp.self = gp.input.readLine();
		            
		            if (!gp.self.matches("o|x")){
		            	System.out.println("Invalid string!");
		            	return;
		            }
		            gp.mine = gp.self.charAt(0);
		            
		            if (gp.mine == 'o') {
		            	gp.enemy = 'x'; }
		            else {
		            	gp.enemy = 'o';
		            	
		            }
		            gp.scoreParse(gp.board, gp.self.charAt(0), true);
		            
	            
		            System.out.println("Score board");
		            for (int x = 0; x < length; x++){
		            	if (x < 10) System.out.print(x + "   ");
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
		            		System.out.print("["+gp.scored[x][y]+"]");
		            	}

		            	System.out.println();
		            }
		            	
		            
		            System.out.print("My piece: " + gp.self + "\n");
		            System.out.println("Setup port input buffered reader ");
					System.out.println("Enter move: ");

					gp.sendMove(gp.random());
					//
					
					for (int i = 0; i < gp.board.height(); i++) {
						
						for (int j = 0; j < gp.board.length(); j++) {
							
							if(gp.scored[i][j] == 0) {
								
							} else if ((gp.scored[i][j] > 100000) || (gp.scored[i][j] < -100000)){
								
							} else {
								pointList.add(new Point(gp.scored[i][j], i, j));		
							}
							
						}
					}
					System.out.println(pointList.size());
					if(pointList.size() < 81){
						int listsize = pointList.size();
						for (int z = 0; z < 81-listsize; z++){
							pointList.add(pointList.get(z%listsize));
							if (pointList.size() == 81) break;
						}
					}
					prune = new ABPrune(pointList);
					System.out.println("Max: " + prune.Prune(prune.root, new Point(Integer.MIN_VALUE, 0, 0), new Point(Integer.MAX_VALUE, 0, 0), true).value);
					//gp.sendMove(scan.nextLine());
					
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
		            gp.scoreParse(gp.board, gp.self.charAt(0), false);
		            
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
		            System.out.println(length + " ");
		            for (int x = 0; x < length; x++) {
		            	if (x < 10) {
		            		System.out.print(x + " ");
		            	} else {	
		            		System.out.print(x);
		            	}
		            	for (int y = 0; y< length; y++) {
		            		if (gp.scored[x][y] < -100000 || gp.scored[x][y] > 100000) {
		            			
			            		System.out.print("[ "+gp.board.g[x][y] + " ]");

		            		} else {
		            			
		            		    if (gp.scored[x][y] == 0) {
		            				
		            				System.out.print("[  0]");

		            			} else if (gp.scored[x][y] < 0) {
		            				
		            				System.out.print("[ "+gp.scored[x][y]+"]");

		            			} else if (gp.scored[x][y] > 0) {
		            				
		            				System.out.print("[  "+gp.scored[x][y]+"]");

		            			}

		            		}
		            	}

		            	System.out.println();
		            }
		            //gp.findVertical();
		            
		            gp.self = gp.input.readLine();
				    if (!gp.self.matches("o|x")){
				         System.out.println("Invalid string!");
				         return;
				    }
				    System.out.print("My piece: " + gp.self + "\n");
				    
					System.out.println("Enter move: ");
					
					pointList = new ArrayList<Point>();
					for (int i = 0; i < gp.board.height(); i++) {
						
						for (int j = 0; j < gp.board.length(); j++) {
							
							if(gp.scored[i][j] == 0) {
								
							} else if ((gp.scored[i][j] > 100000) || (gp.scored[i][j] < -100000)){
								
							} else if (gp.scored[i][j] != 0 && gp.scored[i][j] > -2){
								pointList.add(new Point(gp.scored[i][j], i, j));		
							}
							
						}
					}

					System.out.println(pointList.size());
					Collections.sort(pointList, new Comparator<Point>(){
					    @Override public int compare(Point s1, Point s2) {
					        return s2.value - s1.value;
					    }
					});
					
					System.out.println(pointList.size());
					
					if(pointList.size() < 81){
						if (pointList.size() > 10 && pointList.size() < 70) {
							for (int z = 0; z < 5; z++) {
								if (pointList.get(z).value > 500) pointList.add(pointList.get(z));
								if (pointList.get(z).value > 1000)  pointList.add(pointList.get(z));
								if (pointList.get(z).value > 500) {pointList.add(pointList.get(z));} else {
									pointList.add(pointList.get(z));

								}

							}
						}
						
						int listsize = pointList.size();
						for (int z = 0; z < 81-listsize; z++){
							pointList.add(pointList.get(z%listsize));
							if (pointList.size() == 81) break;
						}
						
					}
					
					prune = new ABPrune(pointList);
					Point toPlay = prune.Prune(prune.root, new Point(Integer.MIN_VALUE, 0, 0), new Point(Integer.MAX_VALUE, 0, 0), true);
					System.out.println(toPlay.value + " " + toPlay.row + " " +toPlay.col);
					if (gp.twoByTwo) {
						gp.sendMove(pointList.get(0).row + " " + pointList.get(0).col);
					} else if (gp.fourInaRow) {
						gp.sendMove(pointList.get(0).row + " " + pointList.get(0).col);
						gp.fourInaRow = false;
					} else if (gp.enemyFourInaRow) {
						Collections.sort(pointList, new Comparator<Point>(){
						    @Override public int compare(Point s1, Point s2) {
						        return s1.value - s2.value;
						    }
						});
						gp.sendMove(pointList.get(0).row + " " + pointList.get(0).col);
						gp.enemyFourInaRow = false;
					} else {
						gp.sendMove(toPlay.row + " " +toPlay.col);
					}
					
					System.out.println("--------------");
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
				
		}
	}

}
