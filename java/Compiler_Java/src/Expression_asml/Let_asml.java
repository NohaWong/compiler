package Expression_asml;

import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;
import Code_fourni.Id;
import Type.Type;

public class Let_asml extends Exp_asml {
	private final Id id;
	private final Type t;
	private final Exp_asml e1;
	private final Exp_asml e2;

	public Let_asml(Id id, Type t, Exp_asml e1, Exp_asml e2) {
		this.id = id;
		this.t = t;
		this.e1 = e1;
		this.e2 = e2;
	}

	public <E> E accept(ObjVisitor_asml<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor_asml v) {
		v.visit(this);
	}

	public Id getId() {
		return id;
	}

	public Type getT() {
		return t;
	}

	public Exp_asml getE1() {
		return e1;
	}

	public Exp_asml getE2() {
		return e2;
	}
}
