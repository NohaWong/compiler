package K_Normal_Expression;

import java.util.List;

public class KApp extends KExp {
	private final KVar v;
	private final List<KVar> vs;

	public KApp(KVar v, List<KVar> vs) {
		this.v = v;
		this.vs = vs;
	}

	public <E> E accept(KObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(KVisitor v) {
		v.visit(this);
	}

	public KVar getV() {
		return v;
	}

	public List<KVar> getVs() {
		return vs;
	}
}
