package K_Normal_Expression;

public class KFloat extends KExp {
	private float f;

	public KFloat(float f) {
		this.f = f;
	}

	public <E> E accept(KObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(KVisitor v) {
		v.visit(this);
	}

	public float getF() {
		return f;
	}

	public void setF(float f) {
		this.f = f;
	}
}