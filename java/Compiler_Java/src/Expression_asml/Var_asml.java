package Expression_asml;

import Type.TUnit;
import Type.Type;
import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;
import Code_fourni.Id;

public class Var_asml extends Exp_asml {
	private final Id id;
	private final Type t;

	public Var_asml(Id id, Type t) {
		this.id = id;
		this.t = t;
	}

	public Var_asml(Id id) {
		this.id = id;
		this.t = new TUnit();
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
}