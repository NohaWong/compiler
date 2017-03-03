package Expression_asml;

import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;
import Code_fourni.Id;
import Type.Type;

public class Let_memory_alloc_asml extends Exp_asml {
	private final Id id;
	private final Type t;
	private final Int_asml size;
	private final Exp_asml e2;

	public Let_memory_alloc_asml(Id id, Type t, Int_asml size, Exp_asml e2) {
		this.id = id;
		this.t = t;
		this.size = size;
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

	public Exp_asml getE2() {
		return e2;
	}

	public Int_asml getOffset() {
		return size;
	}
}
