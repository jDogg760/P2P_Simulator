/**
 * 
 */
package simulator;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Joshua Pantarotto
 *
 */
public class TransferTest {

	public Network testNetwork;
	public ArrayList<Node> requestingNodes;
	int cycleCount;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testNetwork = new Network(2,1);
		cycleCount = 1000;
	}

	/**
	 * Test method for {@link simulator.Transfer#Transfer(simulator.Query, int)}.
	 */
//	@Test
//	public void testTransfer() {
//		fail("Not yet implemented"); // TODO
//	}

	/**
	 * Test method for {@link simulator.Transfer#cycleTransfer()}.
	 */
	@Test
	public void testCycleTransfer() {
		ArrayList<Transfer> completedTransfers = new ArrayList<Transfer>();
		
		for (int i = 0; i < cycleCount; i++) {
			requestingNodes = testNetwork.getRequestNodes();
			
			for (Node currNode: requestingNodes){
				Query currQuery = new Query(testNetwork.getRandomFile(),currNode);
				if (!currNode.files.contains(currQuery.requestedFile)) {
					currNode.requestFile(currQuery);
				}
			}
			
			for (Node currNode : testNetwork.nodeList) {
				completedTransfers.addAll(currNode.processTransfers());
			}
		}
		
		for (Transfer currentTran:completedTransfers) {
			for (int i = 0; i < completedTransfers.size(); i++) {
				if (completedTransfers.get(i).transferedFile.id == currentTran.transferedFile.id &&
						!completedTransfers.get(i).equals(currentTran)) {
					assertFalse(completedTransfers.get(i).receiver.equals(currentTran.receiver));
				}
			}
		}
	}

}
