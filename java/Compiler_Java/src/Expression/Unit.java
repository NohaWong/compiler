package Expression;

import Code_fourni.ObjVisitor;
import Code_fourni.Visitor;

public class Unit extends Exp {
	public <E> E accept(ObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}
}