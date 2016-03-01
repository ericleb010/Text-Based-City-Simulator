import java.io.*;
import java.util.*;

/**
 * This is the class which leads the user through the game as it is being set up and played. 
 * This includes level configuration and I/O, user input, and message display.
 * @author Eric Leblanc
 * @version 1.0, 14/12/2014
 */
public class MainGame {

	private static int levelsDefined;									// Number of levels the configuration declare to define.
	private static Level[] levels;										// An array of Level objects. The collection of these defines a game.
	private static int curLevel;										// A variable keeping track of which level we're at.
	
	private static Scanner userIn = new Scanner(System.in);				// The scanner taking in user input.
	
	private static BinaryTree curGame;									// The structure for the game currently being played.
	private static int[] buildingsBuilt = new int[13];					// The integer array keeping tabs on what buildings the user created.
	private static int cash;											// The amount of money available to the user.
	private static final String defaultMessage = 						// A default message reminding the player how to request help.
			"For help on how to play, type \"help\".";
	private static String message;										// A string holding whatever message the user should receive based on last turn.
	private static boolean gameOver = false;							// A flag indicating whether or not the game is over.
	
	// These values will be used for a level's definition.
	private static String description = "";								// A string holding the description for the level.
	private static float initialWater = 0;								// The initial water avaiable in the Hydro building.
	private static float initialElec = 0;								// The initial electricity made available.
	private static int initialCash = 0;									// The initial cash made available to the user.
	private static float distMultiplier = 0;							// The (multiplicative) factor by which a building's cost is increased.
	private static float polMultiplier = 0;								// The base factor by which an industrial building increases pollution among children.
	private static int[] baseCosts = new int[13];						// An array holding the base cost of purchasing each type of building.
	private static float[] water = new float[13];						// An array holding the water usage of each building type.
	private static float[] elec = new float[13];						// An array holding the electricity usage of each building type.
	private static int[] goalBuildings = new int[13];					// The array holding how many of each building is expected for this level.
	private static float[] goalPollutions = new float[13];				// The array holding how much pollution is tolerated for each building in this level.
	private static int goalCash = 0;									// The amount of cash the user is expected to have to complete the level.
	
	/**
	 * The main function, as described.
	 * @param args an array; does not affect play of game.
	 */
	public static void main(String[] args) {
		
		// Define some stuff: file reader, a flag for later, and a string to hold our input.
		Scanner config = null;
		boolean status = false;
		String line = null;

		System.out.println("Welcome to CitySim, a puzzle game by Eric Leblanc!");
		System.out.println("Please enter the name of the configuration file, or leave blank if you aren't sure.");
		System.out.print("Filename: ");

		// Load the configuration. First line is to confirm what we're looking at and how many levels.
		// If we don't get anything we're expecting, including our magic number, re-confirm.
		while (!status) {
			String filename = userIn.nextLine();
			// Blank line? Use default config file.
			if (filename.length() == 0) {
				System.out.println("Searching for 'config.txt'...");
				filename = "config.txt";
			}
			try {
				config = new Scanner(new File(filename)); // This throws an exception.
				// Is this a configuration file?
				if (!(config.hasNext() && (line = config.nextLine()).substring(0, 8).equals("!-CONFIG"))) 
					System.err.println("File is not a configuration file. Please enter a new filename: ");
				else 
					status = true;
			}
			// If the filename is not valid, try again.
			catch (FileNotFoundException e) {
				System.err.println("No such file. Please enter a new filename: ");
			}
		}
		
		// All set!
		System.out.println("\nLoading...");
		
		// Check for the number of levels defined.
		if (line.substring(8).indexOf('[') < 0) {
			// Invalid: no opening bracket.
			System.err.println("Error: Configuration file does not properly specify number of levels. Exiting...");
			System.exit(-1);
		}
		// Isolate the number of levels defined.
		String temp = line.substring(9);
		if (temp.indexOf(']') < 0) {
			// Invalid: no closing bracket.
			System.err.println("Error: Configuration file does not properly specify number of levels. Exiting...");
			System.exit(-1);
		}
		try {
			levelsDefined = Integer.parseInt(temp.substring(0, temp.indexOf(']')));
		} catch (NumberFormatException e) {
			System.err.println("Error: Configuration file does not properly specify number of levels. Exiting...");
			System.exit(-1);
		}
		levels = new Level[levelsDefined];
		
		//-----------------------------------------------------------------------------------------------------------------------
		
		int index = 0;
		curLevel = 0;
		String rule = "";
		// Rules for the configuration file:
		// 		- A '|' character designates a line for configuration. Comments otherwise.
		// 		- A '-' character designates a rule.
		//		- A '*' character ends a level definition.
		// 		- Blank lines are ignored.
		while (config.hasNext()) {
			line = config.nextLine();
			
			// Check if line is blank or is whitespace.
			if (line.length() == 0 || Character.isWhitespace(line.charAt(0)))
				continue;
			
			// Check to make sure that we're looking at a line for configuration.
			if (line.charAt(0) != '|')
				continue;
			
			// If we're trying to define a level which we aren't expecting:
			if (levelsDefined == curLevel) {
				System.err.println("Error: Configuration file only announced " + levelsDefined + " levels, but attempting to create #" + (curLevel + 1) + ". Exiting...");
				System.exit(-1);
			}
			
			// If we next see a '-', we are defining a rule.
			if (line.length() > 1 && line.charAt(1) == '-') {
				rule = line.substring(2).toUpperCase().trim();
			}
			
			// If we see a '*' instead, we're ending definitions for the level.
			else if (line.length() > 1 && line.charAt(1) == '*') {
				
				levels[curLevel] = new Level(
						description,
						initialCash, 
						initialWater, 
						initialElec, 
						distMultiplier, 
						polMultiplier,
						baseCosts, 
						water, 
						elec, 
						goalCash, 
						goalBuildings, 
						goalPollutions
						);
				
				// Re-initialise.
				description = "";
				initialCash = 0;
				initialWater = 0;
				initialElec = 0;
				distMultiplier = 0;
				polMultiplier = 0;
				baseCosts = new int[13];
				water = new float[13];
				elec = new float[13];
				goalCash = 0;
				goalBuildings = new int[13];
				goalPollutions = new float[13];
				
				// Increment the level counter.
				curLevel++;
				rule = "";
			}
			
			// If it's none of the above, we must do some parsing to establish the rules for each level.
			else {
				line = line.substring(1).trim();
				switch(rule) {
					
				case "DESC":
					description += line + "\n";
					break;
					
				case "WATER":
					for (int i = 0; i < 13; i++) {
						try {
							if (i != 12)
								water[i] = Float.parseFloat(line.substring(0, line.indexOf(',')));
							else {
								water[i] = Float.parseFloat(line);
								continue;
							}
						} catch (NumberFormatException e) {
							System.err.println("Error: Invalid value provided for level " + curLevel + ", rule WATER. Exiting...");
							System.exit(-1);
						} catch (StringIndexOutOfBoundsException e) {
							System.err.println("Error: The configuration file is missing a value for level " + curLevel + ", rule WATER. Exiting...");
							System.exit(-1);
						}
						line = line.substring(line.indexOf(',') + 1).trim();
					}
					break;
				
				case "ELEC":
					for (int i = 0; i < 13; i++) {
						try {
							if (i != 12)
								elec[i] = Float.parseFloat(line.substring(0, line.indexOf(',')));
							else {
								elec[i] = Float.parseFloat(line);
								continue;
							}
						} catch (NumberFormatException e) {
							System.err.println("Error: Invalid value provided for level " + curLevel + ", rule ELEC. Exiting...");
							System.exit(-1);
						} catch (StringIndexOutOfBoundsException e) {
							System.err.println("Error: The configuration file is missing a value for level " + curLevel + ", rule ELEC. Exiting...");
							System.exit(-1);
						}
						line = line.substring(line.indexOf(',') + 1).trim();
					}
					break;
					
				case "COST":
					for (int i = 0; i < 13; i++) {
						try {
							if (i != 12)
								baseCosts[i] = Integer.parseInt(line.substring(0, line.indexOf(',')));
							else {
								baseCosts[i] = Integer.parseInt(line);
								continue;
							}
						} catch (NumberFormatException e) {
							System.err.println("Error: Invalid value provided for level " + curLevel + " (i = " + i + "), rule COST. Exiting...");
							System.exit(-1);
						} catch (StringIndexOutOfBoundsException e) {
							System.err.println("Error: The configuration file is missing a value for level " + curLevel + ", rule COST. Exiting...");
							System.exit(-1);
						}
						line = line.substring(line.indexOf(',') + 1).trim();
					}
					break;
				
				// This is a little different.
				case "GOAL":
					try {
						if (index == 0)
							goalCash = Integer.parseInt(line);
						
						else if (index == 1) {
							for (int i = 0; i < 13; i++) {
								if (i != 12)
									goalBuildings[i] = Integer.parseInt(line.substring(0, line.indexOf(',')));
								else {
									goalBuildings[i] = Integer.parseInt(line);
									continue;
								}
								line = line.substring(line.indexOf(',') + 1).trim();
							}
						}
						
						else {
							for (int i = 0; i < 13; i++) {
								if (i != 12)
									goalPollutions[i] = Float.parseFloat(line.substring(0, line.indexOf(',')));
								else {
									goalPollutions[i] = Float.parseFloat(line);
									continue;
								}
								line = line.substring(line.indexOf(',') + 1).trim();
							}
							index = 0;
							break;
						}
					} catch (NumberFormatException e) {
						System.err.println("Error: Invalid value provided for level " + curLevel + ", rule GOAL. Exiting...");
						System.exit(-1);
					} catch (StringIndexOutOfBoundsException e) {
						System.err.println("Error: The configuration file is missing a value for level " + curLevel + ", rule GOAL. Exiting...");
						System.exit(-1);
					}
					index++;
					break;
						
				// This is pre-level prep.
				case "":
					if (line.indexOf(',') > 0) {
						for (int i = 0; i < 5; i++) {
							try {
								if (i == 0) 
									initialCash = Integer.parseInt(line.substring(0, line.indexOf(',')));
								else if (i == 1)
									initialWater = Float.parseFloat(line.substring(0, line.indexOf(',')));
								else if (i == 2) 
									initialElec = Float.parseFloat(line.substring(0, line.indexOf(',')));
								else if (i == 3)
									polMultiplier = Float.parseFloat(line.substring(0, line.indexOf(',')));
								else {
									distMultiplier = Float.parseFloat(line);
									continue;
								}
							} catch (NumberFormatException e) {
								System.err.println("Error: Invalid value provided for pre-level setup for " + curLevel + ". Exiting...");
								System.exit(-1);
							} catch (StringIndexOutOfBoundsException e) {
								System.err.println("Error: The configuration file is missing a value in pre-level setup. Exiting...");
								System.exit(-1);
							}
							line = line.substring(line.indexOf(',') + 1).trim();
						}
						break;
					}
					else {
						System.err.println("Error: No rule defined for line: '" + line + "'. Exiting...");
						System.exit(-1);
					}
					
				// If we are given some other rule, complain.
				default:
					System.err.println("Error: Invalid rule: '" + rule + "'. Exiting...");
					System.exit(-1);
				}
			}
		}
		
		// Quick check to make sure things are alright.
		if (curLevel != levelsDefined) {
			System.err.println("Error: Expected " + levelsDefined + " levels, only received " + curLevel + ". Exiting...");
			System.exit(-1);
		}
		
		// Initialise things for gameplay.
		curLevel = 0;
		curGame = new BinaryTree(levels[curLevel].getInitialWater(), levels[curLevel].getInitialElec(), levels[curLevel].getGoalPollutions());
		cash = levels[curLevel].initialCash;
		
		
		//--------------------------------------------------------------------------------------------------------------------------
		
		// At this point, every thing should be alright and good to go. Provide some instructions.
		System.out.println("\n");
		message = defaultMessage;
		
		// Now that things are set up, start the game!
		while (!gameOver) {
			
			// Reset the screen after every command.
			curGame.refresh();
			resetScreen();
			
			// Check for completion.
			boolean complete = cash >= levels[curLevel].goalCash;
			for (int i = 0; i < 13; i++) {
				if (buildingsBuilt[i] < levels[curLevel].goalBuildings[i]) {
					complete = false;
					break;
				}
			}
			// If we're still good:
			if (complete) {
				for (BinaryTree.Node node : curGame) {
					if (node.building instanceof NonHydro && levels[curLevel].goalPollutions[node.building.type] != 0 && node.building.pollution < levels[curLevel].getGoalPollutions()[node.building.type]) {
						complete = false;
						break;
					}
				}
			}
			// Check if the configuration passes. If so, level complete!
			if (complete) {
				System.out.println(">> COMPLETE!");
				// Check if that was our last level.
				if (++curLevel == levelsDefined) {
					System.out.println("Congratulations! You beat the game!");
					gameOver = true;
				}
				// Otherwise, offer to continue playing.
				else {
					System.out.print(">> Do you wish to play the next level? (y/n) ");
					while (!(line = userIn.nextLine().toLowerCase().trim()).equals("y") && !line.equals("n"))
						System.err.print("Error: invalid option. Please enter another value. (y/n) ");
					
					// If yes, re-initialise and continue.
					if (line.equals("y")) {
						curGame = new BinaryTree(levels[curLevel].getInitialWater(), levels[curLevel].getInitialElec(), levels[curLevel].getGoalPollutions());
						cash = levels[curLevel].initialCash;
						buildingsBuilt = new int[13];
					}
					// If not, end the game.
					else if (line.equals("n"))
						gameOver = true;
				}
				continue;
			}
			
			//-------------------------------------------------------------------------
			
			// If we get to here, the game is not over. Print last message and take in user command.
			System.out.println(">> " + message);
			System.out.print("\n\nWhat would you like to do?\n>> ");
			String origLine;
			while ((origLine = userIn.nextLine().trim().toLowerCase()).length() == 0) {
				System.out.print(">> ");
			}
				
			// Are they asking for help?
			if (origLine.equals("help")) {
				
				message = "Please read the README file if you would like help!";
				continue;
				
				
				// No time! Please read the README!
//				for (int i = 0; i < 50; i++) {
//					System.out.println();
//				}
				//printHelp();
			}
				
			// Are they asking to quit?
			else if (origLine.equals("quit") || origLine.equals("exit"))
				gameOver = true;
	
				
			// If we get here, we are expecting a multi-part argument.
			else if (origLine.indexOf(' ') > 0) {
				String firstWord = origLine.substring(0, origLine.indexOf(' ')).trim();
				line = origLine.substring(origLine.indexOf(' ') + 1).trim();
				
				//--------------------------------------------------------------------------------------------------
				
				// Check to see if user wishes to build a building. Second word is a type. Expecting a third word too.
				if (firstWord.equals("build") && line.indexOf(' ') > 0) {
					int rId = -1;
					String rType = line.substring(0, line.indexOf(' ')).trim().toUpperCase();
					// Remove dashes if the user used them.
					if (rType.indexOf('-') > 0)
						rType = rType.substring(0, rType.indexOf('-')) + rType.substring(rType.indexOf('-') + 1);
					// Third word is the parent ID.
					try {
						rId = Integer.parseInt(line.substring(line.indexOf(' ') + 1).trim());
					} catch (NumberFormatException e) {
						message = "Error: Invalid value provided for ID. Third argument needs to be an integer.\n>> " + defaultMessage;
						continue;
					}
					
					// Is the ID valid?
					if (!curGame.checkID(rId)) {
						message = "Error: " + rId + " is not a valid ID. Please choose another.\n>> " + defaultMessage;
						continue;
					}
					
					// Calculate the cost of the building.
					int cost = (int) (levels[curLevel].getBaseCosts()[Type.toInt(rType)] * levels[curLevel].getDistMultiplier() * (curGame.getDepth(rId) + 1));
					// If the cost is non-zero, it can be purchased.
					if (cost != 0 && cash >= cost) {
						
						// Check what type of building this is.
						if (rType.charAt(0) == 'R' && Type.toInt(rType) != -1) {
							if (!curGame.createChild(rId, new Residential(Type.toInt(rType), levels[curLevel].getWaterNeed()[Type.toInt(rType)], levels[curLevel].getElecNeed()[Type.toInt(rType)]), cost)) {
								// Not a valid ID, probably because of trying to add a third child.
								message = "Error: You can't place another building under #" + rId + ". Please choose another id.\n>> " + defaultMessage;
								continue;
							}
						}
						else if (rType.charAt(0) == 'C' && Type.toInt(rType) != -1) {
							if (!curGame.createChild(rId, new Commercial(Type.toInt(rType), levels[curLevel].getWaterNeed()[Type.toInt(rType)], levels[curLevel].getElecNeed()[Type.toInt(rType)]), cost)) {
								// Not a valid ID, probably because of trying to add a third child.
								message = "Error: You can't place another building under #" + rId + ". Please choose another id.\n>> " + defaultMessage;
								continue;
							}
						}
						else if (rType.charAt(0) == 'I' && Type.toInt(rType) != -1) {
							float rPollution = 0;
							
							if (Type.toInt(rType) == 10) 
								rPollution = levels[curLevel].getPolMultiplier();
							else if (Type.toInt(rType) == 12)
								rPollution = levels[curLevel].getPolMultiplier() * 1.5f;
							
							if (!curGame.createChild(rId, new Industrial(Type.toInt(rType), levels[curLevel].getWaterNeed()[Type.toInt(rType)], levels[curLevel].getElecNeed()[Type.toInt(rType)], rPollution), cost)) {
								// Not a valid ID, probably because of trying to add a third child.
								message = "Error: You can't place another building under #" + rId + ". Please choose another id.\n>> " + defaultMessage;
								continue;
							}
						}
						else {
							// Very likely not a valid type.
							message = "Error: " + rType + " is not a valid building type. Please choose another.\n>> " + defaultMessage;
							continue;
						}
						
						// Wrap up the operation.
						cash -= cost;
						buildingsBuilt[Type.toInt(rType)]++;
						message = defaultMessage;
					}
					else {
						// Can't buy it!
						message = "Error: You can't buy property of type " + Type.translateShort(Type.toInt(rType)) + "!";
					}
				}
				
				//--------------------------------------------------------------------------------------------------
				
				// Or, if they wish to remove one. Here, we're also expecting a second word, which is encompassed by the line variable.
				else if (firstWord.equals("remove")) {
					int rId = -1;
					
					// Attempt to retrieve the ID to delete.
					try {
						rId = Integer.parseInt(line);
					} catch (NumberFormatException e) {
						message = "Error: Invalid value '" + rId + "' provided for ID. Third argument needs to be an integer.\n>> " + defaultMessage;
						continue;
					}
					
					// Is the ID valid?
					if (!curGame.checkID(rId)) {
						message = "Error: " + rId + " is not a valid ID. Please choose another.\n>> " + defaultMessage;
						continue;
					}
					
					if (rId == BinaryTree.hydroID) {
						// User is attempting to remove the Hydro node!
						message = "Error: You can't remove the Hydro station!";
						continue;
					}
					
					// Calculate what amount they would be entitled to.
					int refund = curGame.getCostOfRemoval(rId);
					if (refund == 0) {
						// Not a valid ID.
						message = "Error: " + rId + " is not a valid ID. Please choose another.\n>> " + defaultMessage;
						continue;
					}
					
					// Confirm what they are trying to do here. If they don't strictly select yes, decline to do it.
					System.out.print(">> Are you sure you wish to delete #" + rId + "? This will remove its children and refund you $" + refund + ". (y/N) ");
					line = userIn.nextLine().toLowerCase().trim();
					if (line.equals("y")) {
						BinaryTree.Node rNode = curGame.removeChild(rId);
						cash += refund;
						
						BinaryTree.Node[] list = BinaryTree.getNodeList(rNode);
						for (BinaryTree.Node node : list) {
							buildingsBuilt[node.building.type]--;
						}
						message = defaultMessage;
					}
				}
				
				// Invalid command. Can't recognise the verb.
				else {
					message = "Error: '" + origLine + "' is not a valid command.\n>> " + defaultMessage;
				}
			}
			
			// We recognize the command, but we're missing pieces...
			else if (origLine.equals("build") || origLine.equals("remove")) {
				message = "Error: '" + origLine + "' requires at least one argument.\n>> " + defaultMessage;
			}
			
			// Either a bad unary action or a n-ary action with no arguments.
			else {
				message = "Error: '" + origLine + "' is not a valid command.\n>> " + defaultMessage;
			}
		}
		
		// The user opted to quit if we get here.
		System.out.println("Thanks for playing!");
	}

	//------------------------------------------------------------------------------------------------------------------
	
	/**
	 * A helper method for when we wish to refresh the screen. The method will print blank lines to clear what was there before before printing 
	 * information for the user's play.
	 */
	public static void resetScreen() {
		// Reset the screen.
		for (int i = 0; i < 50; i++) {
			System.out.println();
		}
		System.out.println("LEVEL " + (curLevel + 1) + "/" + levelsDefined);
		System.out.println(levels[curLevel].getDescription() + "\n");
		System.out.println("\n\n --> CASH LEFT: $" + cash + " -- " + ((cash < levels[curLevel].getGoalCash()) ? "NOT " : "") + "OK!");
		System.out.println("(Remember! Building costs are multiplied by " + levels[curLevel].getDistMultiplier() + "x the depth of where its parent was.)");
		System.out.println("******************************************************************************");
		System.out.println("BUILDINGS AVAILABLE:");
		System.out.println("\tResidential");
		for (int i = 0; i < 6; i++) {
			if (levels[curLevel].getBaseCosts()[i] != 0) {
				System.out.format("\t\t%-14s (%s) \tW- %5.1f | E- %5.1f %14s%d)\n", Type.translateLong(i), Type.translateShort(i), 
						levels[curLevel].getWaterNeed()[i], levels[curLevel].getElecNeed()[i], "($", levels[curLevel].getBaseCosts()[i]);
			}
		}
		System.out.println("\tCommercial");
		for (int i = 6; i < 9; i++) {
			if (levels[curLevel].getBaseCosts()[i] != 0) {
				System.out.format("\t\t%-14s (%s) \tW- %5.1f | E- %5.1f %14s%d)\n", Type.translateLong(i), Type.translateShort(i), 
						levels[curLevel].getWaterNeed()[i], levels[curLevel].getElecNeed()[i], "($", levels[curLevel].getBaseCosts()[i]);
			}
		}
		System.out.println("\tIndustrial");
		for (int i = 9; i < 13; i++) {
			if (levels[curLevel].getBaseCosts()[i] != 0) {
				if (i == 9 || i == 11) {
					System.out.format("\t\t%-10s (%s) \tW- %5.1f | E- %5.1f | P- %3.0f%% %4s%d)\n", Type.translateLong(i), Type.translateShort(i), 
							levels[curLevel].getWaterNeed()[i], levels[curLevel].getElecNeed()[i], 0.0f, "($", levels[curLevel].getBaseCosts()[i]);
				}
				else if (i == 10) {
					System.out.format("\t\t%-10s (%s) \tW- %5.1f | E- %5.1f | P- %3.0f%% %4s%d)\n", Type.translateLong(i), Type.translateShort(i), 
							levels[curLevel].getWaterNeed()[i], levels[curLevel].getElecNeed()[i], levels[curLevel].getPolMultiplier() * 100, "($", levels[curLevel].getBaseCosts()[i]);
				}
				else if (i == 12) {
					System.out.format("\t\t%-10s (%s) \tW- %5.1f | E- %5.1f | P- %3.0f%% %4s%d)\n", Type.translateLong(i), Type.translateShort(i), 
							levels[curLevel].getWaterNeed()[i], levels[curLevel].getElecNeed()[i], levels[curLevel].getPolMultiplier() * 1.5 * 100, "($", levels[curLevel].getBaseCosts()[i]);
				}
			}
		}
		System.out.println("******************************************************************************\n");
		curGame.printTree();
	}
}
