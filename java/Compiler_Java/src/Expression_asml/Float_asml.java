package Expression_asml;

import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;

public class Float_asml extends Exp_asml {
	private float f;

	public Float_asml(float f) {
		this.f = f;
	}

	public <E> E accept(ObjVisitor_asml<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor_asml v) {
		v.visit(this);
	}

	public float getF() {
		return f;
	}

	public void setF(float f) {
		this.f = f;
	}
}