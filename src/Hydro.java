/**
 * The implementation of the building of type Hydro, which is considered to be the root node of a BinaryTree.
 * @author Eric Leblanc
 */
public class Hydro extends Building {
	
	/**
	 * Constructor.
	 * @param initialWater a float describing how much water this building starts with.
	 * @param initialElec a float describing how much electricity this building starts with.
	 */
	public Hydro(float initialWater, float initialElec) {
		this.id = BinaryTree.hydroID;
		this.type = Type.toInt("HYD");
		this.waterToChildren = initialWater;
		this.elecToChildren = initialElec;
		this.waterUsage = 0;
		this.elecUsage = 0;
	}	
	
	
}
