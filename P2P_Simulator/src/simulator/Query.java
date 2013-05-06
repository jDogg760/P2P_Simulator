package simulator;

import java.util.ArrayList;

public class Query {
	public File requestedFile;
	public Node requester;
	public Node sender;
	public ArrayList<Node> nodesVisited;
	public int hopCount;
	public static final int ttl = 7;

	
	public Query(File currFile, Node currNode){
		hopCount = 0;
		requestedFile = currFile;
		requester = currNode;
		sender = currNode;
		nodesVisited = new ArrayList<Node>();		
	}
	
	/**
	 * @param oriQuery - copy constructor for query
	 */
	public Query(Query oriQuery) {
		this.hopCount = oriQuery.hopCount;
		this.requestedFile = oriQuery.requestedFile;
		this.requester = oriQuery.requester;
		this.sender = oriQuery.sender;
		this.nodesVisited = new ArrayList<Node>();
		for (Node n : oriQuery.nodesVisited) {
			this.nodesVisited.add(n);
		}
	}

	/**
	 * @param currNode - set the current node as the sender of request
	 */
	public void setSender(Node currNode){
		sender = currNode;
		nodesVisited.add(sender);
	}
}
