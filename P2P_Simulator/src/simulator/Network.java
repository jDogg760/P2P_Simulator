package simulator;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Network {
	protected int nodeCount;
	protected int neighborCount;
	public ArrayList<Node> nodeList;
	public ArrayList<File> fileList;
	
	protected static final int requestsPerCycle = 5;

	public Network(int numNodes, int numNeighbors) {
		nodeCount = numNodes;
		neighborCount = numNeighbors;		
		nodeList = new ArrayList<Node>();
		fileList = new ArrayList<File>();
		initNodeList(nodeCount);
		initFileLists(neighborCount);
	}
	
	public void simulate(Replicator repScheme, int cycleCount) {
		for (int i = 0; i < cycleCount; i++) {
			ArrayList<Node> requestingNodes = getRequestNodes();
			File requestedFile = getRandomFile();
		}
	}
	
	/**
	 * @return list of random nodes that will attempt file requests
	 */
	private ArrayList<Node> getRequestNodes() {
		ArrayList<Node> requestingNodes = new ArrayList<Node>();
		for (int i = 0; i < requestsPerCycle; ++i) {
			requestingNodes.add(getRandomNode());
		}
		return requestingNodes;
	}

	/**
	 * @return random file from Network's fileList member
	 */
	private File getRandomFile() {
		Random generator = new Random();
		return fileList.get(generator.nextInt(fileList.size()));
	}
	
	/**
	 * @return random node from Network's nodeList member
	 */
	private Node getRandomNode() {
		if (nodeList != null) {
			Random generator = new Random();
			return nodeList.get(generator.nextInt(nodeList.size()));
		}
		return null;
	}

	/**
	 * @param numNeighbors
	 */
	private void initFileLists(int numNeighbors) {
		Node currNode;
		for (int i = 0; i < nodeCount; i++) {
			currNode = nodeList.get(i);
			System.out.println("Node id: "+ currNode.nodeId);
			fileList.addAll(currNode.createFileList(numNeighbors));			
			initNeighbors(currNode);
		}
	}

	/**
	 * @param currNode - node to initialize neighbors for
	 */
	private void initNeighbors(Node currNode) {
		int count = 0;
		Node currNeighbor;
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
