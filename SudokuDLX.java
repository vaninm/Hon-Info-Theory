import java.util.*;

/**
 * SudokuDLX converts a sudoku board into an exact cover matrix so that it can call DLX to solve that particular exact cover 
 * problem using Algorithm X (also known as "Dancing Links"), thereby producing all possible solutions for the given sudoku board.  
 * 
 * @author Vani Mohindra, Rafal Szymanski 
 *
 */
public class SudokuDLX {
  
   /**
    * Helper method for sudokuExactCover. getIndex is a transformer function that converts an (R,C,V) triplet 
    * for a cell into its corresponding row number in the exact cover grid. 
    * @param R: row of the cell in the sudoku puzzle
    * @param C: column of the cell in the sudoku puzzle
    * @param V: value of the cell in the sudoku puzzle
    * @return the row number in the exact cover matrix that corresponds to the cell
    */
   private int getIndex (int R, int C, int V)
   {
      return 81*(R-1)+9*(C-1)+V-1;
   }
   
   /**
    * Helper method for makeExactCoverGrid. sudokuExactCover creates an exact cover matrix for an empty sudoku grid (i.e. 
    * a grid in which no values are known). The exact cover matrix has dimensions l x w = 729 x 324. Each of the 324 columns
    * represents a constraint as follows: 81 row constraints [R1 contains 1, R1 contains 2,...,R1 contains 9... ... R8 contains 1, ...R9 contains 9] 
    * followed by 81 column constraints [C1 contains 1, C1 contains 2,...,C1 contains 9... ... C9 contains 1, ...C9 contains 9]    
    * followed by 81 block constraints [B1 contains 1, B1 contains 2,...,B1 contains 9... ... B9 contains 1, ...B9 contains 9],
    * followed by 81 implicit cell constraints (each of the 81 cells must contain exactly one value) [Cell 1 is full, Cell 2 is full, ... Cell 81 is full].
    * Meanwhile, each of the 728 rows, represents a possible partial solution consisting of a (row, column, value) triplet.
    * Each cell in the exact cover matrix is either a 0 or a 1. If the its row's solution satisfies its column's constraint then the cell will store a 1.
    * Otherwise, the cell will store a 0.
    * 
    * @return the exact cover matrix for an empty sudoku grid (i.e. on in which no clues are given)
    */
   private int [][] sudokuExactCover()
   {
      int [][] R = new int [729][324];
      int hBase = 0;
      
      for (int r=1; r<=9; r++)
      {
         for (int c=1; c<=9; c++, hBase++)
         {
            for (int n=1; n<=9; n++)
            {
               R[getIndex(r,c,n)][hBase]=1;
            }
         }
      }
      
      for (int r=1; r<=9; r++)
      {
         for (int n=1; n<=9; n++, hBase++)
         {
            for (int c1=1; c1<=9; c1++)
            {
               R[getIndex(r,c1,n)][hBase]=1;
            }
         }
      }
      
      for (int c=1; c<=9; c++)
      {
         for (int n=1; n<=9; n++, hBase++)
         {
            for (int r1=1; r1<=9; r1++)
            {
               R[getIndex(r1,c,n)][hBase]=1;
            }
         }
      }
      
      for(int br = 1; br <= 9; br += 3){
         for(int bc = 1; bc <= 9; bc += 3){
             for(int n = 1; n <= 9; n++, hBase++){
                 for(int rDelta = 0; rDelta < 3; rDelta++){
                     for(int cDelta = 0; cDelta < 3; cDelta++){
                         R[getIndex(br + rDelta, bc + cDelta, n)][hBase] = 1;
                     }
                 }
             }
         }
     }   
      return R;
   }

   /**
    * Generates an exact cover grid for a particular sudoku board.  
    * 
    * @param sudoku: the sudoku board
    * @return the intermediate exact cover grid
    */
   private int[][] makeExactCoverGrid(int[][] sudoku){
      int[][] R = sudokuExactCover();
      for(int i = 1; i <= 9; i++){
          for(int j = 1; j <= 9; j++){
              int n = sudoku[i - 1][j - 1];
              if (n != 0){ // zero out in the constraint board
                  for(int num = 1; num <= 9; num++){
                      if (num != n){
                          Arrays.fill(R[getIndex(i, j, num)], 0);
                      }
                  }
              }
          }
      }
      return R;
  }
   
   /**
    * Converts the sudoku puzzle into an exact cover problem and uses DLX to solve the exact cover problem,
    * thereby solving the sudoku puzzle. All possible sudoku solutions will be printed to the console. 
    * 
    * @param sudoku: the sudoku board
    * @return 
    */
   protected void runSolver(int[][] sudoku){
      int[][] cover = makeExactCoverGrid(sudoku);
      DLX dlx = new DLX(cover, new SudokuHandler(9),sudoku);
      dlx.runSolver();
   }
   
   public ArrayList <int [][]> getSolutions (int [][] sudoku)
   {
      int [][] cover = makeExactCoverGrid (sudoku);
      DLX dlx = new DLX(cover, new SudokuHandler(9));
      return dlx.getSolutions();
   }
}
