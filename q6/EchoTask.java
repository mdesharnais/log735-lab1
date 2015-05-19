package q6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class EchoTask implements Runnable {
	private final Socket client;
	
	public EchoTask(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		System.out.println("Connection successful.");
		System.out.println("Waiting for input from client...");

		// Try with resources to automatically manage the streams' resources
		try (PrintWriter out = new PrintWriter(this.client.getOutputStream(), true);
			 BufferedReader in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));) {
			String line;

			while ((line = in.readLine()) != null) {
				int currentCount = 0;
				System.out.printf("Server: '%s'\n", line);
				
	        	if (line.equals("Bye.")) {
	        		break;
	        	}
	        	
				Random rand = new Random();
				int randomInt = rand.nextInt(5);

				if (randomInt == 0) {
    				try {
						Thread.sleep(15000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					currentCount = Server.IncrementQueryCounter();
				}
	        	
	        	out.printf("#%d - %s\n", currentCount, line.toUpperCase());
	        }
		} catch (IOException e) {
			System.err.println("Error reading from streams.");
			e.printStackTrace();
		} finally {
			try {
				this.client.close();
			} catch (Exception e) {
				System.err.println("Error closing socket.");
			}
		}
	}
}