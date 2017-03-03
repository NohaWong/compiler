package Normalization_Reduction;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import Code_fourni.*;
import Expression.*;
import Expression.Float;
import K_Normal_Expression.*;
import Type.*;

/**
 * 
 * @author jonathan
 * Effectue une K_Normalization sur l'expression fournie.
 * 
 * 
 */

public class K_Normalization implements ObjVisitor<KExp> {
	
	/**
	 * Constructeur
	 */
	public K_Normalization()
	{
		
	}
	
	/**une Expression de type Unit
	 * Prend une Expression en argument et en renvoi une nouvelle 
	 * @param ast une Expression
	 * @return une nouvelle Expression transformé
	 */
	public KExp Start(Exp ast)
	{
		
		return ast.accept(this);
	}
	
	/*----------------------------------------------------------------------------------
	 *                              Méthodes Visitor
	 * ---------------------------------------------------------------------------------
	 */
	
	/**
	 * Visit une Expression de type Unit et la renvoie
	 * @return une Expression de type Unit K-Normalisé 
	 */
	public KUnit visit(Unit e) {
		KUnit retour = new KUnit();
		return retour;
	}
	
	
	/**
	 * Visit d'une Liste d'Expression et la renvoie
	 * @return une Expression de type List<Exp> modifié 
	 */
	
	/**
	 *  Visit d'une Liste de KVar et la renvoie
	 * @param l une list de KVar
	 * @return une Expression de type List Var K_Normalisé 
	 */
	private List<KExp> accept_list(List<Exp> l) {
        List<KExp> retour = new ArrayList<KExp>();
		if (l.isEmpty()) {
            return retour;
        }
        Iterator<Exp> it = l.iterator();
        KExp exp = it.next().accept(this);
        retour.add(exp);
        while (it.hasNext()) {
            exp = it.next().accept(this);
            retour.add(exp);
        }
        return retour;
    }
	
	/**
	 * Visit une Expression de type Bool et la renvoie
	 * @return une Expression de type Bool K-Normalisé 
	 */
	public KBool visit(Bool e) {
		KBool retour = new KBool(e.isB());
		return retour;
	}

	/**
	 * Visit une Expression de type Int et la renvoie
	 * @return une Expression de type Int K-Normalisé 
	 */
	public KInt visit(Int e) {
		KInt retour = new KInt(e.getI());
		return retour;
	}

	/**
	 * Visit une Expression de type Float et la renvoie
	 * @return une Expression de type Float K-Normalisé 
	 */
	public KFloat visit(Float e) {
		KFloat retour = new KFloat(e.getF());
		return retour;
	}

	/**
	 * Visit une Expression de type Not et la renvoie
	 * @return une Expression de type Not K-Normalisé 
	 */
	public KExp visit(Not e) {
		KExp e1 = e.getE().accept(this);
		List<KExp> le = Arrays.asList(e1);
		KExp retour = insert_let_Exp(le,KNot.class.getName(),ExpToType_List(le));
		return retour;
		
	}
	/**
	 * Visit une Expression de type Neg et la renvoie
	 * @return une Expression de type Neg modifié 
	 */
	public KExp visit(Neg e) {
		KExp e1 = e.getE().accept(this);
		List<KExp> le = Arrays.asList(e1);
		KExp retour = insert_let_Exp(le,KNeg.class.getName(),ExpToType_List(le));
		return retour;
	}
	/**
	 * Visit une Expression de type Add et la renvoie
	 * @return une Expression de type Add K-Normalisé
	 */
	public KExp visit(Add e) {
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		//Exp retour = insert_let_2_Exp(e1,e2,Add.class.getName(),new TInt(),new TInt());
		List<KExp> le = Arrays.asList(e1,e2);
		KExp retour = insert_let_Exp(le,KAdd.class.getName(),ExpToType_List(le));
		return retour;
	}

	
	



	/**
	 * Visit une Expression de type Sub et la renvoie
	 * @return une Expression de type Sub K-Normalisé
	 */
	public KExp visit(Sub e) {
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		//Exp retour = insert_let_2_Exp(e1,e2,Sub.class.getName(),new TInt(),new TInt());
		List<KExp> le = Arrays.asList(e1,e2);
		KExp retour = insert_let_Exp(le,KSub.class.getName(),ExpToType_List(le));
		return retour;
	}
	
	/**
	 * Visit une Expression de type FNeg et la renvoie
	 * @return une Expression de type FNeg K-Normalisé
	 */
	public KExp visit(FNeg e) {
		KExp e1 = e.getE().accept(this);
		List<KExp> le = Arrays.asList(e1);
		KExp retour = insert_let_Exp(le,KFNeg.class.getName(),ExpToType_List(le));
		return retour;
	}

	/**
	 * Visit une Expression de type FAdd et la renvoie
	 * @return une Expression de type FAdd K-Normalisé
	 */
	public KExp visit(FAdd e) {
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		//Exp retour = insert_let_2_Exp(e1,e2,FAdd.class.getName(),new TFloat(),new TFloat());
		List<KExp> le = Arrays.asList(e1,e2);
		KExp retour = insert_let_Exp(le,KFAdd.class.getName(),ExpToType_List(le));
		return retour;
	}

	/**
	 * Visit une Expression de type FSub et la renvoie
	 * @return une Expression de type FSub K-Normalisé
	 */
	public KExp visit(FSub e) {
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		//Exp retour = insert_let_2_Exp(e1,e2,FSub.class.getName(),new TFloat(),new TFloat());
		List<KExp> le = Arrays.asList(e1,e2);
		KExp retour = insert_let_Exp(le,KFSub.class.getName(),ExpToType_List(le));
		return retour;
	}

	/**
	 * Visit une Expression de type FMul et la renvoie
	 * @return une Expression de type FMul K-Normalisé
	 */
	public KExp visit(FMul e) {
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		//Exp retour = insert_let_2_Exp(e1,e2,FMul.class.getName(),new TFloat(),new TFloat());
		List<KExp> le = Arrays.asList(e1,e2);
		KExp retour = insert_let_Exp(le,KFMul.class.getName(),ExpToType_List(le));
		return retour;
	}

	/**
	 * Visit une Expression de type FDiv et la renvoie
	 * @return une Expression de type FDiv K-Normalisé
	 */
	public KExp visit(FDiv e) {
		KExp e1= e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		//Exp retour = insert_let_2_Exp(e1,e2,FDiv.class.getName(),new TFloat(),new TFloat());
		List<KExp> le = Arrays.asList(e1,e2);
		KExp retour = insert_let_Exp(le,KFDiv.class.getName(),ExpToType_List(le));
		return retour;
	}

	/**
	 * Visit une Expression de type Eq et la renvoie
	 * @return une Expression de type Eq K-Normalisé
	 */
	public KExp visit(Eq e) {
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		//Exp retour = insert_let_2_Exp(e1,e2,Eq.class.getName(),new TBool(),new TBool());
		List<KExp> le = Arrays.asList(e1,e2);
		KExp retour = insert_let_Exp(le,KEq.class.getName(),ExpToType_List(le));
		return retour;
	}

	/**
	 * Visit une Expression de type LE et la renvoie
	 * @return une Expression de type LE K-Normalisé
	 */
	public KExp visit(LE e) {
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		//Exp retour = insert_let_2_Exp(e1,e2,LE.class.getName(),new TBool(),new TBool());
		List<KExp> le = Arrays.asList(e1,e2);
		KExp retour = insert_let_Exp(le,KLE.class.getName(),ExpToType_List(le));
		return retour;
	}

	
	/**
	 * Visit une Expression de type If et la renvoie
	 * @return une Expression de type If K-Normalisé
	 */
	public KExp visit(If e) {
		Exp exp_cond = e.getE1();
		KExp exp_then = e.getE2().accept(this);		
		KExp exp_else = e.getE3().accept(this);

		// Si e1 est de type Not , enlever le not et inverser les 2 elements , puis normalisé 
		if (exp_cond instanceof Not)
		{
			exp_cond = ((Not) exp_cond).getE();
			KExp tmp = exp_else;
			exp_else = exp_then;
			exp_then = tmp;
		}	
		if(exp_cond instanceof Bool || exp_cond instanceof Var)
		{
			exp_cond = new Eq(exp_cond,new Bool(false));
			KExp tmp = exp_else;
			exp_else = exp_then;
			exp_then = tmp;
		}
		
		KExp exp_cond_G = null;
		KExp exp_cond_D = null;
		KExp retour = null;
		if(exp_cond instanceof Eq)
		{
			exp_cond_G = ((Eq) exp_cond).getE1().accept(this);
			exp_cond_D = ((Eq) exp_cond).getE2().accept(this);
			retour = insert_let_If(exp_cond_G,exp_cond_D,exp_then,exp_else,KEq.class.getName());
		}
		else if(exp_cond instanceof LE)
		{
			exp_cond_G = ((LE) exp_cond).getE1().accept(this);
			exp_cond_D = ((LE) exp_cond).getE2().accept(this);
			retour = insert_let_If(exp_cond_G,exp_cond_D,exp_then,exp_else,KLE.class.getName());
		}
		else 
		{	
			/* Cas let et autre 
			Si j'ai If let x=1 in x<2 then e1 else e2
			=>
			j'aurai 
			let ?v1 = let x=1 in x<2 in if ?v1 = false then e2 else e1
			*/
			exp_cond_G = exp_cond.accept(this);
			exp_cond_D = new KBool(false);
			retour = insert_let_If(exp_cond_G,exp_cond_D,exp_else,exp_then,KEq.class.getName());
		}
		if(retour == null)
		{
			System.err.println("Erreur dans la K_normalization du If");
			System.exit(-1);
		}
		return retour;
	}

	/**
	 * Visit une Expression de type Let et la renvoie
	 * @return une Expression de type Let K-Normalisé
	 */
	public KExp visit(Let e) {
		Id id = e.getId();
		Type t = e.getT();
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		KLet retour = new KLet(id,t,e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type Var et la renvoie
	 * @return une Expression de type Var K-Normalisé
	 */
	public KVar visit(Var e) {
		Id id = e.getId();
		KVar retour = new KVar(id);
		return retour;
	}


	/**
	 * Visit une Expression de type LetRec et la renvoie
	 * @return une Expression de type LetRec K-Normalisé
	 */
	public KExp visit(LetRec e) {
		KExp e1 = e.getFd().getE().accept(this);
		KFunDef fd = new  KFunDef(e.getFd().getId(),e.getFd().getType(),e.getFd().getArgs(),e1);
		KExp e2 = e.getE().accept(this);
		KLetRec retour = new KLetRec(fd,e2);
		return retour;
	}
	
	/**
	 * Visit une Expression de type App et la renvoie
	 * @return une Expression de type App K-Normalisé
	 */
	public KExp visit(App e) {
		KExp e1 = e.getE().accept(this);
		List<KExp> le = new ArrayList<KExp>(accept_list(e.getEs()));
		KExp retour = insert_let_Exp_List(e1,le,KApp.class.getName(),ExpToType_List(le));
		return retour;
	}

	/**
	 * Visit une Expression de type Tuple et la renvoie
	 * @return une Expression de type Tuple modifié
	 */
	public KExp visit(Tuple e) {
		List<KExp> le = new ArrayList<KExp>(accept_list(e.getEs()));
		KExp retour = insert_let_List(le,KTuple.class.getName(),ExpToType_List(le));
		return retour;
	}

	/**
	 * Visit une Expression de type LetTuple et la renvoie
	 * @return une Expression de type LetTuple modifié
	 */
	public KExp visit(LetTuple e) {
		List<Id> ids = new ArrayList<Id>(e.getIds());
		List<Type> ts = new ArrayList<Type>(e.getTs());
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		KLetTuple retour = new KLetTuple(ids,ts,e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type Array et la renvoie
	 * @return une Expression de type Array K-Normalisé
	 */
	public KExp visit(Array e) {
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		//Exp retour = insert_let_2_Exp(e1,e2,Array.class.getName(),new TInt(),ExpToType.convertir(e2));
		List<KExp> le = Arrays.asList(e1,e2);
		KExp retour = insert_let_Exp(le,KArray.class.getName(),ExpToType_List(le));
		return retour;
	}

	/**
	 * Visit une Expression de type Get et la renvoie
	 * @return une Expression de type Get K-Normalisé
	 */
	public KExp visit(Get e) {
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);
		List<KExp> le = Arrays.asList(e1,e2);
		KExp retour = insert_let_Exp(le,KGet.class.getName(),ExpToType_List(le));
		return retour;
	}
	


	/**
	 * Visit une Expression de type Put et la renvoie
	 * @return une Expression de type Put K-Normalisé
	 */
	public KExp visit(Put e) {
		KExp e1 = e.getE1().accept(this);
		KExp e2 = e.getE2().accept(this);		
		KExp e3 = e.getE3().accept(this);
		List<Type> lt = new ArrayList<Type>();
		lt.add(ExpToType.convertir(e1));
		lt.add(ExpToType.convertir(e2));
		lt.add(ExpToType.convertir(e3));
		
		
		KExp retour = insert_let_Exp(Arrays.asList(e1,e2,e3),KPut.class.getName(),lt);
		return retour;
	}
	
	
	/*----------------------------------------------------------------------------------
	 *                              Méthodes Insert_let
	 * ---------------------------------------------------------------------------------
	 */
	

	

	/**
	 * Prend une liste d'expression en paramètre et les K-Normalise avec l'expression mère. 
	 * @param le Une liste d'expression
	 * @param Exp_Name Le nom de l'Expression mère
	 * @param lt Une liste de type, type de le(i) est lt(i) et le.size = lt.size 
	 * @return renvoi une expression K-Normalisé de type Expression mère (Exp_Name)
	 */
	private KExp insert_let_Exp(List<KExp> le,String Exp_Name,List<Type> lt)
	{

		List<KVar> list_feuille = recupFeuille_List(le);
		
		KExp retour_Exp = Instanciation_Exp(list_feuille,Exp_Name);
		
		for(int i = list_feuille.size()-1;i>=0;i--)
		{
			retour_Exp = insert_let(list_feuille.get(i),lt.get(i),le.get(i),retour_Exp);
		}

		return retour_Exp;
	}
	
	/** 
	 * (Cette fonction sert pour App)
	 * Prend une expression et une liste d' expression en paramètre et les K-Normalise avec l'expression mère. 
	 * @param e Une expression qui peut etre null
	 * @param le Une liste d'expression
	 * @param Exp_Name Le nom de l'Expression mère
 	 * @param lt une list type des expressions (mère et fille)
	 * @return renvoi une expression K-Normalisé de type Expression mère (Exp_Name)
	 */
	private KExp insert_let_Exp_List (KExp e,List<KExp> le,String Exp_Name,List<Type> lt)
	{

		
		if(le.size() != lt.size())
		{
			System.err.println("[K_Normalization/insert_let_List] Erreur Taille des listes differentes");
			System.exit(1);
			
		}
		KVar feuille_e = recupFeuille(e);
		List<KVar> list_feuille = recupFeuille_List(le);
		
		
		KExp retour_Exp = Instanciation_Exp_List(Arrays.asList(feuille_e),list_feuille,Exp_Name);
		retour_Exp = insert_let(feuille_e,ExpToType.convertir(e),e,retour_Exp);
		
		Iterator<KExp> it_exp = le.iterator();
		Iterator<KVar> it_feuille = list_feuille.iterator();
		Iterator<Type> it_type = lt.iterator();
		
		while (it_exp.hasNext() && it_feuille.hasNext()&& it_type.hasNext()) {
			retour_Exp = insert_let(it_feuille.next(),it_type.next(),it_exp.next(),retour_Exp);
        }
		//retour_Exp = insert_let(feuille_e,ExpToType.convertir(e),e,retour_Exp);
		return retour_Exp;
	}
	/** 
	 * (Cette fonction sert pour Tuple)
	 * Prend une liste d' expression en paramètre et les K-Normalise avec l'expression mère. 
	 * @param le Une liste d'expression
	 * @param Exp_Name Le nom de l'Expression mère
 	 * @param lt une liste de type des expressions (mère et fille)
	 * @return renvoi une expression K-Normalisé de type Expression mère (Exp_Name)
	 */
	private KExp insert_let_List(List<KExp> le,String Exp_Name,List<Type> lt)
	{



		if(le.size() != lt.size())
		{
			System.err.println("[K_Normalization/insert_let_List] Erreur Taille des listes differentes");
			System.exit(-3);
			
		}
		
		List<KVar> list_feuille = recupFeuille_List(le);
		
		List<KVar> list_vide = new ArrayList<KVar>();
		KExp retour_Exp = Instanciation_Exp_List(list_vide,list_feuille,Exp_Name);
		
		
		Iterator<KExp> it_exp = le.iterator();
		Iterator<KVar> it_feuille = list_feuille.iterator();
		Iterator<Type> it_type = lt.iterator();
		

		while (it_exp.hasNext() && it_feuille.hasNext() && it_type.hasNext()) {
			retour_Exp = insert_let(it_feuille.next(),it_type.next(),it_exp.next(),retour_Exp);
        }

		return retour_Exp;
	}
	
	
	
	
	
	
	
	/** 
	 * Prend 3 expression en paramètre et les K-Normalise avec l'expression mère qui est un if 
	 * la premier expression est un EQ ou un LE
	 * @param e1 Une expression
	 * @param e2 Une expression
	 * @param exp_then Une expression qui est la 2eme du if
	 * @param exp_else Une expression qui est la 3eme du if
	 * @param Exp_Name Le nom de l'Expression mère
	 * @return renvoi une expression K-Normalisé de type Expression mère (Exp_Name)
	 */
	private KExp insert_let_If (KExp e1,KExp e2,KExp exp_then,KExp exp_else,String Exp_Name)
	{

		KVar feuille_e1 = recupFeuille(e1);
		KVar feuille_e2 = recupFeuille(e2);
		KExp exp_tmp = Instanciation_Exp(Arrays.asList(feuille_e1,feuille_e2),Exp_Name);
		KExp exp_if = new KIf(exp_tmp,exp_then,exp_else);
		KExp retour_Exp = insert_let(feuille_e2,ExpToType.convertir(feuille_e2),e2,exp_if);
		retour_Exp = insert_let(feuille_e1,ExpToType.convertir(feuille_e1),e1,retour_Exp);

		return retour_Exp;
	}

	/**
	 * Insere une expression de type let si c'est nécessaire, c'est à dire que e1 n'est pas une feuille.
	 * @param feuille où soit e1 est sa mère ou e1 est lui-même
	 * @param t Type de l'expression e1 (si c'est un expression arithmetique)
	 * @param e1 Une expression 
	 * @param e2 Une expression qui suit e1 dans le programme.
	 * @return Une expression
	 */
	private KExp insert_let(KExp feuille,Type t,KExp e1 , KExp e2)
	{
		//e1.accept(new PrintVisitor());
		//System.out.println();
		if (!EstFeuille(e1))
			return new KLet(((KVar) feuille).getId(),t,e1,e2);
		return e2;
	}
	
	/**
	 * 
	 * @param e Une expression
	 * @return Renvoi vrai si l'expression est une feuille (Int, Unit, Bool, Var, ou Float)
	 */
	private boolean EstFeuille(KExp e) {
		return ( e instanceof KVar);
	}
	/*
	private boolean EstFeuille(Exp e) {
		return (e instanceof Int || e instanceof Unit || e instanceof Bool || e instanceof Var || e instanceof Float);
	}*/
	
	/**
	 * Renvoi si c'est une feuille la même expression, sinon une nouvelle Var
	 * @param e1 Une expression
	 * @return Une feuille (Var,Int,Bool,Unit ou Float)
	 */
	private KVar recupFeuille(KExp e1)
	{
		if(EstFeuille(e1))
		{
			return (KVar)e1;
		}
		return new KVar(Id.gen());
	}
	
	/**
	 * Prend une list d'expression et la transforme en liste de feuille grâce à la fonction récupFeuille
	 * @param le Une liste d'expression
	 * @return Une liste de Feuille
	 */
	private List<KVar> recupFeuille_List(List<KExp> le)
	{
		List<KVar> list_feuille = new ArrayList<KVar>();
		Iterator<KExp> it = le.iterator();
		while (it.hasNext()) {
            list_feuille.add(recupFeuille(it.next()));
        }
		return list_feuille;
	}
	
	/**
	 * Prend une liste d'expression et la transforme en liste de type grâce à la fonction ExpToType.convertir
	 * @param le une liste de KExpression
	 * @return
	 */
	private List<Type> ExpToType_List(List<KExp> le)
	{
		List<Type> list_Type = new ArrayList<Type>();
		Iterator<KExp> it = le.iterator();
		while (it.hasNext()) {
			list_Type.add(ExpToType.convertir(it.next()));
        }
		return list_Type;
	}
	
	/*--------------------------------------------------------------------------------------------------------
	 *              Méthodes Instanciation de Classe Générique ATTENTION MAGIE NOIRE NE PAS TOUCHER
	 * -------------------------------------------------------------------------------------------------------
	 */	

	
	/**
	 * Cette fonction sert à instancier de manière générique une classe de type Exp possédant uniquement 2 Exps
	 * @param exp_args Une liste d'expression
	 * @param className Le nom de la classe à instancier (recupéré grâce à Nom_de_la_classe.class.getName())
	 * @return Renvoie la nouvelle classe instanciée avec le type donné grâce à className 
	 * @throws Exception Si il est impossible d'instancier la classe , renvoi une Exception
	 */
	static private Object instanciation(List<KVar> exp_args, String className) throws Exception
	{
		Class<?> clazz = Class.forName(className);
	    for (Constructor<?> ctor : clazz.getConstructors()) {
	    	Class<?>[] paramTypes = ctor.getParameterTypes();
	    
	    	// If the arity matches, let's use it.
	        if (exp_args.size() == paramTypes.length){
	        	Object[] Args = new Object[exp_args.size()];
	    	 	for(int i=0;i<exp_args.size();i++)
	    	 	{
	    	 		Args[i] = exp_args.get(i);
	    	 	}
	        	
	        	return ctor.newInstance(Args);
	        }
	    }
	    throw new IllegalArgumentException("Impossible d'instancier l'Exp de nom : "+className);
	}
	
	/**
	 * Cette fonction sert à instancier de manière générique une classe de type Exp possédant uniquement 2 Exps
	 * @param exp_args Une liste d'expression où chaque expression sert pour l'instanciation
	 * @param list_args Une liste d'expression où la liste est un argument pour l'instanciation
	 * @param className Le nom de la classe à instancier (recupéré grâce à <Nom_de_la_classe>.class.getName())
	 * @return Renvoie la nouvelle classe instanciée avec le type donné grâce à className 
	 * @throws Exception Si il est impossible d'instancier la classe , renvoi une Exception
	 */
	static private Object instanciation(List<KVar> exp_args,List<KVar> list_args, String className) throws Exception
	{
		Class<?> clazz = Class.forName(className);
	    for (Constructor<?> ctor : clazz.getConstructors()) {
	    	Class<?>[] paramTypes = ctor.getParameterTypes();
	    
	    	// If the arity matches, let's use it.
	        if (exp_args.size() == paramTypes.length-1){
	        	Object[] Args = new Object[exp_args.size()+1];
	        	int i;
	        	for(i=0;i<exp_args.size();i++)
	    	 	{
	    	 		Args[i] = exp_args.get(i);
	    	 	}
	    	 	Args[i] = list_args;
	        	
	        	return ctor.newInstance(Args);
	        }
	    }
	    throw new IllegalArgumentException("Impossible d'instancier l'Exp ");
	}
	
	
	/**
	 * Cette fonction avec la fonction instanciation, elle l'appelle et récupere l'exception. 
	 * @param exp_args Une liste d'expression où chaque expression sert pour l'instanciation
	 * @param className Le nom de la classe à instancier (recupéré grâce à Nom_de_la_classe.class.getName())
	 * @return Renvoie la nouvelle classe instanciée avec le type donné grâce à className 
	 */
	private KExp Instanciation_Exp(List<KVar> exp_args, String className)
	{
		KExp retour = null; 
		try {
			retour = (KExp)instanciation(exp_args,className);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return retour;
	}
	/**
	 * Cette fonction avec la fonction instanciation, elle l'appelle et récupere l'exception. 
	 * @param exp_args Une liste d'expression où chaque expression sert pour l'instanciation
	 * @param list_args Une liste d'expression où la liste est un argument pour l'instanciation
	 * @param className Le nom de la classe à instancier (recupéré grâce à <Nom_de_la_classe>.class.getName())
	 * @return Renvoie la nouvelle classe instanciée avec le type donné grâce à className 
	 */
	private KExp Instanciation_Exp_List (List<KVar> exp_args,List<KVar> list_args, String className)
	{
		KExp retour = null; 
		try {
			retour = (KExp)instanciation(exp_args,list_args,className);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return retour;
	}

	
}
