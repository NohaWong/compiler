package Expression_asml;

import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;

public class Int_asml extends Exp_asml {
	private final int i;

	public Int_asml(int i) {
		this.i = i;
	}

	public <E> E accept(ObjVisitor_asml<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor_asml v) {
		v.visit(this);
	}

	public int getI() {
		return i;
	}
}
