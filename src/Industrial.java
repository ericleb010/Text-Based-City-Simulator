/**
 * The implementation of a Building of the industrial type. Their short-form names begin with a 'I'.
 * It also has the distinction of being able to pollute the utilities of its children.
 * @author Eric Leblanc
 */
public class Industrial extends NonHydro {
	
	private float waterPollution;
	
	/**
	 * Constructor.
	 * @param type an int decribing the type, as defined by the Type class.
	 * @param waterUsage a float describing how much water this building uses.
	 * @param elecUsage a float describing how much electricity this building uses.
	 * @param waterPollution a float-based factor describing the percentage of its utilities that gets polluted.
	 */		
	public Industrial(int type, float waterUsage, float elecUsage, float waterPollution) {
		this.type = type;
		this.waterUsage = waterUsage;
		this.elecUsage = elecUsage;
		this.waterPollution = waterPollution;
	}
	/**
	 * A getter method specific to this type of building. Gets the percentage of its utilities which will be polluted.
	 * @return a float with the percentage of its utilities which will be polluted.
	 */
	public float getWaterPollution() {
		return this.waterPollution;
	}
}
