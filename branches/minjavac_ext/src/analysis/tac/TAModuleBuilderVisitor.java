package analysis.tac;

import java.util.List;
import analysis.syntaxtree.*;
import analysis.symboltable.*;
import analysis.tac.variables.*;
import analysis.visitors.Visitor;
import analysis.tac.instructions.*;

public class TAModuleBuilderVisitor implements Visitor {
  private TAModule module;
  private TAVariable lastTemp;
  private SymbolTable symbolTable;
 
  public TAModuleBuilderVisitor() {
    lastTemp = null;
    symbolTable = SymbolTable.getInstance();
    module = new TAModule();
    TAModule.setInstance(module);
  }

  public TAModule getModule() {
    return module;
  }

  public void visit(Program program) {
    program.mainC.accept(this);

    for (ClassDecl c : program.classList.getList())
      c.accept(this);
  }

  public void visit(MainClass mainC) {
    module.startClass(mainC.classNameId.name);
    module.startProcedure("main");

    mainC.mainStmt.accept(this);

    module.closeProcedure();
    module.closeClass();
  }

  public void visit(ClassDeclSimple classD) {
    module.startClass(classD.classId.name);
    
    for (MethodDecl methodDecl : classD.methodList.getList())
      methodDecl.accept(this);
    
    module.closeClass();
  }

  public void visit(ClassDeclExtends classD) {
    module.startClass(classD.classId.name);

    for (MethodDecl methodDecl : classD.methodList.getList())
      methodDecl.accept(this);

    module.closeClass();
  }

  public void visit(MethodDecl methodD) {
    NamePool.reset();
    module.startProcedure(methodD.methodNameId.name);

    for (Statement stmt : methodD.statementList.getList())
      stmt.accept(this);

    methodD.returnExpr.accept(this);
    module.addInstruction(new Return(lastTemp));
    module.closeProcedure();
  }

  public void visit(Block block) {
    for (Statement stmt : block.stmtList.getList())
      stmt.accept(this);
  }

  public void visit(If ifStmt) {
    Label trueL = NamePool.newLabel("if_true");
    Label falseL = NamePool.newLabel("if_false");
    Label next = NamePool.newLabel("if_next");

    evalBooleanJump(ifStmt.boolExpr, trueL, falseL);

    module.addInstruction(trueL);
    ifStmt.trueStmt.accept(this);
    module.addInstruction(new Jump(next));

    module.addInstruction(falseL);
    ifStmt.falseStmt.accept(this);

    module.addInstruction(next);
  }

  public void visit(While whileStmt) {
    Label loop = NamePool.newLabel("loop");
    Label trueL = NamePool.newLabel("while_true");
    Label falseL = NamePool.newLabel("while_false");

    module.addInstruction(loop);
    evalBooleanJump(whileStmt.boolExpr, trueL, falseL);

    module.addInstruction(trueL);
    whileStmt.stmt.accept(this);
    module.addInstruction(new Jump(loop));

    module.addInstruction(falseL);
  }

  public void visit(Print printStmt) {
    printStmt.intExpr.accept(this);

    module.addInstruction(new Action(Opcode.SAVE_C_CTX));
    module.addInstruction(new ParameterSetup(lastTemp));

    module.addInstruction(new ProcedureCall(
      NamePool.newVar("void", IntegerType.instance()), new Label("_print_int")
    ));
    
    module.addInstruction(new Action(Opcode.LOAD_C_CTX));
  }

  public void visit(Assign assignStmt) {
    assignStmt.valueExpr.accept(this);

    String varN = assignStmt.varId.name;
    String classN = module.getOpenClass().getName();
    ClassDescriptor cd = symbolTable.getClass(classN);

    TAVariable destiny;
    if (cd.isInScope(varN))
      destiny = new TAFieldVar(varN);
    else
      destiny = getTAVariable(varN);

    module.addInstruction(new Copy(destiny, lastTemp));
  }

  public void visit(ArrayAssign arrayAssign) {
    TAVariable arrayVar = getTAVariable(arrayAssign.arrayId.name);

    arrayAssign.indexExpr.accept(this);
    TAVariable indexVar = lastTemp;

    arrayAssign.valueExpr.accept(this);
    TAVariable valueVar = lastTemp;

    TAArrayCellVar dest = new TAArrayCellVar(arrayVar, indexVar);
    module.addInstruction(new Copy(dest, valueVar));
  }

  public void visit(And andExpr) {
    TAVariable a, b;
    TAVariable temp = NamePool.newVar("and", BooleanType.instance());

    andExpr.e1.accept(this);
    a = lastTemp;

    andExpr.e2.accept(this);
    b = lastTemp;

    module.addInstruction(new Operation(
      Opcode.AND, temp, a, b
    ));

    lastTemp = temp;
  }

  public void visit(LessThan lessExpr) {
    TAVariable a, b;
    TAVariable temp = NamePool.newVar("less_than", BooleanType.instance());

    lessExpr.e1.accept(this);
    a = lastTemp;

    lessExpr.e2.accept(this);
    b = lastTemp;

    Label trueL = NamePool.newLabel("is_less");
    Label nextL = NamePool.newLabel("next");

    module.addInstruction(new ConditionalJump(
      Condition.LESS_THAN, a, b, trueL
    ));

    module.addInstruction(new Copy(temp, new TAConstantVar(0)));
    module.addInstruction(new Jump(nextL));
    module.addInstruction(trueL);
    module.addInstruction(new Copy(temp, new TAConstantVar(1)));
    module.addInstruction(nextL);

    lastTemp = temp;
  }

  public void visit(Plus plusExpr) {
    TAVariable a, b;
    TAVariable temp = NamePool.newVar("add", IntegerType.instance());

    plusExpr.e1.accept(this);
    a = lastTemp;

    plusExpr.e2.accept(this);
    b = lastTemp;

    module.addInstruction(new Operation(
      Opcode.ADD, temp, a, b
    ));

    lastTemp = temp;
  }

  public void visit(Minus minusExpr) {
    TAVariable a, b;
    TAVariable temp = NamePool.newVar("sub", IntegerType.instance());

    minusExpr.e1.accept(this);
    a = lastTemp;

    minusExpr.e2.accept(this);
    b = lastTemp;

    module.addInstruction(new Operation(
      Opcode.SUB, temp, a, b
    ));

    lastTemp = temp;
  }

  public void visit(Times timesExpr) {
    TAVariable a, b;
    TAVariable temp = NamePool.newVar("mult", IntegerType.instance());

    timesExpr.e1.accept(this);
    a = lastTemp;

    timesExpr.e2.accept(this);
    b = lastTemp;

    module.addInstruction(new Operation(
      Opcode.MULT, temp, a, b
    ));

    lastTemp = temp;
  }

  public void visit(ArrayLookup lookup) {
    TAVariable temp = NamePool.newVar("array_lookup", IntegerType.instance());

    lookup.indexExpr.accept(this);
    TAVariable index = lastTemp;

    lookup.arrayExpr.accept(this);
    TAVariable array = lastTemp;

    TAArrayCellVar cell = new TAArrayCellVar(array, index);

    module.addInstruction(new Copy(temp, cell));
    lastTemp = temp;
  }

  public void visit(ArrayLength length) {
    TAVariable temp = NamePool.newVar("array_length", IntegerType.instance());

    length.arrayExpr.accept(this);
    TAVariable arrayVar = lastTemp;

    module.addInstruction(new Operation(
      Opcode.ARRAY_LENGTH, temp, arrayVar
    ));

    lastTemp = temp;
  }

  public void visit(Call callStmt) {
    callStmt.objectExpr.accept(this);
    TAVariable objectRef = lastTemp;

    module.addInstruction(new Action(Opcode.SAVE_CTX));

    List<Exp> params = callStmt.paramExprList.getList();
    for (int i = params.size()-1; i >= 0; --i) {
      params.get(i).accept(this);
      module.addInstruction(new ParameterSetup(lastTemp));
    }

    module.addInstruction(new ParameterSetup(
      new TAThisReferenceVar(objectRef)
    ));

    IdentifierType objType = (IdentifierType)callStmt.objectExpr.getType();
    String classN = objType.className;
    String methodN = callStmt.methodId.name;
    MethodDescriptor methodD = symbolTable.getMethod(methodN, classN);

    Label procLabel = new Label(classN + "@" + methodN);
    TAVariable temp = NamePool.newVar("call", methodD.getReturnType());

    module.addInstruction(new ProcedureCall(temp, procLabel));
    module.addInstruction(new Action(Opcode.LOAD_CTX));

    lastTemp = temp;
  }

  public void visit(IntegerLiteral intLiteral) {
    lastTemp = new TAConstantVar(intLiteral.value);
  }

  public void visit(True t) {
    lastTemp = new TAConstantVar(1);
  }

  public void visit(False f) {
    lastTemp = new TAConstantVar(0);
  }

  public void visit(This thisExp) {
    lastTemp = new TAThisVar();
  }

  public void visit(NewArray newArray) {
    TAVariable temp = NamePool.newVar("new_array", IntArrayType.instance());
    
    newArray.sizeExpr.accept(this);
    TAVariable size = lastTemp;

    module.addInstruction(new Action(Opcode.SAVE_C_CTX));
    module.addInstruction(new ParameterSetup(size));
    module.addInstruction(new ProcedureCall(
      temp, new Label("_new_array")
    ));
    module.addInstruction(new Action(Opcode.LOAD_C_CTX));

    lastTemp = temp;
  }

  public void visit(NewObject newObj) {
    String classN = newObj.classNameId.name;
    Type tempT = new IdentifierType(classN);
    TAVariable temp = NamePool.newVar("new_" + newObj.classNameId, tempT);

    String procLabel = newObj.classNameId + "@@new";

    module.addInstruction(new Action(Opcode.SAVE_CTX));
    module.addInstruction(new ProcedureCall(
      temp, new Label(procLabel)
    ));
    module.addInstruction(new Action(Opcode.LOAD_CTX));
            
    lastTemp = temp;
  }

  public void visit(Not notExp) {
    TAVariable temp = NamePool.newVar("not", BooleanType.instance());

    notExp.boolExpr.accept(this);

    module.addInstruction(new Operation(
      Opcode.NOT, temp, lastTemp
    ));

    lastTemp = temp;
  }

  public void visit(Identifier varId) {
    lastTemp = getTAVariable(varId.name);
  }

  private void evalBooleanJump(Exp e, Label trueL, Label falseL) {
    if (e instanceof False) {
      module.addInstruction(new Jump(falseL));
    }
    else if (e instanceof True) {
      module.addInstruction(new Jump(trueL));
    }
    else if (e instanceof Not) {
      evalBooleanJump(((Not)e).boolExpr, falseL, trueL);
    }
    else if (e instanceof And) {
      And andExpr = (And)e;

      Label cont = NamePool.newLabel("and_cont");
      evalBooleanJump(andExpr.e1, cont, falseL);

      module.addInstruction(cont);
      evalBooleanJump(andExpr.e2, trueL, falseL);
    }
    else if (e instanceof LessThan) {
      LessThan lessExpr = (LessThan)e;

      lessExpr.e1.accept(this);
      TAVariable a = lastTemp;

      lessExpr.e2.accept(this);
      TAVariable b = lastTemp;

      module.addInstruction(new ConditionalJump(
        Condition.LESS_THAN, a, b, trueL
      ));

      module.addInstruction(new Jump(falseL));
    }
    else if (e instanceof Identifier) {
      TAVariable boolVar = getTAVariable(((Identifier)e).name);

      module.addInstruction(new ConditionalJump(
        Condition.IS_TRUE, boolVar, null, trueL
      ));

      module.addInstruction(new Jump(falseL));
    }
    else if (e instanceof Call) {
      e.accept(this);
      TAVariable returnVar = lastTemp;
      
      module.addInstruction(new ConditionalJump(
        Condition.IS_TRUE, returnVar, null, trueL
      ));

      module.addInstruction(new Jump(falseL));
    }
    else {
      throw new IllegalArgumentException("evalBooleanJump");
    }
  }

  public void visit(VarDecl varD) {
    throw new IllegalArgumentException("visit@VarDecl");
  }

  public void visit(Formal param) {
    throw new IllegalArgumentException("visit@Formal");
  }

  public void visit(IntArrayType n) {
    throw new IllegalArgumentException("visit@IntArrayType");
  }

  public void visit(BooleanType n) {
    throw new IllegalArgumentException("visit@BooleanType");
  }

  public void visit(IntegerType n) {
    throw new IllegalArgumentException("visit@IntegerType");
  }

  public void visit(IdentifierType n) {
    throw new IllegalArgumentException("visit@IdentifierType");
  }

  private TAVariable getTAVariable(String name) {
    TAClass c = module.getOpenClass();
    ClassDescriptor cd = symbolTable.getClass(c.getName());
    
    if (!cd.isInScope(name))
      return new TALocalVar(name);

    TAFieldVar fv = new TAFieldVar(name);
    TAVariable temp = NamePool.newVar(name, cd.getVarInScope(name).type());

    module.addInstruction(new Copy(temp, fv));

    return temp;
  }
}
