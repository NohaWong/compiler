package Expression;

import Code_fourni.Id;
import Code_fourni.ObjVisitor;
import Code_fourni.Visitor;
import Type.Type;

public class Let extends Exp {
	private final Id id;
	private final Type t;
	private final Exp e1;
	private final Exp e2;

	public Let(Id id, Type t, Exp e1, Exp e2) {
		this.id = id;
		this.t = t;
		this.e1 = e1;
		this.e2 = e2;
	}

	public <E> E accept(ObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Id getId() {
		return id;
	}

	public Type getT() {
		return t;
	}

	public Exp getE1() {
		return e1;
	}

	public Exp getE2() {
		return e2;
	}
}
