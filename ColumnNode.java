/**
 * The ColumnNode class defines a special type of named DancingNode that keeps track of the number of nodes in its column. 
 * Each ColumnNode represents a constraint on the placement of numbers in the sudoku grid. 
 *   
 * @author Vani Mohindra, Rafal Szymanski
 */
public class ColumnNode extends DancingNode {
   int size; //number of 1s (nodes) in this column
   String name;
   
   /**
    * Constructor: creates a new column node with name n
    * @param n: name of the column 
    */
   public ColumnNode (String n)
   {
      super();
      size=0;
      name=n;
      C=this;
   }
   
   /**
    * The cover method is called on a ColumnNode when the constraint stipulated by that column is to be satisfied. The cover method 
    * removes this column and all rows that satisfy the stipulation of this column from the cover matrix of doubly linked lists. 
    * However, the cover method preserves the links between members of the removed column and rows to make backtracking- if it 
    * turns out to be necessary- easier. 
    */
   public void cover ()
   {
      unlinkLR(); // remove the column header
      for (DancingNode i=this.D; i!=this; i=i.D) // go down the column
      {
         for (DancingNode j=i.R; j!=i; j=j.R) // go right across the row from the current node. your stopping condition is j!=i because you do not want to 
            // break the UD links between the nodes of the condition column that you are removing
         {
            j.unlinkUD(); // the rows are intact but removed. this works because if there IS a node in the row, then it must be a 1 value and must eliminate
            // all rows except the solution row that have a 1 in that column. Plus, you end up removing the condition column as well because each of the nodes 
            // in it are removed along with the rows that they belong to. ALL OF THESE CAN BE ACCESSED BY THE *THIS* COLUMN NODE!
            j.C.size--; 
         }
      }
   }
   
   /**
    * The column and the rows that were removed when this ColumnNode was covered are put back into the exact cover matrix
    * of doubly linked lists in its original position.  
    */
   public void uncover ()
   {
      for (DancingNode i=this.U; i!=this; i=i.U) // go up the column
      {
         for (DancingNode j=i.L; j!=i; j=j.L) // go left across the row 
         {
            j.C.size++;
            j.relinkUD();
         }
      }
      this.relinkLR();
   }
   
   
}
