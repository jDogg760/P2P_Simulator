package simulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;
public class Node {

	protected int MAX_REPLICAS = 5;
	protected UUID nodeId;
	protected HashMap<Integer, Integer> load;
	protected ArrayList<Node> neighbors;
	protected ArrayList<File> files;
	protected Queue<File> replicas;
	protected ArrayList<File> sentFiles;
	protected ArrayList<File> receivedFiles;
	protected ArrayList<Query> completedQueries;
	public Query currRequest;
	protected int cycleCount;

	public Node(UUID currId) {
		nodeId 				= currId;
		neighbors 			= new ArrayList<Node>();
		files 				= new ArrayList<File>();
		replicas			= new LinkedList<File>();
		sentFiles 		= new ArrayList<File>();
		receivedFiles 	= new ArrayList<File>();
		completedQueries = new ArrayList<Query>();
		load				 = new HashMap<Integer, Integer>();
		cycleCount = 0;
	}

	/**
	 * @param numFiles - read the size from file_list.txt and generate random UUID
	 * @return
	 */
	public ArrayList<File> createFileList(int numFiles) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader("file_list.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		for (int i = 0; i < numFiles; i++) {
			UUID newID = UUID.randomUUID();
			int fileSize = 0;
			try {
				fileSize = Integer.parseInt(in.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			File newFile = new File(newID, fileSize);
			files.add(newFile);
		}
		return files;
	}

	/**
	 * @return get a random file in the system
	 */
	public File getRandomFile() {
		Random generator = new Random();
		return files.get(generator.nextInt(files.size()));
	}

	public ArrayList<File> getFileList() {
		return files;
	}

	public void addNeighbor(Node currNode){
		neighbors.add(currNode);
	}

	/**
	 * @param currQuery - send out currQuery to all neighbors
	 */
	public void requestFile(Query currQuery, int scheme){

		cycleCount++;
		File queryFile = getFile(currQuery);
		File queryReplica = getReplica(currQuery);
		if (queryFile != null){
			return;
		}
		else if (queryReplica != null) {
			files.add(queryReplica);
			replicas.remove(queryReplica);
			queryReplica.requests.put(cycleCount, queryReplica.getRequests(cycleCount) + 1);
			completedQueries.add(currQuery);
		}
		else {
			ArrayList<Node> searchList = new ArrayList<Node>();
			ArrayList<Query> queryList = new ArrayList<Query>();
			Node currNode;
			Query newQuery;
			Query currSearch;

			for (Node neighbor: neighbors){
				searchList.add(neighbor);
				newQuery = new Query(currQuery);
				newQuery.nodesVisited.add(neighbor);
				queryList.add(newQuery);
			}

			while (!searchList.isEmpty()){
				currNode = searchList.remove(0);
				currSearch = queryList.remove(0);
				if (!currNode.receiveRequest(currSearch, scheme)){
					for (Node neighbor: currNode.neighbors){
						searchList.add(neighbor);
						newQuery = new Query(currSearch);
						newQuery.nodesVisited.add(neighbor);
						queryList.add(newQuery);
					}
				}
				else {
					return;
				}
			}
		}

	}

	/**
	 * @param currQuery
	 * @return false if node doesn't own file, true if it does
	 */
	public boolean receiveRequest(Query currQuery, int scheme){

		File queryFile = getFile(currQuery);
		File queryReplica = getReplica(currQuery);
		if (queryFile != null || queryReplica != null ) {
			if (queryReplica != null){
				replicas.remove(queryReplica);
				replicas.add(queryReplica); //moving to top of list

				queryReplica.requests.put(cycleCount, queryReplica.getRequests(cycleCount) + 1);
			}
			else
				queryFile.requests.put(cycleCount, queryFile.getRequests(cycleCount) + 1);
			currQuery.hopCount = currQuery.nodesVisited.size();
			currQuery.requester.completedQueries.add(currQuery);

			if (scheme == 1)
				initTransferPassive(currQuery);
			else if (scheme == 2)
				initTransferPath(currQuery);
			else
				initTransferLNLW(currQuery);
			return true;
		}

		return false;

	}

	/**
	 * @param targetNode
	 * @param targetFile
	 * @param isRequestor
	 * @return true when a file is successfully transfered from one node to another
	 */
	public boolean transferFile(Node targetNode, File targetFile, boolean isRequestor) {
		File requestedCopy = new File(targetFile);		// Make new copy of target and set requests = 0

		if (isRequestor) {
			targetNode.files.add(requestedCopy);
		}
		else {
			if (targetNode.replicas.size() >= MAX_REPLICAS)
				targetNode.replicas.remove();
			targetNode.replicas.add(requestedCopy);
		}
		targetNode.receivedFiles.add(requestedCopy);
		Integer fileSize = (Integer)requestedCopy.size;
		if (!load.containsKey(cycleCount)) {
			load.put(cycleCount, fileSize);
		}
		else {
			load.put(cycleCount, getLoad() + fileSize);
		}
		this.sentFiles.add(targetFile);
		return true;
	}

	/**
	 * @param currQuery 
	 * passive replication scheme
	 */
	private void initTransferPassive(Query currQuery) {

		currQuery.nodesVisited.get(currQuery.nodesVisited.size()-1).transferFile(currQuery.requester, currQuery.requestedFile, true);
	}

	/**
	 * @param currQuery
	 * Path replication scheme
	 */
	private void initTransferPath(Query currQuery){
		for (int i = currQuery.nodesVisited.size()-1; i >-1;i--) {	
			if (i > 0)
				currQuery.nodesVisited.get(i).transferFile(currQuery.nodesVisited.get(i-1), currQuery.requestedFile, false);
			else
				currQuery.nodesVisited.get(i).transferFile(currQuery.requester, currQuery.requestedFile, true);
		}

	}

	/**
	 * @param currQuery
	 * LNLW replication scheme
	 */
	private void initTransferLNLW(Query currQuery) {

		currQuery.nodesVisited.get(currQuery.nodesVisited.size()-1).transferFile(currQuery.requester, currQuery.requestedFile, true); // passive replication

		File mostAccessed = files.get(0);
		int totalFileWeight = 0;
		int totalRequestedFiles = 0;
		int fileWeight = 0;
		for (File file : files) {
			if (file.requests.containsKey(cycleCount)){
				fileWeight = file.getFileWeight(cycleCount);
				totalRequestedFiles++;
				totalFileWeight += fileWeight;
				if (mostAccessed.getFileWeight(cycleCount) < fileWeight) {
					mostAccessed = file;
				}
			}
		}
		for (File file : replicas) {
			if (file.requests.containsKey(cycleCount)){
				fileWeight = file.getFileWeight(cycleCount);
				totalRequestedFiles++;
				totalFileWeight += fileWeight;
				if (mostAccessed.getFileWeight(cycleCount) < fileWeight) {
					mostAccessed = file;
				}
			}
		}

		double avgPopFileWeight = (double) mostAccessed.getFileWeight(cycleCount) / cycleCount;
		double avgTotalWeight = (double)totalFileWeight/ (totalRequestedFiles *cycleCount);
		double numReplicas = Math.floor(avgPopFileWeight / avgTotalWeight);

		Collections.sort(neighbors, new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
				return (int)n1.getLoad().compareTo(n2.getLoad());
			}
		}); 

		int replicasNeeded = ((int)numReplicas > neighbors.size()) ? neighbors.size() : (int)numReplicas;

		for (int i = 0; i <  replicasNeeded; i++) {
			if (!neighbors.get(i).hasFile(mostAccessed))
				transferFile(neighbors.get(i), mostAccessed, false);
		}
	}

	/**
	 * @param aQuery
	 * @return a file if it exists in the replica list
	 */
	public File getReplica(Query aQuery) {
		for (File replica : replicas) {
			if (replica.id == aQuery.requestedFile.id) {
				return replica;
			}
		}
		return null;
	}

	/**
	 * @param aQuery
	 * @return a file if it exists in the file list
	 */
	public File getFile(Query aQuery) {
		for (File file : files) {
			if (file.id == aQuery.requestedFile.id) {
				return file;
			}
		}
		return null;
	}


	/**
	 * @param aFile
	 * @return true if a node owns a file
	 */
	private boolean hasFile(File aFile){
		for (File currFile : files){
			if (currFile.id == aFile.id)
				return true;
		}

		for (File currFile : replicas){
			if (currFile.id == aFile.id)
				return true;
		}

		return false;
	}

	/**
	 * @return the current load of the node
	 */
	private Integer getLoad() {
		Integer instantLoad = load.get(cycleCount);
		if (instantLoad != null) {
			return instantLoad;
		}
		return 0;
	}
}
