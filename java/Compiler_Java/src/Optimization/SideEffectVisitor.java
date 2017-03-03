package Optimization;

import K_Normal_Expression.*;

/**
 * Classe de détermination des effets de bord d'une expression
 * @author bizarda
 *
 */
class SideEffectVisitor implements KVisitor{

	private boolean has_side_effect;
	
	/**
	 * Constructeur par défaut
	 */
	public SideEffectVisitor() {
		has_side_effect = false;
	}
	
	/**
	 * Détermine si une expression a des effets de bord
	 * @param AST l'expression à tester
	 * @return vrai ssi AST a des effets de bord
	 */
	public boolean Start(KExp AST){
		AST.accept(this);
		return has_side_effect;
	}
	
	/**
	 * 
	 */
	public void visit(KUnit e) {
	}
	
	/**
	 * 
	 */
	public void visit(KBool e) {
	}
	
	/**
	 * 
	 */
	public void visit(KInt e) {
	}
	
	/**
	 * 
	 */
	public void visit(KFloat e) {
	}
	
	/**
	 * 
	 */
	public void visit(KNot e) {
		if(!has_side_effect)
			e.getV().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KNeg e) {
		if(!has_side_effect)
			e.getV().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KAdd e) {
		if(!has_side_effect){
			e.getV1().accept(this);
			e.getV2().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KSub e) {
		if(!has_side_effect){
			e.getV1().accept(this);
			e.getV2().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KFNeg e) {
		if(!has_side_effect)
			e.getV().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KFAdd e) {
		if(!has_side_effect){
			e.getV1().accept(this);
			e.getV2().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KFSub e) {
		if(!has_side_effect){
			e.getV1().accept(this);
			e.getV2().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KFMul e) {
		if(!has_side_effect){
			e.getV1().accept(this);
			e.getV2().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KFDiv e) {
		if(!has_side_effect){
			e.getV1().accept(this);
			e.getV2().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KEq e) {
		if(!has_side_effect){
			e.getV1().accept(this);
			e.getV2().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KLE e) {
		if(!has_side_effect){
			e.getV1().accept(this);
			e.getV2().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KIf e) {
		if(!has_side_effect){
			e.getE1().accept(this);
			e.getE2().accept(this);
			e.getE3().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KLet e) {
		if(!has_side_effect){
			e.getE1().accept(this);
			e.getE2().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KVar e) {
	}
	
	/**
	 * 
	 */
	public void visit(KLetRec e) {
		if(!has_side_effect){
			e.getFd().getE().accept(this);
			e.getE().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KApp e) {
		// Un appel de fonction est considéré comme un effet de bord
		has_side_effect = true;
	}
	
	/**
	 * 
	 */
	public void visit(KTuple e) {
		if(!has_side_effect)
			for(KVar elem : e.getVs())
				elem.accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KLetTuple e) {
		if(!has_side_effect){
			e.getE1().accept(this);
			e.getE2().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KArray e) {
		if(!has_side_effect){
			e.getV1().accept(this);
			e.getV2().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KGet e) {
		if(!has_side_effect){
			e.getV1().accept(this);
			e.getV2().accept(this);
		}
	}
	
	/**
	 * 
	 */
	public void visit(KPut e) {
		// Une écriture dans un tableau est un effet de bord
		has_side_effect = true;
	}
}