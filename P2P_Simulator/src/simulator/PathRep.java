package simulator;

public class PathRep implements RepStrategy {

	@Override
	public void replicate(Node ownerNode, Query targetQuery) {
		// TODO Auto-generated method stub
		for (int i =0; i < targetQuery.nodesVisited.size(); i++){
			ownerNode.transferFile(targetQuery.nodesVisited.get(i),targetQuery.requestedFile);
		}
	}

}
