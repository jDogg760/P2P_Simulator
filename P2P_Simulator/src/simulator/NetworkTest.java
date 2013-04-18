package simulator;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class NetworkTest {

	Network network;
	
	@Before
	public void setUp() throws Exception {
		network = new Network(20,5);
	}

	@Test
	public void testNetwork() {
		for (int i = 0; i < network.nodeList.size(); i++) {
			Node testNode = network.nodeList.get(i);
			for (int j = 0; j < testNode.neighbors.size(); j++) {
				Node testNeighbor = testNode.neighbors.get(j);
				assertTrue(testNode.nodeId != testNeighbor.nodeId);
			}
		}		
	}
	
	@Test
	public void testGetRequestNodes() {
		ArrayList<Node> requestNodes = network.getRequestNodes();
		for (Node currentNode:requestNodes) {
			assertTrue(network.nodeList.contains(currentNode));
		}
	}
	
	

}
