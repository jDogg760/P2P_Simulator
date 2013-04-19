package simulator;

import java.util.ArrayList;

public class P2PNetwork {
	public static void main(String args[]){
		Network testNetwork = new Network(20,5);
		int jobAmt = 30;
	
		ArrayList<Node> requestingNodes = new ArrayList<Node>();
		
		for (int i = 0; i < jobAmt; i++){
			requestingNodes = testNetwork.getRequestNodes();
			
			for (Node currNode: requestingNodes){
				Query currQuery = new Query(testNetwork.getRandomFile(),currNode);
				
				currNode.requestFile(currQuery);
			}
			
			for (Node currNode : testNetwork.nodeList){
				currNode.processTransfers();
			}
			
			
		}
		
		

	}
}



