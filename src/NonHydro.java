/**
 * This is an abstraction of a building which does not act as a Hydro building. 
 * This is not meant to be instantiated.
 * @author Eric Leblanc
 */
public abstract class NonHydro extends Building {
	
	/**
	 * Allows the <code>MainGame</code> to move water around. The parameters take information from their parent.
	 * The amount of each utility the child uses is then subtracted, so that it can be transmitted to its child.
	 * @param waterAvailableFromParent a float indicating how much water is provided from the parent.
	 * @param elecAvailableFromParent a float indicating how much electricity is provided from the parent.
	 */
	public void setConditions(float waterAvailableFromParent, float elecAvailableFromParent) {
		waterToChildren = waterAvailableFromParent - this.waterUsage;
		elecToChildren = elecAvailableFromParent - this.elecUsage;
	}
	
	/**
	 * Allows the <code>MainGame</code> to assign pollution levels directly. The parameters take information from their parent.
	 * @param pollution a float indicating how much pollution is transmitted by the parent.
	 */
	public void setPollution(float pollution) {
		this.pollution = pollution;
	}
}
