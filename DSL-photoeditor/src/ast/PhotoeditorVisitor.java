package ast;

import java.io.IOException;

public interface PhotoeditorVisitor<T> {
    T visit(Create c) throws IOException;
    T visit(Run r) throws IOException;
    T visit(Brightness b);
    T visit(Effect e) throws IOException;
    T visit(Flip f);
    T visit(Func f);
    T visit(Mosaic f);
    T visit(Path p) throws IOException;
    T visit(Position p);
    T visit(Program p) throws IOException;
    T visit(RangeSize s) throws IOException;
    T visit(RangeText t);
    T visit(Rotate r) throws IOException;
    T visit(Saturation s);
    T visit(Size s);
    T visit(Transparency t);
    T visit(Var v) throws IOException;
}
