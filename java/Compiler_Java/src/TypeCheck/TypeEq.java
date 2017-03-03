package TypeCheck;

import Type.Type;

/**
 * Equation entre deux types
 * @author bizarda
 *
 */
public class TypeEq {
	private Type t1;
	private Type t2;
	
	/**
	 * Constructeur
	 * @param t1 le type partie gauche de l'équation
	 * @param t2 le type partie droite de l'équation
	 */
	public TypeEq(Type t1, Type t2) {
		this.t1 = t1;
		this.t2 = t2;
	}
	
	/**
	 * Getter
	 * @return le type partie gauche de l'équation
	 */
	public Type getT1() {
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
