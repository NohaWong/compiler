package Type;

public class TClosure extends Type{

	public boolean equals(Object o) {
		return (o instanceof TClosure);
	}

	@Override
	public Type clone() {
		return new TClosure();
	}

	@Override
	public String toString(){
		return "closure";
	}

}
