package ast;

import java.io.IOException;

public class RangeSize extends Range {
    private Size size;
    private Position position;

    public RangeSize(Size size, Position pos) {
        this.size = size;
        this.position = pos;
    }


    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String getName(){
        return size.toString() + position.toString();
    }

    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}
