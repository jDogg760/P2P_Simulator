package simulator;

import java.util.ArrayList;

public class Query {
	public File requestedFile;
	public Node requester;
	public Node sender;
	public ArrayList<Node> nodesVisited;
	public long timeStart;
	public long timeFinish;
	public int hopCount;
	public static final int ttl = 7;
	public boolean inProgress;
	//	Query(){};

	public Query(File currFile, Node currNode){
		hopCount = 0;
		requestedFile = currFile;
		requester = currNode;
		sender = currNode;
		nodesVisited = new ArrayList<Node>();
		inProgress = false;
		startTime();
	}
	
	public Query(Query q) {
		this.hopCount = q.hopCount;
		this.requestedFile = q.requestedFile;
		this.requester = q.requester;
		this.sender = q.sender;
		this.nodesVisited = new ArrayList<Node>();
		for (Node n : q.nodesVisited) {
			this.nodesVisited.add(n);
		}
		this.inProgress = q.inProgress;
		this.timeStart = q.timeStart;
		this.timeFinish = q.timeFinish;
	}

	public void setSender(Node currNode){
		sender = currNode;
		nodesVisited.add(sender);
	}

	private void startTime() {
		timeStart = System.currentTimeMillis();
	}

	private void stopTime() {
		timeFinish = System.currentTimeMillis();
	}

	/**
	 * @return search time in ms
	 */
	public long searchTime() {
		stopTime();
		return timeFinish - timeStart;
	}

}
