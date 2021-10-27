package ast;

import libs.Node;

import java.io.IOException;
import java.util.List;

public class Create extends Node {
    //"CREATE" NAME "{" PATH FUNC+ VAR+ "}"
    private String name;
    private Path path;
    private List<Func> funcs;
    private List<Var> vars;

    public Create(String name, Path path, List<Func> funcs, List<Var> vars){
        this.name = name;
        this.path = path;
        this.funcs= funcs;
        this.vars = vars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public List<Func> getFuncs() {
        return funcs;
    }

    public void setFuncs(List<Func> funcs) {
        this.funcs = funcs;
    }

    public List<Var> getVars() {
        return vars;
    }

    public void setVars(List<Var> vars) {
        this.vars = vars;
    }

    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}
