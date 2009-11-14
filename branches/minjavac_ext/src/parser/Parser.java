package parser;

import analysis.syntaxtree.* ;

public class Parser implements ParserConstants {
  public static void main(String args[]) throws Exception {

    Parser parser = null ;

    if (args.length == 1) {
        parser = new Parser( new java.io.FileInputStream( args[ 0 ] ) ) ;
    }
    else if (args.length == 0) {
      parser = new Parser( System.in ) ;
    }
    else {
      System.out.println( "Usage: Parser < input filename or Parser input filename." );
      return;
    }

    Program p = null ;

    try {
      p = parser.Goal() ;
    }
    catch ( ParseException e ) {
      System.out.println( "Exiting." ) ;
      throw e ;
    }

    if ( p == null ) {
      System.out.println( "Input program is syntatically correct." ) ;
    }
    else {
      PrettyPrintVisitor v = new PrettyPrintVisitor() ;
      v.visit( p ) ;
    }

    return ;
  }

  static final public Program Goal() throws ParseException {
  MainClass m;
  ClassDeclList cl;
    m = MainClass();
    cl = ClassDeclarationList();
    jj_consume_token(0);
   {if (true) return new Program(m, cl);}
    throw new Error("Missing return statement in function");
  }

  static final public ClassDeclList ClassDeclarationList() throws ParseException {
  ClassDeclList l = new ClassDeclList();
  ClassDecl cd;
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CLASS:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      cd = ClassDeclaration();
                             l.addElement(cd);
    }
    {if (true) return l;}
    throw new Error("Missing return statement in function");
  }

  static final public MainClass MainClass() throws ParseException {
  Identifier name, args;
  Statement stmt;
    jj_consume_token(CLASS);
    name = Identifier();
    jj_consume_token(LBRACE);
    jj_consume_token(PUBLIC);
    jj_consume_token(STATIC);
    jj_consume_token(VOID);
    jj_consume_token(MAIN);
    jj_consume_token(LPARENS);
    jj_consume_token(STRING);
    jj_consume_token(LBRACKET);
    jj_consume_token(RBRACKET);
    args = Identifier();
    jj_consume_token(RPARENS);
    jj_consume_token(LBRACE);
    stmt = Statement();
    jj_consume_token(RBRACE);
    jj_consume_token(RBRACE);
    {if (true) return new MainClass(name, args, stmt);}
    throw new Error("Missing return statement in function");
  }

  static final public ClassDecl ClassDeclaration() throws ParseException {
  ClassDecl c;
  Identifier name;
    jj_consume_token(CLASS);
    name = Identifier();
    c = ClassDeclarationTail(name);
    {if (true) return c;}
    throw new Error("Missing return statement in function");
  }

  static final public ClassDecl ClassDeclarationTail(Identifier name) throws ParseException {
  Identifier basec;
  VarDeclList vl;
  MethodDeclList ml;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EXTENDS:
      jj_consume_token(EXTENDS);
      basec = Identifier();
      jj_consume_token(LBRACE);
      vl = VarDeclarationList();
      ml = MethodDeclarationList();
      jj_consume_token(RBRACE);
    {if (true) return new ClassDeclExtends(name, basec, vl, ml);}
      break;
    case LBRACE:
      jj_consume_token(LBRACE);
      vl = VarDeclarationList();
      ml = MethodDeclarationList();
      jj_consume_token(RBRACE);
    {if (true) return new ClassDeclSimple(name, vl, ml);}
      break;
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public VarDeclList VarDeclarationList() throws ParseException {
  VarDeclList vl = new VarDeclList();
  VarDecl v;
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INT:
      case BOOLEAN:
      case ID:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      v = VarDeclaration();
                          vl.addElement(v);
    }
    {if (true) return vl;}
    throw new Error("Missing return statement in function");
  }

  static final public MethodDeclList MethodDeclarationList() throws ParseException {
  MethodDeclList ml = new MethodDeclList();
  MethodDecl m;
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PUBLIC:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_3;
      }
      m = MethodDeclaration();
                             ml.addElement(m);
    }
    {if (true) return ml;}
    throw new Error("Missing return statement in function");
  }

  static final public VarDecl VarDeclaration() throws ParseException {
  Type t;
  Identifier i;
    t = Type();
    i = Identifier();
    jj_consume_token(SEMI);
    {if (true) return new VarDecl(t, i);}
    throw new Error("Missing return statement in function");
  }

  static final public MethodDecl MethodDeclaration() throws ParseException {
  Type returnT;
  Identifier name;
  FormalList params;
  VarDeclList varL = new VarDeclList();
  StatementList stmtL = new StatementList();
  Exp returnE;
    jj_consume_token(PUBLIC);
    returnT = Type();
    name = Identifier();
    jj_consume_token(LPARENS);
    params = FormalParameterList();
    jj_consume_token(RPARENS);
    jj_consume_token(LBRACE);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT:
    case BOOLEAN:
    case PRINT:
    case IF:
    case WHILE:
    case LBRACE:
    case ID:
      VarDeclarationOrStatement(varL, stmtL);
      break;
    default:
      jj_la1[4] = jj_gen;
      ;
    }
    jj_consume_token(RETURN);
    returnE = Expression();
    jj_consume_token(SEMI);
    jj_consume_token(RBRACE);
    {if (true) return new MethodDecl(
      returnT, name, params, varL, stmtL, returnE
    );}
    throw new Error("Missing return statement in function");
  }

  static final public FormalList FormalParameterList() throws ParseException {
  FormalList fl = new FormalList();
  Formal f;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT:
    case BOOLEAN:
    case ID:
      f = FormalParameter();
      fl.addElement(f);
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_4;
        }
        jj_consume_token(COMMA);
        f = FormalParameter();
                                     fl.addElement(f);
      }
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
    {if (true) return fl;}
    throw new Error("Missing return statement in function");
  }

  static final public Formal FormalParameter() throws ParseException {
  Type varT;
  Identifier name;
    varT = Type();
    name = Identifier();
    {if (true) return new Formal(varT, name);}
    throw new Error("Missing return statement in function");
  }

  static final public Type Type() throws ParseException {
  Type t;
  Identifier id;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT:
      t = ArrayOrIntegerType();
     {if (true) return t;}
      break;
    case BOOLEAN:
      t = BooleanType();
     {if (true) return t;}
      break;
    case ID:
      id = Identifier();
     {if (true) return new IdentifierType(id.toString());}
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public Type ArrayOrIntegerType() throws ParseException {
    jj_consume_token(INT);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACKET:
      jj_consume_token(LBRACKET);
      jj_consume_token(RBRACKET);
                           {if (true) return new IntArrayType();}
      break;
    default:
      jj_la1[8] = jj_gen;
      ;
    }
    {if (true) return new IntegerType();}
    throw new Error("Missing return statement in function");
  }

  static final public Type BooleanType() throws ParseException {
    jj_consume_token(BOOLEAN);
    {if (true) return new BooleanType();}
    throw new Error("Missing return statement in function");
  }

  static final public void VarDeclarationOrStatement(VarDeclList vl, StatementList stl) throws ParseException {
  Statement stmt;
  Identifier id;
  Type t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT:
    case BOOLEAN:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INT:
        t = ArrayOrIntegerType();
        break;
      case BOOLEAN:
        t = BooleanType();
        break;
      default:
        jj_la1[9] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      VarDeclarationTail(t, vl, stl);
      break;
    case PRINT:
    case IF:
    case WHILE:
    case LBRACE:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACE:
        stmt = Block();
        break;
      case IF:
        stmt = IfStatement();
        break;
      case WHILE:
        stmt = WhileStatement();
        break;
      case PRINT:
        stmt = PrintStatement();
        break;
      default:
        jj_la1[10] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
     stl.addElement(stmt);
      Statements(stl);
      break;
    case ID:
      id = Identifier();
      IdentifierDeclarationOrStatement(id, vl, stl);
      break;
    default:
      jj_la1[11] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void VarDeclarationTail(Type t, VarDeclList vl, StatementList stl) throws ParseException {
  Identifier id;
    id = Identifier();
    jj_consume_token(SEMI);
    vl.addElement(new VarDecl(t, id));
    VarDeclarationOrStatement(vl, stl);
  }

  static final public void IdentifierDeclarationOrStatement(Identifier id1, VarDeclList vl, StatementList stl) throws ParseException {
  Identifier id2;
  Statement stmt;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ID:
      id2 = Identifier();
      jj_consume_token(SEMI);
     Type t = new IdentifierType(id1.toString());
     vl.addElement(new VarDecl(t, id2));
      VarDeclarationOrStatement(vl, stl);
      break;
    case ASSIGN:
    case LBRACKET:
      stmt = IdentifierStatementTail(id1);
     stl.addElement(stmt);
      Statements(stl);
      break;
    default:
      jj_la1[12] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Statements(StatementList stl) throws ParseException {
  Statement stmt;
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PRINT:
      case IF:
      case WHILE:
      case LBRACE:
      case ID:
        ;
        break;
      default:
        jj_la1[13] = jj_gen;
        break label_5;
      }
      stmt = Statement();
                        stl.addElement(stmt);
    }
  }

  static final public Statement Statement() throws ParseException {
  Identifier id;
  Statement stmt;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PRINT:
    case IF:
    case WHILE:
    case LBRACE:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACE:
        stmt = Block();
        break;
      case IF:
        stmt = IfStatement();
        break;
      case WHILE:
        stmt = WhileStatement();
        break;
      case PRINT:
        stmt = PrintStatement();
        break;
      default:
        jj_la1[14] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
     {if (true) return stmt;}
      break;
    case ID:
      id = Identifier();
      stmt = IdentifierStatementTail(id);
     {if (true) return stmt;}
      break;
    default:
      jj_la1[15] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public Statement Block() throws ParseException {
  StatementList stmtL = new StatementList();
    jj_consume_token(LBRACE);
    Statements(stmtL);
    jj_consume_token(RBRACE);
    {if (true) return new Block(stmtL);}
    throw new Error("Missing return statement in function");
  }

  static final public Statement IfStatement() throws ParseException {
  Exp e;
  Statement s1, s2;
    jj_consume_token(IF);
    jj_consume_token(LPARENS);
    e = Expression();
    jj_consume_token(RPARENS);
    s1 = Statement();
    jj_consume_token(ELSE);
    s2 = Statement();
    {if (true) return new If(e, s1, s2);}
    throw new Error("Missing return statement in function");
  }

  static final public Statement WhileStatement() throws ParseException {
  Exp e;
  Statement stmt;
    jj_consume_token(WHILE);
    jj_consume_token(LPARENS);
    e = Expression();
    jj_consume_token(RPARENS);
    stmt = Statement();
    {if (true) return new While(e, stmt);}
    throw new Error("Missing return statement in function");
  }

  static final public Statement PrintStatement() throws ParseException {
  Exp e;
    jj_consume_token(PRINT);
    jj_consume_token(LPARENS);
    e = Expression();
    jj_consume_token(RPARENS);
    jj_consume_token(SEMI);
    {if (true) return new Print(e);}
    throw new Error("Missing return statement in function");
  }

  static final public Statement IdentifierStatementTail(Identifier id) throws ParseException {
  Exp e1, e2;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACKET:
      jj_consume_token(LBRACKET);
      e1 = Expression();
      jj_consume_token(RBRACKET);
      jj_consume_token(ASSIGN);
      e2 = Expression();
      jj_consume_token(SEMI);
      {if (true) return new ArrayAssign(id, e1, e2);}
      break;
    case ASSIGN:
      jj_consume_token(ASSIGN);
      e1 = Expression();
      jj_consume_token(SEMI);
      {if (true) return new Assign(id, e1);}
      break;
    default:
      jj_la1[16] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public Exp Expression() throws ParseException {
  Exp e;
    e = AndExpression();
    {if (true) return e;}
    throw new Error("Missing return statement in function");
  }

  static final public Exp AndExpression() throws ParseException {
  Exp x, y;
    x = LessThanExpression();
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND:
        ;
        break;
      default:
        jj_la1[17] = jj_gen;
        break label_6;
      }
      jj_consume_token(AND);
      y = LessThanExpression();
      x = new And(x, y);
    }
    {if (true) return x;}
    throw new Error("Missing return statement in function");
  }

  static final public Exp LessThanExpression() throws ParseException {
  Exp x, y;
    x = AdditiveExpression();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LESS:
      jj_consume_token(LESS);
      y = AdditiveExpression();
      x = new LessThan(x, y);
      break;
    default:
      jj_la1[18] = jj_gen;
      ;
    }
    {if (true) return x;}
    throw new Error("Missing return statement in function");
  }

  static final public Exp AdditiveExpression() throws ParseException {
  Exp x, y;
    x = TimesExpression();
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ADD:
      case SUB:
        ;
        break;
      default:
        jj_la1[19] = jj_gen;
        break label_7;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ADD:
        jj_consume_token(ADD);
        y = TimesExpression();
                                    x = new Plus(x, y);
        break;
      case SUB:
        jj_consume_token(SUB);
        y = TimesExpression();
                                    x = new Minus(x, y);
        break;
      default:
        jj_la1[20] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    {if (true) return x;}
    throw new Error("Missing return statement in function");
  }

  static final public Exp TimesExpression() throws ParseException {
  Exp x, y;
    x = PrefixExpression();
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MULT:
        ;
        break;
      default:
        jj_la1[21] = jj_gen;
        break label_8;
      }
      jj_consume_token(MULT);
      y = PrefixExpression();
      x = new Times(x, y);
    }
    {if (true) return x;}
    throw new Error("Missing return statement in function");
  }

  static final public Exp PrefixExpression() throws ParseException {
  Exp x;
  int c = 0;
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NOT:
        ;
        break;
      default:
        jj_la1[22] = jj_gen;
        break label_9;
      }
      jj_consume_token(NOT);
           ++c;
    }
    x = PostFixExpression();
    for (int i = 0; i < c; ++i) x = new Not(x);
    {if (true) return x;}
    throw new Error("Missing return statement in function");
  }

  static final public Exp PostFixExpression() throws ParseException {
  Exp e, pr;
    pr = PrimaryExpression();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACKET:
    case DOT:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACKET:
        e = ArrayLookup(pr);
        break;
      case DOT:
        e = ArrayLengthOrMethodCall(pr);
        break;
      default:
        jj_la1[23] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
       {if (true) return e;}
      break;
    default:
      jj_la1[24] = jj_gen;
      ;
    }
    {if (true) return pr;}
    throw new Error("Missing return statement in function");
  }

  static final public Exp ArrayLookup(Exp pr) throws ParseException {
  Exp e;
    jj_consume_token(LBRACKET);
    e = Expression();
    jj_consume_token(RBRACKET);
    {if (true) return new ArrayLookup(pr, e);}
    throw new Error("Missing return statement in function");
  }

  static final public Exp ArrayLengthOrMethodCall(Exp pr) throws ParseException {
  Exp e;
    jj_consume_token(DOT);
    e = ArrayLengthOrMethodCallTail(pr);
    {if (true) return e;}
    throw new Error("Missing return statement in function");
  }

  static final public Exp ArrayLengthOrMethodCallTail(Exp pr) throws ParseException {
  Identifier id;
  ExpList el = new ExpList();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LENGTH:
      jj_consume_token(LENGTH);
     {if (true) return new ArrayLength(pr);}
      break;
    case ID:
      id = Identifier();
      jj_consume_token(LPARENS);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case THIS:
      case TRUE:
      case FALSE:
      case NEW:
      case NOT:
      case LPARENS:
      case NUM:
      case ID:
        ExpressionList(el);
        break;
      default:
        jj_la1[25] = jj_gen;
        ;
      }
      jj_consume_token(RPARENS);
     {if (true) return new Call(pr, id, el);}
      break;
    default:
      jj_la1[26] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public Exp PrimaryExpression() throws ParseException {
  Exp e;
  Identifier id;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NUM:
      e = IntegerLiteral();
     {if (true) return e;}
      break;
    case TRUE:
      jj_consume_token(TRUE);
     {if (true) return new True();}
      break;
    case FALSE:
      jj_consume_token(FALSE);
     {if (true) return new False();}
      break;
    case ID:
      id = Identifier();
     {if (true) return new Identifier(id.toString());}
      break;
    case THIS:
      jj_consume_token(THIS);
     {if (true) return new This();}
      break;
    case LPARENS:
      jj_consume_token(LPARENS);
      e = Expression();
      jj_consume_token(RPARENS);
     {if (true) return e;}
      break;
    case NEW:
      jj_consume_token(NEW);
      e = ArrayOrObjectAllocation();
     {if (true) return e;}
      break;
    default:
      jj_la1[27] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public Exp ArrayOrObjectAllocation() throws ParseException {
  Identifier id;
  Exp e;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT:
      jj_consume_token(INT);
      jj_consume_token(LBRACKET);
      e = Expression();
      jj_consume_token(RBRACKET);
     {if (true) return new NewArray(e);}
      break;
    case ID:
      id = Identifier();
      jj_consume_token(LPARENS);
      jj_consume_token(RPARENS);
     {if (true) return new NewObject(id);}
      break;
    default:
      jj_la1[28] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public void ExpressionList(ExpList el) throws ParseException {
  Exp e;
    e = Expression();
    el.addElement(e);
    label_10:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[29] = jj_gen;
        break label_10;
      }
      jj_consume_token(COMMA);
      e = Expression();
      el.addElement(e);
    }
  }

  static final public Identifier Identifier() throws ParseException {
  Token t;
    t = jj_consume_token(ID);
             {if (true) return new Identifier(t.image);}
    throw new Error("Missing return statement in function");
  }

  static final public Exp IntegerLiteral() throws ParseException {
  Token t;
  int num;
    t = jj_consume_token(NUM);
    num = Integer.valueOf(t.image);
    {if (true) return new IntegerLiteral(num);}
    throw new Error("Missing return statement in function");
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public ParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[30];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x800000,0x10000000,0x1200,0x2000000,0x1181200,0x0,0x1200,0x1200,0x0,0x1200,0x1180000,0x1181200,0x0,0x1180000,0x1180000,0x1180000,0x0,0x0,0x0,0xc0000000,0xc0000000,0x0,0x0,0x0,0x0,0x21c000,0x40000,0x21c000,0x200,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x200,0x8000,0x0,0x8200,0x800,0x8000,0x8000,0x80,0x0,0x200,0x8200,0x8090,0x8200,0x200,0x8200,0x90,0x2,0x8,0x0,0x0,0x1,0x4,0x1080,0x1080,0xc024,0x8000,0xc020,0x8000,0x800,};
   }

  /** Constructor with InputStream. */
  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Parser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Parser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Parser(ParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[49];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 30; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 49; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

}
