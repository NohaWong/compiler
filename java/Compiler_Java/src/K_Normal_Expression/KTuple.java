package K_Normal_Expression;

import java.util.List;

public class KTuple extends KExp {
	private final List<KVar> vs;

	public KTuple(List<KVar> vs) {
		this.vs = vs;
	}

	public <E> E accept(KObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(KVisitor v) {
		v.visit(this);
	}

	public List<KVar> getVs() {
		return vs;
	}
}
