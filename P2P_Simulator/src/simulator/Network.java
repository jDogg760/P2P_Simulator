package simulator;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Network {
	protected int nodeCount;
	protected int neighborCount;
	public ArrayList<Node> nodeList;
	public ArrayList<File> fileList;

	public Network(int nodes, int numNeighbors) {
		
		
		nodeCount = nodes;
		neighborCount = numNeighbors;
		
		nodeList = new ArrayList<Node>();
		fileList = new ArrayList<File>();
		
		
		initNetwork();
	}

	private void initNetwork() {
		Node currNode;
		

		initNodeList(nodeCount);

		for (int i = 0; i < nodeCount; i++) {
			currNode = nodeList.get(i);
			initFiles(currNode);	
			initNeighbors( currNode);
		}
	}

	private void initFiles(Node currNode) {
		
		System.out.println("Node id: "+ currNode.nodeId);
		fileList.addAll(currNode.createFileList(5));
		
	}

	/**
	 * @param neighbors
	 * @param currNode
	 * @param count
	 */
	private void initNeighbors(Node currNode) {
		Node currNeighbor;
		int count = 0;
		
		while (count < neighborCount) {
			Random generator = new Random();
			currNeighbor = nodeList.get(generator.nextInt(nodeList.size()));
			if (!currNode.neighbors.contains(currNeighbor) && currNode != currNeighbor) {
				currNode.addNeighbor(currNeighbor);
				count++;
			}
		}
	}

	/**
	 * @param nodes
	 */
	private void initNodeList(int nodes) {
		for (int i =0; i < nodes; i++) {
			nodeList.add(new Node(UUID.randomUUID()));	
		}
	}
	
	

}
