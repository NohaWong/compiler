package K_Normal_Expression;

import Code_fourni.Id;
import Type.Type;

public class KLet extends KExp {
	private final Id id;
	private final Type t;
	private final KExp e1;
	private final KExp e2;

	public KLet(Id id, Type t, KExp e1, KExp e2) {
		this.id = id;
		this.t = t;
		this.e1 = e1;
		this.e2 = e2;
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

	public Type getT() {
		return t;
	}

	public KExp getE1() {
		return e1;
	}

	public KExp getE2() {
		return e2;
	}
}
