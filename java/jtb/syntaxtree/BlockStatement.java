//
// Generated by JTB 1.3.2
//

package jtb.syntaxtree;

/**
 * Grammar production:
 * f0 -> LocalVariableDeclaration() ";"
 *       | Statement()
 *       | ClassOrInterfaceDeclaration(0)
 */
public class BlockStatement implements Node {
   private Node parent;
   public NodeChoice f0;

   public BlockStatement(NodeChoice n0) {
      f0 = n0;
      if ( f0 != null ) f0.setParent(this);
   }

   public void accept(jtb.visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(jtb.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(jtb.visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(jtb.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
   public void setParent(Node n) { parent = n; }
   public Node getParent()       { return parent; }
}

