package K_Normal_Expression;

import Code_fourni.Id;

public class KVar extends KExp {
	private final Id id;

	public KVar(Id id) {
		this.id = id;
	}

	public <E> E accept(KObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(KVisitor v) {
		v.visit(this);
	}

	public Id getId() {
		return id;
	}
}