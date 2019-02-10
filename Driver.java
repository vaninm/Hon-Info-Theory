public class Driver {
   
   public static void main (String [] args)
   {
      SudokuDLX megamind = new SudokuDLX();
      int [][] puzzle = new int [9][9];
      
      puzzle[0][3]=8;
      puzzle[0][5]=1;
      puzzle[1][6]=4;
      puzzle[1][7]=3;
      puzzle[2][0]=5;
      puzzle[3][4]=7;
      puzzle[3][6]=8;
      puzzle[4][6]=1;
      puzzle[5][1]=2;
      puzzle[5][4]=3;
      puzzle[6][0]=6;
      puzzle[6][7]=7;
      puzzle[6][8]=5;
      puzzle[7][2]=3;
      puzzle[7][3]=4;
      puzzle[8][3]=2;
      puzzle[8][6]=6;
      puzzle[5][2]=9;

      
      /* 
      Example of using dumb greedy algorithm.  
      puzzle[5][2]=9;
      puzzle[2][8]=9;
      puzzle[2][2]=4;
      puzzle[3][7]=4;
      */
      
      
     
     /** int [][] puzzle = {{0,0,0,2,6,0,7,0,1},
                         {6,8,0,0,7,0,0,9,0},
                         {1,9,0,0,0,4,5,0,0},
                         {8,2,0,1,0,0,0,4,0},
                         {0,0,4,6,0,2,9,0,0},
                         {0,5,0,0,0,3,0,2,8},
                         {0,0,9,3,0,0,0,7,4},
                         {0,4,0,0,5,0,0,3,6},
                         {7,0,3,0,1,8,0,0,0}}; **/
      
      megamind.runSolver(puzzle);
   } 
}
