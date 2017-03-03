package Type;

/**
 * Type tableau
 * 
 */
public class TArray extends Type {
	private Type type;
	
	/**
	 * Constructeur par défaut
	 */
	public TArray(){
		this.type = Type.gen();
	}
	
	/**
	 * Constructeur d'un tableau d'éléments d'un certain type
	 * @param type le type des éléments du tableau
	 */
	public TArray(Type type){
		this.type = type;
	}
	
	@Override
	public String toString(){
		return "array of (" + this.type + ")";
	}

	/**
	 * Getter
	 * @return le type des éléments du tableau
	 */
	public Type getType() {
		return type;
	}

	@Override
	public boolean equals(Object o) {
    	if(!(o instanceof TArray))
    		return false;
    	else
    		return this.type.equals(((TArray)o).type);
	}

	@Override
	public Type clone() {
		return new TArray(type.clone());
	}
}