package Expression;

import java.util.List;

import Code_fourni.ObjVisitor;
import Code_fourni.Visitor;

public class App extends Exp {
	private final Exp e;
	private final List<Exp> es;

	public App(Exp e, List<Exp> es) {
		this.e = e;
		this.es = es;
	}

	public <E> E accept(ObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Exp getE() {
		return e;
	}

	public List<Exp> getEs() {
		return es;
	}
}
