package Expression_asml;

import java.util.List;

import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;

public class AppC_asml extends Exp_asml {
	private final Var_asml e;
	private final List<Var_asml> es;

	public AppC_asml(Var_asml e, List<Var_asml> es) {
		this.e = e;
		this.es = es;
	}

	public <E> E accept(ObjVisitor_asml<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor_asml v) {
		v.visit(this);
	}

	public Var_asml getE() {
		return e;
	}

	public List<Var_asml> getEs() {
		return es;
	}
}
