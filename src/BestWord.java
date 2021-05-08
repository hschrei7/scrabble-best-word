import java.util.AbstractMap.SimpleEntry;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Map.Entry;

public class BestWord implements IBestWord {

    Board board;
    int[][] intBoard;
    ArrayList<String>[] dictionary;

    BestWord(Board b) {
        this.board = b;
        intBoard = makeIntegerRepresentationOfBoard();
    }

    /**
     * Creates an integer representation of the board. 1 means there is a tile, 0
     * means it's blank.
     */
    public int[][] makeIntegerRepresentationOfBoard() {
        int[][] ans = new int[15][15];
        Square[][] boardSquares = this.board.getBoard();
        for (int i = 0; i < boardSquares.length; i++) {
            for (int j = 0; j < boardSquares[i].length; j++) {
                if (boardSquares[i][j].hasTile()) {
                    ans[i][j] = 1;
                } else {
                    ans[i][j] = 0;
                }
            }
        }
        return ans;
    }

    public void printIntBoard() {
        for (int[] arr : this.intBoard) {
            for (int curr : arr) {
                System.out.print(curr + " ");
            }
            System.out.println();
        }
    }

    /**
     * Returns all valid starting locations (anchors) for a new move
     * 
     * @return Coordinates for all valid starting locations for a new move, where an
     *         Entry represents <row, column>
     */
    public ArrayList<Entry<Integer, Integer>> getValidAnchors() {
        ArrayList<Entry<Integer, Integer>> validAnchors = new ArrayList<Entry<Integer, Integer>>();

        for (int i = 0; i < this.intBoard.length; i++) {
            for (int j = 0; j < this.intBoard[i].length; j++) {
                if (isValidAnchor(i, j)) {
                    Entry<Integer, Integer> coords = new SimpleEntry<Integer, Integer>(i, j);
                    validAnchors.add(coords);
                }
            }
        }

        return validAnchors;
    }

    /**
     * A helper method to check if a given location on the board is a valid anchor
     * by checking its neighbors.
     * 
     * @param row row
     * @param col column
     * @return True if the location is a valid anchor, false otherwise
     */
    private boolean isValidAnchor(int row, int col) {
        // If the space is already filled, return false
        if (this.intBoard[row][col] == 1) {
            return false;
        }
        // If the space is the middle square and is 0, return true.
        if (row == 7 && col == 7) {
            return true;
        }

        int upperRow = row - 1;
        int lowerRow = row + 1;
        int leftCol = col - 1;
        int rightCol = col + 1;

        // Check the four surrounding positions. Before checking, ensure that the
        // row/column exists.
        if (upperRow >= 0 && this.intBoard[upperRow][col] == 1) {
            return true;
        }
        if (lowerRow <= 14 && this.intBoard[lowerRow][col] == 1) {
            return true;
        }
        if (leftCol >= 0 && this.intBoard[row][leftCol] == 1) {
            return true;
        }
        if (rightCol <= 14 && this.intBoard[row][rightCol] == 1) {
            return true;
        }
        return false;
    }

    /**
     * Obtains a list of all possible sequences of tile placements, ignoring word
     * validity and focusing on valid tile connections
     * 
     * @param A list of all valid anchors for the current board
     * @return An Entry where the key contains the range of all vertical placements
     *         and the value contains the ranges of all horizontal placements
     */
    public Entry<ArrayList<Entry<Integer, Entry<Integer, Integer>>>, ArrayList<Entry<Integer, Entry<Integer, Integer>>>> allValidTilePlacements(
            ArrayList<Entry<Integer, Integer>> anchors) {

        ArrayList<Entry<Integer, Entry<Integer, Integer>>> verticalPlacements = new ArrayList<Entry<Integer, Entry<Integer, Integer>>>();
        ArrayList<Entry<Integer, Entry<Integer, Integer>>> horizontalPlacements = new ArrayList<Entry<Integer, Entry<Integer, Integer>>>();

        for (Entry<Integer, Integer> anchor : anchors) {
            // try all vertical combos above and below anchor
            for (int i = 0; i < 7; i++) {
                int above = collectPlacementsAbove(anchor, i);
                if (above == -1) {
                    continue;
                }
                for (int j = 6 - i; j >= 0; j--) {
                    int below = collectPlacementsBelow(anchor, j);
                    if (below == -1) {
                        continue;
                    }
                    // We have identified a valid vertical range to add tiles. Add that range to our
                    // answer.
                    Entry<Integer, Integer> rowRange = new SimpleEntry<Integer, Integer>(above, below);
                    Entry<Integer, Entry<Integer, Integer>> colAndRange = new SimpleEntry<Integer, Entry<Integer, Integer>>(
                            anchor.getValue(), rowRange);
                    verticalPlacements.add(colAndRange);
//                    System.out.println("The anchor Row: " + anchor.getKey() + " Col: " + anchor.getValue());
//                    System.out.println("has range " + rowRange.getKey() + " - " + rowRange.getValue());
                }
            }
            // try all horizontal combos to left and right of anchor
            for (int i = 0; i < 7; i++) {
                int left = collectPlacementsLeft(anchor, i);
                if (left == -1) {
                    continue;
                }
                for (int j = 6 - i; j >= 0; j--) {
                    int right = collectPlacementsRight(anchor, j);
                    if (right == -1) {
                        continue;
                    }
                    // We have identified a valid horizontal range to add tiles. Add that range to
                    // our answer.
                    Entry<Integer, Integer> colRange = new SimpleEntry<Integer, Integer>(left, right);
                    Entry<Integer, Entry<Integer, Integer>> rowAndRange = new SimpleEntry<Integer, Entry<Integer, Integer>>(
                            anchor.getKey(), colRange);
                    horizontalPlacements.add(rowAndRange);
//                    System.out.println("The anchor Row: " + anchor.getKey() + " Col: " + anchor.getValue());
//                    System.out.println("has horizontal range " + colRange.getKey() + " - "+ colRange.getValue());
                }
            }
        }

        Entry<ArrayList<Entry<Integer, Entry<Integer, Integer>>>, ArrayList<Entry<Integer, Entry<Integer, Integer>>>> placements = new SimpleEntry<ArrayList<Entry<Integer, Entry<Integer, Integer>>>, ArrayList<Entry<Integer, Entry<Integer, Integer>>>>(
                verticalPlacements, horizontalPlacements);
        return placements;
    }

    private int collectPlacementsAbove(Entry<Integer, Integer> anchor, int range) {

        int row = anchor.getKey();
        int col = anchor.getValue();

        if (range == 0) {
            return row;
        }

        for (int i = row - 1; i >= 0; i--) {
            if (this.intBoard[i][col] == 0) {
                range--;
                if (range == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int collectPlacementsBelow(Entry<Integer, Integer> anchor, int range) {

        int row = anchor.getKey();
        int col = anchor.getValue();

        if (range == 0) {
            return row;
        }

        for (int i = row + 1; i <= 14; i++) {
            if (this.intBoard[i][col] == 0) {
                range--;
                if (range == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int collectPlacementsLeft(Entry<Integer, Integer> anchor, int range) {

        int row = anchor.getKey();
        int col = anchor.getValue();

        if (range == 0) {
            return col;
        }

        for (int i = col - 1; i >= 0; i--) {
            if (this.intBoard[row][i] == 0) {
                range--;
                if (range == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int collectPlacementsRight(Entry<Integer, Integer> anchor, int range) {

        int row = anchor.getKey();
        int col = anchor.getValue();

        if (range == 0) {
            return col;
        }

        for (int i = col + 1; i <= 14; i++) {
            if (this.intBoard[row][i] == 0) {
                range--;
                if (range == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Iterates through the list of placements and finds all valid words for each
     * 
     * First entry is Column:{rowStart, rowEnd} Second entry is Row:{colStart,
     * colEnd}
     * 
     * Will parse the placements and send them one by one to validWordsForPlacement
     * 
     * @param placements All valid placements for the given board
     */
    public ArrayList<HashMap<Entry<Integer, Integer>, Character>> tryAllPlacements(
            Entry<ArrayList<Entry<Integer, Entry<Integer, Integer>>>, ArrayList<Entry<Integer, Entry<Integer, Integer>>>> placements) {
        readDictionary("scrabbleDictionary.txt");
        ArrayList<Entry<Integer, Entry<Integer, Integer>>> verticalPlays = placements.getKey();
        ArrayList<Entry<Integer, Entry<Integer, Integer>>> horizontalPlays = placements.getValue();
        ArrayList<HashMap<Entry<Integer, Integer>, Character>> legalMoves = new ArrayList<HashMap<Entry<Integer, Integer>, Character>>();
        for (Entry entry : verticalPlays) {
            legalMoves.addAll(validWordsForVerticalPlacement(entry));
        }
        for (Entry entry : horizontalPlays) {
            legalMoves.addAll(validWordsForHorizontalPlacement(entry));
        }
        System.out.println("numMoves: " + legalMoves.size());
        return legalMoves;
    }

    /**
     * Method to read and build an arraylist from a .txt file on disk.
     * 
     * @param filename
     */
    private void readDictionary(String filename) {
        dictionary = new ArrayList[16];
        for (int i = 0; i <= 15; i++) {
            dictionary[i] = new ArrayList<String>();
        }
        File f = new File(filename);
        try {
            Scanner s = new Scanner(f);
            // obtain all words from the dictionary
            while (s.hasNextLine()) {
                String nextWord = s.nextLine();
                dictionary[nextWord.length()].add(nextWord);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds all valid words for a given placement of tiles
     * 
     * Checking board for existing tiles in a placement. Forming array from existing
     * tiles, where array length is size of placement. Build arraylist of valid
     * words from dictionary. Checking dictionary for: array length character
     * comparison at index. Run valid horizontal neighbors help.
     * 
     * If word passes all above tests, add it to ArrayList
     * 
     * Column:{rowStart, rowEnd}
     * 
     * @param placement A HashSet of coordinates representing a placement of
     *                  letter-less tiles
     * @return A list of words, where each word is stored as a mapping of coordinate
     *         to letter placed at that coordinate
     */
    public ArrayList<HashMap<Entry<Integer, Integer>, Character>> validWordsForVerticalPlacement(
            Entry<Integer, Entry<Integer, Integer>> placement) {
        ArrayList<HashMap<Entry<Integer, Integer>, Character>> validMoves = new ArrayList<HashMap<Entry<Integer, Integer>, Character>>();
        int playCol = placement.getKey();
        int playRowStart = placement.getValue().getKey();
        int playRowEnd = placement.getValue().getValue();
        int playLength = (playRowEnd - playRowStart) + 1;
        char[] playArray = new char[playLength];
        int arrayCounter = 0;
        for (int row = playRowStart; row <= playRowEnd; row++) {
            if (board.getSquare(row, playCol).getTile() == null) {
                playArray[arrayCounter] = ' ';
            } else {
                playArray[arrayCounter] = board.getSquare(row, playCol).getTile().getLetter();
            }
            arrayCounter += 1;
        }
        for (String word : dictionary[playLength]) {
            if (word.length() != playArray.length) {
                continue;
            }
            boolean wordMatch = true;
            for (int i = 0; i < playArray.length; i++) {
                if (playArray[i] == ' ') {
                    continue;
                } else if (playArray[i] == word.charAt(i)) {
                    continue;
                } else {
                    wordMatch = false;
                }
            }
            if (wordMatch) {
                HashMap<Entry<Integer, Integer>, Character> newWordChars = new HashMap<Entry<Integer, Integer>, Character>();
                int charCounter = 0;
                for (int row = playRowStart; row <= playRowEnd; row++) {
                    if (validHorizontalNeighbors(row, playCol, word.charAt(charCounter))) {
                        Entry<Integer, Integer> newEntry = new SimpleEntry(row, playCol);
                        newWordChars.put(newEntry, word.charAt(charCounter));
                        charCounter += 1;
                    }
                }
                validMoves.add(newWordChars);
            }
        }
        return validMoves;
    }

    private boolean validHorizontalNeighbors(int row, int col, char c) {
        // Will scan horizontally in both east and west
        // until we hit an empty spot.
        String returnString = "" + c;
        int leftCharInt = -1;
        // Check letters to left
        int leftCol = col;
        while ((leftCharInt != 0) && (leftCol >= 0)) {
            if (board.getTile(row, leftCol) == null) {
                leftCol--;
                break;
            }
            returnString = board.getTile(row, leftCol).getLetter() + returnString;
            leftCol--;
        }
        // Check letters to right
        int rightCharInt = -1;
        int rightCol = col;
        while ((rightCharInt != 0) && (rightCol <= 14)) {
            if (board.getTile(row, rightCol) == null) {
                rightCol++;
                break;
            }
            returnString = returnString + board.getTile(row, rightCol).getLetter();
            rightCol++;
        }
        int stringLen = returnString.length();
        if (!dictionary[stringLen].contains(returnString)) {
            return false;
        }
        return true;
    }

    /**
     * Finds all valid words for a given placement of tiles
     * 
     * Checks
     * 
     * @param placement A HashSet of coordinates representing a placement of
     *                  letter-less tiles
     * @return A list of words, where each word is stored as a mapping of coordinate
     *         to letter placed at that coordinate
     */
    public ArrayList<HashMap<Entry<Integer, Integer>, Character>> validWordsForHorizontalPlacement(
            Entry<Integer, Entry<Integer, Integer>> placement) {
        ArrayList<HashMap<Entry<Integer, Integer>, Character>> validMoves = new ArrayList<HashMap<Entry<Integer, Integer>, Character>>();
        int playRow = placement.getKey();
        int playColStart = placement.getValue().getKey();
        int playColEnd = placement.getValue().getValue();
        int playLength = (playColEnd - playColStart) + 1;
        char[] playArray = new char[playLength];
        int arrayCounter = 0;
        for (int col = playColStart; col <= playColEnd; col++) {
            if (board.getSquare(playRow, col).getTile() == null) {
                playArray[arrayCounter] = ' ';
            } else {
                playArray[arrayCounter] = board.getSquare(playRow, col).getTile().getLetter();
            }
            arrayCounter += 1;
        }
        for (String word : dictionary[playLength]) {
            if (word.length() != playArray.length) {
                continue;
            }
            boolean wordMatch = true;
            for (int i = 0; i < playArray.length; i++) {
                if (playArray[i] == ' ') {
                    continue;
                } else if (playArray[i] == word.charAt(i)) {
                    continue;
                } else {
                    wordMatch = false;
                    break;
                }
            }
            if (wordMatch) {
                HashMap<Entry<Integer, Integer>, Character> newWordChars = new HashMap<Entry<Integer, Integer>, Character>();
                int charCounter = 0;
                for (int col = playColStart; col <= playColEnd; col++) {
                    if (validVerticalNeighbors(col, playRow, word.charAt(charCounter))) {
                        Entry<Integer, Integer> newEntry = new SimpleEntry(playRow, col);
                        newWordChars.put(newEntry, word.charAt(charCounter));
                        charCounter += 1;
                    }
                }
                validMoves.add(newWordChars);
            }
        }
        return validMoves;
    }
    
    
    private boolean validVerticalNeighbors(int row, int col, char c) {
        // Will scan horizontally in both east and west
        // until we hit an empty spot.
        String returnString = "" + c;
        int topCharInt = -1;
        // Check letters to left
        int topRow = row;
        while ((topCharInt != 0) && (topRow >= 0)) {
            if (board.getTile(topRow, col) == null) {
                topRow--;
                break;
            }
            returnString = board.getTile(topRow, col).getLetter() + returnString;
            topRow--;
        }
        // Check letters to right
        int botCharInt = -1;
        int botRow = row;
        while ((botCharInt != 0) && (botRow <= 14)) {
            if (board.getTile(botRow, col) == null) {
                botRow++;
                break;
            }
            returnString = returnString + board.getTile(botRow, col).getLetter();
            botRow++;
        }
        int stringLen = returnString.length();
        if (!dictionary[stringLen].contains(returnString)) {
            return false;
        }
        return true;
    }

    /**
     * Takes a list of all valid moves and returns the highest scoring one
     * 
     * @param moves A list of all valid moves
     * @return The highest scoring move
     */
    public HashMap<Entry<Integer, Integer>, Character> highestScoringMove(
            ArrayList<HashMap<Entry<Integer, Integer>, Character>> moves) {
        HashMap<Entry<Integer, Integer>, Character> best = new HashMap<Entry<Integer, Integer>, Character>();
        int bestScore = 0;
        for (HashMap<Entry<Integer, Integer>, Character> h : moves) {
            int test = calculateScore(h);
            if (test > bestScore) {
                bestScore = test;
                best = h;
            }
        }
        return best;
    }

    /**
     * Helper function to compute score of a given legal move
     * 
     * @param move - a specific legal move
     * @return the score of the move
     */
    public int calculateScore(HashMap<Entry<Integer, Integer>, Character> move) {
        int score = 0;
        int keyA;
        int keyB;
        int valA;
        int valB;
        boolean vert = false;
        for (Entry<Integer, Integer> e : move.keySet()) {
            // is this word getting placed horizontally or vertically?
            // if yes, our adjacent keys to look at are left and right
            // if no, our adjacent values to look at are above and below
            // depending on orientation, we will want to decrement/increment
            // along one of these axes
            if (verticalPlacement(move)) {
                keyA = e.getKey() - 1;
                keyB = e.getKey() + 1;
                valA = e.getValue();
                valB = e.getValue();
                vert = true;
            } else {
                keyA = e.getKey();
                keyB = e.getKey();
                valA = e.getValue() - 1;
                valB = e.getValue() + 1;
            }
            // once orientation established, go through word
            // from tile to beginning, then tile to end
            // tally a temp score along the way so we can figure out what to do
            // vis a vis multiplier at the end
            int tempScore = 0;
            if (intBoard[keyA][valA] == 1) {
                while (intBoard[keyA][valA] == 1) {
                    Tile t = board.getTile(keyA, valA);
                    tempScore += t.getPoints();
                    if (vert) {
                        keyA--;
                    } else {
                        valA--;
                    }
                }
            }
            if (intBoard[keyB][valB] == 1) {
                while (intBoard[keyB][valB] == 1) {
                    Tile t = board.getTile(keyB, valB);
                    tempScore += t.getPoints();
                    if (vert) {
                        keyB--;
                    } else {
                        valB--;
                    }
                }
            }
            // we've summed everything left/above and right/below
            // now we need to include the value of the newly placed tile
            // and figure out the multiplier
            Tile newTile = new Tile(move.get(e));
            tempScore += newTile.getPoints();
            int multCode = board.getSquare(e.getKey(), e.getValue()).getMultiplier();
            if (multCode == 1) {
                tempScore += newTile.getPoints();
            } else if (multCode == 2) {
                tempScore += (2 * newTile.getPoints());
            } else if (multCode == 3) {
                tempScore = tempScore * 2;
            } else if (multCode == 4) {
                tempScore = tempScore * 3;
            }
            // we are going to sum each of these temp scores to our total
            score += tempScore;
        }
        // now we need to sum the score of our word in isolation
        // and add to total score to be returned
        // we are going to keep track of double and triple word scores
        // in an array, and then at the end we will iterate through array
        // and multiply the word by the appropriate amount for however
        // many word multipliers we collected
        ArrayList<Integer> XWordScores = new ArrayList<Integer>();
        int newTempScore = 0;
        for (Entry<Integer, Integer> e1 : move.keySet()) {
            char c = move.get(e1);
            Tile t1 = new Tile(c);
            int value = t1.getPoints();
            newTempScore += value;
            Square s1 = board.getSquare(e1.getKey(), e1.getValue());
            int newMultCode = s1.getMultiplier();
            if (newMultCode == 1) {
                newTempScore += value;
            } else if (newMultCode == 2) {
                newTempScore += (value * 2);
            } else if (newMultCode == 3) {
                XWordScores.add(2);
            } else if (newMultCode == 4) {
                XWordScores.add(3);
            }
        }
        if (XWordScores.size() > 0) {
            for (int i : XWordScores) {
                newTempScore = newTempScore * i;
            }
        }
        score += newTempScore;
        return score;
    }

    /**
     * Helper function to denote whether the word is meant to be played Vertically
     * or Horizontally
     * 
     * @param move - a specific legal move
     * @return true if meant to be played vertically, false if not
     */
    public boolean verticalPlacement(HashMap<Entry<Integer, Integer>, Character> move) {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for (Entry<Integer, Integer> e : move.keySet()) {
            int y = e.getValue();
            temp.add(y);
        }
        if (temp.size() > 1) {
            if (temp.get(0) != temp.get(1)) {
                return false;
            } else {
                return true;
            }
        }
        return true;
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
        bw.tryAllPlacements(bw.allValidTilePlacements(validAnchors));
    }
}
