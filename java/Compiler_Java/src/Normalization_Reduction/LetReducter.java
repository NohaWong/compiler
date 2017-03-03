package Normalization_Reduction;

import java.util.ArrayList;
import java.util.List;

import Code_fourni.Id;
import K_Normal_Expression.*;
import Type.Type;

/**
 * Classe pour le "lissage" des Let
 * @author bizarda
 *
 */
public class LetReducter implements KObjVisitor<KExp> {
	
	/**
	 * Calcule un nouvel AST avec les Let "lissés"
	 * @param AST l'AST K-normal et Alpha-converti à transformer
	 * @return AST avec les Let "lissés", K-normal et Alpha-converti
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
	 * Lisse le Let si nécessaire
	 */
	public KExp visit(KLet e) {
		Id x = e.getId();
		Type tx = e.getT();
		KExp E1 = e.getE1().accept(this);
		KExp result;
		if(E1 instanceof KLet){
			KLet letexp = (KLet) E1;
			Id y = letexp.getId();
			Type ty = letexp.getT();
			KExp e1 = letexp.getE1();
			KExp e2 = letexp.getE2();
			KExp e3 = e.getE2().accept(this);
			// let x = (let y = e1 in e2) in e3
			// -> let y = e1 in let x = e2 in e3
			result = new KLet(y,ty,e1,new KLet(x,tx,e2,e3).accept(this));
		}
		else if(E1 instanceof KLetTuple){
			KLetTuple lettexp = (KLetTuple) E1;
			List<Id> ids = lettexp.getIds();
			List<Type> ts = lettexp.getTs();
			KExp e1 = lettexp.getE1();
			KExp e2 = lettexp.getE2();
			KExp e3 = e.getE2().accept(this);
			// let x = (let (id1,id2,...) = e1 in e2) in e3
			// -> let (id1,id2,...) = e1 in let x = e2 in e3
			result = new KLetTuple(ids,ts,e1,new KLet(x,tx,e2,e3).accept(this));
		}
		else if(E1 instanceof KLetRec){
			KLetRec letrexp = (KLetRec) E1;
			KFunDef fd = letrexp.getFd();
			KExp e2 = letrexp.getE();
			KExp e3 = e.getE2().accept(this);
			// let x = (let fd = e1 in e2) in e3
			// -> let fd = e1 in let x = e2 in e3
			result = new KLetRec(fd,new KLet(x,tx,e2,e3).accept(this));
		}
		else{
			result = new KLet(x,tx,E1,e.getE2().accept(this));
		}
		return result;
	}

	/**
	 * 
	 */
	public KExp visit(KVar e) {
		return e;
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
	public KExp visit(KLetRec e) {
		KExp e1 = e.getFd().getE().accept(this);
		KFunDef fd = new KFunDef(e.getFd().getId(),e.getFd().getType(),e.getFd().getArgs(),e1);
		KExp e2 = e.getE().accept(this);
		KLetRec retour = new KLetRec(fd,e2);
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
		KExp E1 = e.getE1().accept(this);
		KExp result;
		if(E1 instanceof KLet){
			KLet letexp = (KLet) E1;
			Id y = letexp.getId();
			Type ty = letexp.getT();
			KExp e1 = letexp.getE1();
			KExp e2 = letexp.getE2();
			KExp e3 = e.getE2().accept(this);
			// let (id1,id2,...) = (let y = e1 in e2) in e3
			// -> let y = e1 in let (id1,id2,...) = e2 in e3
			result = new KLet(y,ty,e1,new KLetTuple(ids,ts,e2,e3).accept(this));
		}
		else if(E1 instanceof KLetTuple){
			KLetTuple lettexp = (KLetTuple) E1;
			List<Id> idys = lettexp.getIds();
			List<Type> tys = lettexp.getTs();
			KExp e1 = lettexp.getE1();
			KExp e2 = lettexp.getE2();
			KExp e3 = e.getE2().accept(this);
			// let (id1,id2,...) = (let (idy1,idy2,...) = e1 in e2) in e3
			// -> let (idy1,idy2,...) = e1 in let (id1,id2,...) = e2 in e3
			result = new KLetTuple(idys,tys,e1,new KLetTuple(ids,ts,e2,e3).accept(this));
		}
		else if(E1 instanceof KLetRec){
			KLetRec letrexp = (KLetRec) E1;
			KFunDef fd = letrexp.getFd();
			KExp e2 = letrexp.getE();
			KExp e3 = e.getE2().accept(this);
			// let (id1,id2,...) = (let fd = e1 in e2) in e3
			// -> let fd = e1 in let (id1,id2,...) = e2 in e3
			result = new KLetRec(fd,new KLetTuple(ids,ts,e2,e3).accept(this));
		}
		else{
			result = new KLetTuple(ids,ts,E1,e.getE2().accept(this));
		}
		return result;
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

