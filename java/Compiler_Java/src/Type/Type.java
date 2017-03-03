package Type;

/**
 * Classe abstraite des Types
 * 
 */
public abstract class Type {
    private static int x = 0;
    
	/**
	 * Génère un nouveau Type (avec un nouveau nom)
	 * @return le Type généré
	 */
    public static Type gen() {
        return new TVar("?" + x++);
    }

    @Override
    public abstract boolean equals(Object o);
    
    @Override
    public abstract Type clone();
    
    @Override
    public abstract String toString();
    
    /**
     * Détermine si le Type donné est un type constant (TInt, TBool, TFloat, ou TUnit)
     * @param t le Type à tester
     * @return vrai ssi t est TInt, TBool, TFloat, ou TUnit
     */
    public static boolean isConstantType(Type t){
		return (t instanceof TInt) || (t instanceof TBool) || (t instanceof TFloat) || (t instanceof TUnit);
	}
}