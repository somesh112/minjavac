package analysis.syntaxtree;import analysis.visitors.TypeVisitor;import analysis.visitors.Visitor;public class False implements Exp {  public void accept(Visitor v) {    v.visit(this);  }  public Type accept(TypeVisitor v) {    return v.visit(this);  }}