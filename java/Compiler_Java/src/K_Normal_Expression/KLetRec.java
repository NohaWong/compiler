package K_Normal_Expression;

public class KLetRec extends KExp {
	private final KFunDef fd;
	private final KExp e;

	public KLetRec(KFunDef fd, KExp e) {
		this.fd = fd;
		this.e = e;
	}

	public <E> E accept(KObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(KVisitor v) {
		v.visit(this);
	}

	public KFunDef getFd() {
		return fd;
	}

	public KExp getE() {
		return e;
	}
}