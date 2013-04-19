package simulator;

import java.util.UUID;

public class Transfer {
//	public UUID fileName;
//	public int fileSize;
	Node sender;
	Node receiver;
	public File transferedFile;
	public int remaining;
	public int maxAmt;
	public int transferTime;
	
	public Transfer(Query currQuery, int allowedTransfer) {
//		fileName = targetFile.id;
//		fileSize = targetFile.size;
		sender = currQuery.nodesVisited.get(currQuery.nodesVisited.size()-1);
		receiver = currQuery.requester;
		transferedFile = currQuery.requestedFile;
		remaining = transferedFile.size;		
		maxAmt = allowedTransfer;
		transferTime = 0;
	}
	
	public boolean cycleTransfer(){
		if (remaining <= maxAmt) {
			remaining = 0;
			sender.transferFile(receiver, transferedFile);
			System.out.println("Transfer done");
			return true;
		}
		else {
			transferTime++;
			remaining = remaining - maxAmt;
			return false;
		}
	}
}
