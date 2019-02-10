import java.util.*;

/**
 * Interface that defines the methods handleSolution and parseBoard.
 * @author Rafal Szymanski, Vani Mohindra 
 */
public interface SolutionHandler{
    void handleSolution(List<DancingNode> solution);
    public int[][] parseBoard(List<DancingNode> answer);// NEW ADDITION
}

/**
 * SudokuHandler provides the means for translating a sudoku solution in the form of an exact
 * cover matrix of doubly linked lists into a sudoku solution in the form of a 9x9 integer array
 * so that the solution can be more easily interpreted by the human user.
 * 
 * @author Rafal Szymanski, Vani Mohindra 
 */
class SudokuHandler implements SolutionHandler{
    int size = 9;

    /**
     * handleSolution prints the sudoku solution to the console as 9x9 array when given the 
     * solution as an exact cover matrix of doubly linked lists
     * 
     * @param answer is the exact cover representation of the sudoku solution
     */
    public void handleSolution(List<DancingNode> answer) { 
        int[][] result = parseBoard(answer);
        int N = result.length;
        for(int i = 0; i < N; i++){
            String ret = "";
            for(int j = 0; j < N; j++){
                ret += result[i][j] + " ";
            }
            System.out.println(ret);
        }
        System.out.println();
    }

    /**
     * helper method for handleSolution. parseBoard does the heavy lifting of converting the 
     * exact cover representation of the sudoku solution into an integer array representation
     * 
     * @param answer is the exact cover representation of the sudoku solution
     */
    public int[][] parseBoard(List<DancingNode> answer){
        int[][] result = new int[size][size];
        for(DancingNode n : answer){
            DancingNode rcNode = n;
            int min = Integer.parseInt(rcNode.C.name);
            for(DancingNode tmp = n.R; tmp != n; tmp = tmp.R){
                int val = Integer.parseInt(tmp.C.name);
                if (val < min){
                    min = val;
                    rcNode = tmp;
                }
            }
            int ans1 = Integer.parseInt(rcNode.C.name);
            int ans2 = Integer.parseInt(rcNode.R.C.name);
            int r = ans1 / size;
            int c = ans1 % size;
            int num = (ans2 % size) + 1;
            result[r][c] = num;
        }
        return result;
    }


    /**
     * Constructor: creates an instance of the SudokuHandler class
     * 
     * @param boardSize is the side length of the sudoku board whose solution is being handled
     */
    public SudokuHandler(int boardSize){
        size = boardSize;
    }

}

class DefaultHandler implements SolutionHandler{
   /**
    * DefaultHandler prints all of the contents of the exact cover matrix to console as a 2-d array
    * 
    * @param answer is the exact cover representation of the sudoku solution
    */
    public void handleSolution(List<DancingNode> answer){
        for(DancingNode n : answer){
            String ret = "";
            ret += n.C.name + " ";
            DancingNode tmp = n.R;
            while (tmp != n){
                ret += tmp.C.name + " ";
                tmp = tmp.R;
            }
            System.out.println(ret);
        }
    }

   @Override                                                    
   /**
    * Must be defined for any class that implements SolutionHandler. Does nothing. 
    */
   public int[][] parseBoard(List<DancingNode> answer) 
   {          
      return null;                                      
   }
}