package ast;

import libs.Node;

import java.io.IOException;
import java.util.List;

public class Effect extends Node {
    private List<String> funcNameList;

    public Effect(List<String> funcNameList) {
        this.funcNameList = funcNameList;
    }

    public List<String> getFuncNameList() {
        return funcNameList;
    }

    public void setFuncName(String funcName) {
        this.funcNameList = funcNameList;
    }

    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}
