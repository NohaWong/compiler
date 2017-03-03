package Asml;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import Code_fourni.Id;
import Expression.App;
import Expression_asml.*;
import K_Normal_Expression.*;
import Main.MinMlLibrary;
import Type.*;

/**
 * 
 * @author Noha
 *  transformation de l'expression en asml
 * 
 *
 */

public class Traductor implements KObjVisitor<Exp_asml> {
	
	/**
	 * Constructeur
	 */
	private List<Exp_asml> ls;
	private List<String> list_var;
	private List<String> list_var_def;
	private List<String> func_closure;
	private List<String> Fonction_Closure;
	private List<Closure_Fonction_Var> List_closure_fonction;
	
	
	private Exp_asml st;
	private Id id;
	private TUnit t;
	private Var_asml v;
	private Hashtable<String,Type> symbol_table;
	private String path;
	private List<Couple_var_asml> trans;
	private List<Id> already_exist;
	
	public Traductor(Hashtable<String,Type> table)
	{
		ls = new ArrayList<Exp_asml>();
		id = new Id("_");
		t = new TUnit();
		v = new Var_asml(new Id("Empty"),new TUnit());
		symbol_table = new Hashtable<String,Type>(table);
		path = new String();
		list_var = new ArrayList<String>();
		func_closure = new ArrayList<String>();
		list_var_def = new ArrayList<String>();
		trans = new ArrayList<Couple_var_asml>();
		already_exist = new ArrayList<Id>();
		Fonction_Closure = new ArrayList<String>();
		List_closure_fonction = new ArrayList<Closure_Fonction_Var>();
		
	}
	/**
	 * Prend une Expression en argument et en renvoi une Liste d'Expression
	 * @return une nouvelle Liste d Expression 
	 */
	public List<Exp_asml> Start(KExp ast)
	{
		
		st = new Let_asml(id,t,ast.accept(this),v); 
		List<Exp_asml> ls_temp = new ArrayList<Exp_asml>();
		for(int i = 0; i < ls.size();i++)
		{
			Ajout_Closure_Rec acr = new Ajout_Closure_Rec(List_closure_fonction);
			Exp_asml tmp = acr.Start(ls.get(i));
			ls_temp.add(tmp);
		}
		ls_temp.add(st);
		return ls_temp;
	}
	
	/**
	 * Visit une Expression de type Unit et la renvoie
	 * @return une Expression de type Unit modifié 
	 */
	public Exp_asml visit(KUnit e) {
		Unit_asml retour = new Unit_asml();
		return retour;
	}
	
	
	/**
	 * Visit d'une Liste d'Expression et la renvoie
	 * @return une Expression de type List<Exp> modifié 
	 */
	List<Var_asml> accept_list(List<KVar> l) {
        List<Var_asml> retour = new ArrayList<Var_asml>();
		if (l.isEmpty()) {
            return retour;
        }
        Iterator<KVar> it = l.iterator();
        Var_asml exp = (Var_asml)it.next().accept(this);
        retour.add(exp);
        while (it.hasNext()) {
            exp = (Var_asml)it.next().accept(this);
            retour.add(exp);
        }
        return retour;
    }
	
	/**
	 * Visit une Expression de type Bool et la renvoie
	 * @return une Expression de type Bool modifié 
	 */
	public Exp_asml visit(KBool e) {
		Bool_asml retour_asml = new Bool_asml(e.isB());
		return retour_asml;
	}

	/**
	 * Visit une Expression de type Int et la renvoie
	 * @return une Expression de type Int modifié 
	 */
	public Exp_asml visit(KInt e) {
		Int_asml retour = new Int_asml(e.getI());
		return retour;
	}

	/**
	 * Visit une Expression de type Float et la renvoie
	 * @return une Expression de type Float modifié 
	 */
	public Exp_asml visit(KFloat e) {
		System.err.println("Not implemented");
		System.exit(1);
		Float_asml retour = new Float_asml(e.getF());
		
		return retour;
	}

	/**
	 * Visit une Expression de type Not et la renvoie
	 * @return une Expression de type Not modifié 
	 */
	public Exp_asml visit(KNot e) {
		Var_asml e1 = (Var_asml)e.getV().accept(this);
		Not_asml retour = new Not_asml(e1);
		return retour;
		
	}
	/**
	 * Visit une Expression de type Neg et la renvoie
	 * @return une Expression de type Neg modifié 
	 */
	public Exp_asml visit(KNeg e) {
		Var_asml e1 = (Var_asml)e.getV().accept(this);
		Neg_asml retour = new Neg_asml(e1);
		return retour;
	}
	/**
	 * Visit une Expression de type Add et la renvoie
	 * @return une Expression de type Add modifié
	 */
	public Exp_asml visit(KAdd e) {
		
		Var_asml e1 = (Var_asml)e.getV1().accept(this);
		Var_asml e2 = (Var_asml)e.getV2().accept(this);
		Add_asml retour = new Add_asml(e1,e2);
		return retour;
	}
	/**
	 * Visit une Expression de type Sub et la renvoie
	 * @return une Expression de type Sub modifié
	 */
	public Exp_asml visit(KSub e) {
		Var_asml e1 = (Var_asml)e.getV1().accept(this);
		Var_asml e2 = (Var_asml)e.getV2().accept(this);
		Sub_asml retour = new Sub_asml(e1,e2);
		return retour;
	}
	
	/**
	 * Visit une Expression de type FNeg et la renvoie
	 * @return une Expression de type FNeg modifié
	 */
	public Exp_asml visit(KFNeg e) {
		System.err.println("Not implemented");
		System.exit(1);
		Var_asml e1 = (Var_asml)e.getV().accept(this);
		FNeg_asml retour = new FNeg_asml(e1);
		return retour;
	}

	/**
	 * Visit une Expression de type FAdd et la renvoie
	 * @return une Expression de type FAdd modifié
	 */
	public Exp_asml visit(KFAdd e) {
		System.err.println("Not implemented");
		System.exit(1);
		Var_asml e1 = (Var_asml)e.getV1().accept(this);
		Var_asml e2 = (Var_asml)e.getV2().accept(this);
		FAdd_asml retour = new FAdd_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type FSub et la renvoie
	 * @return une Expression de type FSub modifié
	 */
	public Exp_asml visit(KFSub e) {
		System.err.println("Not implemented");
		System.exit(1);
		Var_asml e1 = (Var_asml)e.getV1().accept(this);
		Var_asml e2 = (Var_asml)e.getV2().accept(this);
		FSub_asml retour = new FSub_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type FMul et la renvoie
	 * @return une Expression de type FMul modifié
	 */
	public Exp_asml visit(KFMul e) {
		System.err.println("Not implemented");
		System.exit(1);
		Var_asml e1 = (Var_asml)e.getV1().accept(this);
		Var_asml e2 = (Var_asml)e.getV2().accept(this);
		FMul_asml retour = new FMul_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type FDiv et la renvoie
	 * @return une Expression de type FDiv modifié
	 */
	public Exp_asml visit(KFDiv e) {
		System.err.println("Not implemented");
		System.exit(1);
		Var_asml e1 = (Var_asml)e.getV1().accept(this);
		Var_asml e2 = (Var_asml)e.getV2().accept(this);
		FDiv_asml retour = new FDiv_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type Eq et la renvoie
	 * @return une Expression de type Eq modifié
	 */
	public Exp_asml visit(KEq e) {
		Var_asml e1 = (Var_asml)e.getV1().accept(this);
		Var_asml e2 = (Var_asml)e.getV2().accept(this);
		Eq_asml retour = new Eq_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type LE et la renvoie
	 * @return une Expression de type LE modifié
	 */
	public Exp_asml visit(KLE e) {
		Var_asml e1 = (Var_asml)e.getV1().accept(this);
		Var_asml e2 = (Var_asml)e.getV2().accept(this);
		LE_asml retour = new LE_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type If et la renvoie
	 * @return une Expression de type If modifié
	 */
	public Exp_asml visit(KIf e) {
		Exp_asml e1 = e.getE1().accept(this);
		Exp_asml e2 = e.getE2().accept(this);		
		Exp_asml e3 = e.getE3().accept(this);
		If_asml retour = new If_asml(e1,e2,e3);
		return retour;
	}

	/**
	 * Visit une Expression de type Let et la renvoie
	 * @return une Expression de type Let modifié
	 */
	public Exp_asml visit(KLet e) {
		Id id = e.getId();
		
		if ((symbol_table.get(path +" "+ id)) instanceof TFun)
			Fonction_Closure.add(path + " " +id.getId());
		
		
		//list_var.add(id.toString());
		list_var_def.add(path + " " +id.toString());
		Type t = e.getT();
		boolean AjouterClosure = false;
		Let_asml retour,var_float;
		Exp_asml e1 = e.getE1().accept(this);
		Id closure_name = null;
		
		
		
		
		
		
		
		if(e1 instanceof App_asml){
			Id closure = ((App_asml) e1).getE().getId();
			//System.out.println(closure);
			if(func_closure.contains(closure.getId())){
				//retour = new Let_asml(id,new TClosure(),e1,e2);
				//System.out.println("ici");
				func_closure.add(id.getId());
			}
			
		
			/*if (((TFun)symbol_table.get(path +" "+ closure.getId())).getRetour() instanceof TFun)
			{
				closure_name=Id.gen();
				System.out.println("On Ajoute : " + new Var_asml(id) +" ; " + closure_name.getId());
				//trans.add(new Couple_var_asml(new Var_asml(id),new Var_asml(closure_name),path));
				AjouterClosure = true;
				list_var.add(id.getId().toString());
				symbol_table.put(path + " "+ closure_name.getId(), (TFun)symbol_table.get(path +" "+ id.getId()));
			}*/
			
		}
		/*else if (e1 instanceof AppC_asml)
		{	
			Id closure = ((AppC_asml) e1).getE().getId();
			if (((TFun)symbol_table.get(path +" "+ closure.getId())).getRetour() instanceof TFun)
			{
				closure_name=Id.gen();
				System.out.println("On Ajoute : " + ((AppC_asml) e1).getE().getId().getId() +" ; " + closure_name.getId());
				//trans.add(new Couple_var_asml(new Var_asml(id),new Var_asml(closure_name),path));
				AjouterClosure = true;
				list_var.add(id.getId().toString());
				symbol_table.put(path + " "+ closure_name.getId(), (TFun)symbol_table.get(path +" "+ id.getId()));
			}
		}*/
		
		
		
		Exp_asml e2 = e.getE2().accept(this);
		if (AjouterClosure)
		{
			//System.out.println("ICI");
			Let_memory_store_asml alloc = new Let_memory_store_asml(Id.gen(),new TUnit(),new Var_asml(closure_name),new Int_asml(0),new Var_asml(id),e2);
			e2 = alloc;
			e2 = new Let_memory_alloc_asml(closure_name,new TClosure(),new Int_asml(1),e2);
		}
		if(e1 instanceof Float_asml){
			var_float = new Let_asml(id,t,e1,new Unit_asml());
			retour = new Let_asml(new Id(Id.gen().getId()+"_addr"),t,new Var_asml(id,new TFloat()),e2);
			ls.add(var_float);
		}else if(e1 instanceof App_asml){
			Id closure = ((App_asml) e1).getE().getId();
			if(func_closure.contains(closure.getId())){
				retour = new Let_asml(id,new TClosure(),e1,e2);
				//func_closure.add(id.getId().substring(1));
			}else{
				retour = new Let_asml(id,TFun.gen(((App_asml) e1).getEs().size()),e1,e2);
			}
		}else{
			retour = new Let_asml(id,t,e1,e2);
		}
		

		
		return retour;
	}
	
	
	/**
	 * Visit une Expression de type Var et la renvoie
	 * @return une Expression de type Var modifié
	 */
	/*
	public Exp_asml visit(KVar e) {
		Id id = e.getId();
		//System.out.println("ID : " +id.getId().toString());
		Var_asml retour;
		retour = new Var_asml(id,new TUnit());
		int index=containt_List_couple_var_asml(retour,path);
		if(index != -1)
			retour = trans.get(index).getV2();
		//System.out.println("TAMER " +index);
		
		list_var.add(id.toString());
		return retour;
	}*/


	public Exp_asml visit(KVar e) {
		Id id = e.getId(),new_id;
		String path_temp = new String(path);
		//System.out.println("ID :" + id.getId());
		//print(trans);
		
		
		if(path_temp.endsWith(id.getId()))
		{
			path_temp = path_temp.replace(" "+id.toString(),"");
		}
		
		Var_asml retour = null;
		if(list_var.contains(path+" " +id.toString())){
			new_id = id;
			
			retour = new Var_asml(new_id,new TUnit());
			//trans.add(new Couple_var_asml(origin,retour,path_temp));
			//list_var.add(path+" "+new_id.toString());
		}
		else{
			Var_asml origin = new Var_asml(id,new TUnit());
			int index=containt_List_couple_var_asml(origin,path_temp);
			//System.out.println("Path :" + path_temp);
			if(index != -1)
			{
				retour = trans.get(index).getV2();
				symbol_table.put(path + " "+ retour.getId(), (TFun)symbol_table.get(path +" "+ id.getId()));
				//list_var.add(retour.getId().getId());
			}else
			{
				new_id = id;
				retour = new Var_asml(new_id,new TUnit());
				//trans.add(new Couple_var_asml(retour,retour,path_temp));
				list_var.add(path+" "+ new_id.toString());
			}
			
		
		}	
		
		return retour;
	}

//	private String Get_path(String var, Enumeration<String> path){
//		String temp = new String();
//		temp=path.nextElement();
//		while(path.hasMoreElements()&&temp.indexOf(var, 0)!=temp.length()-var.length()){
//			temp=path.nextElement();
//		}
//		return temp;
//	}
	
	private void print(List<Couple_var_asml> l){
		for(Couple_var_asml cpl:l){
			cpl.print();
		}
	}
	/**
	 * Visit une Expression de type LetRec et la renvoie
	 * @return une Expression de type LetRec modifié
	 */
	public Exp_asml visit(KLetRec e) {
		
		Id id = new Id(e.getFd().getId().toString()), closure_name = Id.gen();
		boolean CreerClosure = false;
		List<Id> args = new ArrayList<Id>(e.getFd().getArgs());
		path=path+" "+id.toString();
		for(Id arg_id:args)
		{
			if ((symbol_table.get(path +" "+ arg_id)) instanceof TFun)
				Fonction_Closure.add(path + " " +arg_id.getId());
			
		}
		
		List<String> temp = new ArrayList<String>(),list_var_alloc = new ArrayList<String>(),list_func = MinMlLibrary.getPredefinedSymbols();
		Let_memory_store_asml alloc;
		Let_memory_load_asml new_addr;
		Int_asml offset;
		Couple_var_asml couple = null;
		
		
		
		
		Exp_asml e1 = e.getFd().getE().accept(this);
		temp.clear();
		temp.addAll(list_var);
		int i=0,size;
		/*System.out.println("On est ici : " +id.getId());
		System.out.println("List_var");
		print_list(list_var);
		System.out.println("FIN List_var");
		System.out.println("List_var_def");
		print_list(list_var_def);
		System.out.println("FIN List_var_def");
		*/
		// SI une Var n'est ni declarer dans la fonction , ni passer en argument alors faire une allocation
		
		for(String str:list_var){
			if((!symbol_table.containsKey(str)) && (!containt_List_String(list_func,str))){
				if(!list_var_def.contains(str)){
					if((!containt_List_ID(args,str))&&(!str.equals(path+ " "+ id.getId()))){ //TODO Seconde pas traité , renvoi toujours vrai
						i++;
						size = i;
						//System.out.println("A allouer :" +last_elt_path(str));
						
						//int index = containt_List_couple_var_asml(new Var_asml(new Id(str)),path);
						/*if (index != -1)
						{
							Id var_name = new Id(trans.get(index).getV2().getId().toString());
							offset = new Int_asml(size*4);
							new_addr = new Let_memory_load_asml(var_name,new TVar(str),new Var_asml(new Id("%self"),new TUnit()),offset,e1);
							e1=new_addr;
							list_var_alloc.add(str);
						}
						else
						{*/
							String no_path_str = last_elt_path(str);
							Id var_name = new Id(no_path_str);
							offset = new Int_asml(size*4);
							new_addr = new Let_memory_load_asml(var_name,new TVar(no_path_str),new Var_asml(new Id("%self"),new TUnit()),offset,e1);
							e1=new_addr;
							list_var_alloc.add(no_path_str);
							//couple = new Couple_var_asml(new Var_asml(new Id(str)),new Var_asml(var_name),new String(path));
							symbol_table.put(str, new TVar(no_path_str));
							
							//trans.add(couple);
						//}
							//System.out.println("-----");
						//print(trans);
					}
				}
			}
		}
		list_var.clear();
		list_var.addAll(temp);
		
		
		
		
		
		/*LetRec_asml retour;
		if(!list_var_alloc.isEmpty()){
			FunDef_asml fd = new  FunDef_asml(id,new TClosure(),args,e1);
			retour = new LetRec_asml(fd,new Unit_asml());
			func_closure.add(id.getId());
		}else{
			FunDef_asml fd = new  FunDef_asml(id,((TFun)symbol_table.get(path)).getRetour(),args,e1);
			retour = new LetRec_asml(fd,new Unit_asml());
		}
		
		ls.add(retour);*/
		path = path.replace(" "+id.toString(),"");
		for(String var_alloc:list_var_alloc)
		{
			list_var.add(path +" "+var_alloc);
		}
		if(!list_var_alloc.isEmpty()){
			
			symbol_table.put(path + " "+ closure_name.getId(), (TFun)symbol_table.get(path +" "+ id.getId()));
			trans.add(new Couple_var_asml(new Var_asml(id),new Var_asml(closure_name),path));
			//print(trans);
			
			List_closure_fonction.add(new Closure_Fonction_Var(list_var_alloc,id.getId()));
			
			
		}

		//System.out.println("List var alloc");
		//print_list(list_var_alloc);
		//System.out.println("FIN List var alloc");

		Exp_asml e2 = e.getE().accept(this);
		if(!list_var_alloc.isEmpty()){
			for(int j = list_var_alloc.size()-1;j>=0;j--){
				String name = new String();
				name = list_var_alloc.get(j);
				Exp_asml part_d = e2.accept(new Return_Exp_asml_func());
				Var_asml var = new Var_asml(new Id(name));
				//System.out.println("VAR :"+var.getId().getId());
				//System.out.println("VAR : "+var.getId().getId());
				/*if(list_var.contains(name)){
					Id new_id = Id.gen();
					Var_asml Var_origin = new Var_asml(new Id (name));
					//trans.add(new Couple_var_asml(Var_origin,new Var_asml(new_id),path));
				}*/
				/*int index = containt_List_couple_var_asml(var,path);
				if(index!=-1){
					var = trans.get(index).getV2();
					
				}*/
				//System.out.println("VAR :"+var.getId().getId());
				//print(trans);
				if(part_d instanceof Var_asml && func_closure.contains(((Var_asml) part_d).getId().getId())){
					alloc = new Let_memory_store_asml(Id.gen(),new TUnit(),new Var_asml(closure_name),new Int_asml((j+1)*4),var,new Var_asml(closure_name));
				}else{
					alloc = new Let_memory_store_asml(Id.gen(),new TUnit(),new Var_asml(closure_name),new Int_asml((j+1)*4),var,e2);
				}
				e2 = alloc;
			}
			Id id_addr = Id.gen(); 
			//System.out.println("ON FAIT DES RAJOUTS : SUR : "+ closure_name.getId());
			alloc = new Let_memory_store_asml(Id.gen(),new TUnit(),new Var_asml(closure_name),new Int_asml(0),new Var_asml(id_addr),e2);
			e2 = alloc;
			Let_asml Let_addr = new Let_asml(id_addr,new TUnit(),new Var_asml(id),e2);
			e2 = Let_addr;
			e2 = new Let_memory_alloc_asml(closure_name,new TClosure(),new Int_asml((i+1)*4),e2);
			
		}

		/*if (((TFun)symbol_table.get(path +" "+ id.getId())).getRetour() instanceof TFun)
		{
			closure_name=Id.gen();
			alloc = new Let_memory_store_asml(new Id("_addr"+id.toString()),new TUnit(),new Var_asml(closure_name),new Int_asml(0),new Var_asml(id),e2);
			e2 = alloc;
			e2 = new Let_memory_alloc_asml(closure_name,new TClosure(),new Int_asml(1),e2);
		}
		*/
		
		path=path+" "+id.toString();
		List<Id> new_args = new ArrayList<Id>();
		for(Id id_arg:args)
		{
			Var_asml tmp = new Var_asml(id_arg);
		/*	int index = containt_List_couple_var_asml(tmp, path);
			if(index != -1)
			{
			
				new_args.add(trans.get(index).getV2().getId());
			}
			else*/
				new_args.add(id_arg);
		}
		
		
		LetRec_asml retour;
		
		if(!list_var_alloc.isEmpty()){
			FunDef_asml fd = new  FunDef_asml(id,new TClosure(),new_args,e1);
			retour = new LetRec_asml(fd,new Unit_asml());
			func_closure.add(id.getId());
		}else{
			FunDef_asml fd = new  FunDef_asml(id,((TFun)symbol_table.get(path)).getRetour(),new_args,e1);
			retour = new LetRec_asml(fd,new Unit_asml());
		}
		
		ls.add(retour);
		path = path.replace(" "+id.toString(),"");
		return e2;
	}
	
	private void print_list(List<String> l){
		for(String str:l){
			System.out.println(str);
		}
	}
	/**
	 * Visit une Expression de type App et la renvoie
	 * @return une Expression de type App modifié
	 *//*
	public Exp_asml visit(KApp e) {
		Var_asml e1 = (Var_asml)e.getV().accept(this);
		Var_asml ret;
		App_asml retour_app;
		Id id = Id.gen();
		AppC_asml retour_appC;
		List<String> list_func = new ArrayList<String>(MinMlLibrary.getPredefinedSymbols());
		List<Var_asml> le = new ArrayList<Var_asml>(accept_list(e.getVs()));
		if(e1 instanceof Var_asml){
			id = ((Var_asml) e1).getId();
			//print_list(func_closure);
			if(list_func.contains(id.getId())){
				//System.out.println("fonc-sys");
				ret = new Var_asml(new Id("min_caml_"+((Var_asml) e1).getId().toString()),TFun.gen(e.getVs().size()));
			}else if(func_closure.contains(id.getId())){
				//System.out.println("fonc-closure");
				ret = new Var_asml(id, new TClosure());
			}else{
				//System.out.println("fonc-norm");
				ret = new Var_asml(new Id(((Var_asml) e1).getId().toString()),TFun.gen(e.getVs().size()));
			}
		}else{
			ret = e1;
		}
		if(func_closure.contains(id.getId())){
			retour_appC = new AppC_asml(ret,le);
			return retour_appC;
		}else{
			retour_app = new App_asml(ret,le);
			return retour_app;
		}
	}
	

	*/
	public Exp_asml visit(KApp e) {
		Var_asml e1 = (Var_asml)e.getV().accept(this);
		Var_asml ret;
		Id id = e1.getId();
		List<String> list_func = new ArrayList<String>(MinMlLibrary.getPredefinedSymbols());
		List<Var_asml> le = new ArrayList<Var_asml>(accept_list(e.getVs()));
		
		
		print_list(Fonction_Closure);
		//Si e1 est dans list-func , --> app
		//
		
		if(list_func.contains(id.getId())){
		
			//System.out.println("fonc-sys");
			ret = new Var_asml(new Id("min_caml_"+((Var_asml) e1).getId().toString()),TFun.gen(e.getVs().size()));
			return new App_asml(ret,le);
		}
		else if(list_func.contains(id.getId()))
		{
			ret = new Var_asml(id, new TClosure());
			return new AppC_asml(ret,le);
		}
		else  
		{
			if(Fonction_Closure.contains(path+ " "+ id.getId()))
			{
				ret = new Var_asml(id, new TClosure());
				return new AppC_asml(ret,le);
			}
			
			//System.out.println("COUCOU path :" +path);
			int index = Estdans_list_Closure_Fonction(path);
			
			if (index != -1)
			{
				ret = new Var_asml(id, new TClosure());
				return new AppC_asml(ret,le);
			}
			
			
			Var_asml tmp = new Var_asml(e.getV().getId());
			
			if (!tmp.getId().getId().equals(id.getId()))
			{
					//System.out.println("COUCOU");
					ret = new Var_asml(id, new TClosure());
					return new AppC_asml(ret,le);
			}	
			else
			{	
					ret = new Var_asml(new Id(((Var_asml) e1).getId().toString()),TFun.gen(e.getVs().size()));
					return new App_asml(ret,le);
			}
			
			
		}
		
		
	}
	/**
	 * Visit une Expression de type Tuple et la renvoie
	 * @return une Expression de type Tuple modifié
	 */
	public Exp_asml visit(KTuple e) {
		System.err.println("Not implemented");
		System.exit(1);
		return null;
	}

	/**
	 * Visit une Expression de type LetTuple et la renvoie
	 * @return une Expression de type LetTuple modifié
	 */
	public Exp_asml visit(KLetTuple e) {
		System.err.println("Not implemented");
		System.exit(1);
		return null;
	}

	/**
	 * Visit une Expression de type Array et la renvoie
	 * @return une Expression de type Array modifié
	 */
	public Exp_asml visit(KArray e) {
		System.err.println("Not implemented");
		System.exit(1);
		Var_asml e1 = (Var_asml)e.getV1().accept(this);
		Var_asml e2 = (Var_asml)e.getV2().accept(this);
		Array_asml retour = new Array_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type Get et la renvoie
	 * @return une Expression de type Get modifié
	 */
	public Exp_asml visit(KGet e) {
		System.err.println("Not implemented");
		System.exit(1);
		Var_asml e1 = (Var_asml)e.getV1().accept(this);
		Var_asml e2 = (Var_asml)e.getV2().accept(this);
		Get_asml retour = new Get_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type Put et la renvoie
	 * @return une Expression de type Put modifié
	 */
	public Exp_asml visit(KPut e) {
		System.err.println("Not implemented");
		System.exit(1);
		Var_asml e1 = (Var_asml)e.getV1().accept(this);
		Var_asml e2 = (Var_asml)e.getV2().accept(this);		
		Var_asml e3 = (Var_asml)e.getV3().accept(this);
		Put_asml retour = new Put_asml(e1,e2,e3);
		return retour;
	}

	
	/*
	public List<Exp> Get_ls(){
		return this.ls;
	}
	*/
	
	
/*	private void add_List_couple_var_asml(Couple_var_asml cpl)
	{
		int index = -1;
		for(int i = 0; i < trans.size();i++)
		{
			if(trans.get(i).getV1().getId().getId().equals(cpl.getV1().getId().getId()))
			{
				index = i;
			}
		}
		if(index != -1)
			trans.remove(index);
		trans.add(cpl);
	}*/
	
	
	private int containt_List_couple_var_asml(Var_asml v,String p)
	{
		int index = -1;
		
		for(int i = 0; i < trans.size();i++)
		{
			if(trans.get(i).getV1().getId().getId().equals(v.getId().getId()) && trans.get(i).getPath().equals(p))
			{
				index = i;
			}
		}
		return index;
		
	}
	
	
	private boolean containt_List_ID(List<Id> args, String var)
	{
		for(Id arg:args)
		{
			if(var.endsWith(arg.getId()))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean containt_List_String(List<String> args, String var)
	{
		for(String arg:args)
		{
			if(var.endsWith(arg))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Renvoi le dernier element d'un path passer un parametre
	 * @param p un String qui est de la forme d'un path
	 * @return un String qui correspond au nom d'une var ou d'une fonction
	 */
	private String last_elt_path(String p)
	{
		String tmp = new String(p);
		int index = tmp.lastIndexOf(" ");
		tmp = tmp.substring(index+1,tmp.length());
		return tmp;
	}
	
	
	private int Estdans_list_Closure_Fonction(String p)
	{
		int index = -1;
		for(int i = 0;i < List_closure_fonction.size();i++)
		{
			if (List_closure_fonction.get(i).getFonction().equals(p))
			{
				index = i;
			}
		}
		return index;
	}
	
}