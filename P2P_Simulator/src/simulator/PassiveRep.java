package simulator;

public class PassiveRep implements RepStrategy {

	@Override
	public void replicate(Node ownerNode, Query targetQuery) {
		
		ownerNode.transferFile(targetQuery.sender, targetQuery.requestedFile);
	}

}
