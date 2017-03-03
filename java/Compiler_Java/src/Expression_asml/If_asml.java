package Expression_asml;

import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;

public class If_asml extends Exp_asml {
	private final Exp_asml e1;
	private final Exp_asml e2;
	private final Exp_asml e3;

	public If_asml(Exp_asml e1, Exp_asml e2, Exp_asml e3) {
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
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

	public Exp_asml getE3() {
		return e3;
	}
	
}
