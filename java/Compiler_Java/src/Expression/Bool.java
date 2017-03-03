package Expression;

import Code_fourni.ObjVisitor;
import Code_fourni.Visitor;

public class Bool extends Exp {
	private final boolean b;

	public Bool(boolean b) {
		this.b = b;
	}

	public <E> E accept(ObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public boolean isB() {
		return b;
	}

}