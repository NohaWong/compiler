package Type;

/**
 * Type entier
 * 
 */
public class TInt extends Type {
	public String toString(){
		return "int";
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof TInt);
	}

	@Override
	public Type clone() {
		return new TInt();
	}
}
