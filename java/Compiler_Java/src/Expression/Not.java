package Expression;

import Code_fourni.ObjVisitor;
import Code_fourni.Visitor;

public class Not extends Exp {
	private final Exp e;

	public Not(Exp e) {
		this.e = e;
	}

	public <E> E accept(ObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Exp getE() {
		return e;
	}
}
