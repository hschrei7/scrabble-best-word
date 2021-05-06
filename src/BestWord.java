import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class BestWord implements IBestWord {

    Board board;
    int[][] intBoard;
    
    BestWord(Board b){
        this.board = b;
        intBoard = makeIntegerRepresentationOfBoard();
    }
    
    /**
     * Creates an integer representation of the board. 1 means there is a tile, 0 means it's blank.
     */
    public int[][] makeIntegerRepresentationOfBoard() {
        int[][] ans = new int[15][15];
        Square[][] boardSquares = this.board.getBoard();
        for(int i = 0; i < boardSquares.length; i++) {
            for(int j = 0; j < boardSquares[i].length; j++){
                if(boardSquares[i][j].hasTile()) {
                    ans[i][j] = 1;
                }
                else {
                    ans[i][j] = 0;
                }
            }
        }
        return ans;
    }
    
    public void printIntBoard() {
        for(int[] arr : this.intBoard) {
            for(int curr : arr) {
                System.out.print(curr + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * Returns all valid starting locations (anchors) for a new move
     * @return Coordinates for all valid starting locations for a new move, where an Entry represents <row, column>
     */
    public ArrayList<Entry<Integer, Integer>> getValidAnchors(){
        ArrayList<Entry<Integer, Integer>> validAnchors = new ArrayList<Entry<Integer, Integer>>();
        
        for(int i = 0; i < this.intBoard.length; i++) {
            for(int j = 0; j < this.intBoard[i].length; j++) {
                if(isValidAnchor(i, j)) {
                    Entry<Integer, Integer> coords = new SimpleEntry<Integer, Integer>(i, j);
                    validAnchors.add(coords);
                }
            }
        }
        
        return validAnchors;
    }
    
    
    /**
     * A helper method to check if a given location on the board is a valid anchor by checking its neighbors.
     * @param row row
     * @param col column
     * @return True if the location is a valid anchor, false otherwise
     */
    private boolean isValidAnchor(int row, int col) {
        // If the space is already filled, return false
        if(this.intBoard[row][col] == 1) {
            return false;
        }
        // If the space is the middle square and is 0, return true.
        if(row == 7 && col == 7) {
            return true;
        }
        
        int upperRow = row - 1;
        int lowerRow = row + 1;
        int leftCol = col - 1;
        int rightCol = col + 1;
        
        // Check the four surrounding positions. Before checking, ensure that the row/column exists.
        if(upperRow >= 0 && this.intBoard[upperRow][col] == 1) {
            return true;
        }
        if(lowerRow <= 14 && this.intBoard[lowerRow][col] == 1) {
            return true;
        }
        if(leftCol >= 0 && this.intBoard[row][leftCol] == 1) {
            return true;
        }
        if(rightCol <= 14 && this.intBoard[row][rightCol] == 1) {
            return true;
        }
        return false;
    }
    
    
    /**
     * Obtains a list of all possible sequences of tile placements, ignoring word validity and focusing on valid tile connections
     * @param A list of all valid anchors for the current board
     * @return An Entry where the key contains the range of all vertical placements and the value contains the ranges of all horizontal placements
     */
    public Entry<ArrayList<Entry<Integer, Entry<Integer, Integer>>>, ArrayList<Entry<Integer, Entry<Integer, Integer>>>> allValidTilePlacements(ArrayList<Entry<Integer, Integer>> anchors){
        
        ArrayList<Entry<Integer, Entry<Integer, Integer>>> verticalPlacements = new ArrayList<Entry<Integer, Entry<Integer, Integer>>>();
        ArrayList<Entry<Integer, Entry<Integer, Integer>>> horizontalPlacements = new ArrayList<Entry<Integer, Entry<Integer, Integer>>>();
        
        for(Entry<Integer, Integer> anchor : anchors) {
            //try all vertical combos above and below anchor
            for(int i = 0; i < 7; i++) {
                int above = collectPlacementsAbove(anchor, i);
                if(above == -1) {
                    continue;
                }
                for(int j = 6 - i; j >= 0; j--) {
                    int below = collectPlacementsBelow(anchor, j);
                    if(below == -1) {
                        continue;
                    }
                    //We have identified a valid vertical range to add tiles. Add that range to our answer. 
                    Entry<Integer, Integer> rowRange = new SimpleEntry<Integer, Integer>(above, below);
                    Entry<Integer, Entry<Integer, Integer>> colAndRange = new SimpleEntry<Integer, Entry<Integer, Integer>>(anchor.getValue(), rowRange);
                    verticalPlacements.add(colAndRange);
//                    System.out.println("The anchor Row: " + anchor.getKey() + " Col: " + anchor.getValue());
//                    System.out.println("has range " + rowRange.getKey() + " - "+ rowRange.getValue());
                }
            }
            //try all horizontal combos to left and right of anchor
            for(int i = 0; i < 7; i++) {
                int left = collectPlacementsLeft(anchor, i);
                if(left == -1) {
                    continue;
                }
                for(int j = 6 - i; j >= 0; j--) {
                    int right = collectPlacementsRight(anchor, j);
                    if(right == -1) {
                        continue;
                    }
                    //We have identified a valid horizontal range to add tiles. Add that range to our answer. 
                    Entry<Integer, Integer> colRange = new SimpleEntry<Integer, Integer>(left, right);
                    Entry<Integer, Entry<Integer, Integer>> rowAndRange = new SimpleEntry<Integer, Entry<Integer, Integer>>(anchor.getKey(), colRange);
                    horizontalPlacements.add(rowAndRange);
                    System.out.println("The anchor Row: " + anchor.getKey() + " Col: " + anchor.getValue());
                    System.out.println("has horizontal range " + colRange.getKey() + " - "+ colRange.getValue());
                }
            }
        }
        
        Entry<ArrayList<Entry<Integer, Entry<Integer, Integer>>>, ArrayList<Entry<Integer, Entry<Integer, Integer>>>> placements = new SimpleEntry<ArrayList<Entry<Integer, Entry<Integer, Integer>>>, ArrayList<Entry<Integer, Entry<Integer, Integer>>>>(verticalPlacements, horizontalPlacements);
        return placements;
    }
    
    private int collectPlacementsAbove(Entry<Integer, Integer> anchor, int range){    
        
        int row = anchor.getKey();
        int col = anchor.getValue();
        
        if(range == 0) {
            return row;
        }      
        
        for(int i = row-1; i >= 0; i--) {
            if(this.intBoard[i][col] == 0) {
                range--;
                if(range == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private int collectPlacementsBelow(Entry<Integer, Integer> anchor, int range){
        
        int row = anchor.getKey();
        int col = anchor.getValue();
        
        if(range == 0) {
            return row;
        }        
        
        for(int i = row+1; i <= 14; i++) {
            if(this.intBoard[i][col] == 0) {
                range--;
                if(range == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private int collectPlacementsLeft(Entry<Integer, Integer> anchor, int range){    
        
        int row = anchor.getKey();
        int col = anchor.getValue();
        
        if(range == 0) {
            return col;
        }      
        
        for(int i = col-1; i >= 0; i--) {
            if(this.intBoard[row][i] == 0) {
                range--;
                if(range == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private int collectPlacementsRight(Entry<Integer, Integer> anchor, int range){    
        
        int row = anchor.getKey();
        int col = anchor.getValue();
        
        if(range == 0) {
            return col;
        }      
        
        for(int i = col+1; i <= 14; i++) {
            if(this.intBoard[row][i] == 0) {
                range--;
                if(range == 0) {
                    return i;
                }
            }
        }
        return -1;
    }      
    
    /**
     * Iterates through the list of placements and finds all valid words for each 
     * @param placements All valid placements for the given board
     */
    public void tryAllPlacements(ArrayList<HashSet<Entry<Integer, Integer>>> placements) {

    }
    
    /**
     * Finds all valid words for a given placement of tiles
     * @param placement A HashSet of coordinates representing a placement of letter-less tiles
     * @return A list of words, where each word is stored as a mapping of coordinate to letter placed at that coordinate
     */
    public ArrayList<HashMap<Entry<Integer, Integer>, Character>> validWordsForPlacement(HashSet<Entry<Integer, Integer>> placement){
        return null;
    }
    
    /**
     * Takes a list of all valid moves and returns the highest scoring one
     * @param moves A list of all valid moves 
     * @return The highest scoring move
     */
    public HashMap<Entry<Integer, Integer>, Character> highestScoringMove(ArrayList<HashMap<Entry<Integer, Integer>, Character>> moves){
        return null;
    }

    public static void main(String[] args) {
        Board b = new Board();
        b.createRandomBoard();
        BestWord bw = new BestWord(b);
        b.printBoard();
        bw.printIntBoard();
        ArrayList<Entry<Integer, Integer>> validAnchors = bw.getValidAnchors();
//        System.out.println("Here are all valid anchors:");
//        for(Entry<Integer, Integer> curr : validAnchors) {
//            System.out.println("Row: " + curr.getKey() + "  Col: " + curr.getValue());
//        }
        bw.allValidTilePlacements(validAnchors);
    }    
}
