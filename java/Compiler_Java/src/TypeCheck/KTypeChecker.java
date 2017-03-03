package TypeCheck;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import Type.*;
import K_Normal_Expression.*;
import Main.MinMlLibrary;
import Code_fourni.Id;

/**
 * Classe pour la vérification des types d'un AST K-normal
 * @author bizarda
 * 
 * Méthode principale : check(KExp AST)
 */
public class KTypeChecker{
	
	private boolean is_well_typed;
	private Hashtable<String,Type> symboles;
	private boolean verbose;
	
	/**
	 * Constructeur par défaut
	 */
	public KTypeChecker(){
		symboles = new Hashtable<String,Type>();
		this.verbose = false;
	}
	
	/**
	 * Constructeur
	 * @param verbose si le TypeChecker doit afficher la table des symboles/types à la fin de son exécution
	 */
	public KTypeChecker(boolean verbose){
		symboles = new Hashtable<String,Type>();
		this.verbose = verbose;
	}

	/**
	 * Méthode principale de check/inférence des types
	 * @param AST l'AST K-normal à tester
	 * @return vrai ssi AST est correctement typé
	 */
	public boolean Start(KExp AST){
		
		Map <String,Type> predefined = MinMlLibrary.getPredefinedMap();
		
		is_well_typed = true;
		
		List<TypeEq> equations = genEquations(AST,predefined,new TUnit()," ");
		
		if(!is_well_typed){
			System.exit(1);
		}
		
		resoudreEquations(equations);

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
	 * Genere les equations de types d'apres la KExp donnée
	 * @param exp la KExp en question
	 * @param env l'environnement dans lequel traiter exp
	 * @param t type attendu de exp
	 * @param context base pour la table des symboles
	 * @return une liste d'equations issue du traitement de exp
	 */
	private List<TypeEq> genEquations(KExp exp, Map<String,Type> env, Type t, String context){
		
		List<TypeEq> result = new ArrayList<TypeEq>();

    	if(exp instanceof KVar){
    		KVar varexp = (KVar) exp;
    		Type type_var = env.get(varexp.getId().getId());
    		if(type_var == null){
    			System.err.println("Error : Unbound value " + varexp.getId().getId());
    			is_well_typed = false;
    		}
    		TypeEq teq = new TypeEq(type_var,t);
    		result.add(teq);
    	}
    	else if(exp instanceof KUnit){
    		TypeEq teq = new TypeEq(new TUnit(),t);
    		result.add(teq);
    	}
    	else if(exp instanceof KBool){
    		TypeEq teq = new TypeEq(new TBool(),t);
    		result.add(teq);
    	}
    	else if(exp instanceof KInt){
    		TypeEq teq = new TypeEq(new TInt(),t);
    		result.add(teq);
    	}
    	else if(exp instanceof KFloat){
    		TypeEq teq = new TypeEq(new TFloat(),t);
    		result.add(teq);
    	}
    	else if(exp instanceof KNot){
    		KNot notexp = (KNot) exp;
    		TypeEq teq = new TypeEq(new TBool(),t);
    		result.add(teq);
    		List<TypeEq> l = genEquations(notexp.getV(), env, new TBool(),context);
    		result.addAll(l);
    	}
    	else if(exp instanceof KNeg){
    		KNeg negexp = (KNeg) exp;
    		TypeEq teq = new TypeEq(new TInt(),t);
    		result.add(teq);
    		List<TypeEq> l = genEquations(negexp.getV(), env, new TInt(),context);
    		result.addAll(l);
    	}
    	else if(exp instanceof KFNeg){
    		KFNeg fnegexp = (KFNeg) exp;
    		TypeEq teq = new TypeEq(new TFloat(),t);
    		result.add(teq);
    		List<TypeEq> l = genEquations(fnegexp.getV(), env, new TFloat(),context);
    		result.addAll(l);
    	}
    	else if(exp instanceof KAdd){
    		KAdd addexp = (KAdd) exp;
    		TypeEq teq = new TypeEq(new TInt(),t);
    		result.add(teq);
    		List<TypeEq> l1 = genEquations(addexp.getV1(), env, new TInt(),context);
    		List<TypeEq> l2 = genEquations(addexp.getV2(), env, new TInt(),context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof KSub){
    		KSub subexp = (KSub) exp;
    		TypeEq teq = new TypeEq(new TInt(),t);
    		result.add(teq);
    		List<TypeEq> l1 = genEquations(subexp.getV1(), env, new TInt(),context);
    		List<TypeEq> l2 = genEquations(subexp.getV2(), env, new TInt(),context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof KFAdd){
    		KFAdd faddexp = (KFAdd) exp;
    		TypeEq teq = new TypeEq(new TFloat(),t);
    		result.add(teq);
    		List<TypeEq> l1 = genEquations(faddexp.getV1(), env, new TFloat(),context);
    		List<TypeEq> l2 = genEquations(faddexp.getV2(), env, new TFloat(),context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof KFSub){
    		KFSub fsubexp = (KFSub) exp;
    		TypeEq teq = new TypeEq(new TFloat(),t);
    		result.add(teq);
    		List<TypeEq> l1 = genEquations(fsubexp.getV1(), env, new TFloat(),context);
    		List<TypeEq> l2 = genEquations(fsubexp.getV2(), env, new TFloat(),context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof KFMul){
    		KFMul fmulexp = (KFMul) exp;
    		TypeEq teq = new TypeEq(new TFloat(),t);
    		result.add(teq);
    		List<TypeEq> l1 = genEquations(fmulexp.getV1(), env, new TFloat(),context);
    		List<TypeEq> l2 = genEquations(fmulexp.getV2(), env, new TFloat(),context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof KFDiv){
    		KFDiv fdivexp = (KFDiv) exp;
    		TypeEq teq = new TypeEq(new TFloat(),t);
    		result.add(teq);
    		List<TypeEq> l1 = genEquations(fdivexp.getV1(), env, new TFloat(),context);
    		List<TypeEq> l2 = genEquations(fdivexp.getV2(), env, new TFloat(),context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof KIf){
    		KIf ifexp = (KIf) exp;
    		List<TypeEq> l = genEquations(ifexp.getE1(), env, new TBool(),context);
    		List<TypeEq> l1 = genEquations(ifexp.getE2(), env, t,context);
    		List<TypeEq> l2 = genEquations(ifexp.getE3(), env, t,context);
    		result.addAll(l);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof KLet){
    		KLet letexp = (KLet) exp;
    		Type type_id = letexp.getT();
    		List<TypeEq> l1 = genEquations(letexp.getE1(), env, type_id, context);
    		Map<String,Type> env2 = new Hashtable<String,Type>(env);
    		env2.put(letexp.getId().getId(), type_id);
    		symboles.put(context + letexp.getId().getId(), type_id);
    		List<TypeEq> l2 = genEquations(letexp.getE2(), env2, t, context);
    		result.addAll(l1);
    		result.addAll(l2);
    	}
    	else if(exp instanceof KLetRec){
    		KLetRec letrecexp = (KLetRec) exp;
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
    	else if(exp instanceof KApp){
    		KApp appexp = (KApp) exp;
    		List<KVar> args = appexp.getVs();
    		List<Type> types_args = new ArrayList<Type>();
    		for(KVar arg : args){
    			Type type_arg = Type.gen();
    			types_args.add(type_arg);
        		List<TypeEq> l = genEquations(arg, env, type_arg, context);
        		result.addAll(l);
    		}
    		Type type_retour = Type.gen();
    		TypeEq teq = new TypeEq(type_retour,t);
    		result.add(teq);
    		Type type_fun = new TFun(types_args,type_retour);
    		List<TypeEq> l = genEquations(appexp.getV(), env, type_fun, context);
    		result.addAll(l);
    	}
    	else if(exp instanceof KEq){
    		KEq eqexp = (KEq) exp;
    		TypeEq teq = new TypeEq(new TBool(),t);
    		result.add(teq);
    		Type type_eq = Type.gen();
			List<TypeEq> l1 = genEquations(eqexp.getV1(), env, type_eq, context);
			List<TypeEq> l2 = genEquations(eqexp.getV2(), env, type_eq, context);
			result.addAll(l1);
			result.addAll(l2);
    	}
    	else if(exp instanceof KLE){
    		KLE leexp = (KLE) exp;
    		TypeEq teq = new TypeEq(new TBool(),t);
    		result.add(teq);
    		Type type_le = Type.gen();
			List<TypeEq> l1 = genEquations(leexp.getV1(), env, type_le, context);
			List<TypeEq> l2 = genEquations(leexp.getV2(), env, type_le, context);
			result.addAll(l1);
			result.addAll(l2);
    	}
    	else if(exp instanceof KTuple){
    		KTuple tupleexp = (KTuple) exp;
    		List<Type> liste_type = new ArrayList<Type>();
    		for(KVar elem : tupleexp.getVs()){
    			Type type_elem = Type.gen();
    			liste_type.add(type_elem);
    			List<TypeEq> l = genEquations(elem, env, type_elem, context);
    			result.addAll(l);
    		}
    		result.add(new TypeEq(new TTuple(liste_type),t));
    	}
    	else if(exp instanceof KLetTuple){
    		KLetTuple lettupleexp = (KLetTuple) exp;
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
    	else if(exp instanceof KArray){
    		KArray arrayexp = (KArray) exp;
    		Type type_array = Type.gen();
    		TypeEq teq = new TypeEq(new TArray(type_array),t);
    		result.add(teq);
			List<TypeEq> l1 = genEquations(arrayexp.getV1(), env, new TInt(), context);
			List<TypeEq> l2 = genEquations(arrayexp.getV2(), env, type_array, context);
			result.addAll(l1);
			result.addAll(l2);
    	}
    	else if(exp instanceof KGet){
    		KGet getexp = (KGet) exp;
    		Type type_array = Type.gen();
    		TypeEq teq = new TypeEq(type_array,t);
    		result.add(teq);
			List<TypeEq> l1 = genEquations(getexp.getV1(), env, new TArray(type_array), context);
			List<TypeEq> l2 = genEquations(getexp.getV2(), env, new TInt(), context);
			result.addAll(l1);
			result.addAll(l2);
    	}
    	else if(exp instanceof KPut){
    		KPut putexp = (KPut) exp;
    		Type type_array = Type.gen();
    		TypeEq teq = new TypeEq(new TUnit(),t);
    		result.add(teq);
			List<TypeEq> l1 = genEquations(putexp.getV1(), env, new TArray(type_array), context);
			List<TypeEq> l2 = genEquations(putexp.getV2(), env, new TInt(), context);
			List<TypeEq> l3 = genEquations(putexp.getV3(), env, type_array, context);
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
				resoudreEquations(equations);
			}
			else if(t1 instanceof TVar){
				if(((TVar)t1).equals(((TVar)t2))){
					// L'equation est de la forme ?a = ?a
					resoudreEquations(equations);
				}
				else{
					// L'equation est de la forme ?a = ?b
					equations = replaceAll(equations,(TVar) t1,t2);
					synchroniserSymboles(new ResolvedEq((TVar)t1,t2));
					resoudreEquations(equations);
				}
			}
			else if(t1 instanceof TArray){
				TArray ta1 = (TArray) t1;
				TArray ta2 = (TArray) t2;
				// On remplace l'equation actuelle par une equation entre les deux types des tableaux
				equations.add(new TypeEq(ta1.getType(),ta2.getType()));
				resoudreEquations(equations);
			}
			else if(t1 instanceof TFun){
				TFun tf1 = (TFun) t1;
				TFun tf2 = (TFun) t2;
				List<Type> l1 = tf1.getArgsTypes();
				List<Type> l2 = tf2.getArgsTypes();
				
				if(l1.size() != l2.size()){
						System.err.println(error_msg);
						is_well_typed = false;
				}
				else{
					for(int i = 0; i<l1.size(); i++){
						equations.add(new TypeEq(l1.get(i),l2.get(i)));
					}
					equations.add(new TypeEq(tf1.getRetour(),tf2.getRetour()));
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
						equations = replaceAll(equations,(TVar) t1,t2);
						synchroniserSymboles(new ResolvedEq((TVar)t1,t2));
					}
					else
						equations.add(current);
				}
				else if(t2 instanceof TArray){
					if(!(((TArray)t2).getType() instanceof TVar)){
						equations = replaceAll(equations,(TVar) t1,t2);
						synchroniserSymboles(new ResolvedEq((TVar)t1,t2));
					}
					else
						equations.add(current);
				}
				else if(t2 instanceof TTuple){
					if(((TTuple)t2).estCompletementDefini()){
						equations = replaceAll(equations,(TVar) t1,t2);
						synchroniserSymboles(new ResolvedEq((TVar)t1,t2));
					}
					else
						equations.add(current);
				}
				else{
					equations = replaceAll(equations,(TVar) t1,t2);
					synchroniserSymboles(new ResolvedEq((TVar)t1,t2));
				}
				resoudreEquations(equations);
			}
			else{
				// t1 et t2 sont clairement non unifiables
				System.err.println(error_msg);
				is_well_typed = false;
			}
		}
	}

	/**
	 * Remplace les occurences de t1 dans t par t2
	 * @param t le type où remplacer
	 * @param t1 le motif à remplacer
	 * @param t2 le motif par lequel remplacer
	 * @return t modifié
	 */
	private Type replace(Type t, TVar t1, Type t2){
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