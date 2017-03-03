package Expression_asml;

import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;

public class Bool_asml extends Exp_asml {
	private final boolean b;

	public Bool_asml(boolean b) {
		this.b = b;
	}

	public <E> E accept(ObjVisitor_asml<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor_asml v) {
		v.visit(this);
	}

	public boolean isB() {
		return b;
	}

}