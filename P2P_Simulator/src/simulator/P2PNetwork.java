package simulator;

import java.util.ArrayList;

public class P2PNetwork {
	public static void main(String args[]){
		Network testNetwork = new Network(100,5);
		int cycleCount = 1000;
		int successfulQueries = 0;
//		int totalTransfers = 0;
		int totalSearchTime = 0;
//		int [] networkLoad = new int [cycleCount];
		ArrayList<Query> completedQueries = new ArrayList<Query>();
	
		ArrayList<Node> requestingNodes = new ArrayList<Node>();
		
		for (int i = 1; i < cycleCount+1; i++){
//			System.out.println("Cycle: " + (i+1));
			requestingNodes = testNetwork.getRequestNodes();
			
			for (Node currNode: requestingNodes){
				Query currQuery = new Query(testNetwork.getRandomFile(),currNode);
			
					
					currNode.requestFile(currQuery, i);
//					completedQueries.add(currQuery);
				
			}
			
//			int cycleLoad = 0;
//			for (Node currNode : testNetwork.nodeList) {
//				currNode.processTransfers();
//				cycleLoad += currNode.load;
//			}			
//			networkLoad[i] = cycleLoad;
		}
		
		int networkLoad =0;
		int filesTransferred = 0; 
		
		for (Node aNode: testNetwork.nodeList) {
//			for (Transfer aTransfer : aNode.sendTransfers) {
//				successfulQueries++;
//				totalSearchTime += aTransfer.query.hopCount;
//			}
//			for (Transfer aTransfer : aNode.completed) {
//				totalTransfers++;
//				successfulQueries++;
//				totalSearchTime += aTransfer.query.hopCount;
//			}
//			successfulQueries += aNode.completedQueries.size();
			System.out.println("Node: " + aNode.nodeId);
			for (Query aQuery : aNode.completedQueries){
//				System.out.println("\tQuery: " + aQuery.requestedFile.id + " Hop count: " + aQuery.hopCount);
				completedQueries.add(aQuery);
				totalSearchTime += aQuery.hopCount;
			}
			
			for (File aFile : aNode.sentFiles){
				networkLoad += aFile.size;
				filesTransferred++;
			}	
		}
		
		for (int i = 0; i < completedQueries.size(); i++) {
			Query aQuery = completedQueries.get(i);
			System.out.println("\tQuery: " + aQuery.requester.nodeId + " Hop count: " + aQuery.hopCount + " " + aQuery.requestedFile.id);
			
			if (i < completedQueries.size()-2)
				if (completedQueries.get(i).requester.nodeId != completedQueries.get(i+1).requester.nodeId  )
					System.out.println();
		}
		
		System.out.println("Total Transfers Completed: " + completedQueries.size());
		System.out.println("Avg Search Time: " + (double) totalSearchTime / completedQueries.size());
		System.out.println("Avg Network Load: " + (double)networkLoad/testNetwork.nodeList.size() );
	}
}



