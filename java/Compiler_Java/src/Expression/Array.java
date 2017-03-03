package Expression;

import Code_fourni.ObjVisitor;
import Code_fourni.Visitor;

public class Array extends Exp {
	private final Exp e1;
	private final Exp e2;

	public Array(Exp e1, Exp e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	public <E> E accept(ObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Exp getE1() {
		return e1;
	}

	public Exp getE2() {
		return e2;
	}
	
}