// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g 2010-05-01 17:56:30

package compgeom.util.parser;
import compgeom.*;
import java.math.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RationalParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Digits", "'.'", "'...'", "'/'", "'-'"
    };
    public static final int Digits=4;
    public static final int EOF=-1;
    public static final int T__8=8;
    public static final int T__7=7;
    public static final int T__6=6;
    public static final int T__5=5;

    // delegates
    // delegators


        public RationalParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public RationalParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return RationalParser.tokenNames; }
    public String getGrammarFileName() { return "/home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g"; }


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



    // $ANTLR start "parse"
    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:83:1: parse returns [BigInteger[] nd] : n= rational EOF ;
    public final BigInteger[] parse() throws RecognitionException {
        BigInteger[] nd = null;

        BigInteger[] n = null;


        try {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:84:3: (n= rational EOF )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:84:6: n= rational EOF
            {
            pushFollow(FOLLOW_rational_in_parse48);
            n=rational();

            state._fsp--;

            match(input,EOF,FOLLOW_EOF_in_parse50); 
            nd = n;

            }

        }

          catch(RecognitionException e) {
            throw new RuntimeException(e);
          }
        finally {
        }
        return nd;
    }
    // $ANTLR end "parse"


    // $ANTLR start "rational"
    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:87:1: rational returns [BigInteger[] nd] : (rd= repeatingDecimal | d= decimal | f= fraction | i= integer );
    public final BigInteger[] rational() throws RecognitionException {
        BigInteger[] nd = null;

        BigInteger[] rd = null;

        BigInteger[] d = null;

        BigInteger[] f = null;

        RationalParser.integer_return i = null;


        try {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:88:3: (rd= repeatingDecimal | d= decimal | f= fraction | i= integer )
            int alt1=4;
            alt1 = dfa1.predict(input);
            switch (alt1) {
                case 1 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:88:6: rd= repeatingDecimal
                    {
                    pushFollow(FOLLOW_repeatingDecimal_in_rational72);
                    rd=repeatingDecimal();

                    state._fsp--;

                    nd = rd;

                    }
                    break;
                case 2 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:89:6: d= decimal
                    {
                    pushFollow(FOLLOW_decimal_in_rational83);
                    d=decimal();

                    state._fsp--;

                    nd = d;

                    }
                    break;
                case 3 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:90:6: f= fraction
                    {
                    pushFollow(FOLLOW_fraction_in_rational104);
                    f=fraction();

                    state._fsp--;

                    nd = f;

                    }
                    break;
                case 4 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:91:6: i= integer
                    {
                    pushFollow(FOLLOW_integer_in_rational124);
                    i=integer();

                    state._fsp--;

                    nd = new BigInteger[]{(i!=null?i.i:null), BigInteger.ONE};

                    }
                    break;

            }
        }

          catch(RecognitionException e) {
            throw new RuntimeException(e);
          }
        finally {
        }
        return nd;
    }
    // $ANTLR end "rational"


    // $ANTLR start "repeatingDecimal"
    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:94:1: repeatingDecimal returns [BigInteger[] nd] : i= integer '.' d= Digits '...' ;
    public final BigInteger[] repeatingDecimal() throws RecognitionException {
        BigInteger[] nd = null;

        Token d=null;
        RationalParser.integer_return i = null;


        try {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:95:3: (i= integer '.' d= Digits '...' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:95:6: i= integer '.' d= Digits '...'
            {
            pushFollow(FOLLOW_integer_in_repeatingDecimal156);
            i=integer();

            state._fsp--;

            match(input,5,FOLLOW_5_in_repeatingDecimal158); 
            d=(Token)match(input,Digits,FOLLOW_Digits_in_repeatingDecimal162); 
            match(input,6,FOLLOW_6_in_repeatingDecimal164); 
            nd = numDen((i!=null?input.toString(i.start,i.stop):null), (d!=null?d.getText():null));

            }

        }

          catch(RecognitionException e) {
            throw new RuntimeException(e);
          }
        finally {
        }
        return nd;
    }
    // $ANTLR end "repeatingDecimal"


    // $ANTLR start "decimal"
    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:98:1: decimal returns [BigInteger[] nd] : i= integer '.' d= Digits ;
    public final BigInteger[] decimal() throws RecognitionException {
        BigInteger[] nd = null;

        Token d=null;
        RationalParser.integer_return i = null;


        try {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:99:3: (i= integer '.' d= Digits )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:99:6: i= integer '.' d= Digits
            {
            pushFollow(FOLLOW_integer_in_decimal190);
            i=integer();

            state._fsp--;

            match(input,5,FOLLOW_5_in_decimal192); 
            d=(Token)match(input,Digits,FOLLOW_Digits_in_decimal196); 

                   nd = new BigInteger[]{
                     new BigInteger((i!=null?input.toString(i.start,i.stop):null) + (d!=null?d.getText():null)), 
                     BigInteger.TEN.pow((d!=null?d.getText():null).length())
                   };
                 

            }

        }

          catch(RecognitionException e) {
            throw new RuntimeException(e);
          }
        finally {
        }
        return nd;
    }
    // $ANTLR end "decimal"


    // $ANTLR start "fraction"
    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:107:1: fraction returns [BigInteger[] nd] : n= integer '/' d= integer ;
    public final BigInteger[] fraction() throws RecognitionException {
        BigInteger[] nd = null;

        RationalParser.integer_return n = null;

        RationalParser.integer_return d = null;


        try {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:108:3: (n= integer '/' d= integer )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:108:6: n= integer '/' d= integer
            {
            pushFollow(FOLLOW_integer_in_fraction218);
            n=integer();

            state._fsp--;

            match(input,7,FOLLOW_7_in_fraction220); 
            pushFollow(FOLLOW_integer_in_fraction224);
            d=integer();

            state._fsp--;

            nd = new BigInteger[]{(n!=null?n.i:null), (d!=null?d.i:null)};

            }

        }

          catch(RecognitionException e) {
            throw new RuntimeException(e);
          }
        finally {
        }
        return nd;
    }
    // $ANTLR end "fraction"

    public static class integer_return extends ParserRuleReturnScope {
        public BigInteger i;
    };

    // $ANTLR start "integer"
    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:111:1: integer returns [BigInteger i] : ( '-' d= Digits | d= Digits );
    public final RationalParser.integer_return integer() throws RecognitionException {
        RationalParser.integer_return retval = new RationalParser.integer_return();
        retval.start = input.LT(1);

        Token d=null;

        try {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:112:3: ( '-' d= Digits | d= Digits )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==8) ) {
                alt2=1;
            }
            else if ( (LA2_0==Digits) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:112:6: '-' d= Digits
                    {
                    match(input,8,FOLLOW_8_in_integer244); 
                    d=(Token)match(input,Digits,FOLLOW_Digits_in_integer248); 
                    retval.i = new BigInteger("-"+(d!=null?d.getText():null));

                    }
                    break;
                case 2 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:113:6: d= Digits
                    {
                    d=(Token)match(input,Digits,FOLLOW_Digits_in_integer259); 
                    retval.i = new BigInteger((d!=null?d.getText():null));

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }

          catch(RecognitionException e) {
            throw new RuntimeException(e);
          }
        finally {
        }
        return retval;
    }
    // $ANTLR end "integer"

    // Delegated rules


    protected DFA1 dfa1 = new DFA1(this);
    static final String DFA1_eotS =
        "\12\uffff";
    static final String DFA1_eofS =
        "\2\uffff\2\4\3\uffff\1\11\2\uffff";
    static final String DFA1_minS =
        "\2\4\2\5\1\uffff\1\4\1\uffff\1\6\2\uffff";
    static final String DFA1_maxS =
        "\1\10\1\4\2\7\1\uffff\1\4\1\uffff\1\6\2\uffff";
    static final String DFA1_acceptS =
        "\4\uffff\1\4\1\uffff\1\3\1\uffff\1\1\1\2";
    static final String DFA1_specialS =
        "\12\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\2\3\uffff\1\1",
            "\1\3",
            "\1\5\1\uffff\1\6",
            "\1\5\1\uffff\1\6",
            "",
            "\1\7",
            "",
            "\1\10",
            "",
            ""
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "87:1: rational returns [BigInteger[] nd] : (rd= repeatingDecimal | d= decimal | f= fraction | i= integer );";
        }
    }
 

    public static final BitSet FOLLOW_rational_in_parse48 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_parse50 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_repeatingDecimal_in_rational72 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_decimal_in_rational83 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fraction_in_rational104 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_integer_in_rational124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_integer_in_repeatingDecimal156 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_5_in_repeatingDecimal158 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Digits_in_repeatingDecimal162 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_6_in_repeatingDecimal164 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_integer_in_decimal190 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_5_in_decimal192 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Digits_in_decimal196 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_integer_in_fraction218 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_7_in_fraction220 = new BitSet(new long[]{0x0000000000000110L});
    public static final BitSet FOLLOW_integer_in_fraction224 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_8_in_integer244 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_Digits_in_integer248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Digits_in_integer259 = new BitSet(new long[]{0x0000000000000002L});

}