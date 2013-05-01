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

	//	private static final int MAX_LOAD = 100;
	//	public static final int MAX_TRANSFER = 30;
	protected int MAX_REPLICAS = 5;
	protected UUID nodeId;
	protected HashMap<Integer, Integer> load;
	protected ArrayList<Node> neighbors;
	protected ArrayList<File> files;
	protected Queue<File> replicas;
	protected ArrayList<File> sentFiles;
	protected ArrayList<File> receivedFiles;
	protected ArrayList<Query> completedQueries;
	//	protected ArrayList<Transfer> completed;
	//	protected Queue<Transfer> transferQueue;
	public Query currRequest;
	//	public int maxTransferBandwidth;
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
		//		completed 			= new ArrayList<Transfer>();
		//		maxTransferBandwidth = 100;
		cycleCount = 0;
	}
	

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			File newFile = new File(newID, fileSize);
			files.add(newFile);
			//			System.out.println("\t"+newFile.id);
		}
		return files;
	}

	public File getRandomFile() {
		Random generator = new Random();
		return files.get(generator.nextInt(files.size()));
	}

	public boolean transferFile(Node targetNode, File targetFile, boolean isRequestor) {
		//		File requestedFile = getRandomFile();
		//if (load + requestedFile.size < 100) {
		//load += requestedFile.size;
		//	neighbor.load += requestedFile.size;
		//		if (load < MAX_LOAD && query.nodesVisited.get(query.nodesVisited.size() - 1).load < MAX_LOAD) {
		//			query.sender = this;
		//			query.sender.files.add(query.requestedFile);
		//		}

		File requestedCopy = new File(targetFile);		// Make new copy of target and set requests = 0
//		if (targetFile.requests.containsKey(cycleCount))
			targetFile.requests.put(cycleCount, targetFile.getRequests(cycleCount) + 1);
//		else
//			targetFile.requests.put(cycleCount, 1);

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
		//		int cycleLoad = load.get(cycleCount);
		if (!load.containsKey(cycleCount)) {
			load.put(cycleCount, fileSize);
		}
		else {
			//			cycleLoad += fileSize;
			load.put(cycleCount, getLoad() + fileSize);
		}
		this.sentFiles.add(targetFile);
		return true;
	}

	public ArrayList<File> getFileList() {
		return files;
	}

	public void setQuery(File currFile){
		currRequest = new Query(currFile,this);
	}


	public void requestFile(Query currQuery){
		//		currQuery.setSender(this);
		//		LinkedList<Node> searchList = new LinkedList<Node>();
		//		Node currNode;
		//		Query searchQuery;
		//
		//		for(int i =0; i < neighbors.size(); i++){
		//			searchList.add(neighbors.get(i));
		//		}
		//		
		//		while (!searchList.isEmpty()) {
		//			searchQuery = new Query (currQuery); // Copy the query
		//			currNode = searchList.remove();	// Get the neighbor
		//			if (currNode.receiveRequest(searchQuery)) {
		//				return true;
		//			}
		//		}
		//		for (Node neighbor : neighbors) {
		//			searchQuery = new Query (currQuery);
		//			searchQuery.nodesVisited.add(neighbor);
		//			return neighbor.requestFile(searchQuery);
		//		}
		//		return false;
		cycleCount++;
		File queryFile = getFile(currQuery);
		File queryReplica = getReplica(currQuery);
		if (queryFile != null){
			//			completedQueries.add(currQuery);
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
				if (!currNode.receiveRequest(currSearch)){
					for (Node neighbor: currNode.neighbors){
						//						if (!currSearch.nodesVisited.contains(neighbor)){
						searchList.add(neighbor);
						newQuery = new Query(currSearch);
						newQuery.nodesVisited.add(neighbor);
						queryList.add(newQuery);
						//						}
					}
				}
				else {
					//				currNode.transferFile(currSearch);
					return;
				}
			}
		}

	}

	public File getReplica(Query aQuery) {
		for (File replica : replicas) {
			if (replica.id == aQuery.requestedFile.id) {
				return replica;
			}
		}
		return null;
	}

	public File getFile(Query aQuery) {
		for (File file : files) {
			if (file.id == aQuery.requestedFile.id) {
				return file;
			}
		}
		return null;
	}
	

	public boolean receiveRequest(Query currQuery){
		//		currQuery.hopCount++;
		//		if (currQuery.hopCount <= Query.ttl) {
		//			currQuery.nodesVisited.add(this);
		//			if (!currQuery.inProgress){
		File queryFile = getFile(currQuery);
		File queryReplica = getReplica(currQuery);
		if (queryFile != null || queryReplica != null ) {
			if (queryReplica != null){
				//				files.add(currQuery.requestedFile);
				replicas.remove(queryReplica);
				replicas.add(queryReplica); //moving to top of list
			}
			currQuery.hopCount = currQuery.nodesVisited.size();
			currQuery.requester.completedQueries.add(currQuery);
//			initTransferPath(currQuery);
//			initTransferPassive(currQuery);
			initTransferLALW(currQuery);
			return true;
		}
		//transferFile(currQuery.requester, currQuery.requestedFile);
		//					currQuery.inProgress = true;
		//					initTransfer(currQuery);
		//					if (replicas.contains(currQuery.requestedFile)){
		//						files.add(currQuery.requestedFile);
		//						replicas.remove(currQuery.requestedFile);
		//					}
		//					initTransferPath(currQuery);
		//					currQuery.requester.completedQueries.add(currQuery);
		//					return true;
		//				} 
		return false;
		//				else {
		//					Query forwardQuery = new Query(currQuery.requestedFile, currQuery.requester);
		//					forwardQuery = currQuery;
		//					requestFile(forwardQuery);
		//				}

		//			}
		//		}
		//		return false;
	}

	//	public void transferFile(Query currQuery){
	//		
	//		
	//	}
	//	private boolean loadCheck() {
	//		return this.load + MAX_TRANSFER < MAX_LOAD;
	//	}

	/**
	 * @param currQuery
	 */
	private void initTransferPassive(Query currQuery) {
		//			Transfer newTransfer = new Transfer(currQuery, 30);
		//			transferQueue.add(newTransfer);
		//	
		//			if (loadCheck()) {
		//				newTransfer = transferQueue.remove();
		//				sendTransfers.add(newTransfer);
		//				newTransfer.query.requester.receiveTransfers.add(newTransfer);	
		//				newTransfer.query.inProgress = true;
		//				load += MAX_TRANSFER;
		//				currQuery.requester.load += MAX_TRANSFER;
		//			}

		currQuery.nodesVisited.get(currQuery.nodesVisited.size()-1).transferFile(currQuery.requester, currQuery.requestedFile, true);
	}

	private void initTransferPath(Query currQuery){
		//		for (int i = currQuery.nodesVisited.size()-1; i > -1;i--){
		//			Transfer newTransfer = new Transfer(currQuery);
		//			if (i  > 0){
		//				newTransfer.sender = currQuery.nodesVisited.get(i);
		//				newTransfer.receiver = currQuery.nodesVisited.get(i-1);
		//			}
		//
		//			//			currQuery.nodesVisited.get(i).transferQueue.add(newTransfer);
		//			newTransfer.sender.sentFiles.add(newTransfer);
		//			newTransfer.receiver.receivedFiles.add(newTransfer);
		//			
		//		}

		for (int i = currQuery.nodesVisited.size()-1; i >-1;i--) {	
			if (i > 0)
				currQuery.nodesVisited.get(i).transferFile(currQuery.nodesVisited.get(i-1), currQuery.requestedFile, false);
			else
				currQuery.nodesVisited.get(i).transferFile(currQuery.requester, currQuery.requestedFile, true);
		}
	}

	private void initTransferRandom(Query q) {
		// TODO
	}

	private void initTransferLALW(Query q) {
		q.nodesVisited.get(q.nodesVisited.size()-1).transferFile(q.requester, q.requestedFile, true); // passive replication
		File mostAccessed = files.get(0);
		int totalFileWeight = 0;
		int fileWeight = 0;
		for (File file : files) {
			fileWeight = file.getFileWeight(cycleCount);
			totalFileWeight += fileWeight;
			if (mostAccessed.getFileWeight(cycleCount) < fileWeight) {
				mostAccessed = file;
			}
		}
		for (File file : replicas) {
			fileWeight = file.getFileWeight(cycleCount);
			totalFileWeight += fileWeight;
			if (mostAccessed.getFileWeight(cycleCount) < fileWeight) {
				mostAccessed = file;
			}
		}
		double avgPopFileWeight = (double) mostAccessed.getFileWeight(cycleCount) / cycleCount;
		double avgTotalWeight = (double) totalFileWeight / (cycleCount * (replicas.size() + files.size()));
		double numReplicas = Math.floor(avgPopFileWeight / avgTotalWeight);
//		System.out.println("Replicas: " + replicas);
		ArrayList<Integer> neighborLoad = new ArrayList<Integer>();
		for (Node neighbor : neighbors) {
			if (neighbor.getLoad() != null) {
//				System.out.println("Neighbor load: " + neighbor.getLoad());
				neighborLoad.add((Integer) neighbor.getLoad());
			}
			else
				neighborLoad.add(0);
		}
		Collections.sort(neighbors, new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
					return (int)n1.getLoad().compareTo(n2.getLoad());
			}
		}); 
		int replicasNeeded = ((int)numReplicas > neighbors.size()) ? neighbors.size() : (int)numReplicas;
		for (int i = 0; i <  replicasNeeded; i++) {
			transferFile(neighbors.get(i), mostAccessed, false);
		}
	}
	
//	public void sortNeighborLoad(){
//		int currLoad = neighbors.get(0).getLoad(cycleCount);
//		
//		Collections.sort(neighbors, new Comparator<Node>(){
//			public int compare(Node a, Node b){
//				return a.getLoad(cycleCount) == b.getLoad(cycleCount);
//			}
//		});
//			
//		
//	}



	private Integer getLoad() {
		Integer instantLoad = load.get(cycleCount);
		if (instantLoad != null) {
			return instantLoad;
		}
		return 0;
	}

	/**
	 * @param myQuery
	 * @return true if transfer of a given file is already in progress on a node
//	 */
	//	public boolean transferInProgress (Query myQuery) {
	//		UUID fileID = myQuery.requestedFile.id;
	//		Node requestor = myQuery.requester;
	//		//Node sender = myQuery.sender;
	//
	//		for (Transfer tran : sendTransfers) {
	//			if (tran.transferedFile.id.equals(fileID) && 
	//					tran.receiver.equals(requestor)) {
	//				return true;
	//			}
	//		}
	//		return false;
	//	}


	//	public ArrayList<Transfer> processTransfers(){
	//		for (int j = 0; j < sendTransfers.size(); j++){
	//			Transfer currTransfer = sendTransfers.get(j);
	//			maxTransferBandwidth = MAX_LOAD / (sendTransfers.size() + receiveTransfers.size()); //TODO: change to float
	//			int maxReceiveBandwidth = MAX_LOAD / (currTransfer.receiver.sendTransfers.size() + currTransfer.receiver.receiveTransfers.size());
	//
	//			int bandwidth = Math.min(maxReceiveBandwidth, maxTransferBandwidth);
	//			if (currTransfer.cycleTransfer(bandwidth)) {
	//				//System.out.println("removing: " + currTransfer.transferedFile.id);
	//				sendTransfers.remove(currTransfer);
	//				completed.add(currTransfer);
	//				currTransfer.receiver.receiveTransfers.remove(currTransfer);
	//				load = load - MAX_TRANSFER;
	//				currTransfer.receiver.load = load - MAX_TRANSFER;
	//			}
	//		}
	//		return completed;
	//
	//
	//}


	//	public Query getQuery(){
	//		return currRequest;
	//	}
	//	public void clearQuery(){
	//		currRequest = new Query();
	//	}

	public void addNeighbor(Node currNode){
		neighbors.add(currNode);
	}

}
