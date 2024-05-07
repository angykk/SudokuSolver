package code;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Board{
	
	/*The Sudoku Board is made of 9x9 cells for a total of 81 cells.
	 * In this program we will be representing the Board using a 2D Array of cells.
	 * 
	 */

	private Cell[][] board = new Cell[9][9];
	
//	private Guess[] list = new Guess[81];
//	private int numGuess = 0;
	
	//The variable "level" records the level of the puzzle being solved.
	private String level = "";

	
	///TODO: CONSTRUCTOR
	//This must initialize every cell on the board with a generic cell.  It must also assign all of the boxIDs to the cells
	public Board()
	{
		for(int x = 0; x < 9; x++)
			for(int y = 0 ; y < 9; y++)
			{
				board[x][y] = new Cell();
				board[x][y].setBoxID( 3*(x/3) + (y)/3+1);
			}
	}
	
	
	///TODO: loadPuzzle
	/*This method will take a single String as a parameter.  The String must be either "easy", "medium" or "hard"
	 * If it is none of these, the method will set the String to "easy".  The method will set each of the 9x9 grid
	 * of cells by accessing either "easyPuzzle.txt", "mediumPuzzle.txt" or "hardPuzzle.txt" and setting the Cell.number to 
	 * the number given in the file.  
	 * 
	 * This must also set the "level" variable
	 * TIP: Remember that setting a cell's number affects the other cells on the board.
	 */
	public void loadPuzzle(String level) throws Exception
	{
		this.level = level;
		String fileName = "easyPuzzle.txt";
		if(level.contentEquals("medium"))
			fileName = "mediumPuzzle.txt";
		else if(level.contentEquals("hard"))
			fileName = "hardPuzzle.txt";
		
		Scanner input = new Scanner (new File(fileName));
		
		for(int x = 0; x < 9; x++) {
			for(int y = 0 ; y < 9; y++)
			{
				int number = input.nextInt();
				if(number != 0)
					solve(x, y, number);
			}
		}			
		input.close();
		
	}
	
	public int getNumber(int x, int y) {
		return board[x][y].getNumber();
	}
	
	public boolean[] getPotential(int x, int y) {
		return board[x][y].getPotential();
	}
	
	///TODO: isSolved
	/*This method scans the board and returns TRUE if every cell has been solved.  Otherwise it returns FALSE
	 * 
	 */
	public boolean isSolved()
	{
		boolean solve = true;
		
		for(int x = 0; x < 9; x ++) {
			for(int y = 0; y < 9; y ++) {
				if(board[x][y].getNumber() == 0) {
					solve = false;
				}
			}
		}
		
		return solve;
	}


	///TODO: DISPLAY
	/*This method displays the board neatly to the screen.  It must have dividing lines to show where the box boundaries are
	 * as well as lines indicating the outer border of the puzzle
	 */
	public void display()
	{
		for(int x = 0; x < 9; x ++) {
			if(x%3 == 0) {
				for(int z = 0; z < 9; z ++) {
					System.out.print(" - ");
				}
				System.out.println();
			}
			for(int y = 0; y < 9; y ++) {
				if(y%3 == 0) {
					System.out.print(" | ");
				}
					System.out.print(board[x][y].getNumber() + " ");
				if(y == 8) {
					System.out.print("| ");
				}
			}
			System.out.println();
			if(x == 8) {
				for(int z = 0; z < 9; z ++) {
					System.out.print(" - ");
				}
				System.out.println();
			}
			}
	}
	
	///TODO: solve
	/*This method solves a single cell at x,y for number.  It also must adjust the potentials of the remaining cells in the same row,
	 * column, and box.
	 */
	public void solve(int x, int y, int number)
	{
		board[x][y].setNumber(number);
		
		for(int z = y - 1;z >= 0;z --) {//column
			board[x][z].cantBe(number);
		}
		for(int z = y + 1; z < 9;z ++) {//column
			board[x][z].cantBe(number);
		}
		
		for(int v = x - 1; v >= 0; v --) {//row
			board[v][y].cantBe(number);
		}
		for(int v = x+1; v < 9; v ++) {//row
			board[v][y].cantBe(number);
		}
		
		for(int z = 0; z < 9; z ++) {//box
			for(int v = 0; v < 9; v ++) {
				if(board[z][v].getBoxID() == board[x][y].getBoxID() && z != x && v!=y) {
					board[z][v].cantBe(number);
				}
			}
		}
		
		board[x][y].turnOn(number);
		
	}
	
	
//	logicCycles() continuously cycles through the different logic algorithms until no more changes are being made.
	public void logicCycles()throws Exception
	{
		
		while(isSolved() == false)
		{
			
			int changesMade = 0;
			do
			{
				changesMade = 0;
				changesMade += logic1();
				changesMade += logic2();
				changesMade += logic3();
				changesMade += logic4();
				
				if(errorFound()) {
					break;
				}
			}while(changesMade != 0);
			
			display();

			if(changesMade == 0  && !isSolved()) {
				display();
				System.out.println("GUESS");
				for(int x = 0; x < 9; x++) {
					for(int y = 0; y <9; y++) {
						if(board[x][y].getNumber() == 0) {
							guess(x,y);
						}
					}
				}
			}
				
				
			}
		}		
	
	public boolean guess(int row, int col)
	{
		if( col == 9) {
			row ++;
			col = 0;
		}
		if(board[row][col].getNumber()!= 0) {
			return guess(row, col+1);
		}
		for(int v = 1; v <= 9; v++) {
			board[row][col].setNumber(v);
			if(isValid(row, col, v)) {
				return true;
			}
			board[row][col].setNumber(0);
		}
		return false;
	}
	
	public boolean isValid(int row, int col, int num) {
		for(int x = 0; x < 9; x ++) {
			if(board[row][x].getNumber() == num) {
				return false;
			}
		}
		for(int x = 0; x < 9; x ++) {
			if(board[x][col].getNumber() == num) {
				return false;
			}
		}
		for(int x = row - row%3; x < row - row%3 + 3; x++) {
			for(int y = col-col%3;y<col-col%3 + 3; y++) {
				if(board[x][y].getNumber()==num) {
					return false;
				}
			}
		}
		
		return true;
		
	}

	///TODO: logic1
	/*This method searches each row of the puzzle and looks for cells that only have one potential.  If it finds a cell like this, it solves the cell 
	 * for that number. This also tracks the number of cells that it solved as it traversed the board and returns that number.
	 */
	public int logic1()
	{
		int changesMade = 0;

		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9;y++) {
				if(board[x][y].numberOfPotentials() == 1 && board[x][y].getNumber() == 0) {
					solve(x,y,board[x][y].getFirstPotential());
					changesMade ++;
				}
			}
		}
		return changesMade;
					
	}
	
	///TODO: logic2
	/*This method searches each row for a cell that is the only cell that has the potential to be a given number.  If it finds such a cell and it
	 * is not already solved, it solves the cell.  It then does the same thing for the columns.This also tracks the number of cells that 
	 * it solved as it traversed the board and returns that number.
	 */
	
	public int logic2()
	{
		int changesMade = 0;
		
		int sum = 0;
		int index = 0;
		
		
		//column
		for(int x = 0; x < 9; x ++) {
			for(int z = 1; z <= 9; z ++) {
				sum = 0;
				for(int y = 0; y < 9; y ++) {
					if(board[y][x].getNumber() == z) {
						sum = 5;
						break;
					}
					else if(board[y][x].canBe(z)) {
						sum++;
						index = y;
					}
				}
				
				if(sum == 1 && board[index][x].getNumber() == 0) {
					solve(index, x, z);
					changesMade ++;
				}
			}
		}
		
		//row
		index = 0;
		
		for(int x = 0; x < 9; x ++) {
			for(int z = 1; z <= 9; z ++) {
				sum = 0;
				for(int y = 0; y < 9; y ++) {
					if(board[x][y].getNumber() == z) {
						sum = 5;
						break;
					}
					else if(board[x][y].canBe(z)) {
						sum++;
						index = y;
					}
				}
				if(sum == 1 && board[x][index].getNumber() == 0) {
					solve(x, index, z);
					changesMade ++;
				}
			}
		}
			
		return changesMade;
	}
	
	///TODO: logic3
	/*This method searches each box for a cell that is the only cell that has the potential to be a given number.  If it finds such a cell and it
	 * is not already solved, it solves the cell. This also tracks the number of cells that it solved as it traversed the board and returns that number.
	 */
	public int logic3()
	{
		
		int changesMade = 0;
		int sum = 0;
		int index = 0;
		int indexY = 0;
		
			
		for(int x = 0; x < 9; x += 3) {
			for(int y = 0; y < 9; y += 3) {
					for(int t = 1; t <= 9; t++) {
						sum = 0;
						for(int z = 0; z < 3; z++) {
							for(int v = 0; v < 3; v++) {
								
								if(board[x+z][y+v].canBe(t) && board[x+z][y+v].getNumber() == 0) {
									sum++;
									index = x+z;
									indexY = y+v;
								}
							}
							
						}
						
						if(sum == 1) {
							solve(index, indexY, t);
							changesMade++;
						}
					
					
				}
			}
			
		}
		
	
		
		return changesMade;
	}
	
	
	///TODO: logic4
		/*This method searches each row for the following conditions:
		 * 1. There are two unsolved cells that only have two potential numbers that they can be
		 * 2. These two cells have the same two potentials (They can't be anything else)
		 * 
		 * Once this occurs, all of the other cells in the row cannot have these two potentials.  Write an algorithm to set these two potentials to be false
		 * for all other cells in the row.
		 * 
		 * Repeat this process for columns and rows.
		 * 
		 * This also tracks the number of cells that it solved as it traversed the board and returns that number.
		 */
	public int logic4()
	{
		int changesMade = 0;
		
		for (int x = 0; x < 9; x++)
			for (int y = 0; y < 9; y++) 
				if (board[x][y].numberOfPotentials() == 2) 
					for (int z = y+1; z < 9; z++) 
						if (board[x][z].numberOfPotentials() == 2  && Arrays.equals(board[x][z].getPotential(),board[x][y].getPotential()))
							for (int z2 = 0; z2 < 9; z2++) 
								if (z2 != y && z2 != z) {
									if (board[x][z2].canBe(board[x][y].getFirstPotential())) {
										changesMade++;
										board[x][z2].cantBe(board[x][y].getFirstPotential());
										
									}
									if(board[x][z2].canBe(board[x][y].getXthPotential(2))){
										changesMade++;
										board[x][z2].cantBe(board[x][y].getXthPotential(2));
									}						
								}
		
		for (int x = 0; x < 9; x++)
			for (int y = 0; y < 9; y++) 
				if (board[y][x].numberOfPotentials() == 2) 
					for (int z = y+1; z < 9; z++) 
						if (board[z][x].numberOfPotentials() == 2  && Arrays.equals(board[z][x].getPotential(),board[y][x].getPotential()))
							for(int z2 = 0; z2 < 9;z2 ++)
								if (z2!= y && z2 != z) {
									if (board[z2][x].canBe(board[y][x].getFirstPotential())) {
										changesMade++;
										board[z2][x].cantBe(board[y][x].getFirstPotential());
									}
									
									if(board[z2][x].canBe(board[y][x].getXthPotential(2))) {
										changesMade++;	
										board[z2][x].cantBe(board[y][x].getXthPotential(2));			
										
									}
									
								}
		
		for (int x = 0; x < 9; x += 3) 
			for (int y = 0; y < 9; y += 3) 
				for (int z = 0; z < 3; z ++) 
					for (int v = 0; v < 3; v ++) 						
						if (board[x + z][y + v].numberOfPotentials() == 2) 
							for (int z2 = 0; z2 < 3; z2++) 
								for (int v2 = 0; v2 < 3; v2 ++) 
									if (board[x + z2][y + v2].numberOfPotentials() == 2 && (z2 != z || v2 != v) && Arrays.equals(board[x + z][y + v].getPotential(),board[x+z2][y+v2].getPotential()))
										for (int i = 0; i < 3; i++) 
											for (int j = 0; j < 3; j++) 
												if ((i != z2 || j != v2) && (i != z || j != z)) {
													if (board[x + i][y + j].canBe(board[x + z][y + z].getFirstPotential())) {
														changesMade++;
														board[x + i][y + j].cantBe(board[x + z][y + z].getFirstPotential());
													}
													
													if(board[x + i][y + j].canBe(board[x + z][y + z].getXthPotential(2))) {
														changesMade++;
														board[x + i][y + j].cantBe(board[x + z][y + z].getXthPotential(2));
													}
													
												}
						

		return changesMade;
	}
	
	
	///TODO: errorFound
	/*This method scans the board to see if any logical errors have been made.  It can detect this by looking for a cell that no longer has the potential to be 
	 * any number.
	 */
	public boolean errorFound()
	{
		boolean error = false;
		for(int x = 0; x < 9; x ++) {
			for(int y = 0; y < 9; y ++) {
				if(board[x][y].numberOfPotentials() == 0) {
					error = true;
				}
			}
		}
		return error;

	
	
}
}
