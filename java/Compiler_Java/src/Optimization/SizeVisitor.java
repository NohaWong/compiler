package Optimization;
import K_Normal_Expression.*;

/**
 * Classe de détermination de la taille d'une expression K-normale
 * @author bizarda
 *
 */
class SizeVisitor implements KObjVisitor<Integer> {

	/**
	 * Calcule la taille d'une expression K-normale
	 * @param e l'expression K-normale à mesurer
	 * @return la taille de e, en nombre de K-noeuds
	 */
	public int Start(KExp e){
		return e.accept(this);
	}
	
	/**
	 * 
	 */
	public Integer visit(KUnit e) {
		return 0;
	}

	/**
	 * 
	 */
	public Integer visit(KBool e) {
		return 0;
	}

	/**
	 * 
	 */
	public Integer visit(KInt e) {
		return 0;
	}

	/**
	 * 
	 */
	public Integer visit(KFloat e) {
		return 0;
	}

	/**
	 * 
	 */
	public Integer visit(KNot e) {
		return 1 + e.getV().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KNeg e) {
		return 1 + e.getV().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KAdd e) {
		return 1 + e.getV1().accept(this) + e.getV2().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KSub e) {
		return 1 + e.getV1().accept(this) + e.getV2().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KFNeg e) {
		return 1 + e.getV().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KFAdd e) {
		return 1 + e.getV1().accept(this) + e.getV2().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KFSub e) {
		return 1 + e.getV1().accept(this) + e.getV2().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KFMul e) {
		return 1 + e.getV1().accept(this) + e.getV2().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KFDiv e) {
		return 1 + e.getV1().accept(this) + e.getV2().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KEq e) {
		return 1 + e.getV1().accept(this) + e.getV2().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KLE e) {
		return 1 + e.getV1().accept(this) + e.getV2().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KIf e) {
		return 1 + e.getE1().accept(this) + e.getE2().accept(this) + e.getE3().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KLet e) {
		return 1 + e.getE1().accept(this) + e.getE2().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KVar e) {
		return 0;
	}

	/**
	 * 
	 */
	public Integer visit(KLetRec e) {
		return 1 + e.getFd().getE().accept(this) + e.getE().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KApp e) {
		int res = 1 + e.getV().accept(this);
		for(KVar arg : e.getVs())
			res += arg.accept(this);
		return res;
	}

	/**
	 * 
	 */
	public Integer visit(KTuple e) {
		int res = 1;
		for(KVar arg : e.getVs())
			res += arg.accept(this);
		return res;
	}

	/**
	 * 
	 */
	public Integer visit(KLetTuple e) {
		return 1 + e.getE1().accept(this) + e.getE2().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KArray e) {
		return 1 + e.getV1().accept(this) + e.getV2().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KGet e) {
		return 1 + e.getV1().accept(this) + e.getV2().accept(this);
	}

	/**
	 * 
	 */
	public Integer visit(KPut e) {
		return 1 + e.getV1().accept(this) + e.getV2().accept(this) + e.getV3().accept(this);
	}
}
