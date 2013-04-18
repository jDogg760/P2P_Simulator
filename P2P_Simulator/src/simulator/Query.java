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
	public static final int ttl = 2;

	//	Query(){};

	Query(File currFile, Node currNode){
		hopCount = 0;
		requestedFile = currFile;
		requester = currNode;
		sender = currNode;
		nodesVisited = new ArrayList<Node>();
		startTime();
	}

	public void update(Node currNode){

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
