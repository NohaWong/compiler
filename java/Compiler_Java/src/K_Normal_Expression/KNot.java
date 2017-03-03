package K_Normal_Expression;

public class KNot extends KExp {
	private final KVar e;

	public KNot(KVar e) {
		this.e = e;
	}

	public <E> E accept(KObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(KVisitor v) {
		v.visit(this);
	}

	public KVar getV() {
		return e;
	}
}
