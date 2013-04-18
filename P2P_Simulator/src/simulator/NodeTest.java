package simulator;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NodeTest {

//	Network network;
	Node node1;
	Node node2;
	
	@Before
	public void setUp() throws Exception {
		node1= new Node(UUID.randomUUID());
		node2= new Node(UUID.randomUUID());
		node1.createFileList(5);
		node2.createFileList(5);
		node1.neighbors.add(node2);
		node2.neighbors.add(node1);
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
//	public void testNode() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testCreateFileList() {
//		fail("Not yet implemented");
//	}

//	@Test
//	public void testTransferFile() {
//		
//		File testTransferFile = node1.getRandomFile();
//		assertFalse(node2.files.contains(testTransferFile));
//		node1.transferFile(node2, testTransferFile);
//		assertTrue(node2.files.contains(testTransferFile));
//	}

//	@Test
//	public void testGetFileList() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetQuery() {
//		fail("Not yet implemented");
//	}
//
	@Test
	public void testRequestFile() {
		File testRequestFile = node2.getRandomFile();
		node1.setQuery(testRequestFile);
		assertFalse(node1.files.contains(testRequestFile));
		node1.requestFile(node1.currRequest);
		assertTrue(node1.files.contains(testRequestFile));
		
	}

	@Test
	public void testReceiveRequest() {
		
	}

//	@Test
//	public void testClearQuery() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddNeighbor() {
//		fail("Not yet implemented");
//	}

}
