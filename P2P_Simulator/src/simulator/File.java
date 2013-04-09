package simulator;

import java.util.UUID;

public class File {
	
	public UUID id;
	public int size;
	public int requests;
	
	public File(UUID id, int size) {
		this.id = id;
		this.size = size;
		requests = 0;
	}
	
	public void clearRequests() {
		requests = 0;
	}
}
