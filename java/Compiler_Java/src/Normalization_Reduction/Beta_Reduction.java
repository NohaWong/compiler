package Normalization_Reduction;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Code_fourni.Id;
import K_Normal_Expression.*;
import Type.Type;


/**
 * 
 * @author jonathan
 * Effectue une Beta-Reduction sur une expression 
 * Une Beta-Reduction consiste à Supprimer les affectations inutile du type 
 * Let x = 2 in Let v = x in v+2
 * en
 * Let x = 2 in x+2
 * 
 */

public class Beta_Reduction implements KObjVisitor<KExp> {
	
	
	// Invariant : A un indice i , la var doit etre ramplacer par la var qui remplace; 
	private TriElementList list_tel;
	private boolean Changed;
	
	/**
	 * Constructeur
	 */
	public Beta_Reduction()
	{
		list_tel = new TriElementList();
		Changed = false;
	}
	/**une Expression de type Unit
	 * Prend une Expression en argument et en renvoi une nouvelle 
	 * @param ast une Expression
	 * @return une nouvelle Expression transformé
	 */
	public KExp Start(KExp ast)
	{
		list_tel = new TriElementList();
		Changed = false;
		return ast.accept(this);
	}
	
	/**
	 * Visit une Expression de type Unit et la renvoie
	 * @return une Expression de type Unit modifié 
	 */
	public KExp visit(KUnit e) {
		KUnit retour = new KUnit();
		return retour;
	}
	
	
	/**
	 * Visit d'une Liste d'Expression et la renvoie
	 * @param l une liste de KVar de type List Kvar  
	 * @return une List de KVar KNoramlisé
	 */
	private List<KVar> accept_list(List<KVar> l) {
        List<KVar> retour = new ArrayList<KVar>();
		if (l.isEmpty()) {
            return retour;
        }
        Iterator<KVar> it = l.iterator();
        KVar exp = (KVar) it.next().accept(this);
        retour.add(exp);
        while (it.hasNext()) {
            exp = (KVar) it.next().accept(this);
            retour.add(exp);
        }
        return retour;
    }
	
	/**
	 * Visit une Expression de type KBool et la renvoie
	 * @return une Expression de type KBool modifié 
	 */
	public KBool visit(KBool e) {
		KBool retour = new KBool(e.isB());
		return retour;
	}

	/**
	 * Visit une Expression de type Int et la renvoie
	 * @return une Expression de type Int modifié 
	 */
	public KInt visit(KInt e) {
		KInt retour = new KInt(e.getI());
		return retour;
	}

	/**
	 * Visit une Expression de type Float et la renvoie
	 * @return une Expression de type Float modifié 
	 */
	public KFloat visit(KFloat e) {
		KFloat retour = new KFloat(e.getF());
		return retour;
	}

	/**
	 * Visit une Expression de type Not et la renvoie
	 * @return une Expression de type Not modifié 
	 */
	public KNot visit(KNot e) {
		KVar v1 = (KVar)e.getV().accept(this);
		KNot retour = new KNot(v1);
		return retour;
		
	}
	/**
	 * Visit une Expression de type Neg et la renvoie
	 * @return une Expression de type Neg modifié 
	 */
	public KNeg visit(KNeg e) {
		KVar v1 = (KVar)e.getV().accept(this);
		KNeg retour = new KNeg(v1);
		return retour;
	}
	/**
	 * Visit une Expression de type Add et la renvoie
	 * @return une Expression de type Add modifié
	 */
	public KAdd visit(KAdd e) {
		KVar v1 = (KVar)e.getV1().accept(this);
		KVar v2 = (KVar)e.getV2().accept(this);
		KAdd retour = new KAdd(v1,v2);
		return retour;
	}
	/**
	 * Visit une Expression de type Sub et la renvoie
	 * @return une Expression de type Sub modifié
	 */
	public KSub visit(KSub e) {
		KVar v1 = (KVar)e.getV1().accept(this);
		KVar v2 = (KVar)e.getV2().accept(this);
		KSub retour = new KSub(v1,v2);
		return retour;
	}
	
	/**
	 * Visit une Expression de type FNeg et la renvoie
	 * @return une Expression de type FNeg modifié
	 */
	public KFNeg visit(KFNeg e) {
		KVar v1 = (KVar)e.getV().accept(this);
		KFNeg retour = new KFNeg(v1);
		return retour;
	}

	/**
	 * Visit une Expression de type FAdd et la renvoie
	 * @return une Expression de type FAdd modifié
	 */
	public KFAdd visit(KFAdd e) {
		KVar v1 = (KVar)e.getV1().accept(this);
		KVar v2 = (KVar)e.getV2().accept(this);
		KFAdd retour = new KFAdd(v1,v2);
		return retour;
	}

	/**
	 * Visit une Expression de type FSub et la renvoie
	 * @return une Expression de type FSub modifié
	 */
	public KFSub visit(KFSub e) {
		KVar v1 = (KVar)e.getV1().accept(this);
		KVar v2 = (KVar)e.getV2().accept(this);
		KFSub retour = new KFSub(v1,v2);
		return retour;
	}

	/**
	 * Visit une Expression de type FMul et la renvoie
	 * @return une Expression de type FMul modifié
	 */
	public KFMul visit(KFMul e) {
		KVar v1 = (KVar)e.getV1().accept(this);
		KVar v2 = (KVar)e.getV2().accept(this);
		KFMul retour = new KFMul(v1,v2);
		return retour;
	}

	/**
	 * Visit une Expression de type FDiv et la renvoie
	 * @return une Expression de type FDiv modifié
	 */
	public KFDiv visit(KFDiv e) {
		KVar v1= (KVar)e.getV1().accept(this);
		KVar v2 = (KVar)e.getV2().accept(this);
		KFDiv retour = new KFDiv(v1,v2);
		return retour;
	}

	/**
	 * Visit une Expression de type Eq et la renvoie
	 * @return une Expression de type Eq modifié
	 */
	public KEq visit(KEq e) {
		KVar v1 = (KVar)e.getV1().accept(this);
		KVar v2 = (KVar)e.getV2().accept(this);
		KEq retour = new KEq(v1,v2);
		return retour;
	}

	/**
	 * Visit une Expression de type LE et la renvoie
	 * @return une Expression de type LE modifié
	 */
	public KLE visit(KLE e) {
		KVar v1 = (KVar)e.getV1().accept(this);
		KVar v2 = (KVar)e.getV2().accept(this);
		KLE retour = new KLE(v1,v2);
		return retour;
	}

	/**
	 * Visit une Expression de type If et la renvoie
	 * @return une Expression de type If modifié
	 */
	public KIf visit(KIf e) {
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);		
		KExp e3 = e.getE3().accept(this);
		KIf retour = new KIf(e1,e2,e3);
		return retour;
	}
//TODO
	/**
	 * Visit une Expression de type Let et la renvoie
	 * Si une modification est faite, Changed devient vrai
	 * @return une Expression de type Let Beta-reduite
	 */
	public KExp visit(KLet e) {
		Id id = e.getId();
		Type t = e.getT();
		boolean keepAffectation = true;
		int index = list_tel.Index_id_in_id1(id);
		
		if(index != -1)
		{
			list_tel.setKeepAffectation(index,true);
		}
		
		KExp e1 = e.getE1().accept(this);
		if (e1 instanceof KVar)
		{
			keepAffectation = false;
			list_tel.add(((KVar) e1).getId(), id, keepAffectation);
		}
		
		KExp e2 = e.getE2().accept(this);
		
		
		KExp retour = null;
		index = list_tel.Index_id_in_id2(id);
		if(index != -1)
		{
			keepAffectation = list_tel.getKeepAffection(index);
		}
		
		if(keepAffectation)
		{
			retour = new KLet(id,t,e1,e2);
		}
		else
		{
			Changed = true;
			retour = e2;
		}
		return retour;
	}

	/**
	 * Visit une Expression de type Var et la renvoie
	 * @return une Expression de type Var modifié
	 */
	public KVar visit(KVar e) {
		Id id = e.getId();
		KVar retour = new KVar(list_tel.id_to_replace(id));
		return retour;
	}


	/**
	 * Visit une Expression de type LetRec et la renvoie
	 * @return une Expression de type LetRec modifié
	 */
	public KLetRec visit(KLetRec e) {
		KExp e1 = e.getFd().getE().accept(this);
		KFunDef fd = new  KFunDef(e.getFd().getId(),e.getFd().getType(),e.getFd().getArgs(),e1);
		KExp e2 = e.getE().accept(this);
		KLetRec retour = new KLetRec(fd,e2);
		return retour;
	}
	
	/**
	 * Visit une Expression de type App et la renvoie
	 * @return une Expression de type App modifié
	 */
	public KApp visit(KApp e) {
		KVar v1 = (KVar)e.getV().accept(this);
		List<KVar> lv = new ArrayList<KVar>(accept_list(e.getVs()));
		KApp retour = new KApp(v1,lv);
		return retour;
	}
	
	
	
	

	/**
	 * Visit une Expression de type Tuple et la renvoie
	 * @return une Expression de type Tuple modifié
	 */
	public KTuple visit(KTuple e) {
		List<KVar> lv = new ArrayList<KVar>(accept_list(e.getVs()));
		KTuple retour = new KTuple(lv);
		return retour;
	}

	/**
	 * Visit une Expression de type LetTuple et la renvoie
	 * @return une Expression de type LetTuple modifié
	 */
	public KLetTuple visit(KLetTuple e) {
		List<Id> ids = new ArrayList<Id>(e.getIds());
		List<Type> ts = new ArrayList<Type>(e.getTs());
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		KLetTuple retour = new KLetTuple(ids,ts,e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type Array et la renvoie
	 * @return une Expression de type Array modifié
	 */
	public KArray visit(KArray e) {
		KVar v1 = (KVar)e.getV1().accept(this);
		KVar v2 = (KVar)e.getV2().accept(this);
		KArray retour = new KArray(v1,v2);
		return retour;
	}

	/**
	 * Visit une Expression de type Get et la renvoie
	 * @return une Expression de type Get modifié
	 */
	public KGet visit(KGet e) {
		KVar v1 = (KVar)e.getV1().accept(this);
		KVar v2 = (KVar)e.getV2().accept(this);
		KGet retour = new KGet(v1,v2);
		return retour;
	}

	/**
	 * Visit une Expression de type Put et la renvoie
	 * @return une Expression de type Put modifié
	 */
	public KPut visit(KPut e) {
		KVar v1 = (KVar)e.getV1().accept(this);
		KVar v2 = (KVar)e.getV2().accept(this);
		KVar v3 = (KVar)e.getV3().accept(this);
		KPut retour = new KPut(v1,v2,v3);
		return retour;
	}
	
	
	/**
	 * 
	 * @return Renvoi vrai si la Beta_Reduction a fait une modification sur l'arbre fournie
	 */
	public boolean isChanged() {
		return Changed;
	}
	
	

}
