package simulator;

import java.util.ArrayList;

public class P2PNetwork {
	public static void main(String args[]){
		Network testNetwork = new Network(20,5);
		int cycleCount = 100;
	
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
			
			for (Node currNode : testNetwork.nodeList) {
				currNode.processTransfers();
			}			
		}
	}
}



