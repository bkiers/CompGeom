grammar Line;

@parser::header {
package compgeom.util.parser;
import compgeom.*;
}
@lexer::header {
package compgeom.util.parser;
}

@members {
  public static RLine2D parse(String function) {
    try {
      ANTLRStringStream in = new ANTLRStringStream(function.toLowerCase());
      LineLexer lexer = new LineLexer(in);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      LineParser parser = new LineParser(tokens);
      return parser.parse();
    } catch(Exception e) {
      throw new IllegalArgumentException("invalid function: '"+function+"'\n"+e.getMessage());
    }
  }
  
  @Override
  public void recover(IntStream input, RecognitionException e) {
    throw new IllegalArgumentException();
  }
  
  @Override
  public Object recoverFromMismatchedSet(IntStream input, RecognitionException e, BitSet follow) throws RecognitionException {
    throw new IllegalArgumentException();
  } 
  
  @Override
  protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
    throw new IllegalArgumentException();
  }
}

@rulecatch {
  catch(RecognitionException e) {
    throw new RuntimeException(e);
  }
}

parse returns [RLine2D line]
  :  f=function {$line = $f.line;} EOF
  ;

function returns [RLine2D line]
  :  'f' '(' 'x' ')' '->' r=rhs {$line = $r.line;}
  |  'y' '=' r=rhs              {$line = $r.line;}
  |  'x' '=' n=number           {$line = RLine2D.vertical($n.rat);}
  ;

rhs returns [RLine2D line]
  :  m=slope b=constant {$line = new RLine2D($m.rat, $b.rat);}
  |  m=slope            {$line = new RLine2D($m.rat, compgeom.Rational.ZERO);}
  |  b=number           {$line = RLine2D.horizontal($b.rat);}
  ;

slope returns [compgeom.Rational rat]
@init{$rat = compgeom.Rational.ONE;}
  :  (n=number {$rat = $n.rat;} '*'? )? 'x'
  ;

constant returns [compgeom.Rational rat]
  :  '+' n=number {$rat = $n.rat;}
  |  '-' n=number {$rat = $n.rat.negate();}
  ;
  
number returns [compgeom.Rational rat]
  :  d=Decimal  {$rat = new compgeom.Rational($d.text);}
  |  r=Rational {$rat = new compgeom.Rational($r.text);}
  |  i=Integer  {$rat = new compgeom.Rational($i.text);}
  ;

Rational
  :  Integer '/' Integer
  ;
  
Decimal
  :  Integer '.' Digits
  ;

Integer
  :  '-'? Digits
  ;

fragment
Digits
  :  '0'..'9'+
  ;

Space
  :  (' ' | '\t' | '\r' | '\n') {skip();}
  ;