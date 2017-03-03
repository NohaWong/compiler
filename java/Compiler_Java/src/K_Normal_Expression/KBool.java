package K_Normal_Expression;

public class KBool extends KExp {
	private final boolean b;

	public KBool(boolean b) {
		this.b = b;
	}

	public <E> E accept(KObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(KVisitor v) {
		v.visit(this);
	}

	public boolean isB() {
		return b;
	}

}