package Type;

/**
 * Type unit
 * 
 */
public class TUnit extends Type {
	public String toString(){
		return "unit";
	}
	
	@Override
	public boolean equals(Object o){
		return (o instanceof TUnit);
	}

	@Override
	public Type clone() {
		return new TUnit();
	}
}