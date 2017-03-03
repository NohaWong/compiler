package Normalization_Reduction;

import java.util.ArrayList;
import java.util.List;

import Asml.PrintAsml;
import Code_fourni.PrintVisitor;
import Expression.*;
import Expression.Float;

import Expression_asml.*;

import K_Normal_Expression.*;


import Type.*;


/**
 * Classe pour recuperer le type d'une Expression
 * @author jonathan
 *
 */

public class ExpToType {
	
	
	/**
	 * Prend une expression et renvoi le type correspondant
	 * @param e Une expression
	 * @return Un Type
	 */
	public static Type convertir(Exp e)
	{
		if(e instanceof Array)
		{
			return new TArray();
		}
		else if(e instanceof Bool || e instanceof Eq || e instanceof LE || e instanceof Not)
		{
			return new TBool();
		}
		else if(e instanceof Float || e instanceof FAdd || e instanceof FDiv || e instanceof FMul || e instanceof FNeg || e instanceof FSub )
		{
			return new TFloat();
		}
		else if(e instanceof Int || e instanceof Add || e instanceof Sub || e instanceof Neg)
		{
			return new TInt();
		}
		else if(e instanceof Tuple)
		{
			//TODO faire ExpToType sur chaque element du tuple

			 List<Type> elems_types = new ArrayList<Type>();
			 for(Exp elt:((Tuple) e).getEs())
			 {
				 elems_types.add(ExpToType.convertir(elt));
			 }
			
			return new TTuple(elems_types);
		}
		else if(e instanceof Unit)
		{
			return new TUnit();
		}
		else if(e instanceof Var)
		{
			
			return Type.gen();
		}
		else if(e instanceof Let)
		{
			return convertir(((Let) e).getE2());
		}
		else if(e instanceof LetRec)
		{
			return convertir(((LetRec) e).getFd().getE());
		}
		else if (e instanceof App)
		{
			return Type.gen();
		}
		else if (e instanceof Get)
		{
			return Type.gen();
		}
		else if (e instanceof Put)
		{
			return new TUnit();
		}
		else if(e instanceof If)
		{
			return convertir(((If) e).getE2());
		}
		else if( e instanceof LetTuple )
		{
			return convertir(((LetTuple) e).getE2());
		}
		else{
			System.err.println("[ExpToType] Impossible de convertire l'expression en type");
			e.accept(new PrintVisitor());
			System.out.println();
			System.exit(1);
			return null;
		}
		
	}


	/**
	 * Prend une expression asml et renvoie le type correspondant
	 * @param e Une expression asml
	 * @return Un Type
	 */
	public static Type convertir(Exp_asml e)
	{
		if(e instanceof Array_asml)
		{
			return new TArray();
		}
		else if(e instanceof Bool_asml || e instanceof Eq_asml|| e instanceof LE_asml || e instanceof Not_asml)
		{
			return new TBool();
		}
		else if(e instanceof Float_asml || e instanceof FAdd_asml || e instanceof FDiv_asml || e instanceof FMul_asml || e instanceof FNeg_asml || e instanceof FSub_asml )
		{
			return new TFloat();
		}
		else if(e instanceof Int_asml || e instanceof Add_asml || e instanceof Sub_asml || e instanceof Neg_asml )
		{
			return new TInt();
		}
		else if(e instanceof Unit_asml)
		{
			return new TUnit();
		}
		else if(e instanceof Var_asml)
		{
			
			return Type.gen();
		}
		else if(e instanceof Let_asml)
		{
			return convertir(((Let_asml) e).getE2());
		}
		else if(e instanceof LetRec_asml)
		{
			return convertir(((LetRec_asml) e).getFd().getE());
		}
		else if (e instanceof App_asml)
		{
			return Type.gen();
		}
		else if (e instanceof Get_asml)
		{
			return Type.gen();
		}
		else if (e instanceof Put_asml)
		{
			return new TUnit();
		}
		else if(e instanceof If_asml)
		{
			return convertir(((If_asml) e).getE2());
		}
		else if( e instanceof Let_memory_alloc_asml )
		{
			return Type.gen();
		}
		else if( e instanceof Let_memory_load_asml )
		{
			return Type.gen();
		}
		else if( e instanceof Let_memory_store_asml )
		{
			return Type.gen();
		}
		else if( e instanceof AppC_asml )
		{
			return Type.gen();
		}
		else{
			System.err.println("[ExpToType] Impossible de convertire l'expression en type");
			e.accept(new PrintAsml(System.out));
			System.out.println();
			System.exit(1);
			return null;
		}
		
	}
	/**
	 * Prend une expression et renvoi le type correspondant
	 * @param e Une expression
	 * @return Un Type
	 */
	public static Type convertir(KExp e)
	{
		if(e instanceof KArray)
		{
			return new TArray();
		}
		else if(e instanceof KBool || e instanceof KEq || e instanceof KLE || e instanceof KNot)
		{
			return new TBool();
		}
		else if(e instanceof KFloat || e instanceof KFAdd || e instanceof KFDiv || e instanceof KFMul || e instanceof KFNeg || e instanceof KFSub )
		{
			return new TFloat();
		}
		else if(e instanceof KInt || e instanceof KAdd || e instanceof KSub  || e instanceof KNeg)
		{
			return new TInt();
		}
		else if(e instanceof KTuple)
		{
			//TODO faire ExpToType sur chaque element du tuple

			 List<Type> elems_types = new ArrayList<Type>();
			 for(KExp elt:((KTuple) e).getVs())
			 {
				 elems_types.add(ExpToType.convertir(elt));
			 }
			
			return new TTuple(elems_types);
		}
		else if(e instanceof KUnit)
		{
			return new TUnit();
		}
		else if(e instanceof KVar)
		{
			
			return Type.gen();
		}
		else if(e instanceof KLet)
		{
			return convertir(((KLet) e).getE2());
		}
		else if(e instanceof KLetRec)
		{
			return convertir(((KLetRec) e).getFd().getE());
		}
		else if (e instanceof KApp)
		{
			return Type.gen();
		}
		else if (e instanceof KGet)
		{
			return Type.gen();
		}
		else if (e instanceof KPut)
		{
			return new TUnit();
		}
		else if(e instanceof KIf)
		{
			return convertir(((KIf) e).getE2());
		}
		else if( e instanceof KLetTuple )
		{
			return convertir(((KLetTuple) e).getE2());
		}
		else{
			System.err.println("[ExpToType] Impossible de convertire l'expression en type");
			e.accept(new KPrintVisitor());
			System.out.println();
			System.exit(1);
			return null;
		}
		
	}
}
