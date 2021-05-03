
public interface ISquare {
    
    /**
     * Gets the Tile object that belongs to this square
     * @return The Square's Tile object, or null if it doesn't have a tile
     */
    Tile getTile();

    /**
     * Returns whether or not this Square has a Tile placed on it yet
     * @return True if the Square has a Tile, and false otherwise
     */
    boolean hasTile();

    /**
     * Places a Tile on this Square
     * @param tile The Tile to place
     */
    void setTile(Tile tile);

    /**
     * Gets the multiplier for this Square
     * 0 = no multiplier
     * 1 = double letter
     * 2 = triple letter
     * 3 = double word
     * 4 = triple word
     * @return An int representation of the multiplier
     */
    int getMultiplier();

    /**
     * Returns the squares tile if it has one. Otherwise, it returns the squares multiplier.
     * @return A String representation of the squares tile if it has one. Otherwise, it returns the squares multiplier.
     */
    String getSquareString();
}
