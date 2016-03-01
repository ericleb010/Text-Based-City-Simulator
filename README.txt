NOTE: THIS PROJECT IS COMPILED IN JAVA 1.8.


Thanks for playing CITYSIM! 
A game developed by Eric Leblanc.

The CITYSIM game is a text-based puzzle game which challenges you to build 
a city structure that satisfies the requirements for a particular level. 
You have the option of choosing from among 13 different building types, which 
span three different categories: Residential, Commercial, and Industrial. 

The city is structured like a binary tree, with a Hydro station at the center. 
It is what will feed your buildings with water and electricity. The goal is to 
build the required structures for each level without running out of money, 
water and electricity, and also without running into too much water pollution.

Here is an example of what you will see when running level 1:






-------------------------------------------------------------------------------------
LEVEL 1
Welcome to CITYSIM! 


To COMPLETE this level, you must:
--- BUILD (1) building of type R-LC




 --> CASH LEFT: $50 -- OK!
(Remember! Building costs are multiplied by 1.0x the depth of where its parent was.)
******************************************************************************
BUILDINGS AVAILABLE:
	Residential
		Low-Income     (R-LC) 	W-   0.0 | E-   0.0             ($10)
	Commercial
	Industrial
******************************************************************************

-----------------------------------------


| D [128: HYDRO ($0)
| 1 [W:50.0/E:50.0/100%


-----------------------------------------

What would you like to do?
>> 


-------------------------------------------------------------------------------------






Okay. At the top, you'll notice the level counter and the title and/or description of
the level. This is followed by the important part: the goal of the level! Always look
for the "COMPLETE" keyword and the following requirements.

Next is the general "marketplace" for buying buildings. First you'll notice that your 
cash is indicated there, and either an "OK!" or "NOT OK!" appears beside it. What 
you'll see depends on how much cash the level expects you to have to complete it. 

Now we can buy stuff! Usually a level will require you to build a city with a required
amount of certain buildings. The buildings that are available to you for that level
will show up in that asterisk box. Within it, you'll see the description for each type,
followed by the short-form name (important!), and its consumption of Water and 
Electricity. If you're looking at a building of category Industrial, you'll also see a
"P- %" attribute, which indicates how much that building pollutes its children's 
utilities.

To actually buy one of these, you'll need to know two things: the three-letter short-
form name of the building you wish to build, and the ID of the property in your city
under which you would like to attach it to. If we look at what's already in our city:



| D [128: HYDRO ($0)
| 1 [W:50.0/E:50.0/100%



128 is our Hydro station's ID! (You'll also notice the depth "D1". We'll talk about 
that in a second!) So in order to build a building of type "Low-Income" under the Hydro
station, we type:


>> build <type> <ID>


or in this case:


>> build R-LC 128


And then we end up with this:



| D [128: HYDRO ($0)
| 1 [W:50.0/E:50.0/100%
|           D [64: R-LC ($10)
|           2 [W:40.0/E:40.0/100%



Nice! We just bought our first building. Notice the stats attached to it: it cost us $10
to build, and it currently has 40 units of water and electricity at its disposal. The 
water quality is also 100%! Great! Let's build another one:


>> build R-LC 64


So that we end up with:


| D [128: HYDRO ($0)
| 1 [W:50.0/E:50.0/100%
|           D [64: R-LC ($10)
|           2 [W:40.0/E:40.0/100%
|                     D [32: R-LC ($20)
|                     3 [W:30.0/E:30.0/100%



Huh. Notice anything different here? The price to build this guy actually went up! This 
is due to the distance from the Hydro center. Notice again the "D3" value? This is how
distant the building is from the root. In order to properly calculate the cost of building
something, you must take the base cost in the table and multiply it by the distance of its
parent from the Hydro station, and multiply it by the constant marked above the table. In 
our case,


COST = Base Cost of $10 * Parent distance 2 * Constant 1.0 = $20


Alright. What if we figure that's too much money for us? Well, luckily you can simply remove
the building from your tree and be reimbursed the amount you spent. But a word of caution: 
all buildings attached below will also be removed! It may be wise not to make many mistakes
early on! In any case, if you are willing to remove a building, you will type:


>> remove <ID>


You will be greeted by a confirmation request (which defaults to NO if you enter the wrong
thing), and then you will be refunded the money it cost to build everything you removed! So
if you type:


>> remove 64


You will find the result to be:


| D [128: HYDRO ($0)
| 1 [W:50.0/E:50.0/100%



And you will notice that you have been refunded your $30 back. There are other mechanisms
that make this game a little more sophisticated, but you'll see those as you play!





---------------------------------------------------------------------------------------------





If you are looking to make your own levels, this section will explain how configuration works.


The game requires you to place a plain text file in the same folder as this README file, with
the following particularities (whitespace and line starting with non-"|" characters are ignored!):

 -- The file must begin with the "!-CONFIG[??]" keyword, where ?? is the number of levels 
	defined within. It must match, otherwise you'll get an error.

 -- Now we are defining level 1. There is pre-level configuration to do first though! This will
	be where we tell the game
		-- (1) How much cash the user starts off with
		-- (2) How much water the Hydro station starts off with 
		-- (3) How much electricity it'll have
		-- (4) By how much the polluting buildings will pollute (a multiplicative factor!)
		-- (5) By how much the distance increases the price of building something.

	This will be formatted in the following way: "| (1), (2), (3), (4), (5)"

 -- Now we must define rules for the level. A rule title is declared with a "|-" at the beginning
	of the line, followed by the rule in CAPS. There are five rules to fill:

		-- (1) DESC: The level description. Every "|" following this rule will contain
			     strings to be printed, followed by a new line break. Empty lines OK.
		-- (2) WATER/ELEC/COST: 13 comma-separated values, like the pre-level config, which
			     define (respectively) the water usage, electricity usage, and cost to 
			     build that building. The values are defined in the following order: 

			R-LC, R-MC, R-HC, R-ED, R-IF, R-PK, C-SB, C-LB, C-HB, I-SC, I-SD, I-LC, I-LD

		-- (3) GOAL: This rule require three lines. 
			-- The first is a single value indicating the goal cash.
			-- The second is another 13-value set indicating how many builds are expected
			-- The third is another set indicating how much %pollution this building tolerates.

 -- Finally, the level definition is ended with a "|*" line, which also increments the level counter if
	you wish to define more levels! This line is required for each level though!






Have fun!










