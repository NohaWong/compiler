package Optimization;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import Code_fourni.Id;
import K_Normal_Expression.*;
import Type.*;

/**
 * Classe pour la propagation des constantes dans un AST
 * @author bizarda
 * 
 */
public class ConstantFolder implements KObjVisitor<KExp> {
	
	private Hashtable<String,KExp> constants;
	private boolean full_folding_allowed;
	
	/**
	 * Constructeur par défaut
	 */
	public ConstantFolder()
	{
		constants = new Hashtable<String,KExp>();
		full_folding_allowed = false;
	}
	
	/**
	 * Calcule un nouvel AST avec les constantes propagées
	 * @param AST l'AST à transformer, K-normal et Alpha-converti
	 * @return AST avec les constantes propagées, K-normal et Alpha-converti
	 */
	public KExp Start(KExp AST)
	{
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
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getV().accept(this);
		full_folding_allowed = present_ffa;
		if(e1 instanceof KVar)
			return new KNot((KVar)e1);
		else{ // e1 instanceof KBool
			boolean val = ((KBool)e1).isB();
			if(full_folding_allowed)
				return new KBool(!val);
			else{
				Id new_id = Id.gen();
				return new KLet(new_id,new TBool(),new KBool(!val),new KVar(new_id));
			}
		}
	}
	
	/**
	 * 
	 */
	public KExp visit(KNeg e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getV().accept(this);
		full_folding_allowed = present_ffa;
		if(e1 instanceof KInt){
			int val = ((KInt)e1).getI();
			if(full_folding_allowed && val<0)
				return new KInt(-val);
			else{
				// On crée un nouveau let pour préserver la K-normalité
				Id new_id = Id.gen();
				Id new_id_2 = Id.gen();
				return new KLet(new_id,new TInt(),e1,new KLet(new_id_2,new TInt(),new KNeg(new KVar(new_id)),new KVar(new_id_2)));
			}
		}
		else
			return e;
	}
	
	/**
	 * 
	 */
	public KExp visit(KAdd e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getV1().accept(this);
		KExp e2 = e.getV2().accept(this);
		full_folding_allowed = present_ffa;
		if(e1 instanceof KInt && e2 instanceof KInt){
			int val1 = ((KInt)e1).getI();
			int val2 = ((KInt)e2).getI();
			KInt result = new KInt(val1+val2);
			if(full_folding_allowed)
				return result;
			else{
				// On crée un nouveau let pour préserver la K-normalité
				Id new_id = Id.gen();
				return new KLet(new_id,new TInt(),result,new KVar(new_id));
			}
		}
		else
			return e;
	}
	
	/**
	 * 
	 */
	public KExp visit(KSub e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getV1().accept(this);
		KExp e2 = e.getV2().accept(this);
		full_folding_allowed = present_ffa;
		if(e1 instanceof KInt && e2 instanceof KInt){
			int val = ((KInt)e1).getI() - ((KInt)e2).getI();
			if(full_folding_allowed && val>=0)
				return new KInt(val);
			else{
				// On crée un nouveau let pour préserver la K-normalité
				if(val<0){
					Id new_id = Id.gen();
					return new KLet(new_id,new TInt(),new KInt(-val),new KNeg(new KVar(new_id)).accept(this));
				}
				else{
					Id new_id = Id.gen();
					return new KLet(new_id,new TInt(),new KInt(val),new KVar(new_id));
				}
			}
		}
		else
			return e;
	}
	
	/**
	 * 
	 */
	public KExp visit(KFNeg e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getV().accept(this);
		full_folding_allowed = present_ffa;
		if(e1 instanceof KFloat){
			float val = ((KFloat)e1).getF();
			if(full_folding_allowed && val<0)
				return new KFloat(-val);
			else{
				// On crée un nouveau let pour préserver la K-normalité
				Id new_id = Id.gen();
				Id new_id_2 = Id.gen();
				return new KLet(new_id,new TFloat(),e1,new KLet(new_id_2,new TFloat(),new KFNeg(new KVar(new_id)),new KVar(new_id_2)));
			}
		}
		else
			return e;
	}

	/**
	 * 
	 */
	public KExp visit(KFAdd e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getV1().accept(this);
		KExp e2 = e.getV2().accept(this);
		full_folding_allowed = present_ffa;
		if(e1 instanceof KFloat && e2 instanceof KFloat){
			float val1 = ((KFloat)e1).getF();
			float val2 = ((KFloat)e2).getF();
			KFloat result = new KFloat(val1+val2);
			if(full_folding_allowed)
				return result;
			else{
				// On crée un nouveau let pour préserver la K-normalité
				Id new_id = Id.gen();
				return new KLet(new_id,new TFloat(),result,new KVar(new_id));
			}
		}
		else
			return e;
	}

	/**
	 * 
	 */
	public KExp visit(KFSub e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getV1().accept(this);
		KExp e2 = e.getV2().accept(this);
		full_folding_allowed = present_ffa;
		if(e1 instanceof KFloat && e2 instanceof KFloat){
			float val = ((KFloat)e1).getF() - ((KFloat)e2).getF();
			if(full_folding_allowed && val>=0)
				return new KFloat(val);
			else{
				// On crée un nouveau let pour préserver la K-normalité
				if(val<0){
					Id new_id = Id.gen();
					return new KLet(new_id,new TFloat(),new KFloat(-val),new KFNeg(new KVar(new_id)).accept(this));
				}
				else{
					Id new_id = Id.gen();
					return new KLet(new_id,new TFloat(),new KFloat(val),new KVar(new_id));
				}
			}
		}
		else
			return e;
	}

	/**
	 * 
	 */
	public KExp visit(KFMul e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getV1().accept(this);
		KExp e2 = e.getV2().accept(this);
		full_folding_allowed = present_ffa;
		if(e1 instanceof KFloat && e2 instanceof KFloat){
			float val1 = ((KFloat)e1).getF();
			float val2 = ((KFloat)e2).getF();
			KFloat result = new KFloat(val1*val2);
			if(full_folding_allowed)
				return result;
			else{
				// On crée un nouveau let pour préserver la K-normalité
				Id new_id = Id.gen();
				return new KLet(new_id,new TFloat(),result,new KVar(new_id));
			}
		}
		else
			return e;
	}

	/**
	 * 
	 */
	public KExp visit(KFDiv e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getV1().accept(this);
		KExp e2 = e.getV2().accept(this);
		full_folding_allowed = present_ffa;
		if(e1 instanceof KFloat && e2 instanceof KFloat){
			float val1 = ((KFloat)e1).getF();
			float val2 = ((KFloat)e2).getF();
			KFloat result = new KFloat(val1/val2);
			if(full_folding_allowed)
				return result;
			else{
				// On crée un nouveau let pour préserver la K-normalité
				Id new_id = Id.gen();
				return new KLet(new_id,new TFloat(),result,new KVar(new_id));
			}
		}
		else
			return e;
	}

	/**
	 * 
	 */
	public KExp visit(KEq e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getV1().accept(this);
		KExp e2 = e.getV2().accept(this);
		full_folding_allowed = present_ffa;
		KBool result;
		if((e1 instanceof KInt && e2 instanceof KInt) || (e1 instanceof KFloat && e2 instanceof KFloat)
				|| (e1 instanceof KBool && e2 instanceof KBool) || (e1 instanceof KUnit && e2 instanceof KUnit)){
			
			if(e1 instanceof KInt && e2 instanceof KInt){
				int val1 = ((KInt)e1).getI();
				int val2 = ((KInt)e2).getI();
				result = new KBool(val1 == val2);
			}
			else if(e1 instanceof KFloat && e2 instanceof KFloat){
				float val1 = ((KInt)e1).getI();
				float val2 = ((KInt)e2).getI();
				result = new KBool(val1 == val2);
			}
			else if(e1 instanceof KBool && e2 instanceof KBool){
				boolean val1 = ((KBool)e1).isB();
				boolean val2 = ((KBool)e2).isB();
				result = new KBool(val1 == val2);
			}
			else
				result = new KBool(true);
			
			if(full_folding_allowed)
				return result;
			else{
				// On crée un nouveau let pour préserver la K-normalité
				Id new_id = Id.gen();
				return new KLet(new_id,new TBool(),result,new KVar(new_id));
			}
		}
		else
			return e;
	}

	/**
	 * 
	 */
	public KExp visit(KLE e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getV1().accept(this);
		KExp e2 = e.getV2().accept(this);
		full_folding_allowed = present_ffa;
		KBool result;
		if((e1 instanceof KInt && e2 instanceof KInt) || (e1 instanceof KFloat && e2 instanceof KFloat)){
			if(e1 instanceof KInt && e2 instanceof KInt){
				int val1 = ((KInt)e1).getI();
				int val2 = ((KInt)e2).getI();
				result = new KBool(val1 <= val2);
			}
			else{
				float val1 = ((KInt)e1).getI();
				float val2 = ((KInt)e2).getI();
				result = new KBool(val1 <= val2);
			}
			if(full_folding_allowed)
				return result;
			else{
				// On crée un nouveau let pour préserver la K-normalité
				Id new_id = Id.gen();
				return new KLet(new_id,new TBool(),result,new KVar(new_id));
			}
		}
		else
			return e;
	}

	/**
	 * 
	 */
	public KExp visit(KIf e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getE1().accept(this);
		full_folding_allowed = present_ffa;
		KExp e2 = e.getE2().accept(this);
		KExp e3 = e.getE3().accept(this);
		if(e1 instanceof KBool){
			boolean val1 = ((KBool)e1).isB();
			return val1 ? e2 : e3;
		}
		else
			return e;
	}

	/**
	 * 
	 */
	public KExp visit(KLet e) {
		Id id = e.getId();
		Type t = e.getT();
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getE1().accept(this);
		full_folding_allowed = present_ffa;
		if(KExp.isConstant(e1))
			constants.put(id.getId(), e1);
		KExp e2 = e.getE2().accept(this);
		if(KExp.isConstant(e2) && full_folding_allowed)
			return e2;
		else
			return new KLet(id,t,e1,e2);
	}

	/**
	 * 
	 */
	public KExp visit(KVar e) {
		KExp val = getConstantFromVar(e);
		if(val != null && full_folding_allowed)
			return val;
		else
			return e;
	}

	/**
	 * 
	 */
	public KExp visit(KLetRec e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = false;
		KExp e1 = e.getFd().getE().accept(this);
		full_folding_allowed = present_ffa;
		KExp e2 = e.getE().accept(this);
		KFunDef fd = new KFunDef(e.getFd().getId(),e.getFd().getType(),e.getFd().getArgs(),e1);
		KLetRec retour = new KLetRec(fd,e2);
		return retour;
	}
	
	/**
	 * Visit d'une Liste d'KExp et la renvoie
	 * @param l la liste d'KExp à visiter
	 * @return la liste d'KExp modifiée
	 */
	private List<KVar> accept_list(List<KVar> l) {
        List<KVar> retour = new ArrayList<KVar>();
		
        for(KVar v : l){
        	KVar var = (KVar) v.accept(this);
        	retour.add(var);
        }
        return retour;
    }
	
	/**
	 * 
	 */
	public KExp visit(KApp e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = false;
		KVar e1 = (KVar) e.getV().accept(this);
		List<KVar> le = new ArrayList<KVar>(accept_list(e.getVs()));
		full_folding_allowed = present_ffa;
		KApp retour = new KApp(e1,le);
		return retour;
	}
	
	/**
	 * 
	 */
	public KExp visit(KTuple e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = false;
		List<KVar> le = new ArrayList<KVar>(accept_list(e.getVs()));
		full_folding_allowed = present_ffa;
		KTuple retour = new KTuple(le);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KLetTuple e) {
		List<Id> ids = new ArrayList<Id>(e.getIds());
		List<Type> ts = new ArrayList<Type>(e.getTs());
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = true;
		KExp e1 = e.getE1().accept(this);
		full_folding_allowed = present_ffa;
		if(e1 instanceof KTuple){
			KTuple kte1 = (KTuple) e1;
			for(int i=0; i<ids.size(); i++){
				KExp const_elem = getConstantFromVar(kte1.getVs().get(i));
				if(const_elem != null)
					constants.put(ids.get(i).getId(), const_elem);
			}
		}
		KExp e2 = e.getE2().accept(this);

		if(KExp.isConstant(e2) && full_folding_allowed)
			return e2;
		else
			return new KLetTuple(ids,ts,e1,e2);
	}

	/**
	 * 
	 */
	public KExp visit(KArray e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = false;
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		full_folding_allowed = present_ffa;
		KArray retour = new KArray(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KGet e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = false;
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		full_folding_allowed = present_ffa;
		KGet retour = new KGet(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KPut e) {
		boolean present_ffa = full_folding_allowed;
		full_folding_allowed = false;
		KVar v1 = (KVar) e.getV1().accept(this);
		KVar v2 = (KVar) e.getV2().accept(this);
		KVar v3 = (KVar) e.getV3().accept(this);
		full_folding_allowed = present_ffa;
		KPut retour = new KPut(v1,v2,v3);
		return retour;
	}
	
	/**
	 * Renvoie la constante associée à la variable, null si elle n'existe pas.
	 * @param var une KVar
	 * @return une KExp constante (Unit, Bool, Int, Float ou Tuple)
	 */
	private KExp getConstantFromVar(KVar var){
		String id = var.getId().getId();
		KExp res = constants.get(id);
		while(res instanceof KVar){
			res = constants.get(((KVar)res).getId().getId());
		}
		return res;
	}
}