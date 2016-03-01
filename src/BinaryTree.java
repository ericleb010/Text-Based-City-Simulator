import java.util.*;

/**
 * The class defining the main data structure, a binary search tree, and its functionalities. This is the backbone of the entire game.
 * It also contains the definition of a node, which encapsulates a building object within a tree. 
 * This allows for the abstraction of this information, away from the MainGame, and with it, its functionalities.
 * This allows the implementer to focus on the main part of the game, while any changes with this structure will not affect it.</p>
 * 
 * This class brings with it a lot of the basic functionalities of a regular BST, namely insertion and deletion. This one is different, 
 * however, in the sense that the user only has a say as to which node their building will be a child of. Since they cannot choose a specific 
 * ID for that child, the children will always be filled in left-to-right. This causes some issues though: because IDs are auto assigned,
 * it would be tedious to implement a single-node deletion. So this implementation of a BST deletes the entire subtree.</p>
 * 
 * The tree also implements Iterable, meaning that anyone with access to an instance of this tree is able to iterate through it. 
 * This is helpful when making checks across the board.
 * @author Eric Leblanc
 * @version 1.0, 14/12/2014
 */
public class BinaryTree implements Iterable<BinaryTree.Node> {

	public static final int hydroID = 128;						// A (modifiable) constant defining the root ID. All children IDs are based on this, and
																// -	it is also the main component behind calculating a single node's depth in the tree.
	private int currentDepth;									// A counter for printing the graphical representation of the tree.
	private Node hydro;											// The root node, our hydro building.
	private float[] levelPolGoal;								// An array to hold the pollution goals for the level. Required for printTree().
	
	// Constructors and extra data members.
	
	/**
	 * An inner class defining the nodes on the tree.
	 * @author Eric Leblanc
	 */
	public class Node {
		Node leftChild;
		Node rightChild;
		Building building;
		int cost;
		
		/**
		 * Constructor.
		 * @param building a Building object which will be stored into this node.
		 * @param cost an integer detailing how much it cost to create this node. 
		 */
		public Node(Building building, int cost) {
			this.building = building;
			this.cost = cost;
		}
	}
	
	/**
	 * An iterator for the nodes in the tree.
	 * @author Eric Leblanc
	 */
	private class NodeIterator implements Iterator<Node> {
		
		private int count;
		private Node[] nodeList = getNodeList(hydro);
		
		public boolean hasNext() {
			return count < nodeList.length;
		}

		public Node next() {
			return nodeList[count++];
		}
	}
	
	/**
	 * Constructor.
	 * @param hydroWater a float defining how much water the Hydro building will contain.
	 * @param hydroElec a float defining how much electricity the Hydro building will hold.
	 * @param levelPolGoal an array of floats defining the expectation for pollution levels among all building types.
	 */
	public BinaryTree(float hydroWater, float hydroElec, float[] levelPolGoal) {
		hydro = new Node(new Hydro(hydroWater, hydroElec), 0);
		this.levelPolGoal = levelPolGoal;
	}
	
	//----------------------------------------------------------------------------
	
	/**
	 * Keeps everything up to date recursively. Should be called any time a change is made to the tree.
	 * Will update water supply, electrical supply, and water pollution levels for every building in the tree, in O(n) time.
	 * @see #refreshRecur(Node)
	 */
	public void refresh() {
		refreshRecur(hydro);
	}
	
	/**
	 * A helper method to hide the Hydro node. This provides the functionality.
	 * @param node the node with which the recursion starts. Meant to be the Hydro node.
	 * @see #refresh()
	 */
	private void refreshRecur(Node node) {
		
		// Stop conditions.
		if (node.leftChild == null && node.rightChild == null)
			return;
		
		// Refresh the water and electricity given to the children. If only one child:
		if (node.leftChild == null || node.rightChild == null)
			if (node.rightChild == null) 
				((NonHydro) node.leftChild.building).setConditions(node.building.waterToChildren, node.building.elecToChildren);
			else {
				((NonHydro) node.rightChild.building).setConditions(node.building.waterToChildren, node.building.elecToChildren);
			}
		// If both children are there:
		else {
			((NonHydro) node.leftChild.building).setConditions(node.building.waterToChildren / 2, node.building.elecToChildren / 2);
			((NonHydro) node.rightChild.building).setConditions(node.building.waterToChildren / 2, node.building.elecToChildren / 2);
		}
		
		// Refresh water pollution. If we are refreshing children of an industrial building, there's more pollution to add.
		if (node.building instanceof Industrial) {
			if (node.leftChild != null)
				((NonHydro) node.leftChild.building).setPollution(node.building.getPollution() * (1 - ((Industrial) node.building).getWaterPollution()));
			if (node.rightChild != null)
				((NonHydro) node.rightChild.building).setPollution(node.building.getPollution() * (1 - ((Industrial) node.building).getWaterPollution()));
		}
		// Otherwise, we just pass the pollution down.
		else {
			if (node.leftChild != null)
				((NonHydro) node.leftChild.building).setPollution(node.building.getPollution());
			if (node.rightChild != null)
				((NonHydro) node.rightChild.building).setPollution(node.building.getPollution());
		}
		
		// Continue recursively.
		if (node.leftChild != null) 
			refreshRecur(node.leftChild);
		if (node.rightChild != null)
			refreshRecur(node.rightChild);
	}
	
	//----------------------------------------------------------------------------
	
	/**
	 * A method to create a new child, in O(lg n) time, given a parent ID.
	 * @param parentID the integer ID of the parent node under which the user wishes to house the new building.
	 * @param building the actual building object, fully defined.
	 * @param cost the cost of creating this node.
	 * @return a flag indicating the status: <code>true</code> if successful, <code>false</code> if no node with that parentID was found.
	 */
	public boolean createChild(int parentID, Building building, int cost) {
		// Find the parent's node first and store it.
		Node node = searchForChild(parentID, hydro);
		
		// If the node was not found, return a negative status.
		// Alternatively, if the ID indicates that we've reached the maximum depth of the tree, return a negative status.
		if (node == null || Math.pow(2, node.building.getDepth()) == hydroID) 
			return false;
		
		// Now we can create the child node. Put it in the next available slot.
		if (node.leftChild == null) {
			node.leftChild = new Node(building, cost);
			// Set the new ID in a way which will not clash with the other subtree's IDs.
			building.setID(parentID - (hydroID / (int) Math.pow(2, node.building.getDepth() + 1)));
		}
		else if (node.rightChild == null) {
			node.rightChild = new Node(building, cost);
			// Set the new ID in a way which will not clash with the other subtree's IDs.
			building.setID(parentID + (hydroID / (int) Math.pow(2, node.building.getDepth() + 1)));
		}
		else
			// Already reached the maximum amount of nodes for that parent.
			return false;
		// If all goes well, indicate that.
		return true;
	}
	
	/**
	 * The reverse operation. This will find the child node requested and delete the entire subtree associated with it in O(lg n) time.
	 * @param id the ID of the child from which we requested a deletion.
	 * @return the Node object we deleted. If we can't find the node requested, this returns <code>null</code>.
	 */
	public Node removeChild(int id) {
		
		// Search for the parent first.
		Node parent = searchForParent(id, hydro);
		Node child;
		
		// If we find the parent node, we'll find the child we wish to remove underneath.
		if (parent != null) {
			// Find the child and remove its subtree.
			if (parent.leftChild != null && parent.leftChild.building.id == id) {
				child = parent.leftChild;
				parent.leftChild = null;
			}
			else {
				child = parent.rightChild;
				parent.rightChild = null;
			}
			// Return the result.
			return child;
		}
		// If we don't, indicate that.
		else 
			return null;
	}
	
	/**
	 * An accompanying method which indicates how much the subtree was valued. Uses a helper method to recursively do this.
	 * @param id the ID of the subtree's root.
	 * @return the valuation of the subtree removed.
	 * @see #countCosts(Node)
	 */
	public int getCostOfRemoval(int id) {
		return countCosts(searchForChild(id, hydro));
	}
	
	//-----------------------------------------
	
	/**
	 * A method for the Iterable implementation. Requires O(n) time.
	 * @see NodeIterator
	 */
	public NodeIterator iterator() {
		return new NodeIterator();
	}
	
	/**
	 * A method to check the validity of an ID.
	 * @param id the integer ID to check.
	 * @return a flag indicating the success or failure of the operation.
	 */
	public boolean checkID(int id) {
		return searchForChild(id, hydro) != null;
	}
	
	/**
	 * A method to get the depth of a particular node.
	 * @param id the node's ID to check.
	 * @return the depth of the node, or -1 if no node is found.
	 */
	public int getDepth(int id) {
		Node child = searchForChild(id, hydro);
		if (child != null) 
			return child.building.getDepth();
		return -1;
	}
	
	/**
	 * A method returning (pre-order) the list of nodes presently in the tree. Mainly used for the NodeIterator.
	 * @param node the root of the tree on which we wish to base our list.
	 * @return an array of nodes, as requested.
	 */
	public static Node[] getNodeList(Node node) {
		// Stop conditions.
		if (node.leftChild == null && node.rightChild == null) 
			return (new Node[] {node});
		
		// Create two half arrays.
		Node[] list1 = new Node[0];
		Node[] list2 = new Node[0];
		// If need be, recurse through the children to get more nodes.
		if (node.leftChild != null)
			list1 = getNodeList(node.leftChild);
		if (node.rightChild != null)
			list2 = getNodeList(node.rightChild);
		
		// Install the resulting array and insert the nodes we have in a pre-order fashion.
		Node[] t = new Node[list1.length + list2.length + 1];
		t[0] = node;
		
		int i = 1;
		for (Node found : list1) {
			t[i] = found;
			i++;
		}
		for (Node found : list2) {
			t[i] = found;
			i++;
		}
		
		// Return the result.
		return t;
	}
	
	//-----------------------------------------
	
	/**
	 * Returns the node requested. It does this with a complexity of O(lg n), since it is recursing using a binary search algorithm.
	 * @param id the ID of the node we are searching for.
	 * @param node the root node of the subtree we are looking through. Usually the Hydro node.
	 * @return the node requested, if found. It will return <code>null</code> otherwise.
	 */
	private Node searchForChild(int id, Node node) {

		// Check for stop conditions. Either we found the node, or we found a leaf.
		if (node.building.id == id) 
			return node;
		if (node.leftChild == null && node.rightChild == null)
			// Nothing left to check.
			return null;
		
		Node nodeCheck = null;
		
		// Check the left or right child depending on the ID.
		if (id < node.building.id && node.leftChild != null)
			nodeCheck = searchForChild(id, node.leftChild);
		else if (node.rightChild != null) 
			nodeCheck = searchForChild(id, node.rightChild);
		
		// Return the result.
		return nodeCheck;
	}
	
	/**
	 * Returns the parent of the node requested. It does this with a complexity of O(lg n), since it is recursing using a binary search algorithm.
	 * @param id the ID of the child node we are searching for.
	 * @param node the root node of the subtree we are looking through. Usually the Hydro node.
	 * @return the parent of the node requested, if found. It will return <code>null</code> otherwise.
	 */
	private Node searchForParent(int id, Node node) {
		
		// Check for stop conditions. In this case, if we find a leaf.
		if (node.leftChild == null && node.rightChild == null)
			return null;
		
		Node nodeCheck;
		
		// Check the left or right child, depending on the ID, and look for the child we're looking for.
		if (node.building.id > id) {
			// Found it!
			if (node.leftChild.building.id == id)
				return node;
			// Otherwise, recurse further.
			else
				nodeCheck = searchForParent(id, node.leftChild);
		}
		else {
			if (node.rightChild.building.id == id)
				// Found it!
				return node;
			else
				// Otherwise, recurse further.
				nodeCheck = searchForParent(id, node.rightChild);
		}
		
		// Return the result.
		return nodeCheck;
	}
	
	/**
	 * The recursive definition for the getCostOfRemoval method. Done in a pre-order fashion.
	 * @param node the root of the subtree we are looking through.
	 * @return the integer valuation of all the nodes in the subtree.
	 * @see #getCostOfRemoval(int)
	 */
	private int countCosts(Node node) {
		// Check for stop conditions.
		if (node.leftChild == null && node.rightChild == null) {
			return node.cost;
		}
		
		// If one of the two children are null, we only need to add two costs together.
		if (node.rightChild == null || node.leftChild == null) {
			if (node.rightChild == null) 
				return node.cost + countCosts(node.leftChild);
			else 
				return node.cost + countCosts(node.rightChild);
		}
		// Adding all three:
		else
			return node.cost + countCosts(node.leftChild) + countCosts(node.rightChild);
	}
	
	//----------------------------------------------------------------------------
	
	/**
	 * A method allowing for the graphical representation of our tree. Uses a recursive definition with time O(n).
	 * @see #recurPrint(Node)
	 * @see #setSpacingAndPrint(Node)
	 */
	public void printTree() {
		// Reset the depth to zero.
		currentDepth = 0;
		
		// Choose a style of displaying the border!
		System.out.println("------------------------------------------------------------------------------------------------------------------------\n\n");
		//System.out.println("\n\n|-");
		recurPrint(hydro);
		//System.out.println("|-\n\n");
		System.out.println("\n\n------------------------------------------------------------------------------------------------------------------------\n");
	}
	/**
	 * A recursive method which does the work of traversing and printing in-order.
	 * @param node the root of the subtree which we are printing.
	 * @return a node if we've reached a leaf, and a <code>null</code> reference otherwise.
	 * @see #printTree()
	 * @see #setSpacingAndPrint(Node)
	 */
	private Node recurPrint(Node node) {
		// Check for stop conditions.
		if (node == null) {
	    	return null;
	    }
		// Check if we've hit a leaf.
	    if (node != hydro && node.leftChild == null && node.rightChild == null) {
	    	// We've reached a leaf!
	    	return node;
	    }
	    	
	    // Now, depending on what we're printing out, we increase and decrease our depth.
	    currentDepth++;
	    setSpacingAndPrint(recurPrint(node.rightChild));
	    currentDepth--;
	    setSpacingAndPrint(node);
	    currentDepth++;
	    setSpacingAndPrint(recurPrint(node.leftChild));
	    currentDepth--;
	    
	    // Return the null reference to demonstrate that there is nothing left to print.
	    return null;
	}
	
	/**
	 * A helper method for recurPrint which prints everything we find expect for <code>null</code> references.
	 * @param node the node we are expecting to print.
	 */
	private void setSpacingAndPrint(Node node) {
		// Only print if non-null.
    	if (node != null) {
    		System.out.print("| ");
    		for (int i = 0; i < currentDepth; i++) {
    			System.out.print("          ");
    		}
    		if (node.building.waterToChildren < 0 || node.building.elecToChildren < 0 || node.building instanceof NonHydro && node.building.pollution < levelPolGoal[node.building.type])
    			System.out.print("D [*** " + node.building.id + ": " + Type.translateShort(node.building.type) + " ($" + node.cost + ") ***\n| ");
    		else 
    			System.out.print("D [" + node.building.id + ": " + Type.translateShort(node.building.type) + " ($" + node.cost + ")\n| ");
    		for (int i = 0; i < currentDepth; i++) {
    			System.out.print("          ");
    		}
    		System.out.print((currentDepth + 1) + " [W:" + node.building.waterToChildren + "/E:" + node.building.elecToChildren + "/");
    		System.out.format("%-3.0f%%\n", node.building.pollution * 100);
    	}
    }
}
