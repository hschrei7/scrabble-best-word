import java.util.ArrayList;

public interface IBag {
    
    /**
     * Gets all tiles remaining in the bag
     * @return An arrayList containing all Tile objects yet to be removed from the bag
     */
    ArrayList<Tile> getTiles();
    
    /**
     * Gets a random tile from the Bag and removes it from the Bag object
     * @return The randomly selected tile
     */
    Tile getRandomTile();
    
    /**
     * Removes the given tile from the Bag
     * @param t The tile to remove
     */
    void removeTile(Tile t);
}
