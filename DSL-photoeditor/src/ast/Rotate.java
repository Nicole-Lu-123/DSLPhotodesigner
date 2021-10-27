package ast;

import java.io.IOException;

public class Rotate extends Function{
    private String direction;
    private int num; // times of rotation

    public Rotate(String str, String num){
        this.direction = str;
        this.num = Integer.parseInt(num);
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}
