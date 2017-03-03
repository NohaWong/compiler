package Expression_asml;

import Asml.ObjVisitor_asml;
import Asml.Visitor_asml;

public abstract class Exp_asml {
	public abstract void accept(Visitor_asml v);
	

	public abstract <E> E accept(ObjVisitor_asml<E> v);
	
	
	/**
	 * Détermine si une Exp est une constante (Int, Float, Bool ou Unit)
	 * @param e l'Exp à tester
	 * @return vrai ssi e est Int, Float, Bool ou  Unit
	 */
	public static boolean isConstant(Exp_asml e){
		return (e instanceof Int_asml) || (e instanceof Float_asml) || (e instanceof Bool_asml) || (e instanceof Unit_asml);
	}
}
