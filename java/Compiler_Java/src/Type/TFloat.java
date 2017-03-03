package Type;

/**
 * Type réel
 * 
 */
public class TFloat extends Type {
	public String toString(){
		return "float";
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof TFloat);
	}

	@Override
	public Type clone() {
		return new TFloat();
	}
}