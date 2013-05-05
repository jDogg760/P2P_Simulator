package simulator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class P2PNetwork {

	public static void main(String args[]){
		PrintWriter outSearchTime = null;
		PrintWriter outLoad = null;


		try {
//			outSearchTime = new PrintWriter(new FileWriter("Passive_Search.txt"));
//			outLoad = new PrintWriter(new FileWriter("Passive_Load.txt"));
			
			outSearchTime = new PrintWriter(new FileWriter("Path_Search.txt"));
			outLoad = new PrintWriter(new FileWriter("Path_Load.txt"));
			
//			outSearchTime = new PrintWriter(new FileWriter("LALW_Search.txt"));
//			outLoad = new PrintWriter(new FileWriter("LALW_Load.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int nodeCount = 50;

		
		for (int j = 0; j <3; j++){
			
			outSearchTime.println("Number of nodes: " + nodeCount);
			outLoad.println("Number of nodes: " + nodeCount);
			for (int i = 0; i < 5; i++){
				run(outSearchTime,outLoad, nodeCount);
			}
			nodeCount += 50;
			System.out.println("done");
		}
	
	
		outSearchTime.close();
		outLoad.close();
		System.exit(0);
	}

	public static void run(PrintWriter out1, PrintWriter out2, int noOfNodes) {
		Network testNetwork = new Network(noOfNodes,5);
		int cycleCount = 500;
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


				currNode.requestFile(currQuery);
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

			//			System.out.println("Node: " + aNode.nodeId);
			for (Query aQuery : aNode.completedQueries){
				//				System.out.println("\tQuery: " + aQuery.requestedFile.id + " Hop count: " + aQuery.hopCount);
				completedQueries.add(aQuery);
				totalSearchTime += aQuery.hopCount;
			}

			for (File aFile : aNode.sentFiles){
				networkLoad += aFile.size;
				//				networkLoad += aFile.getFileWeight(cycleCount);
				filesTransferred++;
			}	
		}
		//		
		//		for (int i = 0; i < completedQueries.size(); i++) {
		//			Query aQuery = completedQueries.get(i);
		////			System.out.println("\tQuery: " + aQuery.requester.nodeId + " Hop count: " + aQuery.hopCount + " " + aQuery.requestedFile.id);
		//			
		//			if (i < completedQueries.size()-2)
		//				if (completedQueries.get(i).requester.nodeId != completedQueries.get(i+1).requester.nodeId  )
		//					System.out.println();
		//		}

		//		int totalRequest = 0;
		//		for (File currFile: testNetwork.fileList){
		//			System.out.println("File ID: " +currFile.id);
		//			for (Integer key: currFile.requests.keySet()){
		//				System.out.println("Cycle: " + key + "\tRequests: " + currFile.requests.get(key));
		//			}
		//		}

		//		System.out.println("Total Transfers Completed: " + completedQueries.size());
		//		System.out.println("Avg Search Time: " + (double) totalSearchTime / completedQueries.size());
		//		System.out.println("Avg Network Load: " + (double)networkLoad/testNetwork.nodeList.size() );

		
//		searchTime +=  (double) totalSearchTime / completedQueries.size();
//		load += (double)networkLoad/testNetwork.nodeList.size();
		out1.println((double) totalSearchTime / completedQueries.size());
		out2.println((double)networkLoad/testNetwork.nodeList.size());
	}
}



