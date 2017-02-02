import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GomokuPlayer {
	
	protected Socket socket;			
    protected PrintWriter output;            
    protected BufferedReader input;
    public static final int DEFAULTPORT = 17033;    
    
	public GomokuPlayer(String host, int port) {
		
			openSocket(host, port);
			
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
            System.err.println("Failed I/O for: " + host);
            System.exit(1);
        }
    }
	public List<String> getBoard(int length) throws Exception {
		
		String line = null;
		List<String> board = new ArrayList<String>();
		  for (int x = 0; x < length; x ++ ) {
			  line = input.readLine();
              board.add(line);
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
	public static void main(String[] args) {
		boolean start = true;
		GomokuPlayer ai = new GomokuPlayer("localhost", DEFAULTPORT);
		List<String> board = new ArrayList<String>();
		String state = null;
		String line = null;
		int length = 0;
		String self = null;
		Scanner scan = new Scanner(System.in);
		while (true) {
			if (start) {
				try {
					state = ai.input.readLine();
		            System.out.print("Game state: " + state + "\n");
		            if (!state.matches("continuing|win|lose|draw|forfeit-time|forfeit|move")){
		            	System.out.println("Invalid string!");
		            	return;
		            }
		            line = ai.input.readLine();
		            length = line.length();
		            board.add(line);
		            for (int x = 0; x < length-1; x++) {
		            	line = ai.input.readLine();
		            	board.add(line);
		            }
		            self = ai.input.readLine();
		            if (!self.matches("o|x")){
		            	System.out.println("Invalid string!");
		            	return;
		            }
		            System.out.print("My piece: " + self + "\n");
		            System.out.println("Setup port input buffered reader ");
					System.out.println("Enter move: ");

					ai.sendMove(scan.nextLine());
					
					System.out.println("--------------");
					
					start = false;
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {

					state = ai.input.readLine();
		            System.out.print("Game state: " + state + "\n");
		            if (!state.matches("continuing|win|lose|draw|forfeit-time|forfeit-move")){
		            	System.out.println("Invalid string!");
		            	return;
		            } 
		            if (state.matches("win|lose|draw|forfeit-time|forfeit-move")) {
		            	System.out.println("Game over, man!");
		            	return;
		            }
		            
		            board = ai.getBoard(length);
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
		            	for (String value : board.get(x).split("")){
		            		if (value.equals("")){
		            			System.out.print("[ ]");
		               		} else {
		               			System.out.print("["+value+"]");
		               		}
		            	}
		            	System.out.println();
		            }
		            self = ai.input.readLine();
				    if (!self.matches("o|x")){
				         System.out.println("Invalid string!");
				         return;
				    }
				    System.out.print("My piece: " + self + "\n");
				    
					System.out.println("Enter move: ");

					ai.sendMove(scan.nextLine());
					System.out.println("--------------");
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
				
		}
	}

}
