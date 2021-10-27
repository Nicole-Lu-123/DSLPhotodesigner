package ui;
import ast.PhotoeditorEvaluator;
import ast.PhotoeditorParser;
import ast.Program;
import libs.DoTokenizer;

import libs.Tokenizer;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {

        List<String> fixedLiteral1,fixedLiteral2,fixedLiteralsTotal;
        fixedLiteral1 = Arrays.asList("{", "}", ",", ":",",");
        fixedLiteral2 = Arrays.asList("CREATE","RUN","path","func","transparency","saturation","brightness",
                "rotate","flip","mosaic","var","range","size","position","full");
        fixedLiteralsTotal = new ArrayList<>();
        fixedLiteralsTotal.addAll (fixedLiteral1);
        fixedLiteralsTotal.addAll (fixedLiteral2);

        Tokenizer tokenizer = DoTokenizer.SimpleTokenizer("simpletransparency.tvar",fixedLiteralsTotal);
        System.out.println("Done tokenizing");

        PhotoeditorParser p = PhotoeditorParser.getParser(tokenizer);
        Program program = p.parseProgram();
        System.out.println("Done parsing");

        PhotoeditorEvaluator e = new PhotoeditorEvaluator();
        try {
            program.accept(e);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e2){
            JFrame errorjf = new JFrame("ERROR!");
            errorjf.setBounds(300,400,350,100);
            JPanel jp = new JPanel();
            JLabel error = new JLabel(e2.getMessage());
            jp.add(error);
            errorjf.add(jp);
            errorjf.setVisible(true);
        }
        //e.closeFile();
        System.out.println("completed successfully");

    }
}
