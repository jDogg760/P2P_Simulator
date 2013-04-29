package simulator;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Network {
	protected int nodeCount;
	protected int neighborCount;
	public ArrayList<Node> nodeList;
	public ArrayList<File> fileList;
	public ArrayList<Query> queryList;
	
	protected static final int requestsPerCycle = 5;

	public Network(int numNodes, int numNeighbors) {
		nodeCount = numNodes;
		neighborCount = numNeighbors;		
		nodeList = new ArrayList<Node>();
		fileList = new ArrayList<File>();
		queryList = new ArrayList<Query>();
		initNodeList(nodeCount);
		initFileLists(neighborCount);
	}
	
	public void simulate(Replicator repScheme, int cycleCount) {
		for (int i = 0; i < cycleCount; i++) {
			ArrayList<Node> requestingNodes = getRequestNodes();
			File requestedFile;
			for(Node currentNode : requestingNodes) {
				requestedFile = getRandomFile();
				Query currentQuery = new Query(requestedFile, currentNode);
				queryList.add(currentQuery);
				currentNode.requestFile(currentQuery, cycleCount);
				
			}
		}
	}
	
	/**
	 * @return list of random nodes that will attempt file requests
	 */
	protected ArrayList<Node> getRequestNodes() {
		ArrayList<Node> requestingNodes = new ArrayList<Node>();
		for (int i = 0; i < requestsPerCycle; ++i) {
			requestingNodes.add(getRandomNode());
		}
		return requestingNodes;
	}

	/**
	 * @return random file from Network's fileList member
	 */
	public File getRandomFile() {
		if (fileList != null && fileList.size() > 0) {
			Random generator = new Random();
			return fileList.get(generator.nextInt(fileList.size()));
		}
		return null;
	}
	
	/**
	 * @return random node from Network's nodeList member
	 */
	private Node getRandomNode() {
		if (nodeList != null && nodeList.size() > 0) {
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
			fileList.addAll(currNode.createFileList(numNeighbors));			
			initNeighbors(currNode);
		}
	}

	/**
	 * @param currNode - node to initialize neighbors for
	 */
	private void initNeighbors(Node currNode) {
		Node currNeighbor;
		while (currNode.neighbors.size() < neighborCount - 2) {
			Random generator = new Random();
			currNeighbor = nodeList.get(generator.nextInt(nodeList.size()));
//			System.out.println(currNeighbor.neighbors.size());
			if (!currNode.neighbors.contains(currNeighbor) && 
					currNode != currNeighbor && 
					currNeighbor.neighbors.size() <= neighborCount) {
				currNode.addNeighbor(currNeighbor);
				currNeighbor.addNeighbor(currNode);
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
