package K_Normal_Expression;

public class KInt extends KExp {
	private final int i;

	public KInt(int i) {
		this.i = i;
	}

	public <E> E accept(KObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(KVisitor v) {
		v.visit(this);
	}

	public int getI() {
		return i;
	}
}
