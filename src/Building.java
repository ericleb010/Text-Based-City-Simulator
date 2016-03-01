
/**
 * An abstract definition of a Building in the game. For the most part, all buildings in this game have similar features, 
 * but sometimes a distinction needs to be made between Hydro and NonHydro buildings. For the reason, this base class was
 * defined. 
 * 
 * Not to be confused with the functionality of a Node object, which handles cost and is the container for buildings in
 * general. 
 * 
 * @author Eric Leblanc
 */
public abstract class Building {	
	protected int id;								// An integer providing the building with an ID. (May be better to have Node incorporate this later.)
	protected int type;								// An integer describing the type of the building, per the definitions in class Type.
	
	protected float waterToChildren;				// A float with info on how much water gets passed down in total to children.
	protected float elecToChildren;					// Same, but electricity.
	protected float waterUsage;						// A fixed float with how much water a building uses.
	protected float elecUsage;						// Same, but electricity.
	protected float pollution = 1;					// A float with info on how polluted this building's utilities are. 
	
	//----------------------------------------------------
	
	/**
	 * Calculates a building's depth based on its and the root's ID. This is defined as the minimum integer i where<br /><br />
	 * <code>buildingID(mod hydroID / 2^i) = 0</code>
	 * @return an int defining the building's current depth.
	 */
	public int getDepth() {
		int i = 0;
		while (this.id % (BinaryTree.hydroID / Math.pow(2, i)) != 0) {
			i++;
		}
		return i;
	}
	
	/**
	 * Setter method for the building's ID.
	 * @param id an integer for the building's ID.
	 */
	public void setID(int id) {
		this.id = id;
	}
	/**
	 * Getter method for a building's pollution.
	 * @return a float detailing pollution levels.
	 */
	public float getPollution() {
		return this.pollution;
	}
	/**
	 * Getter method for a building's water supply to pass along.
	 * @return a float detailing current water levels.
	 */
	public float getPassableWater() {
		return this.waterToChildren;
	}
	/**
	 * Getter method for a building's electrical supply to pass along.
	 * @return a float detailing current electric levels.
	 */
	public float getPassableElec() {
		return this.elecToChildren;
	}
	/**
	 * Getter method for a building's ID.
	 * @return an integer detailing a building's ID.
	 */
	public float getID() {
		return this.id;
	}
}
