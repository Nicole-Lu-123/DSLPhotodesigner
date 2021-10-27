
package ast;

import libs.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class PhotoeditorParser {
    private static final String NAME = "[a-zA-Z][A-Za-z0-9]*";
    private static final String NUM = "[0-9]+";
    private static final String FUNCNAME = "[a-zA-Z0-9]+";
    private static final String STRING = "[^:{}]+"; // exclude ":" "{" "}"
    private static final String SIGN = "[+|-]";
    private static final String FULL = "full";

    private final Tokenizer tokenizer;


    public static PhotoeditorParser getParser(Tokenizer tokenizer) {
        return new PhotoeditorParser(tokenizer);
    }

    private PhotoeditorParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    //  PROGRAM :: = CREATE RUN
    public Program parseProgram() {
        Create create = parseCreate();
        Run run = parseRun();
        return new Program(create, run);
    }
   // RUN
    private Run parseRun() {
        tokenizer.checkLast("RUN");
        return new Run();
    }

    // CREATE ::= "CREATE" NAME "{" PATH FUNC+ VAR+ "}"
    private Create parseCreate() {
        tokenizer.getAndCheckNext("CREATE");
        String name = tokenizer.getAndCheckNext(NAME);
        tokenizer.getAndCheckNext("^[{]");
        Path path = parsePath();
        List<Func> funcs = new ArrayList<>();
        while (tokenizer.hasTokenFromCurrent("func")) {
            funcs.add(parseFunc());
        }
        List<Var> vars = new ArrayList<>();
        while (tokenizer.hasTokenFromCurrent("var")) {
            vars.add(parseVar());
        }
        tokenizer.getAndCheckNext("}");
        return new Create(name, path, funcs, vars);
    }

    // PATH :: = "path:" STRING
    private Path parsePath() {
        tokenizer.getAndCheckNext("path");
        tokenizer.getAndCheckNext(":");
        String path = tokenizer.getAndCheckNext(STRING);
        return new Path(path);
    }

    // FUNC ::= "func" FUNCNAME { FUNCTIONS+}
    private Func parseFunc() {
        tokenizer.getAndCheckNext("func");
        String name = tokenizer.getAndCheckNext(FUNCNAME);
        tokenizer.getAndCheckNext("^[{]");
        List<Function> functions = new ArrayList<>();
        while (!tokenizer.checkToken("}")) { // condition not correct
            functions.add(parseFunction());
        }
        tokenizer.getAndCheckNext("}");
        return new Func(name, functions); // name is FUNCNAME, functions is a list of Function
    }

    //FUNCTIONS::= TRANSPARENCY | SATURATION | BRIGHTNESS| ROTATE | FLIP |MOSAIC
    private Function parseFunction() {
        List<Func> functions = new ArrayList<>();
        if (tokenizer.checkToken("transparency")) {
            return parseTransparency();
        } else if (tokenizer.checkToken("saturation")) {
            return parseSaturation();
        } else if (tokenizer.checkToken("brightness")) {
            return parseBrightness();
        } else if (tokenizer.checkToken("rotate")) {
            return parseRotate();

        } else if (tokenizer.checkToken("mosaic")) {
            return parseMosaic();

        } else if (tokenizer.checkToken("flip")) {
            return parseFlip();
        } else {
            throw new RuntimeException("Unknown instrution:" + tokenizer.getNext());
        }
    }

    // TRANSPARENCY:: = "transparency" ":" NUM
    private Transparency parseTransparency() {
        tokenizer.getAndCheckNext("transparency");
        tokenizer.getAndCheckNext(":");
        String number = tokenizer.getAndCheckNext(NUM);
        return new Transparency(number);
    }

    // SATURATION ::= "saturation" ":" SIGN-NUM
    private Saturation parseSaturation() {
        tokenizer.getAndCheckNext("saturation");
        tokenizer.getAndCheckNext(":");
        String str = tokenizer.getAndCheckNext(SIGN+NUM);
        if (str.contains("+") | str.contains("-")) {
            String s = str;
            String s1 = s.substring(1, str.length());
            if (s1.matches(NUM)) {
                return new Saturation(str); // str = "+20"
            }
        }
        return null;
    }

    // BRIGHTNESS ::= "brightness" ":" SIGN NUM
    private Brightness parseBrightness() {
        tokenizer.getAndCheckNext("brightness");
        tokenizer.getAndCheckNext(":");
        String str = tokenizer.getAndCheckNext(SIGN+NUM);
        if (str.contains("+") | str.contains("-")) {
            String s = str;
            String s1 = s.substring(1, str.length());
            if (s1.matches(NUM)) {
                return new Brightness(str);
            }
        }
        return null;
    }

    //ROTATE ::= "rotate:" DIRECTION "," NUM
    private Rotate parseRotate() {
        tokenizer.getAndCheckNext("rotate");
        tokenizer.getAndCheckNext(":");
        String str = tokenizer.getAndCheckNext("[a-zA-Z]+");
        if (str.equals("counterclockwise") | str.equals("clockwise")) {
            tokenizer.getAndCheckNext(",");
            String num = tokenizer.getAndCheckNext(NUM);
            return new Rotate(str, num);
        }
        return null;
    }

    // MOSAIC ::= "mosaic"
    private Mosaic parseMosaic() {
        tokenizer.getAndCheckNext("mosaic");
        return new Mosaic();
    }

    // FLIP ::= "flip"
    private Flip parseFlip() {
        tokenizer.getAndCheckNext("flip");
        return new Flip();
    }

    // VAR :: = "var" NAME "{" RANGE EFFECT "}"
    private Var parseVar() {
        tokenizer.getAndCheckNext("var");
        String name = tokenizer.getAndCheckNext(NAME);
        tokenizer.getAndCheckNext("^[{]");
        Range range = parseRange();
        Effect effect = parseEffect();
        tokenizer.getAndCheckNext("}");
        return new Var(name, range, effect);
    }

    //    RANGE::= "range:" ("full" | RANGE')
//    RANGE' ::= "{" "size:" SIZE "position:" POSITION "}"
//    EFFECT ::= "effect" ":" FUNCNAME
    private Range parseRange() {
        tokenizer.getAndCheckNext("range");
        tokenizer.getAndCheckNext(":");
        tokenizer.getAndCheckNext("");
////        tokenizer.getNext();
//        if (tokenizer.checkToken("full")) {
//            return new RangeText("full");
//        } else {
//            tokenizer.getAndCheckNext("{");
//            Size size = parseSize();
//            Position position = parsePosition();
//            tokenizer.getAndCheckNext("}");
//            return new RangeSize(size, position);
//        }
//        tokenizer.getNext();
        String r = tokenizer.getNext();
        if (r.contains("full")) {
            return new RangeText("full");
        } else {
//            tokenizer.getAndCheckNext("{");
            Size size = parseSize();
            Position position = parsePosition();
            tokenizer.getAndCheckNext("}");
            return new RangeSize(size, position);
        }


    }



    // size: 50,50
    private Size parseSize() {
        tokenizer.getAndCheckNext("size");
        tokenizer.getAndCheckNext(":");
        int x = Integer.parseInt(tokenizer.getAndCheckNext(NUM));
        tokenizer.getAndCheckNext(",");
        int y = Integer.parseInt(tokenizer.getAndCheckNext(NUM));
        return new Size(x, y);
    }

    // "position:" 100, 50
    private Position parsePosition() {
        tokenizer.getAndCheckNext("position");
        tokenizer.getAndCheckNext(":");
        int x = Integer.parseInt(tokenizer.getAndCheckNext(NUM));
        tokenizer.getAndCheckNext(",");
        int y = Integer.parseInt(tokenizer.getAndCheckNext(NUM));
        return new Position(x, y);
    }

    // effect: effect1,effect2
    private Effect parseEffect() {
        tokenizer.getAndCheckNext("effect");
        tokenizer.getAndCheckNext(":");
        List<String> effectList = new ArrayList<>();
        while (!tokenizer.checkToken("}")){
            String funcName = tokenizer.getAndCheckNext(NAME);
            effectList.add(funcName);
            if(!tokenizer.checkToken("}")) {
                tokenizer.getAndCheckNext(",");
            }
        }
        return new Effect(effectList);
    }

}