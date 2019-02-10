import java.util.*;

/**
 * DLX uses Donald Knuth's Algorithm X (also known as "Dancing Links") to find all solutions to a given exact cover problem. 
 * The exact cover description of the problem is set up such that each column represents a constraint, each row represents a 
 * possible partial solution, and each cell is filled with a 0 or a 1 depending on whether its row's partial solution satisfies
 * its column's constraint (1) or not (0). The exact cover matrix is implemented as a doubly-linked list of doubly-linked lists
 * in order to make backtracking more efficient. Algorithm X operates on the exact cover matrix as follows:
 *    1. If the matrix is not empty, continue. If the matrix is empty, a solution has been found.
 *    2. Choose a constraint (column) to satisfy according to the constraint-choosing heuristic: the column header with the least 
 *       number of ones in its column will be selected. 
 *    3. Choose a row that fulfills that constraint and add it the working solution. If no rows fulfill the constraint then backtrack. 
 *    4. For each constraint that row satisfies, remove any other rows that also satisfy that constraint and remove the fulfilled
 *       constraints’ columns from the exact cover matrix.    
 * 
 * @author Vani Mohindra, Rafal Szymanski 
 */
public class DLX {
   
   static final boolean verbose = true;
   private ColumnNode header; // this is the root node
   private int solutions = 0;
   private SolutionHandler handler;
   private List<DancingNode> answer;
   SolutionsAnalyzer solanalyzer = new SolutionsAnalyzer(); // NEW ADDITION
   int [][] SUDOKU; // NEW ADDITION
  
   /**
    * Given an exact cover board that is implemented with a 2D array, express that same
    * exact cover board as a circular doubly-linked list of circular doubly-linked lists such that
    * each of the column headers is a ColumnNode that represents a unique constraint and every other 
    * node in the list of lists is a DancingNode that represents a cell in the 2D array that contained
    * a 1.   
    * 
    * @param grid: exact cover board represented as a 2D array of 0s and 1s 
    * @return the root node of the DLX board 
    */
   private ColumnNode makeDLXBoard(int[][] grid){
      final int COLS = grid[0].length;
      final int ROWS = grid.length;

      ColumnNode headerNode = new ColumnNode("header");
      ArrayList<ColumnNode> columnNodes = new ArrayList<ColumnNode>();

      for(int i = 0; i < COLS; i++){
          ColumnNode n = new ColumnNode(Integer.toString(i));
          columnNodes.add(n);
          headerNode = (ColumnNode) headerNode.linkRight(n);
      }
      headerNode = headerNode.R.C;

      for(int i = 0; i < ROWS; i++){
          DancingNode prev = null;
          for(int j = 0; j < COLS; j++){
              if (grid[i][j] == 1){
                  ColumnNode col = columnNodes.get(j);
                  DancingNode newNode = new DancingNode(col);
                  if (prev == null)
                      prev = newNode;
                  col.U.linkBelow(newNode);
                  prev = prev.linkRight(newNode);
                  col.size++;
              }
          }
      }

      headerNode.size = COLS;
      
      return headerNode;
  }
   
  /**
   * Recurisvely search for solutions to the exact cover board using Algorithm X.   
   * 
   * @param k: indicates how many levels deep the recursive stack is
   */
   private void search(int k){
      if (header.R == header){ // all the columns removed
          if(verbose){
              System.out.println("-----------------------------------------");
              System.out.println("Solution #" + solutions + "\n");
          }
          handler.handleSolution(answer);
          solanalyzer.addSolution(handler.parseBoard(answer));// NEW ADDITION
          if(verbose){
              System.out.println("-----------------------------------------");
          }
          solutions++;
      } else{
          ColumnNode c = selectColumnNodeHeuristic();
          c.cover();

          for(DancingNode r = c.D; r != c; r = r.D){
              answer.add(r);

              for(DancingNode j = r.R; j != r; j = j.R){
                  j.C.cover();
              }

              search(k + 1);

              r = answer.remove(answer.size() - 1);
              c = r.C;

              for(DancingNode j = r.L; j != r; j = j.L){
                  j.C.uncover();
              }
          }
          c.uncover();
      }
  }
   
   /**
    * Helper method for search. Determines which constraint to satisfy during Step #2 of Algorithm X by picking the
    * column that has the least number of nodes (i.e. the constraint which is satisfied by the least number of
    * partial solutions).
    * 
    * @return the column header of the column whose constraint is to be satisfied
    */
   private ColumnNode selectColumnNodeHeuristic() // return column node with least number of 1s in its column
   {
      int min = Integer.MAX_VALUE;
      ColumnNode ret = null;
      for (ColumnNode c = (ColumnNode)header.R; c!=header; c=(ColumnNode)c.R)
      {
         if (c.size<min)
         {
            min=c.size;
            ret=c;
         }
      }
      return ret;
   }
   
   /**
    * Constructor: create an instance of DLX for a given exact cover grid. 
    * @param grid: the exact cover grid
    */
   public DLX(int[][] grid){
      this(grid, new DefaultHandler());
  }

   /**
    * Constructor: create an instance of DLX for a given exact cover grid 
    * and solution handler. 
    * @param grid: the exact cover grid
    * @param h: the solution handler
    */
  public DLX(int[][] grid, SolutionHandler h){
      header = makeDLXBoard(grid);
      handler = h;
  }
  
  public DLX(int[][] grid, SolutionHandler h, int [][] sudokuBoard){
     header = makeDLXBoard(grid);
     handler = h;
     SUDOKU = sudokuBoard;
 }

  public void runSolver(){
      solutions = 0;
      answer = new LinkedList<DancingNode>();
      search(0);
      solanalyzer.analyzeSolutions(); // NEW ADDITION
      solanalyzer.smartGreedy(solanalyzer.getInfoBank(), SUDOKU);
  }
  
  public ArrayList <int [][]> getSolutions()
  {
     solutions = 0;
     answer = new LinkedList<DancingNode>();
     return modifiedSearch(0);
  }
  
  private ArrayList <int [][]> modifiedSearch(int k){
     ArrayList<int [][]> solutions = new ArrayList <int [][]> ();
     if (header.R == header){ 
         solutions.add(handler.parseBoard(answer));
     } else{
         ColumnNode c = selectColumnNodeHeuristic();
         c.cover();

         for(DancingNode r = c.D; r != c; r = r.D){
             answer.add(r);

             for(DancingNode j = r.R; j != r; j = j.R){
                 j.C.cover();
             }

             modifiedSearch(k + 1);

             r = answer.remove(answer.size() - 1);
             c = r.C;

             for(DancingNode j = r.L; j != r; j = j.L){
                 j.C.uncover();
             }
         }
         c.uncover();
     }
     return solutions;
 }
}
