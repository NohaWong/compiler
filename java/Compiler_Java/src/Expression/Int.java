package Expression;

import Code_fourni.ObjVisitor;
import Code_fourni.Visitor;

public class Int extends Exp {
	private final int i;

	public Int(int i) {
		this.i = i;
	}

	public <E> E accept(ObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public int getI() {
		return i;
	}
}
