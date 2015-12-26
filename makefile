
JCC = javac


JFLAGS = -g


default: CountWords.class Server.class Client.class


CountWords.class:
		$(JCC) $(JFLAGS) $ doc_process/CountWords.java

Server.class:
		$(JCC) $(JFLAGS) $ server_side/Server.java

Client.class:
		$(JCC) $(JFLAGS) $ client_side/Client.java

clean: 
	$(RM) -r *.class