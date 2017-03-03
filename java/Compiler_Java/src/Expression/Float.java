package Expression;

import Code_fourni.ObjVisitor;
import Code_fourni.Visitor;

public class Float extends Exp {
	private float f;

	public Float(float f) {
		this.f = f;
	}

	public <E> E accept(ObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public float getF() {
		return f;
	}

	public void setF(float f) {
		this.f = f;
	}
}