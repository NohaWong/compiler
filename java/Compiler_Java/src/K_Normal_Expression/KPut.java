package K_Normal_Expression;

public class KPut extends KExp {
	private final KVar v1;
	private final KVar v2;
	private final KVar v3;

	public KPut(KVar v1, KVar v2, KVar v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	public <v> v accept(KObjVisitor<v> v) {
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

	public KVar getV3() {
		return v3;
	}
}