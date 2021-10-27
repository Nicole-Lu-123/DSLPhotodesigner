package ast;

import libs.Node;

import java.io.IOException;
import java.util.List;

public class Var extends Node {
    // VAR :: = "var" NAME "{" RANGE EFFECT+ "}"
    private String name;
    private Range range;
    private Effect funcName;

    public Var(String name, Range range, Effect effect) {
        this.name = name;
        this.range = range;
        this.funcName = effect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public Effect getFuncName() {
        return funcName;
    }

    public void setFuncName(Effect funcName) {
        this.funcName = funcName;
    }

    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}
