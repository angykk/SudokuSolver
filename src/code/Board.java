package code;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Board {

	/*
	 * The Sudoku Board is made of 9x9 cells for a total of 81 cells.
	 * In this program we will be representing the Board using a 2D Array of cells.
	 * 
	 */

	private Cell[][] board = new Cell[9][9];

	// private Guess[] list = new Guess[81];
	// private int numGuess = 0;

	// The variable "level" records the level of the puzzle being solved.
	private String level = "";

	/// TODO: CONSTRUCTOR
	// This must initialize every cell on the board with a generic cell. It must
	/// also assign all of the boxIDs to the cells
	public Board() {
		for (int x = 0; x < 9; x++)
			for (int y = 0; y < 9; y++) {
				board[x][y] = new Cell();
				board[x][y].setBoxID(3 * (x / 3) + (y) / 3 + 1);
			}
	}

	/// TODO: loadPuzzle
	/*
	 * This method will take a single String as a parameter. The String must be
	 * either "easy", "medium" or "hard"
	 * If it is none of these, the method will set the String to "easy". The method
	 * will set each of the 9x9 grid
	 * of cells by accessing either "easyPuzzle.txt", "mediumPuzzle.txt" or
	 * "hardPuzzle.txt" and setting the Cell.number to
	 * the number given in the file.
	 * 
	 * This must also set the "level" variable
	 * TIP: Remember that setting a cell's number affects the other cells on the
	 * board.
	 */
	public void loadPuzzle(String level) throws Exception {
		this.level = level;
		String fileName = "easyPuzzle.txt";
		if (level.contentEquals("medium"))
			fileName = "mediumPuzzle.txt";
		else if (level.contentEquals("hard"))
			fileName = "hardPuzzle.txt";

		Scanner input = new Scanner(new File(fileName));

		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				int number = input.nextInt();
				if (number != 0)
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

	/// TODO: isSolved
	/*
	 * This method scans the board and returns TRUE if every cell has been solved.
	 * Otherwise it returns FALSE
	 * 
	 */
	public boolean isSolved() {
		boolean solve = true;

		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				if (board[x][y].getNumber() == 0) {
					solve = false;
				}
			}
		}

		return solve;
	}

	/// TODO: DISPLAY
	/*
	 * This method displays the board neatly to the screen. It must have dividing
	 * lines to show where the box boundaries are
	 * as well as lines indicating the outer border of the puzzle
	 */
	public void display() {
		for (int x = 0; x < 9; x++) {
			if (x % 3 == 0) {
				for (int z = 0; z < 9; z++) {
					System.out.print(" - ");
				}
				System.out.println();
			}
			for (int y = 0; y < 9; y++) {
				if (y % 3 == 0) {
					System.out.print(" | ");
				}
				System.out.print(board[x][y].getNumber() + " ");
				if (y == 8) {
					System.out.print("| ");
				}
			}
			System.out.println();
			if (x == 8) {
				for (int z = 0; z < 9; z++) {
					System.out.print(" - ");
				}
				System.out.println();
			}
		}
	}

	/// TODO: solve
	/*
	 * This method solves a single cell at x,y for number. It also must adjust the
	 * potentials of the remaining cells in the same row,
	 * column, and box.
	 */
	public void solve(int x, int y, int number) {
		board[x][y].setNumber(number);
		for (int z = 0; z < board.length; z++)
			if (z != x)
				board[z][y].cantBe(number);
		for (int z = 0; z < board[0].length; z++)
			if (z != y)
				board[x][z].cantBe(number);
		for (int z = Math.max(0, x - 3); z <= Math.min(board.length - 1, x + 3); z++)
			for (int v = Math.max(0, y - 3); v <= Math.min(board[0].length - 1, y + 3); v++)
				if (!(v == y && z == x))
					if (board[z][v].getBoxID() == board[x][y].getBoxID())
						board[z][v].cantBe(number);

		board[x][y].turnOn(number);

	}

	// logicCycles() continuously cycles through the different logic algorithms
	// until no more changes are being made.
	public boolean logicCycles() throws Exception {

		while (true) {

			int changesMade = 0;
			display();
			do {
				changesMade = 0;
				changesMade += logic1();
				changesMade += logic2();
				changesMade += logic3();
				changesMade += logic4();

				if (errorFound()) {
					return false;
				}

			} while (changesMade != 0);

			if (isSolved()) {
				return true;
			}

			if (changesMade == 0 && !isSolved()) {
				int x = -1;
				int y = -1;
				top: for (x = 0; x < 9; x++) {
					for (y = 0; y < 9; y++) {
						if (board[x][y].getNumber() == 0) {
							break top;
						}
					}
				}
				int n = board[x][y].getFirstPotential();
				Cell[][] temp = copy(board);
				// for (int i = 0; i < 10; i++) {
				// System.out.print(i);
				// System.out.println(board[x][y].getPotential(i));
				// }
				solve(x, y, n);
				if (!logicCycles() && errorFound()) {
					board = temp;
					board[x][y].cantBe(n);
				} else {
					return true;
				}

			}

		}
	}

	public boolean isValid(int row, int col, int num) {
		for (int x = 0; x < 9; x++) {
			if (board[row][x].getNumber() == num) {
				return false;
			}
		}
		for (int x = 0; x < 9; x++) {
			if (board[x][col].getNumber() == num) {
				return false;
			}
		}
		for (int x = row - row % 3; x < row - row % 3 + 3; x++) {
			for (int y = col - col % 3; y < col - col % 3 + 3; y++) {
				if (board[x][y].getNumber() == num) {
					return false;
				}
			}
		}

		return true;

	}

	/// TODO: logic1
	/*
	 * This method searches each row of the puzzle and looks for cells that only
	 * have one potential. If it finds a cell like this, it solves the cell
	 * for that number. This also tracks the number of cells that it solved as it
	 * traversed the board and returns that number.
	 */
	public int logic1() {
		int changesMade = 0;

		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				if (board[x][y].numberOfPotentials() == 1 && board[x][y].getNumber() == 0) {
					solve(x, y, board[x][y].getFirstPotential());
					changesMade++;
				}
			}
		}
		return changesMade;

	}

	/// TODO: logic2
	/*
	 * This method searches each row for a cell that is the only cell that has the
	 * potential to be a given number. If it finds such a cell and it
	 * is not already solved, it solves the cell. It then does the same thing for
	 * the columns.This also tracks the number of cells that
	 * it solved as it traversed the board and returns that number.
	 */

	public int logic2() {
		int changesMade = 0;

		int sum = 0;
		int index = 0;

		// column
		for (int x = 0; x < 9; x++) {
			for (int z = 1; z <= 9; z++) {
				sum = 0;
				for (int y = 0; y < 9; y++) {
					if (board[y][x].getNumber() == z) {
						sum = 5;
						break;
					} else if (board[y][x].canBe(z)) {
						sum++;
						index = y;
					}
				}

				if (sum == 1 && board[index][x].getNumber() == 0) {
					solve(index, x, z);
					changesMade++;
				}
			}
		}

		// row
		index = 0;

		for (int x = 0; x < 9; x++) {
			for (int z = 1; z <= 9; z++) {
				sum = 0;
				for (int y = 0; y < 9; y++) {
					if (board[x][y].getNumber() == z) {
						sum = 5;
						break;
					} else if (board[x][y].canBe(z)) {
						sum++;
						index = y;
					}
				}
				if (sum == 1 && board[x][index].getNumber() == 0) {
					solve(x, index, z);
					changesMade++;
				}
			}
		}

		return changesMade;
	}

	/// TODO: logic3
	/*
	 * This method searches each box for a cell that is the only cell that has the
	 * potential to be a given number. If it finds such a cell and it
	 * is not already solved, it solves the cell. This also tracks the number of
	 * cells that it solved as it traversed the board and returns that number.
	 */
	public int logic3() {

		int changesMade = 0;
		int sum = 0;
		int index = 0;
		int indexY = 0;

		for (int x = 0; x < 9; x += 3) {
			for (int y = 0; y < 9; y += 3) {
				for (int t = 1; t <= 9; t++) {
					sum = 0;
					for (int z = 0; z < 3; z++) {
						for (int v = 0; v < 3; v++) {

							if (board[x + z][y + v].canBe(t) && board[x + z][y + v].getNumber() == 0) {
								sum++;
								index = x + z;
								indexY = y + v;
							}
						}

					}

					if (sum == 1) {
						solve(index, indexY, t);
						changesMade++;
					}

				}
			}

		}

		return changesMade;
	}

	/// TODO: logic4
	/*
	 * This method searches each row for the following conditions:
	 * 1. There are two unsolved cells that only have two potential numbers that
	 * they can be
	 * 2. These two cells have the same two potentials (They can't be anything else)
	 * 
	 * Once this occurs, all of the other cells in the row cannot have these two
	 * potentials. Write an algorithm to set these two potentials to be false
	 * for all other cells in the row.
	 * 
	 * Repeat this process for columns and rows.
	 * 
	 * This also tracks the number of cells that it solved as it traversed the board
	 * and returns that number.
	 */
	public int logic4() {
		int changesMade = 0;

		// Loop through rows
		for (int row = 0; row < board.length; row++)
			for (int col1 = 0; col1 < board[row].length; col1++)
				if (board[row][col1].getNumber() == 0)
					if (board[row][col1].numberOfPotentials() == 2)
						for (int col2 = col1 + 1; col2 < board[row].length; col2++)
							if (board[row][col2].getNumber() == 0)
								if (board[row][col2].numberOfPotentials() == 2)
									if (Arrays.equals(board[row][col1].getPotential(), board[row][col2].getPotential()))
										for (int col = 0; col < board[row].length; col++)
											if (col != col1 && col != col2) {
												if (board[row][col].canBe(board[row][col1].getFirstPotential())) {
													board[row][col].cantBe(board[row][col1].getFirstPotential());
													changesMade++;
												}
												if (board[row][col].canBe(board[row][col1].getLastPotential())) {
													board[row][col].cantBe(board[row][col1].getLastPotential());
													changesMade++;
												}
											}

		// Loop through columns
		for (int col = 0; col < board[0].length; col++)
			for (int row1 = 0; row1 < board.length; row1++)
				if (board[row1][col].getNumber() == 0)
					if (board[row1][col].numberOfPotentials() == 2)
						for (int row2 = row1 + 1; row2 < board.length; row2++)
							if (board[row2][col].getNumber() == 0)
								if (board[row2][col].numberOfPotentials() == 2)
									if (Arrays.equals(board[row1][col].getPotential(), board[row2][col].getPotential()))
										for (int row = 0; row < board.length; row++)
											if (row != row1 && row != row2) {
												if (board[row][col].canBe(board[row1][col].getFirstPotential())) {
													board[row][col].cantBe(board[row1][col].getFirstPotential());
													changesMade++;
												}
												if (board[row][col].canBe(board[row1][col].getLastPotential())) {
													board[row][col].cantBe(board[row1][col].getLastPotential());
													changesMade++;
												}
											}

		// Loop through boxes
		for (int boxRow = 0; boxRow < board.length / 3; boxRow++)
			for (int boxCol = 0; boxCol < board.length / 3; boxCol++)
				for (int row1 = boxRow * 3; row1 < (boxRow + 1) * 3; row1++)
					for (int col1 = boxCol * 3; col1 < (boxCol + 1) * 3; col1++)
						if (board[row1][col1].getNumber() == 0)
							if (board[row1][col1].numberOfPotentials() == 2)
								for (int row2 = row1; row2 < (boxRow + 1) * 3; row2++)
									for (int col2 = col1 + 1; col2 < (boxCol + 1) * 3; col2++)
										if (board[row2][col2].getNumber() == 0)
											if (board[row2][col2].numberOfPotentials() == 2)
												if (Arrays.equals(board[row1][col1].getPotential(),
														board[row2][col2].getPotential()))
													for (int row = boxRow * 3; row < (boxRow + 1) * 3; row++)
														for (int col = boxCol * 3; col < (boxCol + 1) * 3; col++)
															if (row != row1 && row != row2 && col != col1
																	&& col != col2) {
																if (board[row][col]
																		.canBe(board[row1][col1].getFirstPotential())) {
																	board[row][col].cantBe(
																			board[row1][col1].getFirstPotential());
																	changesMade++;
																}
																if (board[row][col]
																		.canBe(board[row1][col1].getLastPotential())) {
																	board[row][col].cantBe(
																			board[row1][col1].getLastPotential());
																	changesMade++;
																}
															}

		return changesMade;

	}

	private Cell[][] copy(Cell[][] board) {
		Cell[][] copy = new Cell[board.length][board[0].length];

		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[0].length; y++) {
				copy[x][y] = copyCell(board[x][y]);
			}
		}
		return copy;
	}

	private Cell copyCell(Cell c) {
		Cell temp = new Cell();

		if (c.getNumber() == 0) {
			boolean[] pot = c.getPotential();
			for (int i = 0; i < pot.length; i++) {
				if (!pot[i]) {
					temp.cantBe(i);
				}
			}
		} else {
			temp.setNumber(c.getNumber());
		}
		temp.setBoxID(c.getBoxID());

		return temp;
	}

	/// TODO: errorFound
	/*
	 * This method scans the board to see if any logical errors have been made. It
	 * can detect this by looking for a cell that no longer has the potential to be
	 * any number.
	 */
	public boolean errorFound() {
		display();
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				if (board[x][y].numberOfPotentials() == 0) {
					return true;
				}
			}
		}
		return false;
	}
}
