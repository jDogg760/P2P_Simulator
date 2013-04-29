package simulator;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class FileTest {
	
	File f1;

	@Before
	public void setUp() throws Exception {
		f1 = new File(UUID.randomUUID(), 100);
	}

	@Test
	public void testFileFile() {
		f1.requests++;
		File f2 = new File(f1);
		assertTrue(f2.requests == 0);
		assertTrue(f1.id == f2.id);
	}

}
