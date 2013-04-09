package simulator;

import java.util.ArrayList;
import java.util.UUID;

public class Node {
	
	protected int load;
	protected ArrayList<Node> neighbors;
	protected ArrayList<File> files;
	
	public Node() {
		load = 0;
		neighbors = new ArrayList<Node>();
		files = new ArrayList<File>();
	}
	
	public void createFileList(int numFiles) {
		for (int i = 0; i < numFiles; i++) {
			UUID newID = UUID.randomUUID();
			int fileSize = (int) Math.floor(Math.random() * 1000 + 1);	// Max filesize for transfer = 1GB, min = 1MB
			File newFile = new File(newID, fileSize);
			files.add(newFile);
		}
	}
}
