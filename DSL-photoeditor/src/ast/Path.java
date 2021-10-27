package ast;

import libs.Node;

import java.io.IOException;

public class Path extends Node {
    // PATH :: = "path:" STRING
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Path(String path) {
        this.path = path;
    }

    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}
