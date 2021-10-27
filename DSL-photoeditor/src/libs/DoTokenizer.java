package libs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DoTokenizer implements Tokenizer {
    
    // We refer to the tokenization code given in the lecture. After understanding the given code, we wrote it by ourselves


    public static Tokenizer SimpleTokenizer(String filename, List<String> fixedLiteralsList)
    {
        return new DoTokenizer(filename, fixedLiteralsList);
    }
    private static final String RESERVEDWORD = "_";

    private String inputProgram;
    private final List<String> fixedLiterals;
    private String[] tokens;
    private int current = 0;

    private DoTokenizer(String filename, List<String> fixedLiteralsList){
        fixedLiterals = fixedLiteralsList;
        try {
            inputProgram = Files.readString(Paths.get(filename));
        } catch (IOException e) {
            System.out.println("Didn't find file");
            System.exit(0);
        }
        tokenize();
    }


    // We refer to the tokenization code given in the lecture. After understanding the given code, we wrote it by ourselves
    //purpose: return a list of tokens(parsable items).
    private void tokenize (){
        // Use "_"  as seperator, not want \n,{,}
        String simpletokens = inputProgram.replace("\n", "");
        System.out.println(simpletokens);   //check original

        //seperate each two items with “_”, except the first one
        for(String fl : fixedLiterals) {
            simpletokens = simpletokens.replace(fl, RESERVEDWORD+fl+RESERVEDWORD);
        }
        if(simpletokens.length() > 0 && simpletokens.startsWith(RESERVEDWORD)) {
            simpletokens = simpletokens.substring(1); // without first character
        }
        System.out.println(simpletokens);   //check "_" but not first

        //check if there are two "_",if so, replace by only one
        simpletokens = simpletokens.replace(RESERVEDWORD+RESERVEDWORD,RESERVEDWORD);
        System.out.println(simpletokens);

//        simpletokens = simpletokens.split("");

        //split
        tokens = simpletokens.split(RESERVEDWORD);
        System.out.println(Arrays.asList(tokens)); // check 2 connected "_" (Array.aslist(): list to String but as a list)

        //remove space around every charactor of every token(leading and ending)
        int n = tokens.length;
        for (int i = 0; i < n; i++) {
            tokens[i] = tokens[i].trim();
        }

        System.out.println(Arrays.asList(tokens)); // check space
    }

    // We refer to the tokenization code given in the lecture. After understanding the given code, we wrote it by ourselves
    // check only, not counter
    @Override
    public String checkNext(){
        String next = "";  // why it is grey? what's wrong????
        int n = tokens.length;
        if (current < n){
            next = tokens[current];
        } else {
            next = "NO_MORE_TOKENS";
        }
        return next;
    }
    // check and counter
    @Override
    public String getNext(){
        String next = "";  // same as line70
        int n = tokens.length;
        if (current < n){
            next = tokens[current];
            current++;
        } else {
            next = "NULL";
        }
        return next;
    }

    @Override
    public boolean checkLast(String regexp) {
        String last;
        int n = tokens.length;
        last = tokens[n-1]; // last one is length-1
        return (last.matches(regexp));
    }


    // bool, only determine if next match type
    @Override
    public boolean checkToken(String regexp){
        String next = checkNext();
        System.out.println("comparing:"+next+"to"+regexp);// check, delete it later
        return (next.matches(regexp));
    }

    // check and get it, return next as a string
    @Override
    public String getAndCheckNext(String regexp){
        String next = getNext();
        if (!next.matches(regexp)) {
            throw new RuntimeException("Expected something matching: " + regexp + " but got: " + next);
        }
        System.out.println("matched: "+next+"  to  "+regexp); // check, delete it later
        return next;
    }

    @Override
    public boolean moreTokens(){
        return current <tokens.length;
    }

    // return true if the remaining tokens have regexp
    public boolean hasTokenFromCurrent(String regexp){
        String next = "";
        int tempCurrent = current;
        int n = tokens.length;
        while(tempCurrent < n){
            next = tokens[tempCurrent];
            if (next.matches(regexp)) {
                return true;
            }
            tempCurrent++;
        }
        return false;
    }

}
