package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import doc_process.CountWords;

public class Server {
	static ServerSocket serverSocket;
	static Map<String, Integer> wordCountMap;
	static Map<String, Integer> wordReqMap;
	
	private int portNumber;
	
	public Server(int portNumber){
		this.portNumber = portNumber;
	}
	
	public void startServer(){
		Socket clientSocket;
		// Document process
		CountWords cw = new CountWords();
   	 	String folderAddr ="/Users/LH/Downloads/txtfiles";
   	 	System.out.println("Adding files from "+ folderAddr+"...");
   	 	cw.countFreq(folderAddr);
   	 	wordCountMap = CountWords.getDict();
   	 	System.out.println("Words counting complete...");
   	 	
		try{ 
	    	// Start server
			System.out.println("Server starting up...");
	    	serverSocket = new ServerSocket(this.portNumber);
	    	wordReqMap = new ConcurrentHashMap<String,Integer>();
	    	while(true){
	    		System.out.println("Server ready...");
	    		clientSocket = serverSocket.accept();
	    		System.out.println("Server now listening to: "+ clientSocket.getInetAddress().getHostName());
	    		doComm conn = new doComm(clientSocket);
	    		Thread t = new Thread(conn);
	    		t.start();
			} 
	     }catch (IOException e) {
	    	 e.printStackTrace();
	     }
	}
 
    public static void main(String[] args) {
    int portNumber = 4444;
    Server test = new Server(portNumber);
    test.startServer();
    }
}
        
class doComm implements Runnable{  
	Socket client;
    PrintWriter out;
	BufferedReader in;

	public doComm(Socket client){
		try {
			this.client = client;
			out =  new PrintWriter(client.getOutputStream());
			in = new BufferedReader (new InputStreamReader (client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
       Thread out_msg= new Thread(new Runnable() {
          String msg;
          @Override
          public void run() {
             while(true){
            	String sentToClient = "";
            	try {
					msg = in.readLine().toLowerCase().trim();
				}catch (IOException e) {
					e.printStackTrace();
				}
            	if(Server.wordReqMap.containsKey(msg)){
            		int tmp_value = Server.wordReqMap.get(msg)+1;
            		sentToClient = String.valueOf(tmp_value)+" ";
            		Server.wordReqMap.put(msg, tmp_value);
            	}else{
            		Server.wordReqMap.put(msg, 1);
            		sentToClient = String.valueOf(1)+" ";
            	}
				if(Server.wordCountMap.containsKey(msg)){
					out.println(sentToClient+Server.wordCountMap.get(msg).toString());
				}else out.println(sentToClient+String.valueOf(0));
                out.flush();
             }
          }
       });
       out_msg.start();
	}
}