package simulator;

import java.util.ArrayList;
import java.util.UUID;

public class Query {
	public File requestedFile;
	public Node requester;
	public Node sender;
	public ArrayList<Node> nodesVisited;
	
	Query(){};
	
	Query(File currFile, Node currNode){
		requestedFile = currFile;
		requester = currNode;
		sender = currNode;
		nodesVisited = new ArrayList<Node>();
				
	}
	
	public void update(Node currNode){
		
		sender = currNode;
		nodesVisited.add(sender);
	}
	
}
