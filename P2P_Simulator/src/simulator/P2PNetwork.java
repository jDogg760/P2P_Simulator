package simulator;

import java.util.ArrayList;

public class P2PNetwork {
	public static void main(String args[]){
		Network testNetwork = new Network(20,5);
		int cycleCount = 100;
		int successfulQueries = 0;
		int totalTransfers = 0;
		int totalSearchTime = 0;
		int [] networkLoad = new int [cycleCount];
		
	
		ArrayList<Node> requestingNodes = new ArrayList<Node>();
		
		for (int i = 0; i < cycleCount; i++){
			System.out.println("Cycle: " + (i+1));
			requestingNodes = testNetwork.getRequestNodes();
			
			for (Node currNode: requestingNodes){
				Query currQuery = new Query(testNetwork.getRandomFile(),currNode);
				if (!currNode.files.contains(currQuery.requestedFile)) {
					currNode.requestFile(currQuery);
				}
			}
			int cycleLoad = 0;
			for (Node currNode : testNetwork.nodeList) {
				currNode.processTransfers();
				cycleLoad += currNode.load;
			}			
			networkLoad[i] = cycleLoad;
		}
		
		for (Node aNode: testNetwork.nodeList) {
			for (Transfer aTransfer : aNode.sendTransfers) {
				successfulQueries++;
				totalSearchTime += aTransfer.query.hopCount;
			}
			for (Transfer aTransfer : aNode.completed) {
				totalTransfers++;
				successfulQueries++;
				totalSearchTime += aTransfer.query.hopCount;
			}
		}
		
		System.out.println("Total Transfers Completed: " + totalTransfers);
		System.out.println("Avg Search Time: " + (double) totalSearchTime / successfulQueries);
		//System.out.println("Avg Network Load: " + (double) )
	}
}



