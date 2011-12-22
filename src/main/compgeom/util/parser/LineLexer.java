// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g 2010-05-01 11:59:24

package compgeom.util.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class LineLexer extends Lexer {
    public static final int Rational=5;
    public static final int T__16=16;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int Digits=7;
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

    public LineLexer() {;} 
    public LineLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public LineLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g"; }

    // $ANTLR start "T__9"
    public final void mT__9() throws RecognitionException {
        try {
            int _type = T__9;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:7:6: ( 'f' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:7:8: 'f'
            {
            match('f'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__9"

    // $ANTLR start "T__10"
    public final void mT__10() throws RecognitionException {
        try {
            int _type = T__10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:8:7: ( '(' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:8:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__10"

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:9:7: ( 'x' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:9:9: 'x'
            {
            match('x'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__11"

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:10:7: ( ')' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:10:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:11:7: ( '->' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:11:9: '->'
            {
            match("->"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:12:7: ( 'y' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:12:9: 'y'
            {
            match('y'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:13:7: ( '=' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:13:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:14:7: ( '*' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:14:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:15:7: ( '+' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:15:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:16:7: ( '-' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:16:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "Rational"
    public final void mRational() throws RecognitionException {
        try {
            int _type = Rational;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:79:3: ( Integer '/' Integer )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:79:6: Integer '/' Integer
            {
            mInteger(); 
            match('/'); 
            mInteger(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Rational"

    // $ANTLR start "Decimal"
    public final void mDecimal() throws RecognitionException {
        try {
            int _type = Decimal;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:83:3: ( Integer '.' Digits )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:83:6: Integer '.' Digits
            {
            mInteger(); 
            match('.'); 
            mDigits(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Decimal"

    // $ANTLR start "Integer"
    public final void mInteger() throws RecognitionException {
        try {
            int _type = Integer;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:87:3: ( ( '-' )? Digits )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:87:6: ( '-' )? Digits
            {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:87:6: ( '-' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='-') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:87:6: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            mDigits(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Integer"

    // $ANTLR start "Digits"
    public final void mDigits() throws RecognitionException {
        try {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:92:3: ( ( '0' .. '9' )+ )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:92:6: ( '0' .. '9' )+
            {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:92:6: ( '0' .. '9' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:92:6: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "Digits"

    // $ANTLR start "Space"
    public final void mSpace() throws RecognitionException {
        try {
            int _type = Space;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:96:3: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:96:6: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            skip();

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Space"

    public void mTokens() throws RecognitionException {
        // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:8: ( T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | Rational | Decimal | Integer | Space )
        int alt3=14;
        alt3 = dfa3.predict(input);
        switch (alt3) {
            case 1 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:10: T__9
                {
                mT__9(); 

                }
                break;
            case 2 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:15: T__10
                {
                mT__10(); 

                }
                break;
            case 3 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:21: T__11
                {
                mT__11(); 

                }
                break;
            case 4 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:27: T__12
                {
                mT__12(); 

                }
                break;
            case 5 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:33: T__13
                {
                mT__13(); 

                }
                break;
            case 6 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:39: T__14
                {
                mT__14(); 

                }
                break;
            case 7 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:45: T__15
                {
                mT__15(); 

                }
                break;
            case 8 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:51: T__16
                {
                mT__16(); 

                }
                break;
            case 9 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:57: T__17
                {
                mT__17(); 

                }
                break;
            case 10 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:63: T__18
                {
                mT__18(); 

                }
                break;
            case 11 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:69: Rational
                {
                mRational(); 

                }
                break;
            case 12 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:78: Decimal
                {
                mDecimal(); 

                }
                break;
            case 13 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:86: Integer
                {
                mInteger(); 

                }
                break;
            case 14 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Line.g:1:94: Space
                {
                mSpace(); 

                }
                break;

        }

    }


    protected DFA3 dfa3 = new DFA3(this);
    static final String DFA3_eotS =
        "\5\uffff\1\15\4\uffff\1\16\6\uffff";
    static final String DFA3_eofS =
        "\21\uffff";
    static final String DFA3_minS =
        "\1\11\4\uffff\1\60\4\uffff\1\56\6\uffff";
    static final String DFA3_maxS =
        "\1\171\4\uffff\1\76\4\uffff\1\71\6\uffff";
    static final String DFA3_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\uffff\1\6\1\7\1\10\1\11\1\uffff\1\16"+
        "\1\5\1\12\1\15\1\13\1\14";
    static final String DFA3_specialS =
        "\21\uffff}>";
    static final String[] DFA3_transitionS = {
            "\2\13\2\uffff\1\13\22\uffff\1\13\7\uffff\1\2\1\4\1\10\1\11\1"+
            "\uffff\1\5\2\uffff\12\12\3\uffff\1\7\50\uffff\1\1\21\uffff\1"+
            "\3\1\6",
            "",
            "",
            "",
            "",
            "\12\12\4\uffff\1\14",
            "",
            "",
            "",
            "",
            "\1\20\1\17\12\12",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | Rational | Decimal | Integer | Space );";
        }
    }
 

}