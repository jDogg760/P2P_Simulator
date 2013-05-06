package simulator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class P2PNetwork {

	public static void main(String args[]){
		PrintWriter outSearchTime = null;
		PrintWriter outLoad = null;
		int input = 0;
		Scanner reader = new Scanner(System.in);
		
		//users input rep scheme
		while (input < 1 || input >3){
			System.out.println("1 - Passive, 2 - Path, 3 - LNLW\nPlease input replication scheme: ");
			input = reader.nextInt();
		}
		
		reader.close();
		
		//read search and load to file for each rep scheme
		try {

			if (input == 1){
				outSearchTime = new PrintWriter(new FileWriter("Passive_Search.txt"));
				outLoad = new PrintWriter(new FileWriter("Passive_Load.txt"));
			}
			else if (input == 2) {

				outSearchTime = new PrintWriter(new FileWriter("Path_Search.txt"));
				outLoad = new PrintWriter(new FileWriter("Path_Load.txt"));
			}
			else {
				outSearchTime = new PrintWriter(new FileWriter("LNLW_Search.txt"));
				outLoad = new PrintWriter(new FileWriter("LNLW_Load.txt"));
			}


		} catch (IOException e) {
			e.printStackTrace();
		}

		int nodeCount = 50;

		//run the simulation
		for (int j = 0; j <3; j++){
			outSearchTime.println("Number of nodes: " + nodeCount);
			outLoad.println("Number of nodes: " + nodeCount);
			for (int i = 0; i < 5; i++){
				run(outSearchTime,outLoad, nodeCount, input);
			}
			nodeCount += 50;
			System.out.println("done");
		}

		outSearchTime.close();
		outLoad.close();
		System.exit(0);
	}

	/**
	 * @param out1
	 * @param out2
	 * @param noOfNodes
	 * run simulation
	 */
	public static void run(PrintWriter out1, PrintWriter out2, int noOfNodes, int repScheme) {
		Network testNetwork = new Network(noOfNodes,5);
		int cycleCount = 500;
		int totalSearchTime = 0;
		ArrayList<Query> completedQueries = new ArrayList<Query>();

		ArrayList<Node> requestingNodes = new ArrayList<Node>();

		for (int i = 1; i < cycleCount+1; i++){
			requestingNodes = testNetwork.getRequestNodes();

			for (Node currNode: requestingNodes){
				Query currQuery = new Query(testNetwork.getRandomFile(),currNode);
				currNode.requestFile(currQuery, repScheme);

			}
		}

		int networkLoad =0;

		for (Node aNode: testNetwork.nodeList) {
			for (Query aQuery : aNode.completedQueries){
				completedQueries.add(aQuery);
				totalSearchTime += aQuery.hopCount;
			}

			for (File aFile : aNode.sentFiles){
				networkLoad += aFile.size;
			}	
		}
		out1.println((double) totalSearchTime / completedQueries.size());
		out2.println((double)networkLoad/testNetwork.nodeList.size());
	}
}



