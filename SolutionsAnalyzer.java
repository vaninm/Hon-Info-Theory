import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Given a set of possible solutions to a sudoku puzzle, SolutionsAnalyzer is able to identify which squares 
 * in the sudoku board are uncertain and outputs those squares and their possible values 
 * @author Vani Mohindra
 */
public class SolutionsAnalyzer {
   ArrayList <int [][]> solutions = new ArrayList <int [][]> ();
   
   /**
    * Stores another solution to the sudoku problem 
    * @param sol: the new Sudoku solution to be stored
    */
   public void addSolution (int [][] sol)
   {
      solutions.add(sol);
   }
 
   public double getAvgEntropyOfSolutions (ArrayList <int [][]> allSols)
   {
      ArrayList<HashSet <Integer>>[] infoBank = new ArrayList[9];
      for (int i=0; i<infoBank.length; i++)
      {
         infoBank[i] = new ArrayList<HashSet<Integer>>(9);
         ArrayList<HashSet<Integer>> pointer =infoBank[i];
         for (int j=0; j<9; j++)
         {
            pointer.add(new HashSet<Integer>());
         }
      }
      
      Iterator<int [][]> it = allSols.iterator();
      while (it.hasNext())
      {
         int [][] temp = it.next(); // current solution sudoku grid
         for (int i=0; i<temp.length; i++)
         {
            for (int j=0; j<temp[0].length; j++)
            {
               infoBank[i].get(j).add(temp[i][j]);
            }
         }
      }
      
      double sum =0;
      
      for (int i=0; i<infoBank.length; i++)
      {
         for (int j=0; j<infoBank[0].size(); j++)
         {
            int n = infoBank[i].get(j).size();
            double temp = Math.log(n)/Math.log(2); 
            sum += temp;
         }
      }
      sum = sum/81;
      return sum;
   }
   
   /**
    * Analyzes the solutions that it is aware of. Prints to console the [r][c] pairs of uncertain 
    * squares along with their possible values 
    */
   public void analyzeSolutions ()
   {
      ArrayList<HashSet <Integer>>[] infoBank = new ArrayList[9];
      for (int i=0; i<infoBank.length; i++)
      {
         infoBank[i] = new ArrayList<HashSet<Integer>>(9);
         ArrayList<HashSet<Integer>> pointer =infoBank[i];
         for (int j=0; j<9; j++)
         {
            pointer.add(new HashSet<Integer>());
         }
      }
      
      Iterator<int [][]> it = solutions.iterator();
      while (it.hasNext())
      {
         int [][] temp = it.next(); // current solution sudoku grid
         for (int i=0; i<temp.length; i++)
         {
            for (int j=0; j<temp[0].length; j++)
            {
               infoBank[i].get(j).add(temp[i][j]);
            }
         }
      }   
      printInfo(infoBank);
   }
   
   public ArrayList<HashSet <Integer>>[] getInfoBank ()
   {
      ArrayList<HashSet <Integer>>[] infoBank = new ArrayList[9];
      for (int i=0; i<infoBank.length; i++)
      {
         infoBank[i] = new ArrayList<HashSet<Integer>>(9);
         ArrayList<HashSet<Integer>> pointer =infoBank[i];
         for (int j=0; j<9; j++)
         {
            pointer.add(new HashSet<Integer>());
         }
      }
      
      Iterator<int [][]> it = solutions.iterator();
      while (it.hasNext())
      {
         int [][] temp = it.next(); // current solution sudoku grid
         for (int i=0; i<temp.length; i++)
         {
            for (int j=0; j<temp[0].length; j++)
            {
               infoBank[i].get(j).add(temp[i][j]);
            }
         }
      }   
      return infoBank;
   }
   
   /**
    * helper method for analyzeSolutions. Prints to console the [r][c] pairs of uncertain 
    * squares along with their possible values 
    * @param info: the sudoku grid containing possible values for each square
    */
   private void printInfo (ArrayList<HashSet <Integer>>[] info)
   {
      for (int i=0; i<info.length; i++)
      {
         for (int j=0; j<info[0].size(); j++)
         {
            HashSet<Integer> h= info[i].get(j);
            if (h.size()>1)
            {
               Iterator<Integer> hashit = h.iterator();
               System.out.print("["+i+"]"+"["+j+"]: ");
               while (hashit.hasNext())
               {
                  System.out.print(hashit.next()+", ");
               }
               System.out.println();
            }
         }
      }
      dumbGreedy(info);
   }
   
   /**
    *   Pick the square with the most entropy. If there is a tie, return all squares with the
    *   maximum entropy.  
    */
   public void dumbGreedy (ArrayList<HashSet <Integer>>[] info)
   {
      int max = -1;
      ArrayList <int []> highESquares = new ArrayList <int[]>();
      // figure out the maximum entropy (max number of options)
      for (int i=0; i<info.length; i++)
      {
         for (int j=0; j<info[0].size(); j++)
         {
            HashSet<Integer> h= info[i].get(j);
            if (h.size()>=max)
            {
               max = h.size();
            }  
         }  
      }
      // figure out which cells have the max number of options
      for (int i=0; i<info.length; i++)
      {
         for (int j=0; j<info[0].size(); j++)
         {
            HashSet<Integer> h= info[i].get(j);
            if (h.size()==max)
            {
               int [] temp = new int[2];
               temp[0]=i; temp[1]=j;
               highESquares.add(temp);
            }  
         }  
      }
      // print the locations (r,c) of the high entropy squares to the console
      Iterator <int[]> it = highESquares.iterator();
      System.out.println("ACCORDING TO THE DUMB GREEDY ALGORITHM, YOU SHOULD PICK," );
      while (it.hasNext())
      {
         int [] temp = it.next();
         System.out.println("["+temp[0]+"]"+"["+temp[1]+"]");
      }
   }
   
   public void smartGreedy (ArrayList<HashSet <Integer>>[] info, int [][] currentPuzzle)
   {
      double [][] entScores = new double [9][9];
      for (int i=0; i<info.length; i++)
      {
         for (int j=0; j<info[0].size(); j++) // looping through squares 
         {
            HashSet<Integer> h= info[i].get(j);
            if (h.size()<=1){
               entScores[i][j]=Integer.MAX_VALUE;
            } else {
               int [][] puzzle = currentPuzzle;
               
               double sum = 0; // entropy of board for THIS square
               Iterator <Integer> it = h.iterator();
               while (it.hasNext()) // looping through possible values for a given square
               {
                  puzzle[i][j]=it.next();
                  SudokuDLX megamind = new SudokuDLX();
                  ArrayList <int [][]> allSols = megamind.getSolutions(puzzle); // now you have all possible solutions if the sudoku had that value
                  double entropyOfBoardForThisValue = getAvgEntropyOfSolutions(allSols); // entropy of board for THIS square and THIS value
                  sum += entropyOfBoardForThisValue;
               }
               entScores[i][j]=sum/h.size();
            }
         }
      }
      
      Double lowestEntropy = Double.MAX_VALUE;
      // want the square that will result in the lowest entropy
      for (int i=0; i<entScores.length; i++)
      {
         for (int j=0; j<entScores[0].length; j++)
         {
            if (entScores[i][j] < lowestEntropy) lowestEntropy = entScores[i][j];
         }
      }
      
      ArrayList <int []> winners = new ArrayList <int []> ();
      for (int i=0; i<entScores.length; i++)
      {
         for (int j=0; j<entScores[0].length; j++)
         {
            if (Math.abs(lowestEntropy - entScores[i][j])<0.000000001) 
            {
               int [] pair = new int [2];
               pair[0]=i;pair[1]=j;
               winners.add(pair);
            }
         }
      }
      
      Iterator <int[]> it = winners.iterator();
      System.out.println("ACCORDING TO THE SMART GREEDY ALGORITHM, YOU SHOULD PICK," );
      while (it.hasNext())
      {
         int [] temp = it.next();
         System.out.println("["+temp[0]+"]"+"["+temp[1]+"]");
      }   
   }
}
