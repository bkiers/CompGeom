// $ANTLR 3.2 Sep 23, 2009 12:02:23 /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g 2010-05-01 17:56:30

package compgeom.util.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RationalLexer extends Lexer {
    public static final int Digits=4;
    public static final int EOF=-1;
    public static final int T__8=8;
    public static final int T__7=7;
    public static final int T__6=6;
    public static final int T__5=5;

    // delegates
    // delegators

    public RationalLexer() {;} 
    public RationalLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public RationalLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g"; }

    // $ANTLR start "T__5"
    public final void mT__5() throws RecognitionException {
        try {
            int _type = T__5;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:7:6: ( '.' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:7:8: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__5"

    // $ANTLR start "T__6"
    public final void mT__6() throws RecognitionException {
        try {
            int _type = T__6;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:8:6: ( '...' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:8:8: '...'
            {
            match("..."); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__6"

    // $ANTLR start "T__7"
    public final void mT__7() throws RecognitionException {
        try {
            int _type = T__7;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:9:6: ( '/' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:9:8: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__7"

    // $ANTLR start "T__8"
    public final void mT__8() throws RecognitionException {
        try {
            int _type = T__8;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:10:6: ( '-' )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:10:8: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__8"

    // $ANTLR start "Digits"
    public final void mDigits() throws RecognitionException {
        try {
            int _type = Digits;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:117:3: ( ( '0' .. '9' )+ )
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:117:6: ( '0' .. '9' )+
            {
            // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:117:6: ( '0' .. '9' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='0' && LA1_0<='9')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:117:6: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Digits"

    public void mTokens() throws RecognitionException {
        // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:1:8: ( T__5 | T__6 | T__7 | T__8 | Digits )
        int alt2=5;
        switch ( input.LA(1) ) {
        case '.':
            {
            int LA2_1 = input.LA(2);

            if ( (LA2_1=='.') ) {
                alt2=2;
            }
            else {
                alt2=1;}
            }
            break;
        case '/':
            {
            alt2=3;
            }
            break;
        case '-':
            {
            alt2=4;
            }
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            {
            alt2=5;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 2, 0, input);

            throw nvae;
        }

        switch (alt2) {
            case 1 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:1:10: T__5
                {
                mT__5(); 

                }
                break;
            case 2 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:1:15: T__6
                {
                mT__6(); 

                }
                break;
            case 3 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:1:20: T__7
                {
                mT__7(); 

                }
                break;
            case 4 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:1:25: T__8
                {
                mT__8(); 

                }
                break;
            case 5 :
                // /home/bart/Programming/IntelliJ/CompGeom/src/grammar/Rational.g:1:30: Digits
                {
                mDigits(); 

                }
                break;

        }

    }


 

}