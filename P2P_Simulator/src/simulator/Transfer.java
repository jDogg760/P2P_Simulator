package simulator;

public class Transfer {
//	public UUID fileName;
//	public int fileSize;
	Node sender;
	Node receiver;
	Query query;
	public File transferedFile;
	public int remaining;
	public int maxAmt;
	public int transferTime;
	
	public Transfer(Query currQuery, int allowedTransfer) {
//		fileName = targetFile.id;
//		fileSize = targetFile.size;
		query = currQuery;
		sender = currQuery.nodesVisited.get(currQuery.nodesVisited.size()-1);
		receiver = currQuery.requester;
		transferedFile = currQuery.requestedFile;
		remaining = transferedFile.size;		
		maxAmt = allowedTransfer;
		transferTime = 1;
	}
	
	public boolean cycleTransfer(){
		if (remaining <= maxAmt) {
			remaining = 0;
			sender.transferFile(receiver, transferedFile);
			System.out.println("Query: " + query.requestedFile.id + 
					"\t Hop count: " + query.hopCount + "\t Transfer Time: " +
					transferTime + "\t File size: " + query.requestedFile.size +
					"\tRequestor: " + query.requester.nodeId + "\tSender: " + query.sender.nodeId);
			return true;
		}
		else {
			transferTime++;
			remaining = remaining - maxAmt;
			return false;
		}
	}
}
