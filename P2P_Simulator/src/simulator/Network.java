package simulator;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Network {
	protected int nodeCount;
	protected int neighborCount;
	public ArrayList<Node> nodeList = new ArrayList<Node>();
	public ArrayList<File> fileList;

	public Network (int nodes, int neighbors){
		Node currNode;
		Node currNeighbor;
		int count;

		for (int i =0; i < nodes; i++) {
			nodeList.add(new Node(UUID.randomUUID()));	

		}

		for (int i = 0; i < nodes; i++){
			count = 0;
			currNode = nodeList.get(i);
			System.out.println("Node id: "+ currNode.nodeId);
			fileList.addAll(currNode.createFileList(5));
			
			while (count < neighbors) {
				Random generator = new Random();
				currNeighbor =nodeList.get(generator.nextInt(nodeList.size()));
				if (!currNode.neighbors.contains(currNeighbor) && currNode != currNeighbor) {
					currNode.addNeighbor(currNeighbor);
					System.out.println("Neighbor id: "+ currNeighbor.nodeId);
					count++;
				}
			}

		}
	}
	
	

}
