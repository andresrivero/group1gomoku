import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GomokuPlayer {
	
	protected Socket socket;			
    protected PrintWriter output;            
    protected BufferedReader input;
    protected String setup;
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
	public List<String> getBoard() throws Exception {
		
		String line = null;
		List<String> board = new ArrayList<String>();
		  for (int x = 0; x < 11; x ++ ) {
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
		boolean start = false;
		GomokuPlayer ai = new GomokuPlayer("localhost", DEFAULTPORT);
		List<String> board = null;
		Scanner scan = new Scanner(System.in);
		while (true) {
			try {
				
	            System.out.print("Game state: " + ai.input.readLine() + "\n");
	            
	            for (int x = 0; x < 11; x ++ ) {

	                System.out.print(x + ":" + ai.input.readLine() + "\n");
	            }

	            System.out.print("My piece: " + ai.input.readLine() + "\n");
	            System.out.println("Setup port input buffered reader ");
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
