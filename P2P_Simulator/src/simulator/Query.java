package simulator;

import java.util.ArrayList;
import java.util.UUID;

public class Query {
	protected File requestedFile;
	protected Node requester;
	protected ArrayList<UUID> nodesVisited;
	
	Query(){};
	
	Query(File currFile, Node currNode){
		requestedFile = currFile;
		requester = currNode;
		nodesVisited = new ArrayList<UUID>();
				
	}
	
}
