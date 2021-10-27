package ast;

import libs.Node;

import java.io.IOException;

public class Run extends Node {
    public Run() {
    }
    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}
