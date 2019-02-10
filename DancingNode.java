/**
 * The DancingNode class defines a node for an exact cover matrix of doubly linked list that can support Algorithm X, 
 * also known as "Dancing Links." A DancingNode contains references the DancingNodes that are situated above, below, to 
 * the right, and to the left of itself in the exact cover matrix. Additionally, it contains a reference to the ColumnNode, 
 * or column header, of its column. A DancingNode can connect to new nodes to the right or bottom, but it cannot change its
 * column header or the nodes that are to its left and above it. A DancingNode can remove itself from the exact cover grid
 * and also replace itself in the grid in its original position.      
 * 
 * @author Vani Mohindra, Rafal Szymanski
 */
public class DancingNode {
   
   DancingNode L, R, U, D;
   ColumnNode C; 
   
   /**
    * Constructor: create an instance of DancingNode
    */
   public DancingNode()
   {
      L=R=U=D=this;
   }
   
   /**
    * Constructor: create an instance of DancingNode with a given column header
    * @param c: The column header (ColumnNode) of this DancingNode. 
    */
   public DancingNode (ColumnNode c)
   {
      this();
      C=c;
   }
   
   /**
    * Attach the given DancingNode to the bottom of this DancingNode
    * @param n: the DancingNode to be attached below this dancing node
    * @return the newly attached bottom node
    */
   public DancingNode linkBelow (DancingNode n)
   {
      assert (this.C == n.C);
      n.D = this.D;
      n.D.U = n;
      n.U = this;
      this.D = n;
      return n;
   }
   
   /**
    * Attach the given DancingNode to the right of this DancingNode
    * @param n: the DancingNode to be attached to the right of this dancing node
    * @return the newly attached right node
    */
   public DancingNode linkRight (DancingNode n)
   {
      n.R = this.R;
      n.R.L = n;
      n.L = this;
      this.R = n;
      return n;
   }
  
   /**
    * Remove this DancingNode from its row 
    */
   public void unlinkLR()
   {
      this.L.R = this.R;
      this.R.L = this.L;
   }
   
   /**
    * Remove this DancingNode from its column
    */
   public void unlinkUD()
   {
      this.U.D = this.D;
      this.D.U = this.U;
   }
   
   /**
    * Put this DancingNode back into its row in the place where it was removed 
    */
   public void relinkLR()
   {
      this.L.R = this.R.L = this;
   }
   
   /**
    * Put this DancingNode back into its column in the place where it was removed
    */
   public void relinkUD()
   {
      this.U.D = this.D.U = this;
   }
   
}
