package Expression;

import Code_fourni.ObjVisitor;
import Code_fourni.Visitor;

public class LetRec extends Exp {
	private final FunDef fd;
	private final Exp e;

	public LetRec(FunDef fd, Exp e) {
		this.fd = fd;
		this.e = e;
	}

	public <E> E accept(ObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public FunDef getFd() {
		return fd;
	}

	public Exp getE() {
		return e;
	}
}