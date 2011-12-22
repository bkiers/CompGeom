// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g 2010-05-01 11:59:24

package compgeom.util.parser;
import compgeom.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class LineParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Decimal", "Rational", "Integer", "Digits", "Space", "'f'", "'('", "'x'", "')'", "'->'", "'y'", "'='", "'*'", "'+'", "'-'"
    };
    public static final int Rational=5;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int Digits=7;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int T__10=10;
    public static final int Decimal=4;
    public static final int EOF=-1;
    public static final int T__9=9;
    public static final int Integer=6;
    public static final int Space=8;

    // delegates
    // delegators


        public LineParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public LineParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return LineParser.tokenNames; }
    public String getGrammarFileName() { return "/home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g"; }


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



    // $ANTLR start "parse"
    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:46:1: parse returns [RLine2D line] : f= function EOF ;
    public final RLine2D parse() throws RecognitionException {
        RLine2D line = null;

        RLine2D f = null;


        try {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:47:3: (f= function EOF )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:47:6: f= function EOF
            {
            pushFollow(FOLLOW_function_in_parse48);
            f=function();

            state._fsp--;

            line = f;
            match(input,EOF,FOLLOW_EOF_in_parse52); 

            }

        }

          catch(RecognitionException e) {
            throw new RuntimeException(e);
          }
        finally {
        }
        return line;
    }
    // $ANTLR end "parse"


    // $ANTLR start "function"
    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:50:1: function returns [RLine2D line] : ( 'f' '(' 'x' ')' '->' r= rhs | 'y' '=' r= rhs | 'x' '=' n= number );
    public final RLine2D function() throws RecognitionException {
        RLine2D line = null;

        RLine2D r = null;

        compgeom.Rational n = null;


        try {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:51:3: ( 'f' '(' 'x' ')' '->' r= rhs | 'y' '=' r= rhs | 'x' '=' n= number )
            int alt1=3;
            switch ( input.LA(1) ) {
            case 9:
                {
                alt1=1;
                }
                break;
            case 14:
                {
                alt1=2;
                }
                break;
            case 11:
                {
                alt1=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:51:6: 'f' '(' 'x' ')' '->' r= rhs
                    {
                    match(input,9,FOLLOW_9_in_function70); 
                    match(input,10,FOLLOW_10_in_function72); 
                    match(input,11,FOLLOW_11_in_function74); 
                    match(input,12,FOLLOW_12_in_function76); 
                    match(input,13,FOLLOW_13_in_function78); 
                    pushFollow(FOLLOW_rhs_in_function82);
                    r=rhs();

                    state._fsp--;

                    line = r;

                    }
                    break;
                case 2 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:52:6: 'y' '=' r= rhs
                    {
                    match(input,14,FOLLOW_14_in_function91); 
                    match(input,15,FOLLOW_15_in_function93); 
                    pushFollow(FOLLOW_rhs_in_function97);
                    r=rhs();

                    state._fsp--;

                    line = r;

                    }
                    break;
                case 3 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:53:6: 'x' '=' n= number
                    {
                    match(input,11,FOLLOW_11_in_function119); 
                    match(input,15,FOLLOW_15_in_function121); 
                    pushFollow(FOLLOW_number_in_function125);
                    n=number();

                    state._fsp--;

                    line = RLine2D.vertical(n);

                    }
                    break;

            }
        }

          catch(RecognitionException e) {
            throw new RuntimeException(e);
          }
        finally {
        }
        return line;
    }
    // $ANTLR end "function"


    // $ANTLR start "rhs"
    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:56:1: rhs returns [RLine2D line] : (m= slope b= constant | m= slope | b= number );
    public final RLine2D rhs() throws RecognitionException {
        RLine2D line = null;

        compgeom.Rational m = null;

        compgeom.Rational b = null;


        try {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:57:3: (m= slope b= constant | m= slope | b= number )
            int alt2=3;
            switch ( input.LA(1) ) {
            case Decimal:
                {
                switch ( input.LA(2) ) {
                case 16:
                    {
                    int LA2_5 = input.LA(3);

                    if ( (LA2_5==11) ) {
                        int LA2_4 = input.LA(4);

                        if ( ((LA2_4>=17 && LA2_4<=18)) ) {
                            alt2=1;
                        }
                        else if ( (LA2_4==EOF) ) {
                            alt2=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 2, 4, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 5, input);

                        throw nvae;
                    }
                    }
                    break;
                case 11:
                    {
                    int LA2_4 = input.LA(3);

                    if ( ((LA2_4>=17 && LA2_4<=18)) ) {
                        alt2=1;
                    }
                    else if ( (LA2_4==EOF) ) {
                        alt2=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                case EOF:
                    {
                    alt2=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;
                }

                }
                break;
            case Rational:
                {
                switch ( input.LA(2) ) {
                case 16:
                    {
                    int LA2_5 = input.LA(3);

                    if ( (LA2_5==11) ) {
                        int LA2_4 = input.LA(4);

                        if ( ((LA2_4>=17 && LA2_4<=18)) ) {
                            alt2=1;
                        }
                        else if ( (LA2_4==EOF) ) {
                            alt2=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 2, 4, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 5, input);

                        throw nvae;
                    }
                    }
                    break;
                case 11:
                    {
                    int LA2_4 = input.LA(3);

                    if ( ((LA2_4>=17 && LA2_4<=18)) ) {
                        alt2=1;
                    }
                    else if ( (LA2_4==EOF) ) {
                        alt2=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                case EOF:
                    {
                    alt2=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 2, input);

                    throw nvae;
                }

                }
                break;
            case Integer:
                {
                switch ( input.LA(2) ) {
                case EOF:
                    {
                    alt2=3;
                    }
                    break;
                case 16:
                    {
                    int LA2_5 = input.LA(3);

                    if ( (LA2_5==11) ) {
                        int LA2_4 = input.LA(4);

                        if ( ((LA2_4>=17 && LA2_4<=18)) ) {
                            alt2=1;
                        }
                        else if ( (LA2_4==EOF) ) {
                            alt2=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 2, 4, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 5, input);

                        throw nvae;
                    }
                    }
                    break;
                case 11:
                    {
                    int LA2_4 = input.LA(3);

                    if ( ((LA2_4>=17 && LA2_4<=18)) ) {
                        alt2=1;
                    }
                    else if ( (LA2_4==EOF) ) {
                        alt2=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 3, input);

                    throw nvae;
                }

                }
                break;
            case 11:
                {
                int LA2_4 = input.LA(2);

                if ( ((LA2_4>=17 && LA2_4<=18)) ) {
                    alt2=1;
                }
                else if ( (LA2_4==EOF) ) {
                    alt2=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 4, input);

                    throw nvae;
                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:57:6: m= slope b= constant
                    {
                    pushFollow(FOLLOW_slope_in_rhs157);
                    m=slope();

                    state._fsp--;

                    pushFollow(FOLLOW_constant_in_rhs161);
                    b=constant();

                    state._fsp--;

                    line = new RLine2D(m, b);

                    }
                    break;
                case 2 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:58:6: m= slope
                    {
                    pushFollow(FOLLOW_slope_in_rhs172);
                    m=slope();

                    state._fsp--;

                    line = new RLine2D(m, compgeom.Rational.ZERO);

                    }
                    break;
                case 3 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:59:6: b= number
                    {
                    pushFollow(FOLLOW_number_in_rhs194);
                    b=number();

                    state._fsp--;

                    line = RLine2D.horizontal(b);

                    }
                    break;

            }
        }

          catch(RecognitionException e) {
            throw new RuntimeException(e);
          }
        finally {
        }
        return line;
    }
    // $ANTLR end "rhs"


    // $ANTLR start "slope"
    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:62:1: slope returns [compgeom.Rational rat] : (n= number ( '*' )? )? 'x' ;
    public final compgeom.Rational slope() throws RecognitionException {
        compgeom.Rational rat = null;

        compgeom.Rational n = null;


        rat = compgeom.Rational.ONE;
        try {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:64:3: ( (n= number ( '*' )? )? 'x' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:64:6: (n= number ( '*' )? )? 'x'
            {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:64:6: (n= number ( '*' )? )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( ((LA4_0>=Decimal && LA4_0<=Integer)) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:64:7: n= number ( '*' )?
                    {
                    pushFollow(FOLLOW_number_in_slope231);
                    n=number();

                    state._fsp--;

                    rat = n;
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:64:33: ( '*' )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==16) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:64:33: '*'
                            {
                            match(input,16,FOLLOW_16_in_slope235); 

                            }
                            break;

                    }


                    }
                    break;

            }

            match(input,11,FOLLOW_11_in_slope241); 

            }

        }

          catch(RecognitionException e) {
            throw new RuntimeException(e);
          }
        finally {
        }
        return rat;
    }
    // $ANTLR end "slope"


    // $ANTLR start "constant"
    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:67:1: constant returns [compgeom.Rational rat] : ( '+' n= number | '-' n= number );
    public final compgeom.Rational constant() throws RecognitionException {
        compgeom.Rational rat = null;

        compgeom.Rational n = null;


        try {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:68:3: ( '+' n= number | '-' n= number )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==17) ) {
                alt5=1;
            }
            else if ( (LA5_0==18) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:68:6: '+' n= number
                    {
                    match(input,17,FOLLOW_17_in_constant259); 
                    pushFollow(FOLLOW_number_in_constant263);
                    n=number();

                    state._fsp--;

                    rat = n;

                    }
                    break;
                case 2 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:69:6: '-' n= number
                    {
                    match(input,18,FOLLOW_18_in_constant272); 
                    pushFollow(FOLLOW_number_in_constant276);
                    n=number();

                    state._fsp--;

                    rat = n.negate();

                    }
                    break;

            }
        }

          catch(RecognitionException e) {
            throw new RuntimeException(e);
          }
        finally {
        }
        return rat;
    }
    // $ANTLR end "constant"


    // $ANTLR start "number"
    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:72:1: number returns [compgeom.Rational rat] : (d= Decimal | r= Rational | i= Integer );
    public final compgeom.Rational number() throws RecognitionException {
        compgeom.Rational rat = null;

        Token d=null;
        Token r=null;
        Token i=null;

        try {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:73:3: (d= Decimal | r= Rational | i= Integer )
            int alt6=3;
            switch ( input.LA(1) ) {
            case Decimal:
                {
                alt6=1;
                }
                break;
            case Rational:
                {
                alt6=2;
                }
                break;
            case Integer:
                {
                alt6=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:73:6: d= Decimal
                    {
                    d=(Token)match(input,Decimal,FOLLOW_Decimal_in_number300); 
                    rat = new compgeom.Rational((d!=null?d.getText():null));

                    }
                    break;
                case 2 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:74:6: r= Rational
                    {
                    r=(Token)match(input,Rational,FOLLOW_Rational_in_number312); 
                    rat = new compgeom.Rational((r!=null?r.getText():null));

                    }
                    break;
                case 3 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:75:6: i= Integer
                    {
                    i=(Token)match(input,Integer,FOLLOW_Integer_in_number323); 
                    rat = new compgeom.Rational((i!=null?i.getText():null));

                    }
                    break;

            }
        }

          catch(RecognitionException e) {
            throw new RuntimeException(e);
          }
        finally {
        }
        return rat;
    }
    // $ANTLR end "number"

    // Delegated rules


 

    public static final BitSet FOLLOW_function_in_parse48 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_parse52 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_9_in_function70 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_10_in_function72 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_function74 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_12_in_function76 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_function78 = new BitSet(new long[]{0x0000000000000870L});
    public static final BitSet FOLLOW_rhs_in_function82 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_function91 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_function93 = new BitSet(new long[]{0x0000000000000870L});
    public static final BitSet FOLLOW_rhs_in_function97 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_11_in_function119 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_function121 = new BitSet(new long[]{0x0000000000000070L});
    public static final BitSet FOLLOW_number_in_function125 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_slope_in_rhs157 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_constant_in_rhs161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_slope_in_rhs172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_rhs194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_slope231 = new BitSet(new long[]{0x0000000000010800L});
    public static final BitSet FOLLOW_16_in_slope235 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_11_in_slope241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_constant259 = new BitSet(new long[]{0x0000000000000070L});
    public static final BitSet FOLLOW_number_in_constant263 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_constant272 = new BitSet(new long[]{0x0000000000000070L});
    public static final BitSet FOLLOW_number_in_constant276 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Decimal_in_number300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Rational_in_number312 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Integer_in_number323 = new BitSet(new long[]{0x0000000000000002L});

}