package ast;

import java.io.IOException;
import java.util.List;

public class Brightness extends Function{
    private String sign;
    private int num;

    public Brightness(String str) {
        String sign = str.substring(0,1); // sign = "+" or "-"
        this.sign = sign;
        int num = Integer.parseInt(str.substring(1)); // convert the "20" to int 20
        this.num = num;
    }

    public String getSign() {
        return sign;
    }

    public Integer getNum() {
        return num;
    }

    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}
