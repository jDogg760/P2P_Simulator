package simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.Queue;
public class Node {

	//	private static final int MAX_LOAD = 100;
	//	public static final int MAX_TRANSFER = 30;
	protected int MAX_REPLICAS = 5;
	protected UUID nodeId;
	protected int load;
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

	public Node(UUID currId) {
		nodeId 				= currId;
		load 				= 0;
		neighbors 			= new ArrayList<Node>();
		files 				= new ArrayList<File>();
		replicas			= new LinkedList<File>();
		sentFiles 		= new ArrayList<File>();
		receivedFiles 	= new ArrayList<File>();
		completedQueries = new ArrayList<Query>();
		//		completed 			= new ArrayList<Transfer>();
		//		maxTransferBandwidth = 100;
	}

	public ArrayList<File> createFileList(int numFiles) {
		for (int i = 0; i < numFiles; i++) {
			UUID newID = UUID.randomUUID();
			int fileSize = (int) Math.floor(Math.random() * 100 + 1);	// Max file size for transfer = 1GB, min = 1MB
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

		if (isRequestor) {
			targetNode.files.add(targetFile);
		}
		else {
			if (targetNode.replicas.size() >= MAX_REPLICAS)
				targetNode.replicas.remove();
			targetNode.replicas.add(targetFile);
		}
		targetNode.receivedFiles.add(targetFile);
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
		if (files.contains(currQuery.requestedFile)){
//			completedQueries.add(currQuery);
			return;
		}
		else if (replicas.contains(currQuery.requestedFile)) {
			files.add(currQuery.requestedFile);
			replicas.remove(currQuery.requestedFile);
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

	public boolean receiveRequest(Query currQuery){
		//		currQuery.hopCount++;
		//		if (currQuery.hopCount <= Query.ttl) {
		//			currQuery.nodesVisited.add(this);
		//			if (!currQuery.inProgress){
		if (files.contains(currQuery.requestedFile) || replicas.contains(currQuery.requestedFile) ) {
			if (replicas.contains(currQuery.requestedFile)){
//				files.add(currQuery.requestedFile);
				replicas.remove(currQuery.requestedFile);
				replicas.add(currQuery.requestedFile); //moving to top of list
			}
			currQuery.hopCount = currQuery.nodesVisited.size();
			currQuery.requester.completedQueries.add(currQuery);
//								initTransferPath(currQuery);
			initTransferPassive(currQuery);
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
		else 
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
