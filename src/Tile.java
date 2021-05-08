import java.util.*;

public class Tile implements ITile {

	boolean isBlank;
	char letter;
	int points;

	/**
	 * Creates a new Tile with the specified letter
	 * @param letter The letter that the tile should be (input of space as the char means blank tile).
	 */
	Tile(char letter){
		if(letter == ' ') {
			this.isBlank = true;
		}
		else {
			this.isBlank = false;
		}
		this.letter = letter;
		
		if(letter == 'A' || letter == 'E' || letter == 'I' || letter == 'O' || letter == 'U'
						 || letter == 'L' || letter == 'N' || letter == 'S' || letter == 'T' || letter == 'R') {
			this.points = 1;
		}
		else if(letter == 'D' || letter == 'G') {
			this.points = 2;
		}
		else if(letter == 'B' || letter == 'C' || letter == 'M' || letter == 'P') {
			this.points = 3;
		}
		else if(letter == 'F' || letter == 'H' || letter == 'V' || letter == 'W' || letter == 'Y') {
			this.points = 4;
		}
		else if(letter == 'K') {
			this.points = 5;
		}
		else if(letter == 'J' || letter == 'X') {
			this.points = 8;
		}
		else {
			this.points = 10;
		}
	}
	
	public char getLetter() {
	    if (this.isBlank) {
	        return ' ';
	    }
		return letter;
	}

	public boolean isBlank() {
		return isBlank;
	}
	
	public int getPoints() {
	    return this.points;
	}
}
