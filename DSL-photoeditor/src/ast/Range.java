package ast;

import libs.Node;

public abstract class Range extends Node {
    //    RANGE::= "range:" ("full" | RANGE')
//    RANGE' ::= "{" "size:" SIZE "position:" POSITION+ "}"
    public abstract String getName();
}
