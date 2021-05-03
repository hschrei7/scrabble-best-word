
public interface IBoard {
    
    /**
     * Initializes the board with the appropriate 2d array of Squares
     */
    void initializeSquares();
    
    /**
     * Gets the Board
     * @return A 2d array of squares representing the board's current state
     */
    Square[][] getBoard();

    /**
     * Sets the board
     * @param board A 2d array of Square objects representing the new state of the board
     */
    void setBoard(Square[][] board);

    /**
     * Gets the board's Bag, which contains all remaining letters
     * @return A Bag object for this particular Board
     */
    Bag getBag();
    
    /**
     * Prints the board, showing all placed letters and unused bonus squares
     */
    void printBoard();
    
    /**
     * Places a tile on the board at the desired location
     * @param t Tile to place
     * @param row Desired row
     * @param col Desired column
     */
    void placeTile(Tile t, int row, int col);
    
    /**
     * Creates a new board that is completely random
     */
    void createRandomBoard();
}
