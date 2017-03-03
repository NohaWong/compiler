package Expression_asml;

import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;
import Code_fourni.Id;
import Type.Type;

public class Let_memory_store_asml extends Exp_asml {
	private final Id id;
	private final Type t;
	private final Var_asml addr;
	private final Int_asml offset;
	private final Var_asml val;
	private final Exp_asml e2;

	public Let_memory_store_asml(Id id, Type t, Var_asml addr, Int_asml offset, Var_asml val, Exp_asml e2) {
		this.id = id;
		this.t = t;
		this.addr = addr;
		this.offset = offset;
		this.val = val;
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

	public Var_asml getAddr() {
		return addr;
	}

	public Int_asml getOffset() {
		return offset;
	}

	public Var_asml getVal() {
		return val;
	}
}
