package code;

public class Cell {
	/*A Cell represents a single square on the Sudoku Game Board. 
	 * It knows it's number - 0 means it is not solved.
	 * It knows the potential numbers that it could have from 1-9.
	 * The Sudoku game board is sub-divided into 9 smaller 3x3 sections that I will call a box. 
	 * These boxes will be numbered from left to right, top to bottom, from 1 to 9.  Each cell
	 * will know which box it belongs in.
	 */
	
	private int number; // This is the solved value of the cell.
	private boolean[] potential = {false, true, true, true, true, true, true, true, true, true};
	/*This array represents the potential of the cell to be each of the given index numbers.  Index [0] is not used since
	 * the cell cannot be solved for 0. 
	 */
	private int boxID;//The boxID is the box to which the cell belongs.
	
	//USEFUL METHODS:
	
	//This method returns TRUE or False depending on whether the cell has the potential to be number
	public boolean canBe(int number)
	{
		return potential[number];
	}
	
	//This sets the potential array to be false for a given number
	public void cantBe(int number)
	{
		potential[number] = false;
	}
	
	public void turnOn(int number) {
		potential[number] = true;
	}
	
	//This method returns a count of the number of potential numbers that the cell could be.
	public int numberOfPotentials()
	{
		int num = 0;
		for(int x = 0; x < 10; x ++) {
			if(potential[x] == true) {
				num ++;
			}
		}
		return num;
	}
	
	//This method will return the first number that a cell can possibly be.
	public int getFirstPotential()
	{
		int num = 0;
		for(int x = 0; x < 10; x ++) {
			if(potential[x]) {
				num = x;
				break;
			}
		}
		
		return num;
	}
	
	public int getXthPotential(int X) {
		int num = 0;
		int pot = 0;
		
		for(int x = 0; x < 10; x ++) {
			if(potential[x]) {
				num ++;
				if(num == X) {
					pot = x;
					break;
				}
			}
		}
		return pot;
	}
	
	
	//GETTERS AND SETTERS
	public int getNumber() {
		return number;
	}
	
	// This method sets the number for the cell but also sets all of the potentials for the cell (except for the solved number)
	//		to be false
	public void setNumber(int number) {
		
		this.number = number;
		
		for(int x = 0; x < 10; x ++) {
			if(x != number) {
				potential[x] = false;
			}
		}
	}
	
	public void reset() {
		number = 0;
		
		for(int x = 0; x < 10; x ++) {
			if(x == 0) {
				potential[x] = false;
				continue;
			}
			potential[x] = true;
		}
	}
	
	public boolean getPotential(int x) {
		
		return potential[x];
	}
	
	public boolean[] getPotential() {
		return potential;
	}
	public void setPotential(boolean[] potential) {
		this.potential = potential;
	}
	public int getBoxID() {
		return boxID;
	}
	public void setBoxID(int boxID) {
		this.boxID = boxID;
	}

}