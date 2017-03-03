package Expression;

import java.util.List;

import Code_fourni.ObjVisitor;
import Code_fourni.Visitor;

public class Tuple extends Exp {
	private final List<Exp> es;

	public Tuple(List<Exp> es) {
		this.es = es;
	}

	public <E> E accept(ObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public List<Exp> getEs() {
		return es;
	}
}
