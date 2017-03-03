package BackEnd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import BackEnd.Position.Label_Fonction;
import BackEnd.Position.Position_tuple;
import BackEnd.Position.Position_variable;
import BackEnd.Position.Table_de_symboles;
import Code_fourni.Id;
import Expression_asml.*;
import Normalization_Reduction.ExpToType;
import Type.TClosure;
import Type.TFun;
import Type.TInt;
import Type.TUnit;
import Type.Type;

public class Backend_visitor implements Asml.ObjVisitor_asml<Exp_asml> {
	
	VariableAllocator allocator;
	boolean debug;

	/**
	 * Constructeur, attribue un allocateur au Visitor
	 * @param allocator l'allocateur de variables à assigner
	 */
	public Backend_visitor(VariableAllocator allocator)
	{
		this.allocator = allocator;
		debug = true;
	
	}
	
	/**
	 * Constructeur, attribue un allocateur au visiteur et choisi ou non le mode débug 
	 * @param allocator l'allocateur de variables à assigner
	 * @param debug: vrai => les messages de débug seront affichés 
	 */
	public Backend_visitor(VariableAllocator allocator,boolean debug)
	{
		this.allocator = allocator;
		this.debug = debug;
	}
	/**
	 * Prend une Expression en argument et en renvoi une nouvelle 
	 * @param ast une Expression
	 * @return une nouvelle Expression transformé
	 */
	public Exp_asml Start(Exp_asml ast)
	{
		
		return ast.accept(this);
	}
	
	/**
	 * Visit une Expression de type Unit et la renvoie
	 * @param e une Expression de type Unit 
	 * @return une Expression de type Unit modifié 
	 */
	public Exp_asml visit(Unit_asml e) {
		Unit_asml retour = new Unit_asml();
		return retour;
	}
	
	
	/**
	 * Visit d'une Liste d'Expression et la renvoie
	 * @param e une Expression de type List<Exp_asml> 
	 * @return une Expression de type List<Exp_asml> modifié 
	 */
	private List<Var_asml> accept_list(List<Var_asml> l) {
        List<Var_asml> retour = new ArrayList<Var_asml>();
		if (l.isEmpty()) {
            return retour;
        }
        Iterator<Var_asml> it = l.iterator();
        Var_asml exp = (Var_asml)it.next().accept(this);
        retour.add(exp);
        while (it.hasNext()) {
            exp = (Var_asml)it.next().accept(this);
            retour.add(exp);
        }
        return retour;
    }
	
	/**
	 * Visit une Expression de type Bool_asml et la renvoie
	 * @param e une Expression de type Bool_asml 
	 * @return une Expression de type Bool_asml modifié 
	 */
	public Exp_asml visit(Bool_asml e) {
		//TODO
		Bool_asml retour = new Bool_asml(e.isB());
		return retour;
	}

	/**
	 * Visit une Expression de type Int_asml et la renvoie
	 * @param e une Expression de type Int_asml 
	 * @return une Expression de type Int_asml modifié 
	 */
	public Exp_asml visit(Int_asml e) {
		ImmediateOptimizer optimizer = new ImmediateOptimizer();
		if (!optimizer.immediateIsPossible(e.getI())){
			Id id = new Id("label"+Id.genARM().getId());
			allocator.assignerLabelInt(id.getId(), e.getI());
			return new Var_asml(id);
		}
		return e;
	}

	/**
	 * Visit une Expression de type Float et la renvoie
	 * @param e une Expression de type Float 
	 * @return une Expression de type Float modifié 
	 */
	public Exp_asml visit(Float_asml e) {
		Float_asml retour = new Float_asml(e.getF());
		return retour;
	}

	/**
	 * Visit une Expression de type Not et la renvoie
	 * @param e une Expression de type Not 
	 * @return une Expression de type Not modifié 
	 */
	public Exp_asml visit(Not_asml e) {
		Var_asml e1 = (Var_asml)e.getE().accept(this);
		Not_asml retour = new Not_asml(e1);
		return retour;
		
	}
	/**
	 * Visit une Expression de type Neg et la renvoie
	 * @param e une Expression de type Neg 
	 * @return une Expression de type Neg modifié 
	 */
	public Exp_asml visit(Neg_asml e) {
		Var_asml e1 = (Var_asml)e.getE().accept(this);
		Neg_asml retour = new Neg_asml(e1);
		return retour;
	}
	/**
	 * Visit une Expression de type Add et alloue les paramètres de celle ci dans la table des symboles en fonction de la stratégie choisie
	 * @param e une Expression de type Add 
	 * @return une Expression de type Add modifiée
	 */
	public Exp_asml visit(Add_asml e) {
		Var_asml e1 = (Var_asml)e.getE1();
		Var_asml e2 = (Var_asml)e.getE2();
		/*if(e1 instanceof Int_asml){
			int n = ((Int_asml) e1).getI();
			e1 = e1.accept(this);
			Add retour = new Add(e1,e2);
			Let let = new Let(((Var_asml)e1).getId(),ExpToType.convertir(retour),new Int_asml(n),retour);
			return let;
		}*/
		e1 = (Var_asml)e1.accept(this);
		//if(!(e2 instanceof Int_asml))
			e2 = (Var_asml)e2.accept(this);
		Add_asml retour = new Add_asml(e1,e2);
		return retour;
	}
	/**
	 * Visit une Expression de type Sub et alloue les paramètres de celle ci dans la table des symboles en fonction de la stratégie choisie
	 * @param e une Expression de type Sub 
	 * @return une Expression de type Sub modifiée
	 */
	public Exp_asml visit(Sub_asml e) {
		Var_asml e1 = (Var_asml)e.getE1();
		Var_asml e2 = (Var_asml)e.getE2();
		/*if(e1 instanceof Int_asml){
			int n = ((Int_asml) e1).getI();
			e1 = e1.accept(this);
			Sub retour = new Sub(e1,e2);
			Let let = new Let(((Var_asml)e1).getId(),ExpToType.convertir(retour),new Int_asml(n),retour);
			return let;
		}*/
		e1 = (Var_asml)e1.accept(this);
		//if(!(e2 instanceof Int_asml))
			e2 = (Var_asml)e2.accept(this);
		Sub_asml retour = new Sub_asml(e1,e2);
		return retour;
	}
	
	/**
	 * Visit une Expression de type FNeg et la renvoie
	 * @param e une Expression de type FNeg 
	 * @return une Expression de type FNeg modifié
	 */
	public Exp_asml visit(FNeg_asml e) {
		Var_asml e1 = (Var_asml)e.getE().accept(this);
		FNeg_asml retour = new FNeg_asml(e1);
		return retour;
	}

	/**
	 * Visit une Expression de type FAdd et la renvoie
	 * @param e une Expression de type FAdd 
	 * @return une Expression de type FAdd modifié
	 */
	public Exp_asml visit(FAdd_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		FAdd_asml retour = new FAdd_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type FSub et la renvoie
	 * @param e une Expression de type FSub 
	 * @return une Expression de type FSub modifié
	 */
	public Exp_asml visit(FSub_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		FSub_asml retour = new FSub_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type FMul et la renvoie
	 * @param e une Expression de type FMul 
	 * @return une Expression de type FMul modifié
	 */
	public Exp_asml visit(FMul_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		FMul_asml retour = new FMul_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type FDiv et la renvoie
	 * @param e une Expression de type FDiv 
	 * @return une Expression de type FDiv modifié
	 */
	public Exp_asml visit(FDiv_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		FDiv_asml retour = new FDiv_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type Eq et la renvoie
	 * @param e une Expression de type Eq 
	 * @return une Expression de type Eq modifié
	 */
	public Exp_asml visit(Eq_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		Eq_asml retour = new Eq_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type LE et la renvoie
	 * @param e une Expression de type LE 
	 * @return une Expression de type LE modifié
	 */
	public Exp_asml visit(LE_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		LE_asml retour = new LE_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type If et la renvoie
	 * @param e une Expression de type If 
	 * @return une Expression de type If modifié
	 */
	public Exp_asml visit(If_asml e) {
		Exp_asml e1 = e.getE1().accept(this);
		Exp_asml e2 = e.getE2().accept(this);		
		Exp_asml e3 = e.getE3().accept(this);
		If_asml retour = new If_asml(e1,e2,e3);
		return retour;
	}
	
	/**
	 * Visit une Expression de type Let et la renvoie
	 * @param e une Expression de type Let 
	 * @return une Expression de type Let modifié
	 */
	public Exp_asml visit(Let_asml e) {
		//TODO
		//Ne marche que pour les déclarations de variable
		Id id = e.getId();
		Var_asml variable = new Var_asml(id);
		Type t = e.getT();
		Exp_asml e1 = e.getE1();
		Exp_asml e2 = e.getE2();
		Let_asml retour;
		
		if(id.getId().equals("_")){
			allocator.assignerLabelFonction(variable.getId().getId(),new ArrayList<String>());
			e1 = e1.accept(this);
			retour = new Let_asml(variable.getId(),new TUnit(),e1,new Unit_asml());
		}
		else if(id.getId().charAt(0) == '_'){
			//allocator.assignerLabelFloat(variable.getId().getId(),((Float)e.getE1()).getF());
			e1 = e1.accept(this);
			retour = new Let_asml(variable.getId(),new TUnit(),e1,new Unit_asml());
		}
		else {
			
			/*if(e1 instanceof Tuple_asml){
				List<String> id_list = tupleNames((Tuple_asml)e1);
				allocator.allouerTuple(variable.getId().getId(),id_list);
			}
			else*/
				variable = (Var_asml)variable.accept(this);
						
			e1 = e1.accept(this);
			e2 = e2.accept(this);
			retour = new Let_asml(variable.getId(),t,e1,e2);
		}
		return retour;
	}

	/**
	 * Visit une Expression de type Var_asml et la renvoie
	 * @param e une Expression de type Var_asml 
	 * @return une Expression de type Var_asml modifié
	 */
	public Exp_asml visit(Var_asml e) {
		//TODO
		String nom_variable = e.getId().getId();
		Var_asml retour;
		int res_alloc = allocator.allouerVariable(nom_variable);
		if(res_alloc == -1)
			return e;
	
		//Id id = new Id("r" +res_alloc);
		Id id = new Id(nom_variable);
		retour = new Var_asml(id);
		if(debug){
			switch(allocator.strategie){
			case BASIC :
				System.out.println(e.getId().getId()+" -> "+"r"+res_alloc);
				break;
			case STACK :
				System.out.println(e.getId().getId() +" -> [fp, #"+ res_alloc+"]");
				break;
			default : 
				break;
			}
		}	
		return retour;
	}


	/**
	 * Visit une Expression de type LetRec et la renvoie
	 * @param e une Expression de type LetRec 
	 * @return une Expression de type LetRec modifié
	 */
	public Exp_asml visit(LetRec_asml e) {
		//TODO
		Id id_retour = Id.genARM();
		Id id_closure = Id.genARM();
		Id id = e.getFd().getId();
		id = rename_fun(id);
		LetRec_asml retour=null;
		
		ArrayList<String>args = new ArrayList<String>();
		for(Id id_args : e.getFd().getArgs())
			args.add(id_args.getId());
		allocator.assignerLabelFonction(id.getId(),args);
		
		//Construire la table de symbole de l'environnement de la fonction
		Exp_asml e1 = e.getFd().getE();
		
		//e1 = e1.accept(this);
		Table_de_symboles table_fun = ((Label_Fonction)allocator.getPosition(id)).getTable_symbole();
		e1.accept(new Backend_visitor(new VariableAllocator(table_fun),debug));
		
		if(debug){
			System.out.println("\tTABLE DE SYMBOLE DE "+((Label_Fonction)allocator.getPosition(id)).getId());
			((Label_Fonction)allocator.getPosition(id)).getTable_symbole().afficheTableSymbole();
			System.out.println();
		}
		e1 = e1.accept(this);
		
		if(e.getFd().getType() instanceof TUnit){
			//Si la fonction est void
			FunDef_asml fd = new FunDef_asml(id,e.getFd().getType(),e.getFd().getArgs(),e1);
			
			Exp_asml e2 = new Unit_asml();
			retour = new LetRec_asml(fd,e2);
		}	
		else{
			//Si la fonction renvoie un résultat on le stocke dans une variable retour
			//Grace à un let retour = <corps de la fonction>
			table_fun.addRetour(id_retour.getId());
			table_fun.addClosure(id_closure.getId());
			FunDef_asml fd = new  FunDef_asml(id,e.getFd().getType(),e.getFd().getArgs(),
					new Let_asml(id_retour,e.getFd().getType(), e1, new Unit_asml()));
			
			Exp_asml e2 = new Unit_asml();
			retour = new LetRec_asml(fd,e2);
		}
		return retour;
	}
	
	/**
	 * Visite une Expression de type App, assigne un label à la fonction appelée et crée sa table des symboles
	 * @param e une Expression de type App 
	 * @return la même Expression de type App
	 */
	public Exp_asml visit(App_asml e) {
		Var_asml e1 = (Var_asml)e.getE();
		List<Var_asml> es = e.getEs();
		List<String> args = new ArrayList<String>();
		for(Var_asml exp : es){
			if(exp instanceof Var_asml)
				args.add(((Var_asml)exp).getId().getId());
		}
		List<Var_asml> le = new ArrayList<Var_asml>(accept_list(e.getEs()));
		//TODO
		//Cas où on fait un < call (let x = ? in call _fun x) arg > par exemple
		e1 = new Var_asml(rename_fun(e1.getId()));
		allocator.assignerLabelFonction(((Var_asml)e1).getId().getId(),args);
		App_asml retour = new App_asml(e1,le);
		return retour;
	}
	
	
	
	

	/**
	 * Visit une Expression de type Tuple et la renvoie
	 * @param une Expression de type Tuple 
	 * @return une Expression de type Tuple modifié
	 */
	/*public Exp_asml visit(Tuple_asml e) {
		List<Var_asml> var_list = new ArrayList<Var_asml>(accept_list(e.getEs()));
		
		
		Tuple_asml retour = new Tuple_asml(var_list);
		return retour;
	}
	public List<String> tupleNames(Tuple_asml e) {
		List<Var_asml> var_list = new ArrayList<Var_asml>(e.getEs());
		List<String> id_list = new ArrayList<String>();
		for (Var_asml var : var_list){
			allocator.allouerVariable(var.getId().getId());
			id_list.add(var.getId().getId());
		}
		return id_list;
	}*/
	/**
	 * Visit une Expression de type LetTuple et la renvoie
	 * @param une Expression de type LetTuple 
	 * @return une Expression de type LetTuple modifié
	 */
	/*public Exp_asml visit(LetTuple_asml e) {
		List<Id> ids = new ArrayList<Id>(e.getIds());
		List<Type> ts = new ArrayList<Type>(e.getTs());
		Exp_asml e1 = e.getE1().accept(this);
		Exp_asml e2 = e.getE2().accept(this);
		
		//
		Position_variable pos = allocator.getTable_symboles().getPosition(((Var_asml)e1).getId().getId());
		for(int i=0;i<ids.size();i++){
			//Faire correspondre aux variables du tuple les positions existantes dans la variable en paramètre
			allocator.assignerPosition(ids.get(i).getId(),
				allocator.getTable_symboles().getPosition(((((Position_tuple)pos).getElements().get(i)))));
		}
		
		LetTuple_asml retour = new LetTuple_asml(ids,ts,e1,e2);
		return retour;
	}*/
	
	/**
	 * Visit une Expression de type Array et la renvoie
	 * @param e une Expression de type Array 
	 * @return une Expression de type Array modifié
	 */
	public Exp_asml visit(Array_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		Array_asml retour = new Array_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type Get et la renvoie
	 * @param e une Expression de type Get 
	 * @return une Expression de type Get modifié
	 */
	public Exp_asml visit(Get_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		Get_asml retour = new Get_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type Put et la renvoie
	 * @param e une Expression de type Put 
	 * @return une Expression de type Put modifié
	 */
	public Exp_asml visit(Put_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);		
		Var_asml e3 = (Var_asml)e.getE3().accept(this);
		Put_asml retour = new Put_asml(e1,e2,e3);
		return retour;
	}

	/**
	 * 
	 */
	@Override
	public Exp_asml visit(AppC_asml e) {
		// TODO Auto-generated method stub
		Var_asml e1 = (Var_asml)e.getE();
		List<Var_asml> es = e.getEs();
		List<String> args = new ArrayList<String>();
		for(Var_asml exp : es){
			if(exp instanceof Var_asml)
				args.add(((Var_asml)exp).getId().getId());
		}
		List<Var_asml> le = new ArrayList<Var_asml>(accept_list(e.getEs()));
		//TODO
		//Cas où on fait un < call (let x = ? in call _fun x) arg > par exemple
		allocator.assignerLabelFonction(((Var_asml)e1).getId().getId(),args);
		AppC_asml retour = new AppC_asml(e1,le);
		return retour;
	}

	/**
	 * 
	 */
	@Override
	public Exp_asml visit(Let_memory_load_asml e) {
		Id id = e.getId();
		Type t = e.getT();
		Var_asml self = e.getAddr();
		Int_asml off = e.getOffset();
		Exp_asml e2 = e.getE2();

		allocator.allouerVariable(id.getId());
		e2 = e2.accept(this);
		
		Let_memory_load_asml retour = new Let_memory_load_asml(id, t, self, off, e2);
		return retour;
	}

	/**
	 * 
	 */
	@Override
	public Exp_asml visit(Let_memory_store_asml e) {
		Id id = e.getId();
		Type t = e.getT();
		Var_asml addr = e.getAddr();
		Int_asml offset = e.getOffset();
		Var_asml val = e.getVal();
		Exp_asml e2 = e.getE2();
		
		e2 = e2.accept(this);
		
		Let_memory_store_asml retour = new Let_memory_store_asml(id, t, addr, offset, val, e2);
		return retour;
	}

	/**
	 * 
	 */
	@Override
	public Exp_asml visit(Let_memory_alloc_asml e) {
		Id id = e.getId();
		Type t = e.getT();
		Int_asml size = e.getOffset();
		Exp_asml e2 = e.getE2();

		e2 = e2.accept(this);
		
		Let_memory_alloc_asml retour = new Let_memory_alloc_asml(id, t, size, e2);
		return retour;
	}
	
	/**
	 * Renomme une fonction
	 * @param id l'id à renommer
	 * @return le nouvel id
	 */
	public static Id rename_fun(Id id){
		id = new Id (id.getId().replace('?', '_'));
		return id;	
	}
	
	
}