package doc_process;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;


public class CountWords{
	 public static Map<String,Integer> dict = new ConcurrentHashMap<String,Integer>();
	 
	 public static Map<String,Integer> getDict(){
		 return CountWords.dict;
	 }
	 
	 public void countFreq(String fileAddr){
		 File dir = new File(fileAddr);
		 File[] directoryListing = dir.listFiles();
		 if (directoryListing != null) {
		    for (File child : directoryListing) {
		    	String extension = "";
		    	int i = child.toString().lastIndexOf('.');
		    	if (i >= 0) {
		    	    extension = child.toString().substring(i+1);
		    	}
		    	if(extension.equals(new String("txt"))){
			    	CountOneFile counter = new CountOneFile(child);
					 Thread t = new Thread(counter);
					 t.start();
					 try{
						 t.join();
					 }catch( Exception e) {
				         System.out.println("Interrupted");
				     }
		    	}
		    }
		  }
	 }
	 
	 public int yieldResult(String word){ 
		 word = word.toLowerCase().trim();
		 if(dict.containsKey(word)) return dict.get(word);
		 else return 0;
	}
	 
	 static public void main(String[] args){
		 CountWords test = new CountWords();
		 test.countFreq("/Users/LH/Downloads/txtfiles");
		 System.out.println(test.yieldResult("can"));
	 }
}


class CountOneFile implements Runnable {
	private File fileAddr;
	
	public CountOneFile(File fileAddr){
		this.fileAddr = fileAddr;
	}

	public void run(){
		try{
			FileReader fileReader = new FileReader(this.fileAddr);
			BufferedReader bufferedReader = new BufferedReader(fileReader);	
			String line = null;
			while((line = bufferedReader.readLine()) != null) {
				String str = line.replaceAll("[^a-zA-Z|']", " ");
				str = str.replaceAll("\\s+", " ").trim();
				str = str.toLowerCase();
				String[] word = str.split(" ");
				for(String s:word){
					if(s.length()!=0){
						Map<String,Integer> table = CountWords.getDict();
						if(table.containsKey(s)){
							table.put(s,new Integer(CountWords.dict.get(s)+1));
						}else{
							table.put(s,1);
						}
					}
				}
				
            }   
            bufferedReader.close();  
		}
        catch(FileNotFoundException ex) {
                System.out.println("Unable to open file '" + fileAddr + "'");                
            }
            catch(IOException ex) {
                System.out.println("Error reading file '" + fileAddr + "'");                  
            }
	}
}
