package ast;

import java.io.IOException;

public class Flip extends Function{
    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}
