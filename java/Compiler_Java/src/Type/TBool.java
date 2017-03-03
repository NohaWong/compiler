package Type;

/**
 * Type booléen
 *
 */
public class TBool extends Type {
	public String toString(){
		return "bool";
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof TBool);
	}

	@Override
	public Type clone() {
		return new TBool();
	}
}
