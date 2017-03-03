package Expression;

import Code_fourni.Id;
import Code_fourni.ObjVisitor;
import Code_fourni.Visitor;

public class Var extends Exp {
	private final Id id;

	public Var(Id id) {
		this.id = id;
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
}