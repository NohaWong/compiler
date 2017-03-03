package TypeCheck;

import Type.TVar;
import Type.Type;

/**
 * Equation résolue entre un type variable et un type
 * @author bizarda
 *
 */
public class ResolvedEq {
	private TVar t1;
	private Type t2;

	/**
	 * Constructeur
	 * @param t1 le type variable partie gauche de l'équation
	 * @param t2 le type partie droite de l'équation
	 */
	public ResolvedEq(TVar t1, Type t2) {
		this.t1 = t1;
		this.t2 = t2;
	}

	/**
	 * Getter
	 * @return le type variable partie gauche de l'équation
	 */
	public TVar getT1() {
		return t1;
	}
	
	/**
	 * Getter
	 * @return le type partie droite de l'équation
	 */
	public Type getT2() {
		return t2;
	}
	
	@Override
	public String toString(){
		return t1 + " = " + t2;
	}
}
