package code;

public class Guess {
	
	Cell[][] copy = new Cell[9][9];
	
	public Guess(Board org) {

		boolean guessMade = false;
		
		for(int x = 0; x < 9 && !guessMade; x ++) {
			for(int y = 0; y < 9 && !guessMade; y ++) {
				copy[x][y] = new Cell();
				copy[x][y].setBoxID( 3*(x/3) + (y)/3+1);
				
				if(org.getNumber(x, y) != 0) {
					copy[x][y].setNumber(org.getNumber(x,y));
				}
				
				boolean orgPotential[] = org.getPotential(x,y);
				
				for(int num = 1; num <= 9; num ++) {
					if(orgPotential[num] == true) {
						copy[x][y].turnOn(num);
					}
					else {
						copy[x][y].cantBe(num);
					}
				}
			}
		}
	}
	
	public int getNumber(int x , int y) {
		return copy[x][y].getNumber();
	}
	
	public boolean[] getPotential(int x, int y) {
		return copy[x][y].getPotential();
	}
}
