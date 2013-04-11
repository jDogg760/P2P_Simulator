package simulator;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Node {

	protected UUID nodeId;
	protected int load;
	protected ArrayList<Node> neighbors;
	protected ArrayList<File> files;
	public Query currRequest;

	public Node(UUID currId) {
		nodeId = currId;
		load = 0;
		neighbors = new ArrayList<Node>();
		files = new ArrayList<File>();

	}

	public ArrayList<File> createFileList(int numFiles) {
		for (int i = 0; i < numFiles; i++) {
			UUID newID = UUID.randomUUID();
			int fileSize = (int) Math.floor(Math.random() * 1000 + 1);	// Max filesize for transfer = 1GB, min = 1MB
			File newFile = new File(newID, fileSize);
			files.add(newFile);
		}

		return files;
	}

	private File getRandomFile() {
		Random generator = new Random();
		return files.get(generator.nextInt(files.size()));
	}

	public boolean transferFile(Node neighbor, File requestedFile) {
		//		File requestedFile = getRandomFile();
		if (load + requestedFile.size < 100) {
			load += requestedFile.size;
			neighbor.load += requestedFile.size;
			neighbor.files.add(requestedFile);
			return true;
		}
		return false;
	}

	public ArrayList<File> getFileList() {
		return files;
	}

	public void setQuery(File currFile){
		currRequest = new Query(currFile,this);
		requestFile(currRequest);
	}


	public void requestFile(Query currQuery){

		Node preSender = currQuery.sender;
		currQuery.update(this);
		
		for(int i =0; i < neighbors.size(); i++){
			if (neighbors.get(i) != preSender)
				neighbors.get(i).receiveRequest(currQuery);
		}

		
	}

	public void receiveRequest(Query currQuery){
		if (files.contains(currQuery.requestedFile))
			this.transferFile(currQuery.requester, currQuery.requestedFile);
		else {
			requestFile(currQuery);

		}

	}

	//	public Query getQuery(){
	//		return currRequest;
	//	}
	public void clearQuery(){
		currRequest = new Query();
	}

	public void addNeighbor(Node currNode){
		neighbors.add(currNode);
	}
}
