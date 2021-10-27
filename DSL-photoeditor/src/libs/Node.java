package libs;


import ast.PhotoeditorVisitor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public abstract class Node {
    abstract public <T> T accept(PhotoeditorVisitor<T> v) throws IOException;
}