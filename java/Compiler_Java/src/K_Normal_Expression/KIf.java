package K_Normal_Expression;

public class KIf extends KExp {
	private final KExp e1;
	private final KExp e2;
	private final KExp e3;

	public KIf(KExp e1, KExp e2, KExp e3) {
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
	}

	public <E> E accept(KObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(KVisitor v) {
		v.visit(this);
	}

	public KExp getE1() {
		return e1;
	}

	public KExp getE2() {
		return e2;
	}

	public KExp getE3() {
		return e3;
	}
	
}
