grammar Rational;

@parser::header {
package compgeom.util.parser;
import compgeom.*;
import java.math.*;
}
@lexer::header {
package compgeom.util.parser;
}

@members {
  public static BigInteger[] parse(String number) {
    try {
      ANTLRStringStream in = new ANTLRStringStream(number);
      RationalLexer lexer = new RationalLexer(in);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      RationalParser parser = new RationalParser(tokens);
      return parser.parse();
    } catch(Exception e) {
      throw new NumberFormatException(e.getMessage());
    }
  }

  private static BigInteger[] numDen(String intPart, String decPart) {
    boolean negative = intPart.startsWith("-");
    String[] repeating = repeatingDigits(decPart);
    BigDecimal a = new BigDecimal(intPart+"."+repeating[0]);
    StringBuilder den = new StringBuilder();
    for(int i = 0; i < repeating[1].length(); i++) {
      den.append('9');
    }
    for(int i = 0; i < repeating[0].length(); i++) {
      den.append('0');
    }
    BigInteger aBD = a.multiply(new BigDecimal(den.toString())).toBigInteger();
    return new BigInteger[]{
        negative ? aBD.subtract(new BigInteger(repeating[1])) : aBD.add(new BigInteger(repeating[1])), 
        new BigInteger(den.toString())
    };
  }

  private static String[] repeatingDigits(String s) {
    StringBuilder before = new StringBuilder();
    if(s.matches("(\\d)\\1*")) {
      return new String[]{"", String.valueOf(s.charAt(0))};
    }
    for(int i = 0; i < s.length()-1; i++) {
      StringBuilder b = new StringBuilder();
      for(int j = i; j < s.length()-1; j++) {
        b.append(s.charAt(j));
        if(s.substring(i).matches("("+b.toString()+")+")) {
          return new String[]{before.toString(), b.toString()};
        }
      }
      before.append(s.charAt(i));
    }
    return new String[]{"", s};
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

parse returns [BigInteger[\] nd]
  :  n=rational EOF {$nd = $n.nd;}
  ;

rational returns [BigInteger[\] nd]
  :  rd=repeatingDecimal {$nd = $rd.nd;}
  |  d=decimal           {$nd = $d.nd;}
  |  f=fraction          {$nd = $f.nd;}
  |  i=integer           {$nd = new BigInteger[]{$i.i, BigInteger.ONE};}
  ;

repeatingDecimal returns [BigInteger[\] nd]
  :  i=integer '.' d=Digits '...' {$nd = numDen($i.text, $d.text);}
  ;  
  
decimal returns [BigInteger[\] nd]
  :  i=integer '.' d=Digits {
       $nd = new BigInteger[]{
         new BigInteger($i.text + $d.text), 
         BigInteger.TEN.pow($d.text.length())
       };
     }
  ;

fraction returns [BigInteger[\] nd]
  :  n=integer '/' d=integer {$nd = new BigInteger[]{$n.i, $d.i};}
  ;

integer returns [BigInteger i]
  :  '-' d=Digits {$i = new BigInteger("-"+$d.text);}
  |  d=Digits     {$i = new BigInteger($d.text);}
  ;
  
Digits
  :  '0'..'9'+
  ;
  