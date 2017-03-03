package Optimization;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import Code_fourni.Id;
import K_Normal_Expression.*;
import Normalization_Reduction.AlphaConverser;
import Type.Type;


/**
 * Classe pour l'expansion inline (copie des corps de fonction)
 * @author bizarda
 *
 */

public class InlineExpander implements KObjVisitor<KExp> {
	/**
	 * Nombre de K-noeuds maximal pour qu'une fonction soit copiée
	 */
	private static final int DEFAULT_FUN_SIZE_THRESHOLD = 42;
	
	private int threshold;
	private Map<String,KExp> fun_body_map;
	private Map<String,List<Id>> fun_args_map;
	
	/**
	 * Constructeur par défaut
	 */
	public InlineExpander(){
		threshold = DEFAULT_FUN_SIZE_THRESHOLD;
		fun_body_map = new Hashtable<String,KExp>();
		fun_args_map = new Hashtable<String,List<Id>>();
	}
	
	/**
	 * 
	 * Constructeur
	 * @param fun_size_threshold Nombre de K-noeuds maximal pour qu'une fonction soit copiée
	 */
	public InlineExpander(int fun_size_threshold){
		this.threshold = fun_size_threshold;
		fun_body_map = new Hashtable<String,KExp>();
		fun_args_map = new Hashtable<String,List<Id>>();
	}
	
	/**
	 * Calcule un nouvel AST en appliquant l'inline expansion
	 * @param AST Alpha-converti et K-normal
	 * @return AST transformé, Alpha-converti et K-normal
	 */
	public KExp Start(KExp AST) {
		return AST.accept(this);
	}
	
	/**
	 * 
	 */
	public KExp visit(KUnit e) {
		return e;
	}
	
	/**
	 * 
	 */
	public KExp visit(KBool e) {
		return e;
	}

	/**
	 * 
	 */
	public KExp visit(KInt e) {
		return e;
	}

	/**
	 * 
	 */
	public KExp visit(KFloat e) {
		return e;
	}

	/**
	 * 
	 */
	public KExp visit(KNot e) {
		KVar e1 = (KVar) e.getV().accept(this);
		return new KNot(e1);
	}
	
	/**
	 * 
	 */
	public KExp visit(KNeg e) {
		KVar e1 = (KVar) e.getV().accept(this);
		return new KNeg(e1);
	}
	
	/**
	 * 
	 */
	public KExp visit(KAdd e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		return new KAdd(e1,e2);
	}
	
	/**
	 * 
	 */
	public KExp visit(KSub e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		return new KSub(e1,e2);
	}
	
	/**
	 * 
	 */
	public KExp visit(KFNeg e) {
		KVar e1 = (KVar) e.getV().accept(this);
		return new KFNeg(e1);
	}

	/**
	 * 
	 */
	public KExp visit(KFAdd e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		return new KFAdd(e1,e2);
	}

	/**
	 * 
	 */
	public KExp visit(KFSub e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		return new KFSub(e1,e2);
	}

	/**
	 * 
	 */
	public KExp visit(KFMul e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		return new KFMul(e1,e2);
	}

	/**
	 * 
	 */
	public KExp visit(KFDiv e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		return new KFDiv(e1,e2);
	}

	/**
	 * 
	 */
	public KExp visit(KEq e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		return new KEq(e1,e2);
	}

	/**
	 * 
	 */
	public KExp visit(KLE e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		return new KLE(e1,e2);
	}

	/**
	 * 
	 */
	public KExp visit(KIf e) {
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		KExp e3 = e.getE3().accept(this);
		return new KIf(e1,e2,e3);
	}

	/**
	 * 
	 */
	public KExp visit(KLet e) {
		Id id = e.getId();
		Type t = e.getT();
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		KLet retour = new KLet(id,t,e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KVar visit(KVar e) {
		return e;
	}

	/**
	 * 
	 */
	public KExp visit(KLetRec e) {
		KExp e1 = e.getFd().getE().accept(this);
		String fun_id = e.getFd().getId().getId();
		List<String> args = new ArrayList<String>();
		for(Id id : e.getFd().getArgs())
			args.add(id.getId());
		int fun_size = getKExpSize(e1);
		if(fun_size < threshold){
			// Si la fonction est assez petite
			fun_body_map.put(fun_id, e1);
			fun_args_map.put(fun_id, e.getFd().getArgs());
		}
		KExp e2 = e.getE().accept(this);
		KFunDef fd = new KFunDef(e.getFd().getId(),e.getFd().getType(),e.getFd().getArgs(),e1);
		KLetRec retour = new KLetRec(fd,e2);
		return retour;
	}
	
	/**
	 * Visit d'une Liste d'KExp et la renvoie
	 * @param la liste d'KExp à visiter
	 * @return la liste d'KExp modifiée
	 */
	private List<KVar> accept_list(List<KVar> l) {
        List<KVar> retour = new ArrayList<KVar>();
		if (l.isEmpty())
            return retour;
		
        for(KVar e : l){
        	KVar exp = (KVar) e.accept(this);
        	retour.add(exp);
        }
        return retour;
    }
	
	/**
	 * 
	 */
	public KExp visit(KApp e) {
		Id fun_id = e.getV().getId();
		KExp fun_body = fun_body_map.get(fun_id.getId());
		if(fun_body != null){
			Map<String,String> fun_rename_map = new Hashtable<String,String>();
			List<KVar> effective_args = e.getVs();
			List<Id> formal_args = fun_args_map.get(fun_id.getId());
			for(int i=0; i<formal_args.size(); i++){
				String effective_id = effective_args.get(i).getId().getId();
				String formal_id = formal_args.get(i).getId();
				fun_rename_map.put(formal_id,effective_id);
			}
			if(new FreeVarsVisitor().Start(fun_body).contains(fun_id.getId()))
				return new AlphaConverser(fun_rename_map).Start(fun_body);
			else
				return new AlphaConverser(fun_rename_map).Start(fun_body.accept(this));
		}
		else
			return e;
	}
	
	/**
	 * 
	 */
	public KExp visit(KTuple e) {
		List<KVar> le = new ArrayList<KVar>(accept_list(e.getVs()));
		KTuple retour = new KTuple(le);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KLetTuple e) {
		List<Id> ids = new ArrayList<Id>(e.getIds());
		List<Type> ts = new ArrayList<Type>(e.getTs());
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		KLetTuple retour = new KLetTuple(ids,ts,e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KArray e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		KArray retour = new KArray(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KGet e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		KGet retour = new KGet(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KPut e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		KVar e3 = (KVar) e.getV3().accept(this);
		KPut retour = new KPut(e1,e2,e3);
		return retour;
	}
	
	/**
	 * Calcule la taille (en nombre de K-noeuds) d'une expression K-normale
	 * @param e l'expression K-normale à mesurer
	 * @return la taille de e (en nombre de K-noeuds)
	 */
	private static int getKExpSize(KExp e){
		return new SizeVisitor().Start(e);
	}
}
