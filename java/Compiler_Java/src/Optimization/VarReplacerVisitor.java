package Optimization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Code_fourni.Id;
import K_Normal_Expression.*;
import Type.Type;

/**
 * Classe de renommage des variables dans une expression K-normale et Alpha-convertie
 * @author bizarda
 *
 */
class VarReplacerVisitor implements KObjVisitor<KExp> {
	
	private Map<String,String> rename_map;
	
	/**
	 * Renomme les variables dans une expression K-normale et Alpha-convertie
	 * @param AST l'AST à transformer, K-normal et Alpha-converti
	 * @param rename_map un mapping des anciens noms vers les nouveaux noms
	 * @return AST transformé selon rename_map, K-normal et Alpha-converti
	 */
	public KExp Start(KExp AST, Map<String,String> rename_map)
	{
		this.rename_map = rename_map;
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
	public KExp visit(KVar e) {
		Id id = e.getId();
		String new_name = rename_map.get(id.getId());
		if(new_name == null)
			return e;
		else
			return new KVar(new Id(new_name));
	}

	/**
	 * 
	 */
	public KExp visit(KLetRec e) {
		Id id = e.getFd().getId();
		Id new_id = id;
		String new_name = rename_map.get(id.getId());
		if(new_name != null)
			new_id = new Id(new_name);
		List<Id> args = e.getFd().getArgs();
		List<Id> new_args = new ArrayList<Id>();
		for(Id arg : args){
			String new_arg_name = rename_map.get(arg.getId());
			if(new_arg_name != null)
				new_args.add(new Id(new_arg_name));
			else
				new_args.add(arg);
		}
		KExp e1 = e.getFd().getE().accept(this);
		KExp e2 = e.getE().accept(this);
		KFunDef fd = new KFunDef(new_id,e.getFd().getType(),new_args,e1);
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
	 * 
	 */
	public KExp visit(KLetTuple e) {
		List<Id> ids = new ArrayList<Id>(e.getIds());
		List<Type> ts = new ArrayList<Type>(e.getTs());
		List<Id> new_ids = new ArrayList<Id>();
		for(Id id  :ids){
			String new_arg_name = rename_map.get(id.getId());
			if(new_arg_name != null)
				new_ids.add(new Id(new_arg_name));
			else
				new_ids.add(id);
		}
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		KLetTuple retour = new KLetTuple(new_ids,ts,e1,e2);
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
		KVar v1 = (KVar) e.getV1().accept(this);
		KVar v2 = (KVar) e.getV2().accept(this);
		KVar v3 = (KVar) e.getV3().accept(this);
		KPut retour = new KPut(v1,v2,v3);
		return retour;
	}
}
