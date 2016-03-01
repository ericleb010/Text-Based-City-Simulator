
/**
 * This is the test suite for the CitySim package. I will detail the tests in this JavaDoc and explain what they are meant to inductively demonstrate.</p>
 * 
 * - TEST 1: Empty BinaryTree.</p>
 * 
 * Now we assume the BinaryTree structure is instantiatable and that the Hydro node's information can be accessed and printed.</p>
 * 
 * - TEST 2: Insert a single node into the BinaryTree.<br />
 * - TEST 3: Inserting another node into the tree, both with same root.<br />
 * - TEST 4: Inserting the second node into the tree after the first.<br />
 * - TEST 5: Inserting a third node into the tree, so we have two depth three nodes side-by-side.<br />
 * - TEST 6: Attempting to insert more than two nodes for one parent.<br />
 * - TEST 7: We never cross the maximum depth (in this case, 8). Adding 8 children.<br />
 * - TEST 8: Deleting a node from a tree with two nodes.<br />
 * - TEST 9: Deleting a right child from the tree when there is a left one present.<br />
 * - TEST 10: Deleting a left child from the tree when there is a right one present.<br />
 * - TEST 11: Deleting a child when it is to the right of a node.<br />
 * - TEST 12: Deleting a child when it has a child of its own.<br />
 * - TEST 13: Deleting a child when it has a child of its own, to the right.<br /></p>
 * 
 * Now we assume that the removeChild function works properly.</p>
 * 
 * - TEST 14: Creating a child for a right-hand node with no left-hand node corresponding.<br />
 * - TEST 15: Attempting the remove a child that isn't there.</p>
 * 
 * Now we assume that the createChild function works properly</p>
 * 
 * - TEST 16: Test the refresh method.<br />
 * - TEST 17: Test the refresh method on a node with two children.<br />
 * - TEST 18: Test the refresh method on a node with grand-children.<br />
 * - TEST 19: Combining test 17 and 18.<br />
 * - TEST 20: Testing the pollution transmission on a single node.<br />
 * - TEST 21: Testing the pollution transmission on two nodes.<br />
 * - TEST 22: Magnified pollution.</p>
 * 
 * Now we assume that the refresh function works properly</p>
 * 
 * - TEST 23: Testing getCostOfRemoval on a single node.<br />
 * - TEST 24: Testing getCostOfRemoval on a node with a child.<br />
 * - TEST 25: Testing getCostOfRemoval on a node with two children.<br />
 * - TEST 26: Testing getCostOfRemoval on a node with two children and a grand-child.<br />
 * - TEST 27: Testing getCostOfRemoval on a node after removing its child.</p>
 * 
 * Now we assume that the getCostOfRemoval function works properly</p>
 * 
 * - TEST 28: Testing the iterator on an empty tree.<br />
 * - TEST 29: Testing the iterator on a tree with one node.<br />
 * - TEST 30: Testing the iterator on a tree with two nodes side-by-side.<br />
 * - TEST 31: Testing the iterator on grand-children.<br />
 * - TEST 32: Testing the iterator on a tree after there was a deletion.</p>
 * 
 * Now we assume that the BinaryTree is functional and working properly for the game.</p>
 * --------------------------------------------------------------------------------------</p>
 * The rest of the tests are available by playing the game with the provided configuration files.</p>
 * 
 * - TEST 33: Testing the parsing of a valid configuration file.<br />
 * - TEST 34: Testing the parsing of an "auto-win" configuration file.<br />
 * - TEST 35: Testing a two-level configuration.<br />
 * - TEST 36: Testing a configuration with a building type available.<br />
 * - TEST 37: Testing a configuration with two building types available, same category.<br />
 * - TEST 38: Testing a configuration with two different building categories available.<br />
 * - TEST 39: Testing a configuration requiring the building of a structure to win.<br />
 * - TEST 40: Testing a configuration requiring the building of different structures to win.<br />
 * - TEST 41: Testing a configuration requiring a minimum amount of money left.<br />
 * - TEST 42: Testing a configuration requiring a certain pollution amount.<br />
 * - TEST 43: Error: not enough levels defined.<br />
 * - TEST 44: Error: too many levels defined.<br />
 * - TEST 45: Error: bracketing issue when declaring number of levels.<br />
 * - TEST 46: Error: rule is missing a value.<br />
 * - TEST 47: Error: rule has a value too many, or has a non-zero value.<br />
 * - TEST 48: Error: rule is misspelt.<br />
 * </p>
 * @author Eric Leblanc
 */
public class Test {
	public static void main(String[] args) {

		System.out.println("\nThis is the test suite for the CitySim project. The details on what these tests are attempting to prove are located in the JavaDoc.\n\n");
		
		// TEST 1: Empty BinaryTree.
		// EXPECT: Nothing bad should happen.
		// RESULT: Nothing bad happens.
		System.out.println("TEST 1:");
		BinaryTree a = new BinaryTree(0, 0, new float[13]);
		a.printTree();
		
		// TEST 2: Inserting a single node into the tree.
		// EXPECT: Since the HydroID is set to 128, we expect the first node to have ID 64 and positioned to the left.
		// RESULT: Exactly that.
		System.out.println("TEST 2:");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.printTree();
		
		// TEST 3: Inserting another node into the tree, both with same root.
		// EXPECT: First node gets placed into 64, next gets placed into 192.
		// RESULT: Exactly that.
		System.out.println("TEST 3:");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.printTree();
		
		// TEST 4: Inserting the second node into the tree after the first.
		// EXPECT: First node gets placed into 64, next gets placed into 32.
		// RESULT: Exactly that.
		System.out.println("TEST 4:");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(64, new Residential(0, 0, 0), 0);
		a.printTree();
		
		// TEST 5: Inserting a third node into the tree, so we have two depth three nodes side-by-side.
		// EXPECT: We should have nodes 64, 32, and 96.
		// RESULT: Exactly that.
		System.out.println("TEST 5:");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(64, new Residential(0, 0, 0), 0);
		a.createChild(64, new Residential(0, 0, 0), 0);
		a.printTree();
		
		// TEST 6: Attempting to insert more than two nodes for one parent.
		// EXPECT: The third node does not get created. Silently.
		// RESULT: Exactly that.
		System.out.println("TEST 6:");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.printTree();
		
		// TEST 7: We never cross the maximum depth (in this configuration, 8). Adding 8 children.
		// EXPECT: We should only have 7 total nodes added to our tree. The last child was not created.
		// RESULT: Exactly that.
		System.out.println("TEST 7:");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(64, new Residential(0, 0, 0), 0);
		a.createChild(32, new Residential(0, 0, 0), 0);
		a.createChild(16, new Residential(0, 0, 0), 0);
		a.createChild(8, new Residential(0, 0, 0), 0);
		a.createChild(4, new Residential(0, 0, 0), 0);
		a.createChild(2, new Residential(0, 0, 0), 0);
		a.createChild(1, new Residential(0, 0, 0), 0);
		a.printTree();
		
		// TEST 8: Deleting a node from a tree with two nodes.
		// EXPECT: The child with ID 64 does not show up on the tree.
		// RESULT: Exactly that.
		System.out.println("TEST 8:");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(64, new Residential(0, 0, 0), 0);
		a.removeChild(64);
		a.printTree();
		
		// TEST 9: Deleting a right child from the tree when there is a left one present.
		// EXPECT: The child with ID 192 does not show up on the tree. 64 remains.
		// RESULT: Exactly that.
		System.out.println("TEST 9:");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.removeChild(192);
		a.printTree();
		
		// TEST 10: Deleting a left child from the tree when there is a right one present.
		// EXPECT: The child with ID 64 does not show up on the tree. 192 remains.
		// RESULT: Exactly that.
		System.out.println("TEST 10:");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.removeChild(64);
		a.printTree();

		// TEST 11: Deleting a child when it is to the right of a node.
		// EXPECT: The child with ID 192 does not show up on the tree.
		// RESULT: Exactly that.
		System.out.println("TEST 11:");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.removeChild(64);
		a.removeChild(192);
		a.printTree();
		
		// TEST 12: Deleting a child when it has a child of its own.
		// EXPECT: That child and all those underneath should be deleted.
		// RESULT: Exactly that.
		System.out.println("TEST 12:");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(64, new Residential(0, 0, 0), 0);
		a.removeChild(64);
		a.printTree();
		
		// TEST 13: Deleting a child when it has a child of its own, to the right.
		// EXPECT: That child and all those underneath should be deleted.
		// RESULT: Exactly that.
		System.out.println("TEST 13:");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(64, new Residential(0, 0, 0), 0);
		a.createChild(64, new Residential(0, 0, 0), 0);
		a.removeChild(32);
		a.removeChild(64);
		a.printTree();
		
		// ------------------------------------------------------------------------------------
		
		// TEST 14: Creating a child for a right-hand node with no left-hand node corresponding.
		// EXPECT: Nothing bad happens.
		// RESULT: Exactly that.
		System.out.println("TEST 14:");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.removeChild(64);
		a.createChild(192, new Residential(0, 0, 0), 0);
		a.printTree();		
		
		// TEST 15: Attempting the remove a child that isn't there.
		// EXPECT: Silently refuse to do so.
		// RESULT: Exactly that.
		System.out.println("TEST 15:");
		a = new BinaryTree(0, 0, new float[13]);
		a.removeChild(1);
		a.printTree();
		
		// ------------------------------------------------------------------------------------
		
		// TEST 16: Test the refresh method.
		// EXPECT: All water and electricity (minus usage) will be made available to children of #64.
		// RESULT: Exactly that.
		System.out.println("TEST 16:");
		a = new BinaryTree(20, 20, new float[13]);
		a.createChild(128, new Residential(0, 10, 10), 0);
		a.refresh();
		a.printTree();
		
		// TEST 17: Test the refresh method on a node with two children.
		// EXPECT: Half of the water and electricity will be made available to #64 and #192.
		// RESULT: Exactly that.
		System.out.println("TEST 17:");
		a = new BinaryTree(40, 40, new float[13]);
		a.createChild(128, new Residential(0, 10, 10), 0);
		a.createChild(128, new Residential(0, 10, 10), 0);
		a.refresh();
		a.printTree();
		
		// TEST 18: Test the refresh method on a node with grand-children.
		// EXPECT: Start with 40: 10 units will be used for #64, and 10 for #32, so there will be 20 left.
		// RESULT: Exactly that.
		System.out.println("TEST 18:");
		a = new BinaryTree(40, 40, new float[13]);
		a.createChild(128, new Residential(0, 10, 10), 0);
		a.createChild(64, new Residential(0, 10, 10), 0);
		a.refresh();
		a.printTree();
		
		// TEST 19: Combining test 17 and 18.
		// EXPECT: Start with 40: utilities are split between two first nodes, leaving ten for both, and then the final node uses the rest.
		// RESULT: Exactly that.
		System.out.println("TEST 19:");
		a = new BinaryTree(40, 40, new float[13]);
		a.createChild(128, new Residential(0, 10, 10), 0);
		a.createChild(128, new Residential(0, 10, 10), 0);
		a.createChild(64, new Residential(0, 10, 10), 0);
		a.refresh();
		a.printTree();
		
		// TEST 20: Testing the pollution transmission on a single node.
		// EXPECT: Pollution percentage for #32 is brought down to 50%.
		// RESULT: Exactly that.
		System.out.println("TEST 20:");
		a = new BinaryTree(40, 40, new float[13]);
		a.createChild(128, new Industrial(10, 10, 10, 0.5f), 0);
		a.createChild(64, new Residential(0, 10, 10), 0);
		a.refresh();
		a.printTree();
		
		// TEST 21: Testing the pollution transmission on two nodes.
		// EXPECT: Pollution percentage for #32 is brought down to 50%.
		// RESULT: Exactly that.
		System.out.println("TEST 21:");
		a = new BinaryTree(40, 40, new float[13]);
		a.createChild(128, new Industrial(10, 10, 10, 0.5f), 0);
		a.createChild(64, new Residential(0, 10, 10), 0);
		a.createChild(64, new Residential(0, 10, 10), 0);
		a.refresh();
		a.printTree();
		
		// TEST 22: Magnified pollution.
		// EXPECT: Pollution percentage for #16 is brought down to 25%.
		// RESULT: Exactly that.
		System.out.println("TEST 22:");
		a = new BinaryTree(40, 40, new float[13]);
		a.createChild(128, new Industrial(10, 10, 10, 0.5f), 0);
		a.createChild(64, new Industrial(10, 10, 10, 0.5f), 0);
		a.createChild(32, new Residential(0, 10, 10), 0);
		a.refresh();
		a.printTree();
		
		// ------------------------------------------------------------------------------------
		
		// TEST 23: Testing getCostOfRemoval on a single node.
		// EXPECT: In this scenario, the cost of removal should be equal to the price of the single node, or $100.
		// RESULT: Exactly that.
		System.out.print("TEST 23: $");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 100);
		System.out.println(a.getCostOfRemoval(64));
		a.printTree();
		
		// TEST 24: Testing getCostOfRemoval on a node with a child.
		// EXPECT: In this scenario, the cost of removal should be equal to the price of both nodes, or $200.
		// RESULT: Exactly that.
		System.out.print("TEST 24: $");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 100);
		a.createChild(64, new Residential(0, 0, 0), 100);
		System.out.println(a.getCostOfRemoval(64));
		a.printTree();
		
		// TEST 25: Testing getCostOfRemoval on a node with two children.
		// EXPECT: In this scenario, the cost of removal should be equal to the price of all three nodes, or $300.
		// RESULT: Exactly that.
		System.out.print("TEST 25: $");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 100);
		a.createChild(64, new Residential(0, 0, 0), 100);
		a.createChild(64, new Residential(0, 0, 0), 100);
		System.out.println(a.getCostOfRemoval(64));
		a.printTree();		
		
		// TEST 26: Testing getCostOfRemoval on a node with two children and a grand-child.
		// EXPECT: In this scenario, the cost of removal should be equal to the price of all four nodes, or $400.
		// RESULT: Exactly that.
		System.out.print("TEST 26: $");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 100);
		a.createChild(64, new Residential(0, 0, 0), 100);
		a.createChild(64, new Residential(0, 0, 0), 100);
		a.createChild(32, new Residential(0, 0, 0), 100);
		System.out.println(a.getCostOfRemoval(64));
		a.printTree();	
		
		// TEST 27: Testing getCostOfRemoval on a node after removing its child.
		// EXPECT: In this scenario, the cost of removal should be equal to the price of the one node, or $100.
		// RESULT: Exactly that.
		System.out.print("TEST 27: $");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 100);
		a.createChild(64, new Residential(0, 0, 0), 100);
		a.removeChild(32);
		System.out.println(a.getCostOfRemoval(64));
		a.printTree();
		
		// --------------------------------------------------------------------------------------------------
		
		// TEST 28: Testing the iterator on an empty tree.
		// EXPECT: Should only contain the Hydro node.
		// RESULT: Exactly that.
		System.out.print("TEST 28: ( ");
		a = new BinaryTree(0, 0, new float[13]);
		for (BinaryTree.Node node : a) {
			System.out.print(Type.translateShort(node.building.type) + ", ");
		}
		System.out.println(")");
		a.printTree();
		
		// TEST 29: Testing the iterator on a tree with one node.
		// EXPECT: Should only contain the Hydro node and its child.
		// RESULT: Exactly that.
		System.out.print("TEST 29: ( ");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		for (BinaryTree.Node node : a) {
			System.out.print(Type.translateShort(node.building.type) + ", ");
		}
		System.out.println(")");
		a.printTree();
		
		// TEST 30: Testing the iterator on a tree with two nodes side-by-side.
		// EXPECT: Should only contain the Hydro node and its children.
		// RESULT: Exactly that.
		System.out.print("TEST 30: ( ");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(128, new Residential(1, 0, 0), 0);
		for (BinaryTree.Node node : a) {
			System.out.print(Type.translateShort(node.building.type) + ", ");
		}
		System.out.println(")");
		a.printTree();
		
		// TEST 31: Testing the iterator on grand-children.
		// EXPECT: Should contain the Hydro node and its children.
		// RESULT: Exactly that.
		System.out.print("TEST 31: ( ");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(128, new Residential(1, 0, 0), 0);
		a.createChild(64, new Residential(2, 0, 0), 0);
		for (BinaryTree.Node node : a) {
			System.out.print(Type.translateShort(node.building.type) + ", ");
		}
		System.out.println(")");
		a.printTree();
		
		// TEST 32: Testing the iterator on a tree after there was a deletion.
		// EXPECT: Should contain the Hydro node and its children, not the one we deleted (here it was #192, type R-MC).
		// RESULT: Exactly that.
		System.out.print("TEST 32: ( ");
		a = new BinaryTree(0, 0, new float[13]);
		a.createChild(128, new Residential(0, 0, 0), 0);
		a.createChild(128, new Residential(1, 0, 0), 0);
		a.createChild(64, new Residential(2, 0, 0), 0);
		a.removeChild(192);
		for (BinaryTree.Node node : a) {
			System.out.print(Type.translateShort(node.building.type) + ", ");
		}
		System.out.println(")");
		a.printTree();
		
		// ---------------------------------------------------------------------------------------------------
		
		System.out.println("The rest of the tests are available by playing the game with the provided configuration files.");
	}
}
