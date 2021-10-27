package ast;

import libs.Node;

import java.io.IOException;
import java.util.List;

public class Func extends Node {
//    func effect1 {
//        transparency: 50
//        saturation: +50
//        brightness: +90
//        rotate: counterclockwise,  2
//        filp
    //    Mosaic
//    }
    private String name;
    private List<Function> functions;

    public Func(String name, List<Function> functions) {
        this.name = name;
        this.functions = functions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Function> getFunctions(){
        return functions;
    }

    public void setFunctions(List<Function> listOfFunction) {
        this.functions = listOfFunction;
    }

    @Override
    public <T> T accept(PhotoeditorVisitor<T> v) throws IOException {
        return v.visit(this);
    }
}
