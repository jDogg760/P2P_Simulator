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
		
//		assertTrue(network.nodeList.size() == 20);
	}

}
