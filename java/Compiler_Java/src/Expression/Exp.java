package Expression;

import Code_fourni.ObjVisitor;
import Code_fourni.Visitor;

public abstract class Exp {
	public abstract void accept(Visitor v);

	public abstract <E> E accept(ObjVisitor<E> v);
	
	/**
	 * Détermine si une Exp est une constante (Int, Float, Bool ou Unit)
	 * @param e l'Exp à tester
	 * @return vrai ssi e est Int, Float, Bool ou  Unit
	 */
	public static boolean isConstant(Exp e){
		return (e instanceof Int) || (e instanceof Float) || (e instanceof Bool) || (e instanceof Unit);
	}
}
