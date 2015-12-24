package client_side;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client{
	Socket clientSocket;
    BufferedReader in;
    PrintWriter out;
    Scanner sc = new Scanner(System.in);
    static String retStr;
    
    
    public String connectToServer(String hostname, int portNumber){
    	
    	try {
    		clientSocket = new Socket(hostname,portNumber);
    		out = new PrintWriter(clientSocket.getOutputStream());
    		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    		
    		Thread out_msg = new Thread(new Runnable() {
    			String msg;
    			@Override
    			public void run() {
    				while(sc.hasNextLine()){
    					msg = sc.nextLine();
    					if(msg.contains(" ")){
    						System.out.println("One word at a time please...");
    						continue;
    					}
    					out.println(msg);
    					out.flush();
    				}
    				sc.close();
    			}
    		});
    		out_msg.start();
 
    		Thread in_msg = new Thread(new Runnable() {
    			String msg;
    			@Override
    			public void run() {
    				try {
    					retStr = "";
    					msg = in.readLine();
    					while(msg!=null){
    						System.out.println(msg);
    						retStr = msg;
    						msg = in.readLine();
    					}
    					System.out.println("Disconnect from server...");
    					out.close();
    					clientSocket.close();
    				}catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    		});
    		in_msg.start();
      
    	}catch (IOException e) {
    		e.printStackTrace();
    	}
    	return retStr;
    }
    public static void main(String[] args) {
    	String hostname = "127.0.0.1";
    	int portNumber = 4444;
    	Client test = new Client();
    	test.connectToServer(hostname, portNumber);
    	
    	
    }
}