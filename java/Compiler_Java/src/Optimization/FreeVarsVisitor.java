package Optimization;

import java.util.ArrayList;
import java.util.List;

import Code_fourni.Id;
import K_Normal_Expression.*;

/**
 * Classe de détermination des variables libres d'une expression K-normale et Alpha-convertie
 * @author bizarda
 *
 */
public class FreeVarsVisitor implements KVisitor{

	private List<String> free_vars;
	private List<String> nested_vars;
	
	/**
	 * Constructeur par défaut
	 */
	public FreeVarsVisitor() {
		free_vars = new ArrayList<String>();
		nested_vars = new ArrayList<String>();
	}
	
	/**
	 * Détermine la liste des variables libres dans une expression K-normale et Alpha-convertie
	 * @param AST l'expression en question, K-normale et Alpha-convertie
	 * @return la liste des variables libres dans AST
	 */
	public List<String> Start(KExp AST){
		AST.accept(this);
		return free_vars;
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
		e.getV().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KNeg e) {
		e.getV().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KAdd e) {
		e.getV1().accept(this);
		e.getV2().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KSub e) {
		e.getV1().accept(this);
		e.getV2().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KFNeg e) {
		e.getV().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KFAdd e) {
		e.getV1().accept(this);
		e.getV2().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KFSub e) {
		e.getV1().accept(this);
		e.getV2().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KFMul e) {
		e.getV1().accept(this);
		e.getV2().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KFDiv e) {
		e.getV1().accept(this);
		e.getV2().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KEq e) {
		e.getV1().accept(this);
		e.getV2().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KLE e) {
		e.getV1().accept(this);
		e.getV2().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KIf e) {
		e.getE1().accept(this);
		e.getE2().accept(this);
		e.getE3().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KLet e) {
		e.getE1().accept(this);
		// La variable est désormais liée
		nested_vars.add(e.getId().getId());
		e.getE2().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KVar e) {
		String id = e.getId().getId();
		// Si la variable n'a pas été liée, elle est libre
		if(! nested_vars.contains(id))
			free_vars.add(id);
	}
	
	/**
	 * 
	 */
	public void visit(KLetRec e) {
		List<String> nested_vars_save = new ArrayList<String>(nested_vars);
		for(Id id_arg : e.getFd().getArgs()){
			// Les paramètres formels sont liés pour le corps de la fonction
			String id = id_arg.getId();
			nested_vars.add(id);
		}
		String id_fun = e.getFd().getId().getId();
		nested_vars.add(id_fun);
		e.getFd().getE().accept(this);
		
		nested_vars = nested_vars_save;
		
		// Seule la fonction est liée pour la suite
		nested_vars.add(id_fun);
		e.getE().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KApp e) {
		e.getV().accept(this);
		for(KVar arg : e.getVs())
			arg.accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KTuple e) {
		for(KVar elem : e.getVs())
			elem.accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KLetTuple e) {
		e.getE1().accept(this);
		for(Id id_elem : e.getIds()){
			String id = id_elem.getId();
			nested_vars.add(id);
		}
		e.getE2().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KArray e) {
		e.getV1().accept(this);
		e.getV2().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KGet e) {
		e.getV1().accept(this);
		e.getV2().accept(this);
	}
	
	/**
	 * 
	 */
	public void visit(KPut e) {
		e.getV1().accept(this);
		e.getV2().accept(this);
		e.getV3().accept(this);
	}
}