/**
 * A class providing various useful functions converting type names into their integer counterparts, and vice-versa.
 * @author Eric Leblanc
 */
public class Type {
	
	/**
	 * A function converting short-form types into their integer definition.
	 * @param s a short-form, three-letter String describing the type of a building.
	 * @return an integer, which is its translation. Returns -1 if there was no match.
	 */
	public static int toInt(String s) {
		s = s.toUpperCase();
		
		switch(s) {
		case "RLC":
			return 0;
		case "RMC":
			return 1;
		case "RHC":
			return 2;
		case "RED":
			return 3;
		case "RIF":
			return 4;
		case "RPK":
			return 5;
		case "CSB":
			return 6;
		case "CLB":
			return 7;
		case "CHB":
			return 8;
		case "ISC":
			return 9;
		case "ISD":
			return 10;
		case "ILC":
			return 11;
		case "ILD":
			return 12;
		case "HYD":
			return 13;
		default:
			return -1;
		}
	}
	
	/**
	 * A function converting integers into their short-form type definition.
	 * @param n an integer describing a building's type.
	 * @return an string, which is its translation. Returns null if there was no match.
	 */
	public static String translateShort(int n) {
		switch(n) {
		case 0:
			return "R-LC";
		case 1:
			return "R-MC";
		case 2:
			return "R-HC";
		case 3:
			return "R-ED";
		case 4:
			return "R-IF";
		case 5:
			return "R-PK";
		case 6:
			return "C-SB";
		case 7:
			return "C-LB";
		case 8:
			return "C-HB";
		case 9: 
			return "I-SC";
		case 10:
			return "I-SD";
		case 11:
			return "I-LC";
		case 12:
			return "I-LD";
		case 13:
			return "HYDRO";
		default: 
			return null;
		}
	}	
	
	/**
	 * A function converting integers into their long-form type definition.
	 * @param n an integer describing a building's type.
	 * @return an string, which is its translation. Returns null if there was no match.
	 */
	public static String translateLong(int n) {
		switch(n) {
		case 0:
			return "Low-Income";
		case 1:
			return "Middle-Income";
		case 2:
			return "High-Income";
		case 3:
			return "Education";
		case 4:
			return "Hospital";
		case 5:
			return "Park";
		case 6:
			return "Small-Business";
		case 7:
			return "Large-Business";
		case 8:
			return "High-Tech";
		case 9: 
			return "Small Clean";
		case 10:
			return "Small Dirty";
		case 11:
			return "Large Clean";
		case 12:
			return "Large Dirty";
		case 13:
			return "Hydro";
		default:
			return null;
		}
	}
}
