package Normalization_Reduction;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import Code_fourni.Id;
import K_Normal_Expression.*;
import Main.MinMlLibrary;
import Optimization.FreeVarsVisitor;
import Type.Type;

/**
 * Classe d'Alpha-conversion
 * @author bizarda
 *
 */
public class AlphaConverser implements KObjVisitor<KExp> {

	private Map<String,String> rename_map;
	private List<String> defined;

	/**
	 * Constructeur par défaut
	 */
	public AlphaConverser(){
		rename_map = new Hashtable<String,String>();
		defined = MinMlLibrary.getPredefinedSymbols();
	}
	
	/**
	 * Constructeur pour l'inline expansion
	 * @param initial_mapping la mapping initial de renommage
	 */
	public AlphaConverser(Map<String,String> initial_mapping){
		rename_map = new Hashtable<String,String>(initial_mapping);
		defined = MinMlLibrary.getPredefinedSymbols();
		for(String s : initial_mapping.keySet())
			defined.add(s);
	}
	
	/**
	 * Calcule un nouvel AST ayant des noms de symboles tous différents
	 * @param AST un AST K-normal
	 * @return AST Alpha-converti (K-normal)
	 */
	public KExp Start(KExp AST){
		for(String s : new FreeVarsVisitor().Start(AST))
			defined.add(s);
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
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV().accept(this);
		rename_map = rename_map_save;
		KNot retour = new KNot(e1);
		return retour;
		
	}
	
	/**
	 * 
	 */
	public KExp visit(KNeg e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV().accept(this);
		rename_map = rename_map_save;
		KNeg retour = new KNeg(e1);
		return retour;
	}
	
	/**
	 * 
	 */
	public KExp visit(KAdd e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV1().accept(this);
		rename_map = rename_map_save;
		KVar e2 = (KVar) e.getV2().accept(this);
		rename_map = rename_map_save;
		KAdd retour = new KAdd(e1,e2);
		return retour;
	}
	
	/**
	 * 
	 */
	public KExp visit(KSub e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV1().accept(this);
		rename_map = rename_map_save;
		KVar e2 = (KVar) e.getV2().accept(this);
		rename_map = rename_map_save;
		KSub retour = new KSub(e1,e2);
		return retour;
	}
	
	/**
	 * 
	 */
	public KExp visit(KFNeg e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV().accept(this);
		rename_map = rename_map_save;
		KFNeg retour = new KFNeg(e1);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KFAdd e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV1().accept(this);
		rename_map = rename_map_save;
		KVar e2 = (KVar) e.getV2().accept(this);
		rename_map = rename_map_save;
		KFAdd retour = new KFAdd(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KFSub e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV1().accept(this);
		rename_map = rename_map_save;
		KVar e2 = (KVar) e.getV2().accept(this);
		rename_map = rename_map_save;
		KFSub retour = new KFSub(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KFMul e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV1().accept(this);
		rename_map = rename_map_save;
		KVar e2 = (KVar) e.getV2().accept(this);
		rename_map = rename_map_save;
		KFMul retour = new KFMul(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KFDiv e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV1().accept(this);
		rename_map = rename_map_save;
		KVar e2 = (KVar) e.getV2().accept(this);
		rename_map = rename_map_save;
		KFDiv retour = new KFDiv(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KEq e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV1().accept(this);
		rename_map = rename_map_save;
		KVar e2 = (KVar) e.getV2().accept(this);
		rename_map = rename_map_save;
		KEq retour = new KEq(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KLE e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV1().accept(this);
		rename_map = rename_map_save;
		KVar e2 = (KVar) e.getV2().accept(this);
		rename_map = rename_map_save;
		KLE retour = new KLE(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KIf e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KExp e1 = e.getE1().accept(this);
		rename_map = new Hashtable<String,String>(rename_map_save);
		KExp e2 = e.getE2().accept(this);
		rename_map = rename_map_save;
		KExp e3 = e.getE3().accept(this);
		rename_map = rename_map_save;
		KIf retour = new KIf(e1,e2,e3);
		return retour;
	}

	/**
	 * Transforme le symbole défini par le Let si un symbole du même nom est déjà défini
	 */
	public KExp visit(KLet e) {
		Id id = e.getId();
		Type t = e.getT();
		KExp e1 = e.getE1().accept(this);

		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		String old_name = id.getId();
		String new_name;
		do{
			new_name = Id.gen().getId();
		}while(defined.contains(new_name));
		defined.add(new_name);
		
		id = new Id(new_name);
		rename_map.put(old_name,new_name);
		KExp e2 = e.getE2().accept(this);
		rename_map = rename_map_save;

		KLet retour = new KLet(id,t,e1,e2);
		return retour;
	}

	/**
	 * Renomme la KVar avec son nouveau nom
	 */
	public KVar visit(KVar e) {
		Id id = e.getId();
		String old_name = id.getId();
		String new_name = rename_map.get(old_name);
		if(new_name == null){
			// Variable externe
			return e;
		}
		else{
			id = new Id(new_name);
		}
		return new KVar(id);
	}

	/**
	 * Transforme les symboles définis par le LetRec (fonction/arguments) si un symbole du même nom est déjà défini
	 */
	public KExp visit(KLetRec e) {
		Id id_fun = e.getFd().getId();
		String old_fun_name = id_fun.getId();
		Map<String, String> rename_map_save1 = new Hashtable<String,String>(rename_map);
		String new_fun_name;
		do{
			new_fun_name = Id.genf().getId();
		}while(defined.contains(new_fun_name));
		defined.add(new_fun_name);
		
		id_fun = new Id(new_fun_name);
		rename_map.put(old_fun_name,new_fun_name);
		
		Map<String, String> rename_map_save2 = new Hashtable<String,String>(rename_map);
		
		List<Id> args = e.getFd().getArgs();
		List<Id> new_args = new ArrayList<Id>();
		for(Id id : args){
			String old_name = id.getId();
			String new_name;
			do{
				new_name = Id.gen().getId();
			}while(defined.contains(new_name));
			defined.add(new_name);
			
			new_args.add(new Id(new_name));
			rename_map.put(old_name,new_name);
		}
		KExp e1 = e.getFd().getE().accept(this);
		rename_map = rename_map_save2;
		
		KExp e2 = e.getE().accept(this);
		rename_map = rename_map_save1;
		KFunDef fd = new KFunDef(id_fun,e.getFd().getType(),new_args,e1);
		KLetRec retour = new KLetRec(fd,e2);
		return retour;
	}
	
	/**
	 * 
	 */
	private List<KVar> accept_list(List<KVar> l) {
        List<KVar> retour = new ArrayList<KVar>();
        for(KVar exp : l){
    		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
        	retour.add((KVar) exp.accept(this));
        	rename_map = rename_map_save;
        }
        return retour;
    }
	
	/**
	 * 
	 */
	public KExp visit(KApp e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV().accept(this);
    	rename_map = rename_map_save;
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
		KExp e1 = e.getE1().accept(this);

		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		List<Id> new_ids = new ArrayList<Id>();
		for(Id id : ids){
			String old_name = id.getId();
			String new_name;
			do{
				new_name = Id.gen().getId();
			}while(defined.contains(new_name));
			defined.add(new_name);
			
			new_ids.add(new Id(new_name));
			rename_map.put(old_name,new_name);
		}

		KExp e2 = e.getE2().accept(this);
		rename_map = rename_map_save;
		KLetTuple retour = new KLetTuple(new_ids,ts,e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KArray e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV1().accept(this);
		rename_map = rename_map_save;
		KVar e2 = (KVar) e.getV2().accept(this);
		rename_map = rename_map_save;
		KArray retour = new KArray(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KGet e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV1().accept(this);
		rename_map = rename_map_save;
		KVar e2 = (KVar) e.getV2().accept(this);
		rename_map = rename_map_save;
		KGet retour = new KGet(e1,e2);
		return retour;
	}

	/**
	 * 
	 */
	public KExp visit(KPut e) {
		Map<String, String> rename_map_save = new Hashtable<String,String>(rename_map);
		KVar e1 = (KVar) e.getV1().accept(this);
		rename_map = new Hashtable<String,String>(rename_map_save);
		KVar e2 = (KVar) e.getV2().accept(this);
		rename_map = rename_map_save;
		KVar e3 = (KVar) e.getV3().accept(this);
		rename_map = rename_map_save;
		KPut retour = new KPut(e1,e2,e3);
		return retour;
	}
}
