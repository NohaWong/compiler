package Expression_asml;

import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;

public class FAdd_asml extends Exp_asml {
	private final Var_asml e1;
	private final Var_asml e2;

	public FAdd_asml(Var_asml e1, Var_asml e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	public <E> E accept(ObjVisitor_asml<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor_asml v) {
		v.visit(this);
	}

	public Exp_asml getE1() {
		return e1;
	}

	public Exp_asml getE2() {
		return e2;
	}
}
