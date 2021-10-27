package ast;

import libs.Node;

import java.io.IOException;

public class Size extends Node {
    //SIZE ::= NUM "," NUM
    private int width;
    private int height;

    public Size(int x, int y) {
        this.width = x;
        this.height = y;
    }

    public int getWidth() {

        return width;
    }

    public void setWidth(int x) {
        this.width = x;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int y) {
        this.height = y;
    }

    @Override
    public String toString() {
        return "Size{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}
