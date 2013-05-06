
package simulator;

import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;



public class File {

	public UUID id;
	public int size;
	public HashMap<Integer,Integer> requests;

	public File(UUID id, int size) {
		this.id = id;
		this.size = size;
		requests = new HashMap<Integer,Integer>();
	}

	/**
	 * @param file - copy constructor for file
	 */
	public File(File file) {
		id = file.id;
		size = file.size;
		requests = new HashMap<Integer,Integer>();
	}

	/**
	 * @param cycleCount - get the access weight of a file
	 */
	public int getFileWeight(int cycleCount) {
			int weight = 0;
			for(Integer key: requests.keySet()) {
				weight += requests.get(key) * Math.pow(2, -(cycleCount - key));
			}
			return weight;
	}
	
	/**
	 * @param cycleCount - get the request rate of a file
	 */
	public int getRequests(int cycleCount) {
		Integer numRequests = requests.get(cycleCount);
		if (numRequests == null) {
			requests.put(cycleCount, 0);
		}
		return requests.get(cycleCount);
	}

	public void clearRequests() {
		requests.clear();
	}
}
