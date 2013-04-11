package simulator;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NetworkTest {

	Network network;
	
//	@Before
//	public void setUp() throws Exception {
//		
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}

	@Test
	public void testNetwork() {
		network = new Network(20,5);
		for (int i = 0; i < network.nodeList.size(); i++) {
			Node testNode = network.nodeList.get(i);
			for (int j = 0; j < testNode.neighbors.size(); j++) {
				Node testNeighbor = testNode.neighbors.get(j);
				assertTrue(testNode.nodeId != testNeighbor.nodeId);
			}
		}		
	}

}
