package K_Normal_Expression;

public class KSub extends KExp {
	private final KVar e1;
	private final KVar e2;

	public KSub(KVar e1, KVar e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	public <E> E accept(KObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(KVisitor v) {
		v.visit(this);
	}

	public KVar getV1() {
		return e1;
	}

	public KVar getV2() {
		return e2;
	}
}