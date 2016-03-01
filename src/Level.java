/**
 * This is the class which defines Level objects, which are simply a way for us to store crucial data about a particular level.
 * @author Eric Leblanc
 */
public class Level {
	
	// These are all defined in the MainGame class.
	private String description;
	int initialCash;
	float initialWater;
	float initialElec;
	private float distMultiplier;
	private float polMultiplier;
	private int[] baseCosts;
	private float[] waterNeed;
	private float[] elecNeed;
	int[] goalBuildings;
	float[] goalPollutions;
	int goalCash;
	
	
	public Level(
			String description, 
			int initialCash, 
			float initialWater, 
			float initialElec, 
			float distMultiplier,
			float polMultiplier,
			int[] baseCosts, 
			float[] waterNeed, 
			float[] elecNeed, 
			int goalCash, 
			int[] goalBuildings, 
			float[] goalPollutions) {
		
		this.description = description;
		this.initialCash = initialCash;
		this.initialWater = initialWater;
		this.initialElec = initialElec;
		this.distMultiplier = distMultiplier;
		this.polMultiplier = polMultiplier;
		this.baseCosts = baseCosts;
		this.waterNeed = waterNeed;
		this.elecNeed = elecNeed;
		this.goalCash = goalCash;
		this.goalBuildings = goalBuildings;
		this.goalPollutions = goalPollutions;
	}
	
	
	public float getPolMultiplier() {
		return polMultiplier;
	}
	public String getDescription() {
		return description;
	}
	public int getInitialCash() {
		return initialCash;
	}
	public float getInitialWater() {
		return initialWater;
	}
	public float getInitialElec() {
		return initialElec;
	}
	public float getDistMultiplier() {
		return distMultiplier;
	}
	public int[] getBaseCosts() {
		return baseCosts;
	}
	public float[] getWaterNeed() {
		return waterNeed;
	}
	public float[] getElecNeed() {
		return elecNeed;
	}
	public int getGoalCash() {
		return goalCash;
	}
	public int[] getGoalBuildings() {
		return goalBuildings;
	}
	public float[] getGoalPollutions() {
		return goalPollutions;
	}
}
