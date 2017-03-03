package BackEnd;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import BackEnd.Position.*;
import Code_fourni.Id;
import Expression.*;
import Expression.Float;
import Expression_asml.*;
import Type.Type;


/**
 * 
 * @author jonathan & Gabriel
 * Affiche les lignes de code ARM
 */

public class ARMPrinterVisitor implements Asml.ObjVisitor_asml<Exp_asml> {
	private PrintStream output;
	private PrintStream debug_out;
	private ARMPrinter_StateMachine statemachine;
	private Table_de_symboles table_de_symbole;
	
	/**
	 * Constructeur pour double output
	 * @param output le stream sur lequel écrire
	 * @param allocator l'allocateur de la table des symboles. (a changer?)
	 */
	public ARMPrinterVisitor(PrintStream output, PrintStream debug_out, Table_de_symboles table_de_symbole) {
		this.output = output;
		this.debug_out = debug_out;
		this.table_de_symbole = table_de_symbole;
		statemachine = new ARMPrinter_StateMachine(output,debug_out, table_de_symbole);
		//debug_out.println("Fin du constructeur, double out");
	}
	
	/**
	 * Constructeur pour output unique
	 * @param output
	 * @param table_de_symbole
	 */
	public ARMPrinterVisitor(PrintStream output, Table_de_symboles table_de_symbole) {
		this.output = output;
		this.table_de_symbole = table_de_symbole;
		statemachine = new ARMPrinter_StateMachine(output, table_de_symbole);
	}
	
	/**
	 * Print l'expression donnée
	 * @param ast une Expression
	 * @return le reste de l'expression, dont le code n'as aucun impact
	 */
	public Exp_asml Start(Exp_asml ast)
	{
		while(sideEffect_exp(ast))
		{
			ast = ast.accept(this);
		}
		return ast.accept(this);
	}
	
	/**
	 * Visit une Expression de type Unit et la renvoie
	 * @return l'expression donnée
	 */
	public Exp_asml visit(Unit_asml e) {
		return e;
	}
	
	
	/**
	 * Visit d'une Liste d'Expression et la renvoie
	 * @param une Expression de type List<Exp_asml> 
	 * @return une Expression de type List<Exp_asml> modifié 
	 */
	//TODO
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
	 * Visit une Expression de type Bool et la renvoie
	 * @return une Expression de type Bool modifié 
	 */
	public Exp_asml visit(Bool_asml e) {
		return e;
	}

	/**
	 * Visit une Expression de type Int et la renvoie
	 * @return l'expression en entrée
	 */
	public Exp_asml visit(Int_asml e) {
		return e;
	}

	/**
	 * Visit une Expression de type Float et la renvoie
	 * @return l'expression en entrée
	 */
	public Exp_asml visit(Float_asml e) {
		return e;
	}

	/**
	 * Visit une Expression de type Not, la renvoie
	 * @return l'expression donnée
	 */
	public Exp_asml visit(Not_asml e) {
		return e;
	}
	
	/**
	 * Visit une Expression de type Neg, la renvoie
	 * @return l'expression donnée
	 */
	public Exp_asml visit(Neg_asml e) {
		return e;
	}
	/**
	 * Visit une Expression de type add, la renvoie
	 * @return l'expression donnée
	 */
	public Exp_asml visit(Add_asml e) {
		return e;
	}
	/**
	 * Visit une Expression de type Sub, la renvoie
	 * @return l'expression donnée
	 */
	public Exp_asml visit(Sub_asml e) {
		return e;
	}
	
	/**
	 * Visit une Expression de type FNeg et la renvoie
	 * @return une Expression de type FNeg modifié
	 */
	//TODO
	public Exp_asml visit(FNeg_asml e) {
		Var_asml e1 = (Var_asml)e.getE().accept(this);
		FNeg_asml retour = new FNeg_asml(e1);
		return retour;
	}

	/**
	 * Visit une Expression de type FAdd et la renvoie
	 * @return une Expression de type FAdd modifié
	 */
	//TODO
	public Exp_asml visit(FAdd_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		FAdd_asml retour = new FAdd_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type FSub et la renvoie
	 * @return une Expression de type FSub modifié
	 */
	//TODO
	public Exp_asml visit(FSub_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		FSub_asml retour = new FSub_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type FMul et la renvoie
	 * @return une Expression de type FMul modifié
	 */
	//TODO
	public Exp_asml visit(FMul_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		FMul_asml retour = new FMul_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type FDiv et la renvoie
	 * @return une Expression de type FDiv modifié
	 */
	//TODO
	public Exp_asml visit(FDiv_asml e) {
		Var_asml e1= (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		FDiv_asml retour = new FDiv_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type Eq et la renvoie
	 * @return une Expression de type Eq modifié
	 */
	public Exp_asml visit(Eq_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		Eq_asml retour = new Eq_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type Lower Equal et la renvoie
	 * @return une Expression de type LE modifié
	 */
	public Exp_asml visit(LE_asml e) {
		Var_asml e1 = (Var_asml)e.getE1().accept(this);
		Var_asml e2 = (Var_asml)e.getE2().accept(this);
		LE_asml retour = new LE_asml(e1,e2);
		return retour;
	}

	/**
	 * Visit une Expression de type If, renvoie unit
	 * @return unit
	 */
	public Exp_asml visit(If_asml e) {
		int numLabel = table_de_symbole.getNumLabelIf();
		statemachine.ecrire_If(e.getE1(),numLabel);
		
		statemachine.ecrire_ElseEtiq(numLabel);
		e.getE3().accept(this);
		statemachine.ecrire_BALContinue(numLabel);
		
		statemachine.ecrire_ThenEtiq(numLabel);
		e.getE2().accept(this);
		
		statemachine.ecrire_EtiqContinue(numLabel);
		return new Unit_asml();
	}

	/**
	 * Visit une Expression de type Let; renvoie la suite (in)
	 * @return "In" e2
	 */
	public Exp_asml visit(Let_asml e) {
		Id id = e.getId();
		//Type t = e.getT();
		Exp_asml e1 = e.getE1();
		do{
			if(e1 instanceof App_asml)
			{
				//System.out.println("LET: App");
				statemachine.ecrire_call((App_asml)e1, id);
				e1 = new Unit_asml();// On "accept" l'appel
			}
			//Ton if est dans un e2 dans ton exemple ---> Voila pourquoi ça marche pas
			
			else if (e1 instanceof If_asml)
			{
				//System.out.println("LET: If");
				If_asml e_if = (If_asml) e1;
				Let_asml let1 = new Let_asml(e.getId(), e.getT(), e_if.getE2(), new Unit_asml());
				Let_asml let2 = new Let_asml(e.getId(), e.getT(), e_if.getE3(), new Unit_asml());
				If_asml new_if = new If_asml(e_if.getE1(), let1, let2);
				e1 = new_if.accept(this);
				
			}
			else if(e1 instanceof Let_asml)
			{
				//System.out.println("LET: Let");
				e1 = e1.accept(this);
				ecrire_affectation(e1, id);
			}
			else if(e1 instanceof AppC_asml)
			{
				statemachine.ecrire_callClosure((AppC_asml)e1, ((AppC_asml)e1).getE().getId());
				e1 = new Unit_asml();
			}
			else if(e1 instanceof Put_asml)
			{
				//TODO contentieux? on ne fait pas d'affectation dans ce cas
				e1 = e1.accept(this);
			}
			else if(e1 instanceof Let_memory_store_asml)
			{
				e1 = e1.accept(this);
			}
			else
			{
				//System.out.println("LET: default");
				e1 = e1.accept(this);
				ecrire_affectation(e1, id);
			}
		}while(sideEffect_exp(e1));
		
		return e.getE2();
	}

	/**
	 * Visit une Expression de type Var et la renvoie
	 * @return l'expression donnée
	 */
	public Exp_asml visit(Var_asml e) {
		return e;
	}


	/**
	 * Visit une Expression de type LetRec et la renvoie
	 * @return "in" e2
	 */
	public Exp_asml visit(LetRec_asml e) {
		Exp_asml e1 = e.getFd().getE();
		//TODO
		
		Table_de_symboles table_symbole_fun = null;
		if(table_de_symbole.getPosition(e.getFd().getId()) instanceof Label_Fonction)
		{
			//recupération de la table des symboles & declaration du printer
			table_symbole_fun = ((Label_Fonction)table_de_symbole.getPosition(e.getFd().getId())).getTable_symbole();
			ARMPrinterVisitor fonctionPrinter = new ARMPrinterVisitor(output,debug_out, table_symbole_fun);
			
			//fonctionPrinter.setup_env(); fait dans le runner
			fonctionPrinter.setup_env_SavRegs();
			Exp_asml left_over = fonctionPrinter.Start(e.getFd().getE());
			// Return should be nosideEffectExp
			
			fonctionPrinter.restoreRegsReturn();
		}
		else
			System.err.println("ARMPrinter: Problème dans LETREC: pas sur un label_fonction");
		
		Exp_asml e2 = e.getE();
		return e2;
	}
	
	/**
	 * Visite un appel, et l'écris
	 * @return Unit
	 */
	public Exp_asml visit(App_asml e) {
		// Meh, i should not accept any arguments, if Knormalized is ok
		/*Var_asml e1 = (Var_asml)e.getE().accept(this);
		List<Var_asml> le = new ArrayList<Var_asml>(accept_list(e.getEs()));*/
		statemachine.ecrire_call(e, null);
		return new Unit_asml();
	}
	
	

	/**
	 * Visit une Expression de type Array et la renvoie
	 * @return l'expression donnée
	 */
	public Exp_asml visit(Array_asml e) {
		return e;
	}

	/**
	 * Visit une Expression de type Get et la renvoie
	 * @return une Expression de type Get modifié
	 */
	public Exp_asml visit(Get_asml e) {
		return e;
	}

	/**
	 * Visit une Expression de type Put et la renvoie
	 * @return une Expression de type Put modifié
	 */
	public Exp_asml visit(Put_asml e) {
		statemachine.ecrire_put(e);
		return new Unit_asml();
	}
	
	@Override
	public Exp_asml visit(Let_memory_load_asml e) {
		statemachine.ecrire_mem_get(e);
		return e.getE2();
	}

	@Override
	public Exp_asml visit(Let_memory_alloc_asml e) {
		statemachine.ecrire_allouer_mem(e);
		return e.getE2();
	}

	@Override
	public Exp_asml visit(Let_memory_store_asml e) {
		statemachine.ecrire_mem_put(e);
		return e.getE2();
	}

	/**
	 * Ecris l'affectation retournée dans un let
	 * @param e l'expression droite.
	 * @param varID le nom de la variable == le registre de destination
	 */
	private void ecrire_affectation(Exp_asml e, Id varId)
	{
		statemachine.ecrire_affectation(e, varId);
	}
	
	/**
	 * Met en place l'envirronement: 
	 * save + Maj FP
	 * allouer l'espace des variables locales
	 */
	public void setup_env() {
		statemachine.setup_env();
	}
	
	/**
	 * Fait le set up de l'environnement et sauvegarde les registres pour ne pas ecraser les données de l'appelant
	 */
	public void setup_env_SavRegs()
	{
		statemachine.setup_env_SavRegs();
	}
	
	/**
	 * restore les registres de l'appelant & fait le retour 
	 */
	public void restoreRegsReturn(){
		statemachine.restoreRegsReturn();
	}

	@Override
	public Exp_asml visit(AppC_asml e) {
		statemachine.ecrire_callClosure(e, null);
		return new Unit_asml();
	}

	/**
	 * Renvoie si une expression à un effet de bord ie. si elle mérite du code PAR ELLE MÊME.
	 * @return Si l'expression demande elle même du code.
	 */
	private boolean  sideEffect_exp(Exp_asml e)
	{
		if(e instanceof Let_asml)
			return true;
		else if (e instanceof App_asml)
			return true;
		else if (e instanceof LetRec_asml)
			return true;
		else if (e instanceof If_asml)
			return true;
		else if (e instanceof Put_asml)
			return true;
		else if (e instanceof AppC_asml)
			return true;
		else if (e instanceof Let_memory_store_asml)
			return true;
		else if (e instanceof Let_memory_load_asml)
			return true;
		else if (e instanceof Let_memory_alloc_asml)
			return true;
		return false;
	}
}
