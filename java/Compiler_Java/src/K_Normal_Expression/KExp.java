package K_Normal_Expression;

public abstract class KExp {
	public abstract void accept(KVisitor v);

	public abstract <E> E accept(KObjVisitor<E> v);
	
	/**
	 * Détermine si une Exp est une constante (Int, Float, Bool ou Unit ou Tuple)
	 * @param e l'Exp à tester
	 * @return vrai ssi e est Int, Float, Bool ou  Unit ou Tuple
	 */
	public static boolean isConstant(KExp e){
		return (e instanceof KInt) || (e instanceof KFloat) || (e instanceof KBool) || (e instanceof KUnit) || (e instanceof KTuple);
	}
}
