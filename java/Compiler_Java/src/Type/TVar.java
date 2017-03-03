package Type;

/**
 * Type variable
 * 
 */
public class TVar extends Type {
    String v;
    
    /**
     * Constructeur
     * @param v le nom du type variable
     */
    public TVar(String v) {
        this.v = v;
    }
    
    /**
     * Constructeur de copie
     * @param tv la TVar à copier
     */
    public TVar(TVar tv){
    	this.v = tv.v;
    }
    
    @Override
    public String toString() {
        return v; 
    }
    @Override
    public boolean equals(Object o){
    	if(o instanceof TVar)
    		return this.v.equals(((TVar)o).v);
    	else return false;
    }
	@Override
	public Type clone() {
		return new TVar(v);
	}
}
