package Expression_asml;

import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;

public class Unit_asml extends Exp_asml {
	public <E> E accept(ObjVisitor_asml<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor_asml v) {
		v.visit(this);
	}
}