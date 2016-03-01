/**
 * The implementation of a Building of the commercial type. Their short-form names begin with a 'C'.
 * @author Eric Leblanc
 */
public class Commercial extends NonHydro {

	/**
	 * Constructor.
	 * @param type an int decribing the type, as defined by the Type class.
	 * @param waterUsage a float describing how much water this building uses.
	 * @param elecUsage a float describing how much electricity this building uses.
	 */
	public Commercial(int type, float waterUsage, float elecUsage) {
		this.type = type;
		this.waterUsage = waterUsage;
		this.elecUsage = elecUsage;
	}
}
