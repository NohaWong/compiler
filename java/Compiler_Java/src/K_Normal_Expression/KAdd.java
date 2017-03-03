package K_Normal_Expression;

public class KAdd extends KExp {
	private final KVar v1;
	private final KVar v2;

	public KAdd(KVar v1, KVar v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	public <E> E accept(KObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(KVisitor v) {
		v.visit(this);
	}

	public KVar getV1() {
		return v1;
	}

	public KVar getV2() {
		return v2;
	}
}
