package TypeCheck;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import Type.*;
import Expression.*;
import Expression.Float;
import Main.MinMlLibrary;
import Code_fourni.Id;

/**
 * Classe pour la vérification des types d'un AST
 * @author bizarda
 * 
 * Méthode principale : check(Exp AST)
 */
public class TypeChecker{
	
	private boolean is_well_typed;
	private Hashtable<String,Type> symboles;
	private boolean verbose;
	
	/**
	 * Constructeur par défaut
	 */
	public TypeChecker(){
		symboles = new Hashtable<String,Type>();
		this.verbose = false;
	}
	
	/**
	 * Constructeur
	 * @param verbose si le TypeChecker doit afficher la table des symboles/types à la fin de son exécution
	 */
	public TypeChecker(boolean verbose){
		symboles = new Hashtable<String,Type>();
		this.verbose = verbose;
	}

	/**
	 * Méthode principale de check/inférence des types
	 * @param AST l'Exp issue du parser
	 * @return vrai ssi l'expression est correctement typée
	 */
	public boolean Start(Exp AST){
		
		Map <String,Type> predefined = MinMlLibrary.getPredefinedMap();
				/*new Hashtable<String,Type>();
		ArrayList<Type> args = new ArrayList<Type>();
		args.add(new TInt());
		predefined.put(new String("print_int"),new TFun(args,new TUnit()));
		args = new ArrayList<Type>();
		args.add(new TFloat());
		predefined.put(new String("truncate"),new TFun(args,new TInt()));
		args = new ArrayList<Type>();
		args.add(new TUnit());
		predefined.put(new String("print_newline"),new TFun(args,new TUnit()));*/
		
		is_well_typed = true;
		List<TypeEq> equations = genEquations(AST,predefined,new TUnit()," ");
		if(!is_well_typed){
			System.exit(1);
		}
		
		//afficher_symboles();
		resoudreEquations(equations);
		/*
		
		List<ResolvedEq> resolved;
		if(is_well_typed){
			resolved = resoudreEquations(equations);
			
			for(ResolvedEq req : resolved)
				System.out.println(req);
		}*/
		
		//afficher_symboles();

		if(!is_well_typed){
			System.exit(1);
		}
		
		if(verbose)
			afficher_symboles();
		
		return is_well_typed;
	}

	/**
	 * Renvoie la table des symboles calculée par un check préalable.
	 * Si aucun check n'a été effectué, la table est alors vide.
	 * @return la table des symboles. les domaines de définition des variables sont donnés par la forme : "env_sup .. env_inf x"
	 */
	public Hashtable<String,Type> getSymboles(){
		return new Hashtable<String,Type>(symboles);
	}
	
	/**
	 * Genere les equations de types d'apres l'Exp donnée
	 * @param exp l'Exp en question
	 * @param env l'environnement dans lequel traiter exp
	 * @param t type attendu de exp
	 * @param context base pour la table des symboles
	 * @return une liste d'equations issue du traitement de exp
	 */
	private List<TypeEq> genEquations(Exp exp, Map<String,Type> env, Type t, String context){
		
		List<TypeEq> result = new ArrayList<TypeEq>();

    	if(exp instanceof Var){
    		Var varexp = (Var) exp;
    		Type type_var = env.get(varexp.getId().getId());
    		if(type_var == null){
    			System.err.println("Error : Unbound value " + varexp.getId().getId());
    			is_well_typed = false;
    		}
    		TypeEq teq = new TypeEq(type_var,t);
    		result.add(teq);
    	}
    	else if(exp instanceof Unit){
    		TypeEq teq = new TypeEq(new TUnit(),t);
    		result.add(teq);
    	}
    	else if(exp instanceof Bool){
    		TypeEq teq = new TypeEq(new TBool(),t);
    		result.add(teq);
    	}
    	else if(exp instanceof Int){
    		TypeEq teq = new TypeEq(new TInt(),t);
    		result.add(teq);
    	}
    	else if(exp instanceof Float){
    		TypeEq teq = new TypeEq(new TFloat(),t);
    		result.add(teq);
    	}
    	else if(exp instanceof Not){
    		Not notexp = (Not) exp;
    		TypeEq teq = new TypeEq(new TBool(),t);
    		result.add(teq);
    		List<TypeEq> l = genEquations(notexp.getE(), env, new TBool(),context);
    		result.addAll(l);
    	}
    	else if(exp instanceof Neg){
    		Neg negexp = (Neg) exp;
    		TypeEq teq = new TypeEq(new TInt(),t);
    		result.add(teq);
    		List<TypeEq> l = genEquations(negexp.getE(), env, new TInt(),context);
    		result.addAll(l);
    	}
    	else if(exp instanceof FNeg){
    		FNeg fnegexp = (FNeg) exp;
    		TypeEq teq = new TypeEq(new TFloat(),t);
    		result.add(teq);
    		List<TypeEq> l = genEquations(fnegexp.getE(), env, new TFloat(),context);
    		result.addAll(l);
    	}
    	else if(exp instanceof Add){
    		Add addexp = (Add) exp;
    		TypeEq teq = new TypeEq(new TInt(),t);
    		result.add(teq);
    		List<TypeEq> l1 = genEquations(addexp.getE1(), env, new TInt(),context);
    		List<TypeEq> l2 = genEquations(addexp.getE2(), env, new TInt(),context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof Sub){
    		Sub subexp = (Sub) exp;
    		TypeEq teq = new TypeEq(new TInt(),t);
    		result.add(teq);
    		List<TypeEq> l1 = genEquations(subexp.getE1(), env, new TInt(),context);
    		List<TypeEq> l2 = genEquations(subexp.getE2(), env, new TInt(),context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof FAdd){
    		FAdd faddexp = (FAdd) exp;
    		TypeEq teq = new TypeEq(new TFloat(),t);
    		result.add(teq);
    		List<TypeEq> l1 = genEquations(faddexp.getE1(), env, new TFloat(),context);
    		List<TypeEq> l2 = genEquations(faddexp.getE2(), env, new TFloat(),context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof FSub){
    		FSub fsubexp = (FSub) exp;
    		TypeEq teq = new TypeEq(new TFloat(),t);
    		result.add(teq);
    		List<TypeEq> l1 = genEquations(fsubexp.getE1(), env, new TFloat(),context);
    		List<TypeEq> l2 = genEquations(fsubexp.getE2(), env, new TFloat(),context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof FMul){
    		FMul fmulexp = (FMul) exp;
    		TypeEq teq = new TypeEq(new TFloat(),t);
    		result.add(teq);
    		List<TypeEq> l1 = genEquations(fmulexp.getE1(), env, new TFloat(),context);
    		List<TypeEq> l2 = genEquations(fmulexp.getE2(), env, new TFloat(),context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof FDiv){
    		FDiv fdivexp = (FDiv) exp;
    		TypeEq teq = new TypeEq(new TFloat(),t);
    		result.add(teq);
    		List<TypeEq> l1 = genEquations(fdivexp.getE1(), env, new TFloat(),context);
    		List<TypeEq> l2 = genEquations(fdivexp.getE2(), env, new TFloat(),context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof If){
    		If ifexp = (If) exp;
    		List<TypeEq> l = genEquations(ifexp.getE1(), env, new TBool(),context);
    		List<TypeEq> l1 = genEquations(ifexp.getE2(), env, t,context);
    		List<TypeEq> l2 = genEquations(ifexp.getE3(), env, t,context);
    		result.addAll(l);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof Let){
    		Let letexp = (Let) exp;
    		Type type_id = letexp.getT();
    		List<TypeEq> l1 = genEquations(letexp.getE1(), env, type_id, context);
    		Map<String,Type> env2 = new Hashtable<String,Type>(env);
    		env2.put(letexp.getId().getId(), type_id);
    		symboles.put(context + letexp.getId().getId(), type_id);
    		List<TypeEq> l2 = genEquations(letexp.getE2(), env2, t, context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof LetRec){
    		LetRec letrecexp = (LetRec) exp;
    		TFun type_fun = (TFun) letrecexp.getFd().getType();
    		String id_fun = letrecexp.getFd().getId().getId();
    		List<Id> ids_args = letrecexp.getFd().getArgs();
    		Map<String,Type> env2 = new Hashtable<String,Type>(env);
    		String fun_context = context + id_fun + " ";
    		for(int i = 0; i<ids_args.size(); i++){
    			env2.put(ids_args.get(i).getId(), type_fun.getArgsTypes().get(i));
        		symboles.put(fun_context + ids_args.get(i).getId(), type_fun.getArgsTypes().get(i));
    		}
    		env2.put(id_fun, type_fun);

    		List<TypeEq> l1 = genEquations(letrecexp.getFd().getE(), env2, type_fun.getRetour(), fun_context);
    		
    		Map<String,Type> env3 = new Hashtable<String,Type>(env);
    		env3.put(id_fun, type_fun);
    		symboles.put(context + id_fun, type_fun);
    		List<TypeEq> l2 = genEquations(letrecexp.getE(), env3, t, context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof App){
    		App appexp = (App) exp;
    		List<Exp> args = appexp.getEs();
    		List<Type> types_args = new ArrayList<Type>();
    		for(Exp arg : args){
    			Type type_arg = Type.gen();
    			types_args.add(type_arg);
        		List<TypeEq> l = genEquations(arg, env, type_arg, context);
        		result.addAll(l);
    		}
    		Type type_retour = Type.gen();
    		TypeEq teq = new TypeEq(type_retour,t);
    		result.add(teq);
    		Type type_fun = new TFun(types_args,type_retour);
    		List<TypeEq> l = genEquations(appexp.getE(), env, type_fun, context);
    		result.addAll(l);
    	}
    	else if(exp instanceof Eq){
    		Eq eqexp = (Eq) exp;
    		TypeEq teq = new TypeEq(new TBool(),t);
    		result.add(teq);
    		Type type_eq = Type.gen();
			List<TypeEq> l1 = genEquations(eqexp.getE1(), env, type_eq, context);
			List<TypeEq> l2 = genEquations(eqexp.getE2(), env, type_eq, context);
			result.addAll(l1);
			result.addAll(l2);
    	}
    	else if(exp instanceof LE){
    		LE leexp = (LE) exp;
    		TypeEq teq = new TypeEq(new TBool(),t);
    		result.add(teq);
    		Type type_le = Type.gen();
			List<TypeEq> l1 = genEquations(leexp.getE1(), env, type_le, context);
			List<TypeEq> l2 = genEquations(leexp.getE2(), env, type_le, context);
			result.addAll(l1);
			result.addAll(l2);
    	}
    	else if(exp instanceof Tuple){
    		Tuple tupleexp = (Tuple) exp;
    		List<Type> liste_type = new ArrayList<Type>();
    		for(Exp elem : tupleexp.getEs()){
    			Type type_elem = Type.gen();
    			liste_type.add(type_elem);
    			List<TypeEq> l = genEquations(elem, env, type_elem, context);
    			result.addAll(l);
    		}
    		result.add(new TypeEq(new TTuple(liste_type),t));
    	}
    	else if(exp instanceof LetTuple){
    		LetTuple lettupleexp = (LetTuple) exp;
    		List<Type> types_tuple = lettupleexp.getTs();
    		List<Id> ids_tuple = lettupleexp.getIds();
    		List<TypeEq> l1 = genEquations(lettupleexp.getE1(), env, new TTuple(types_tuple), context);
    		Map<String,Type> env2 = new Hashtable<String,Type>(env);
    		for(int i = 0; i<types_tuple.size(); i++){
        		env2.put(ids_tuple.get(i).getId(), types_tuple.get(i));
    		}
    		List<TypeEq> l2 = genEquations(lettupleexp.getE2(), env2, t, context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof Array){
    		Array arrayexp = (Array) exp;
    		Type type_array = Type.gen();
    		TypeEq teq = new TypeEq(new TArray(type_array),t);
    		result.add(teq);
			List<TypeEq> l1 = genEquations(arrayexp.getE1(), env, new TInt(), context);
			List<TypeEq> l2 = genEquations(arrayexp.getE2(), env, type_array, context);
			result.addAll(l1);
			result.addAll(l2);
    	}
    	else if(exp instanceof Get){
    		Get getexp = (Get) exp;
    		Type type_array = Type.gen();
    		TypeEq teq = new TypeEq(type_array,t);
    		result.add(teq);
			List<TypeEq> l1 = genEquations(getexp.getE1(), env, new TArray(type_array), context);
			List<TypeEq> l2 = genEquations(getexp.getE2(), env, new TInt(), context);
			result.addAll(l1);
			result.addAll(l2);
    	}
    	else if(exp instanceof Put){
    		Put putexp = (Put) exp;
    		Type type_array = Type.gen();
    		TypeEq teq = new TypeEq(new TUnit(),t);
    		result.add(teq);
			List<TypeEq> l1 = genEquations(putexp.getE1(), env, new TArray(type_array), context);
			List<TypeEq> l2 = genEquations(putexp.getE2(), env, new TInt(), context);
			List<TypeEq> l3 = genEquations(putexp.getE3(), env, type_array, context);
			result.addAll(l1);
			result.addAll(l2);
			result.addAll(l3);
    	}
		return result;
	}

	/**
	 * Resout le système d'équations donné, met is_well_typed à false et affiche un message si erreur
	 * @param equations un système d'equations de type
	 */
	//private List<ResolvedEq> resoudreEquations(List<TypeEq> equations) {	
	//	List<ResolvedEq> result = new ArrayList<ResolvedEq>();
	//	if(equations.isEmpty())
	//		return result;
	private void resoudreEquations(List<TypeEq> equations) {
		if(equations.isEmpty())
			return;
		
		TypeEq current = equations.get(0);
		equations.remove(0);

		Type t1 = current.getT1();
		Type t2 = current.getT2();
		String error_msg = "Error : incompatible types " + t1 + " and " + t2;
		Class<?> c1 = t1.getClass();
		Class<?> c2 = t2.getClass();
		if(c1.equals(c2)){
			if(Type.isConstantType(t1)){
				//result.addAll(resoudreEquations(equations));
				resoudreEquations(equations);
			}
			else if(t1 instanceof TVar){
				if(((TVar)t1).equals(((TVar)t2))){
					// L'equation est de la forme ?a = ?a
					//result.addAll(resoudreEquations(equations));
					resoudreEquations(equations);
				}
				else{
					// L'equation est de la forme ?a = ?b
					equations = replaceAll(equations,(TVar) t1,t2);
					synchroniserSymboles(new ResolvedEq((TVar)t1,t2));
					//result.addAll(resoudreEquations(equations));
					resoudreEquations(equations);
				}
			}
			else if(t1 instanceof TArray){
				TArray ta1 = (TArray) t1;
				TArray ta2 = (TArray) t2;
				// On remplace l'equation actuelle par une equation entre les deux types des tableaux
				equations.add(new TypeEq(ta1.getType(),ta2.getType()));
				//result.addAll(resoudreEquations(equations));
				resoudreEquations(equations);
			}
			else if(t1 instanceof TFun){
				TFun tf1 = (TFun) t1;
				TFun tf2 = (TFun) t2;
				List<Type> l1 = tf1.getArgsTypes();
				List<Type> l2 = tf2.getArgsTypes();
				
				if(l1.size() != l2.size()){
					//if(!tf1.estCompletementDefinie() || !tf2.estCompletementDefinie()){
				//		equations.add(current);
				//		//result.addAll(resoudreEquations(equations));
				//		resoudreEquations(equations);
				//	}
				//	else{
						System.err.println(error_msg);
						is_well_typed = false;
				//	}
				}
				else{
					for(int i = 0; i<l1.size(); i++){
						equations.add(new TypeEq(l1.get(i),l2.get(i)));
					}
					equations.add(new TypeEq(tf1.getRetour(),tf2.getRetour()));
					//result.addAll(resoudreEquations(equations));
					resoudreEquations(equations);
				}
			}
			else{ // t1 instanceof TTuple
				TTuple tt1 = (TTuple) t1;
				TTuple tt2 = (TTuple) t2;
				List<Type> l1 = tt1.getElems();
				List<Type> l2 = tt2.getElems();
				if(l1.size() != l2.size()){
					System.err.println(error_msg);
					is_well_typed = false;
				}
				else{
					for(int i = 0; i<l1.size(); i++){
						equations.add(new TypeEq(l1.get(i),l2.get(i)));
					}
					//result.addAll(resoudreEquations(equations));
					resoudreEquations(equations);
				}
			}
		}
		else{
			// t1 et t2 ne sont pas le même type
			if(t2 instanceof TVar){
				// Si t2 est TVar on l'échange avec t2
				TVar tmp = new TVar((TVar) t2);
				t2 = t1;
				t1 = tmp;
			}
			if(t1 instanceof TVar){
				if(t2 instanceof TFun){
					if(((TFun)t2).estCompletementDefinie()){
						//result.add(new ResolvedEq((TVar) t1, t2));
						equations = replaceAll(equations,(TVar) t1,t2);
						synchroniserSymboles(new ResolvedEq((TVar)t1,t2));
					}
					else
						equations.add(current);
				}
				else if(t2 instanceof TArray){
					if(!(((TArray)t2).getType() instanceof TVar)){
						//result.add(new ResolvedEq((TVar) t1, t2));
						equations = replaceAll(equations,(TVar) t1,t2);
						synchroniserSymboles(new ResolvedEq((TVar)t1,t2));
					}
					else
						equations.add(current);
				}
				else if(t2 instanceof TTuple){
					if(((TTuple)t2).estCompletementDefini()){
						//result.add(new ResolvedEq((TVar) t1, t2));
						equations = replaceAll(equations,(TVar) t1,t2);
						synchroniserSymboles(new ResolvedEq((TVar)t1,t2));
					}
					else
						equations.add(current);
				}
				else{
					//result.add(new ResolvedEq((TVar) t1, t2));
					equations = replaceAll(equations,(TVar) t1,t2);
					synchroniserSymboles(new ResolvedEq((TVar)t1,t2));
				}
				//result.addAll(resoudreEquations(equations));
				resoudreEquations(equations);
			}
			else{
				// t1 et t2 sont clairement non unifiables
				System.err.println(error_msg);
				is_well_typed = false;
			}
		}
		
		//return result;
	}

	/**
	 * Remplace les occurences de t1 dans t par t2
	 * @param t le type où remplacer
	 * @param t1 le motif à remplacer
	 * @param t2 le motif par lequel remplacer
	 * @return t modifié
	 */
	private Type replace(Type t, TVar t1, Type t2){
		// TODO pourquoi ca peut etre null ?
		if(t == null)
			return t;
		
		if(t.equals(t1))
			return t2;
		else if(t instanceof TArray){
			if(((TArray)t).getType().equals(t1))
				return new TArray(t2);
			else
				return t;
		}
		else if(t instanceof TFun){
			List<Type> l = ((TFun)t).getArgsTypes();
			List<Type> new_l = new ArrayList<Type>();
			for(Type type : l){
				if(type.equals(t1))
					new_l.add(t2);
				else
					new_l.add(type);
			}
			Type new_retour;
			Type old_retour = ((TFun)t).getRetour();
			if(old_retour.equals(t1))
				new_retour = t2;
			else
				new_retour = old_retour;
			return new TFun(new_l,new_retour);
		}
		else if(t instanceof TTuple){
			List<Type> l = ((TTuple)t).getElems();
			List<Type> new_l = new ArrayList<Type>();
			for(Type type : l){
				if(type.equals(t1))
					new_l.add(t2);
				else
					new_l.add(type);
			}
			return new TTuple(new_l);
		}
		else
			return t;
	}
	
	/**
	 * Remplace les occurences de t1 dans equations par t2
	 * @param equations un ensemble d'equations dans lequel remplacer
	 * @param t1 le motif à remplacer
	 * @param t2 le motif par lequel remplacer
	 * @return equations modifié
	 */
	private List<TypeEq> replaceAll(List<TypeEq> equations, TVar t1, Type t2) {
		List<TypeEq> result = new ArrayList<TypeEq>();
		for(TypeEq eq : equations)
			result.add(new TypeEq(replace(eq.getT1(),t1,t2),replace(eq.getT2(),t1,t2)));
		
		return result;
	}

	/**
	 * Modifie la table des symboles pour synchroniser ses types
	 * @param req l'equation résolue
	 */
	private void synchroniserSymboles(ResolvedEq req) {
		TVar current = req.getT1();
    	Enumeration<String> cles = symboles.keys();
    	while(cles.hasMoreElements()){
			String cle = cles.nextElement();
			Type sym_current = symboles.get(cle);
			symboles.put(cle, replace(sym_current,current,req.getT2()));
		}
	}

    /**
     * Affiche l'état de la table des symboles
     */
	private void afficher_symboles(){
    	Enumeration<String> cles = symboles.keys();
		System.out.println("##### Types inférés : #####");
		while(cles.hasMoreElements()){
			String cle = cles.nextElement();
			System.out.println("Var " + cle + " : " + symboles.get(cle));
		}
		System.out.println("###########################");
    }
}