package Expression_asml;

import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;

public class Neg_asml extends Exp_asml {
	private final Var_asml e;

	public Neg_asml(Var_asml e) {
		this.e = e;
	}

	public <E> E accept(ObjVisitor_asml<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor_asml v) {
		v.visit(this);
	}

	public Exp_asml getE() {
		return e;
	}
}
