package code;

public class SudokuSolver {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub

        Board puzzle = new Board();
        puzzle.loadPuzzle("hard");
        puzzle.display();
        puzzle.logicCycles();
        System.out.println("Was there an error found: " + puzzle.errorFound());
        System.out.println("Is the puzzle solved: " + puzzle.isSolved());

		if(puzzle.isSolved()){
			puzzle.display();
		}

    }

}
