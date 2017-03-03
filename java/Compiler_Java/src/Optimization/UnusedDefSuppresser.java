package Optimization;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import Code_fourni.Id;
import K_Normal_Expression.*;
import Type.Type;

/**
 * Classe de suppression des définitions inutilisées de variables
 * @author bizarda
 *
 */
public class UnusedDefSuppresser implements KObjVisitor<KExp> {

	/**
	 * Transforme l'AST K-normal et Alpha-converti en supprimant toutes les définitions inutiles
	 * @param AST l'AST à modifier, K-normal et Alpha-converti
	 * @return l'AST sans les définitions inutiles, K-normal et Alpha-converti
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
		KVar e1 = (KVar) e.getV().accept(this);
		KNot retour = new KNot(e1);
		return retour;
	}
	
	/**
	 * 
	 */
	public KExp visit(KNeg e) {
		KVar e1 = (KVar) e.getV().accept(this);
		KNeg retour = new KNeg(e1);
		return retour;
	}
	
	/**
	 * 
	 */
	public KExp visit(KAdd e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		KAdd retour = new KAdd(e1,e2);
		return retour;
	}
	
	/**
	 * 
	 */
	public KExp visit(KSub e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		KSub retour = new KSub(e1,e2);
		return retour;
	}
	
	/**
	 * 
	 */
	public KExp visit(KFNeg e) {
		KVar e1 = (KVar) e.getV().accept(this);
		KFNeg retour = new KFNeg(e1);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KFAdd e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		KFAdd retour = new KFAdd(e1,e2);
		return retour;
	}
	
	/**
	 * 
	 */
	public KExp visit(KFSub e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		KFSub retour = new KFSub(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KFMul e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		KFMul retour = new KFMul(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KFDiv e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		KFDiv retour = new KFDiv(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KEq e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		KEq retour = new KEq(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KLE e) {
		KVar e1 = (KVar) e.getV1().accept(this);
		KVar e2 = (KVar) e.getV2().accept(this);
		KLE retour = new KLE(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KIf e) {
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);	
		KExp e3 = e.getE3().accept(this);
		KIf retour = new KIf(e1,e2,e3);
		return retour;
	}
	
	/**
	 * Supprime le Let passé en paramètre si celui-ci est inutilisé dans la suite
	 */
	public KExp visit(KLet e) {
		Id id = e.getId();
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		if(e1 instanceof KVar){
			// On a let v1 = v2 in ...
			// On peut donc enlever le Let en remplaçant v1 par v2 dans e2
			Map<String,String> map_v1v2 = new Hashtable<String,String>();
			map_v1v2.put(id.getId(), ((KVar)e1).getId().getId());
			return new VarReplacerVisitor().Start(e2, map_v1v2);
		}
		List<String> free_vars_e2 = getFreeVarsIn(e2);
		if(free_vars_e2.contains(id.getId()) || hasSideEffect(e1)){
			// Cette variable est utilisée par la suite (ou sa définition a un effet de bord),
			// on ne peut pas supprimer le Let
			Type t = e.getT();
			KLet retour = new KLet(id,t,e1,e2);
			return retour;
		}
		else{
			return e2;
		}
	}

	/**
	 * 
	 */
	public KExp visit(KVar e) {
		return e;
	}


	/**
	 * Supprime le LetRec passé en paramètre si celui-ci est inutilisé dans la suite
	 */
	public KExp visit(KLetRec e) {
		Id id_fun = e.getFd().getId();
		KExp e1 = e.getFd().getE().accept(this);
		KExp e2 = e.getE().accept(this);
		List<String> free_vars_e2 = getFreeVarsIn(e2);
		if(free_vars_e2.contains(id_fun.getId())){
			// Cette fonction est utilisée par la suite, on ne peut pas supprimer le LetRec
			KFunDef fd = new KFunDef(id_fun,e.getFd().getType(),e.getFd().getArgs(),e1);
			KLetRec retour = new KLetRec(fd,e2);
			return retour;
		}
		else{
			return e2;
		}
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
		KVar e1 = (KVar) e.getV().accept(this);
		List<KVar> le = new ArrayList<KVar>(accept_list(e.getVs()));
		KApp retour = new KApp(e1,le);
		return retour;
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
	 * Supprime le LetTuple passé en paramètre si celui-ci est inutilisé dans la suite
	 */
	public KExp visit(KLetTuple e) {
		List<Id> ids = new ArrayList<Id>(e.getIds());
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		List<String> free_vars_e2 = getFreeVarsIn(e2);
		boolean no_free_var_in_e2 = true;
		for(Id id : ids){
			if(free_vars_e2.contains(id.getId()))
				// Cette variable est utilisée par la suite, on ne peut pas supprimer le LetTuple
				no_free_var_in_e2 = false;
		}
		if(no_free_var_in_e2){
			return e2;
		}
		else{
			List<Type> ts = new ArrayList<Type>(e.getTs());
			KLetTuple retour = new KLetTuple(ids,ts,e1,e2);
			return retour;
		}
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
		KVar v1 = (KVar) e.getV1().accept(this);
		KVar v2 = (KVar) e.getV2().accept(this);
		KVar v3 = (KVar) e.getV3().accept(this);
		KPut retour = new KPut(v1,v2,v3);
		return retour;
	}
	
	/**
	 * Décide si une KExp a un effet de bord
	 * @param e l'KExp à tester
	 * @return vrai ssi e a un effet de bord
	 */
	private static boolean hasSideEffect(KExp e){
		return new SideEffectVisitor().Start(e);
	}
	
	/**
	 * Détermine la liste des variables libres dans une KExp
	 * @param e l'KExp à tester
	 * @return la liste des variables libres dans e
	 */
	private static List<String> getFreeVarsIn(KExp e){
		return new FreeVarsVisitor().Start(e);
	}
	
}