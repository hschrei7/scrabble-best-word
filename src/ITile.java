
public interface ITile {
    
    /**
     * Gets the letter of this Tile object
     * @return The tile's letter
     */
    char getLetter();

    /**
     * Returns whether or not this tile is a blank
     * @return True if the tile is blank, and false otherwise
     */
    boolean isBlank();
    
    /**
     * Gets the point value of this tile
     * @return An int representing the point value for this tile
     */
    int getPoints(); 
}
