package code;

public class SudokuSolver {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Board puzzle = new Board();
		puzzle.loadPuzzle("hard");
		puzzle.display();
		puzzle.logicCycles();
		System.out.println(puzzle.errorFound());
		System.out.println(puzzle.isSolved());
		
		
	}

}
