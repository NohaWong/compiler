package Expression_asml;

import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;

public class LetRec_asml extends Exp_asml {
	private final FunDef_asml fd;
	private final Exp_asml e;

	public LetRec_asml(FunDef_asml fd, Exp_asml e) {
		this.fd = fd;
		this.e = e;
	}
	

	public <E> E accept(ObjVisitor_asml<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor_asml v) {
		v.visit(this);
	}

	public FunDef_asml getFd() {
		return fd;
	}

	public Exp_asml getE() {
		return e;
	}

}