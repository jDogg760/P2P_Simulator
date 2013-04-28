package simulator;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class QueryTest {
	
	File f;
	Node n;
	Query q;
	Node test1, test2;

	@Before
	public void setUp() throws Exception {
		f = new File(UUID.randomUUID(), 100);
		n = new Node(UUID.randomUUID());
		q = new Query(f, n);
		test1 = new Node(UUID.randomUUID());
		test2 = new Node(UUID.randomUUID());
		q.nodesVisited.add(test1);
		q.nodesVisited.add(test2);
	}

	@Test
	public void testQueryFileNode() {
		assertTrue(q.nodesVisited.size() == 2);
	}

	@Test
	public void testQueryQuery() {
		assertTrue(q.nodesVisited.size() == 2);
		Query q2 = new Query (q);
		assertTrue(q2.nodesVisited.size() == 2);
		Node test3 = new Node(UUID.randomUUID());
		q2.nodesVisited.add(test3);
		assertTrue(q2.nodesVisited.size() == 3);
		assertFalse(q.nodesVisited.size() == 3);
		q2.nodesVisited.remove(test1);
		q2.nodesVisited.remove(test2);
		assertFalse(q2.nodesVisited.contains(test1));
	}
//
//	@Test
//	public void testUpdate() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testSearchTime() {
//		fail("Not yet implemented"); // TODO
//	}

}
