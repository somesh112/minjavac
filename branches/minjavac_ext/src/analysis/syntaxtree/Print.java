package analysis.syntaxtree;import analysis.visitors.TypeVisitor;import analysis.visitors.Visitor;public class Print implements Statement {  public Exp e;  public Print(Exp ae) {    e = ae;  }  public void accept(Visitor v) {    v.visit(this);  }  public Type accept(TypeVisitor v) {    return v.visit(this);  }}