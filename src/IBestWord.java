import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public interface IBestWord {

    /**
     * Creates an integer representation of the board. 1 means there is a tile, 0 means it's blank.
     */
    int[][] makeIntegerRepresentationOfBoard();   
    
    /**
     * Returns all valid starting locations (anchors) for a new move
     * @return Coordinates for all valid starting locations for a new move, where an Entry represents <row, column>
     */
    ArrayList<Entry<Integer, Integer>> getValidAnchors();
    
    /**
     * Obtains a list of all possible sequences of tile placements, ignoring word validity and focusing on valid tile connections
     * @param A list of all valid anchors for the current board
     * @return An Entry where the key contains the range of all vertical placements and the value contains the ranges of all horizontal placements
     */
    Entry<ArrayList<Entry<Integer, Entry<Integer, Integer>>>, ArrayList<Entry<Integer, Entry<Integer, Integer>>>> allValidTilePlacements(ArrayList<Entry<Integer, Integer>> anchors);
    
    /**
     * Iterates through the list of placements and finds all valid words for each 
     * @param placements All valid placements for the given board
     */
    void tryAllPlacements(ArrayList<HashSet<Entry<Integer, Integer>>> placements);
    
    /**
     * Finds all valid words for a given placement of tiles
     * @param placement A HashSet of coordinates representing a placement of letter-less tiles
     * @return A list of words, where each word is stored as a mapping of coordinate to letter placed at that coordinate
     */
    ArrayList<HashMap<Entry<Integer, Integer>, Character>> validWordsForPlacement(HashSet<Entry<Integer, Integer>> placement);
    
    /**
     * Takes a list of all valid moves and returns the highest scoring one
     * @param moves A list of all valid moves 
     * @return The highest scoring move
     */
    HashMap<Entry<Integer, Integer>, Character> highestScoringMove(ArrayList<HashMap<Entry<Integer, Integer>, Character>> moves);
}
