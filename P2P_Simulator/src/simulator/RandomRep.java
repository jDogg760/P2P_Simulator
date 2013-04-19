package simulator;

import java.util.Random;

public class RandomRep implements RepStrategy {

	@Override
	public void replicate(Node ownerNode, Query targetQuery) {
		
		Random rand = new Random();
		rand.nextInt(19);
		
		

	}

}
