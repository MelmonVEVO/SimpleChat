import java.io.*;
import java.util.ArrayList;

/**
 * Class for containing the map and its related methods
 *
 * Pulled from Dylan Drescher (dgd29, author)'s Dungeon of Doom coursework
 */
class DODMap {

	/* Tile help:
	*  0 = floor tile (things can freely pass through) represented with "."
	*  1 = wall tile (nothing can pass through) represented with "#"
	*  2 = gold tile (adds gold to the player when picked up, turns in to a regular floor tile) represented with "G"
	*  3 = exit tile (ends the game when walked on) represented with "E"
	*  4 = player
	*  5 = enemy
	*/

	private ArrayList<ArrayList<Character>> map = new ArrayList<>();
	private int goldRequirement;
	private String name;

	/**
	 * Constructor
	 *
     * @param filename : Name of file to look for a map
     */
	DODMap(String filename) {
		try {
            BufferedReader buffer = new BufferedReader(new FileReader(new File(filename)));
            // reader based off replies from:
			// https://stackoverflow.com/questions/5868369/how-to-read-a-large-text-file-line-by-line-using-java
            String gottenLine;
            try {
                while ((gottenLine = buffer.readLine()) != null) { // read through every line
                    String[] processedLine = gottenLine.split("\\s+"); // split each line by word
                    if (processedLine[0].equals("name")) { // for names
                    	this.name = "";
                    	for (int x = 1; x < processedLine.length; x++) {
							this.name = this.name.concat(processedLine[x] + " ");
						}
                    }
                    else if (processedLine[0].equals("win")) { // for the gold requirements
                        try {
                            this.goldRequirement = Integer.parseInt(processedLine[1]);
                        }
                        catch (NumberFormatException x) {
                            System.out.println("Map file formatted incorrectly!");
                        }
                    }
                    else { // for the map itself
                        ArrayList<Character> toAdd = new ArrayList<>();
                        for (int x = 0; x < processedLine[0].length(); x++) {
                            toAdd.add(processedLine[0].charAt(x));
                        }
                        this.map.add(toAdd);
                    }
                }
            }
            catch (IOException x) { // if an IO error happens
                System.out.println("An error occurred!");
            }
		}
		catch (FileNotFoundException x) { // when a file is not found
			// System.out.println(new File(".").getAbsoluteFile());
			System.out.println("File not found!");
			this.name = "#FILENOTFOUND";
		}
	}

	/**
	 * Gets a specific tile in a map
	 *
	 * @param y : y-coordinate
	 * @param x : x-coordinate
	 * @return : gotten tile
	 */
	char getTile(int y, int x) {
		try {
			return this.map.get(y).get(x);
		}
		catch (IndexOutOfBoundsException z) {
			return ' ';
		}
	}

    /**
     * Returns the name of the map
     *
     * @return : name
     */
	String getName() {
	    return this.name;
    }

    /**
     * Returns the gold requirement
     *
     * @return : gold requirement
     */
	int getGoldRequirement() {
	    return this.goldRequirement;
    }

	/**
	 * Checks if gold can be grabbed, and grabs gold if possible
	 *
	 * @param y : y-coordinate
	 * @param x : x-coordinate
	 * @return : whether gold has been collected or not
	 */
	boolean grabGold(int y, int x) {
		if (map.get(y).get(x) == 'G') {
			ArrayList<Character> newRow = map.get(y);
			newRow.set(x, '.');
			map.set(y, newRow);
			return true;
		}
		return false;
	}

	/**
	 * Returns the size of each column
     *
	 * @return : size
	 */
	int lenCol() {
		return this.map.size();
	}

	/**
	 * Returns the size of each row
     *
	 * @return : size
	 */
	int lenRow() {
		return this.map.get(0).size();
	}

	/**
	 * Prints out the nearest tiles from the player in a 5x5 grid
     *
	 * @param playery : y-coordinate of player
	 * @param playerx : x-coordinate of player
	 * @param enemyy : y-coordinate of enemy
	 * @param enemyx : x-coordinate of enemy
	 */
	String look(int playery, int playerx, int enemyy, int enemyx) {
		StringBuilder out = new StringBuilder();
		out.append("Here's what you see:\n");
		for (int z = playery - 2; z <= playery+2; z++) { // z is the current observed y
			for (int w = playerx - 2; w <= playerx+2; w++) { // w is the current observed x
				if (playery == z && playerx == w) {
					out.append("P");
				}
				else if (enemyy == z && enemyx == w) {
					out.append("B");
				}
				else {
					out.append(getTile(z, w));
				}
			}
			out.append("\n");
		}
		return out.toString();
	}

}
