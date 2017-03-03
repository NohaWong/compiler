package BackEnd;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import BackEnd.Position.*;
import Code_fourni.Id;
import Expression_asml.*;

public class ARMPrinter_StateMachine {
	
	class double_output{
		PrintStream p1,p2;
		boolean double_stream;
		
		public double_output(PrintStream stream1, PrintStream stream2){
			if(stream2 != null){
				double_stream =  true;
				p1 = stream1;
				p2 = stream2;
			}
			else
			{
				double_stream = false;
				p1 = stream1;
			}
		}
		
		public double_output(PrintStream stream1){
			double_stream = false;
			p1 = stream1;
		}
		
		public void println(String s){
			p1.println(s);
			if(double_stream && p2 != null)
			{
				p2.println(s);
			}
		}
	}
	
	private double_output output;
	private enum Etat{
		DEBUT,
		CALL_GET_REG,
		CALL_PUSH_LR,
		CALL_SAV_REG_ARGS,
		CALL_PUSH_ARGS,
		CALL_MAKE_CALL,
		CALL_LD_LR,
		CALL_PUSH_RESULT,
		CALL_RESTORE_REG_ARGS,
		CALL_POP_RESULT,
		CALL_CLOSURE,
		IF_COND_ARG1,
		IF_COND_ARG2,
		IF_COND_CMP,
		LET_SINGLE_OP_VAR,
		LET_SINGLE_OP_IMM,
		LET_SINGLE_OP_BOOL,
		LET_GET_OP_ARRAY,
		LET_GET_OP_OFFSET,
		LET_GET_LDR,
		LET_FIRST_OP,
		LET_SECOND_OP,
		LET_PRINT_OP_SINGLE,
		LET_PRINT_OP_DUAL,
		LET_NEG,
		PUT_SRC,
		PUT_ARRAY,
		PUT_OFFSET,
		FINAL_STORE,
		FINISHED}
	private Table_de_symboles table_de_symbol;
	private Etat etat_courant;
	private String RegOP1, RegOP2, RegDest;
	private String Operation;
	private static final int SIZE_OF_WORD =4;
	private static final int NB_ARGS_PLACES_REG =4;
	
	
	/**
	 * Constructeur
	 * @param output
	 * @param allocator
	 */
	public ARMPrinter_StateMachine(PrintStream output, Table_de_symboles table_de_symbol) {
		super();
		this.output = new double_output(output);
		this.table_de_symbol = table_de_symbol;
		etat_courant = Etat.DEBUT;
	}
	
	/**
	 * Constructeur pour double output
	 * @param output
	 * @param debug_out
	 * @param table_de_symbol
	 */
	public ARMPrinter_StateMachine(PrintStream output,PrintStream debug_out, Table_de_symboles table_de_symbol) {
		super();
		this.output = new double_output(output,debug_out);
		this.table_de_symbol = table_de_symbol;
		etat_courant = Etat.DEBUT;
	}

	/**
	 * Ecris l'appel d'une fonction avec ses arguments
	 * @param app fonction type App ( label + list<Var|Int>
	 * @param id id de variable de retour dans lequel doit être stocké la variable. eventuellement null, si ne pas prendre compte du retour 
	 */
	public void ecrire_call(App_asml app, Id id)
	{
		while(etat_courant != Etat.FINISHED)
			switch(etat_courant)
			{
			case DEBUT:
				if((app.getE() instanceof Var_asml))
				{
					etat_courant = Etat.CALL_SAV_REG_ARGS;
					break;
				}
				etat_courant = Etat.FINISHED;
				break;
				
			case CALL_PUSH_LR://bypassed: ne pas faire, fait dans la mise en place de l'environnement
				pushRegStack("lr");
				etat_courant = Etat.CALL_SAV_REG_ARGS;
				break;
				
			case CALL_SAV_REG_ARGS:
				save_reg_args(app.getEs().size());
				etat_courant = Etat.CALL_PUSH_ARGS;
				break;
				
			case CALL_PUSH_ARGS:
				push_arguments(app.getEs());
				etat_courant = Etat.CALL_MAKE_CALL;
				break;
				
			case CALL_MAKE_CALL:
				make_Call((Var_asml)app.getE());
				etat_courant = Etat.CALL_PUSH_RESULT;
				break;
				
			case CALL_PUSH_RESULT:
				if(id != null && !(table_de_symbol.getPosition(id) instanceof Position_Registre) && !(id.getId().equals("_")))
				{
					//output.println("\n\n\n@the return value is not null  \""+ id + "\"");
					putRegVar(id, 0);
				}
				else if (id != null && !(id.getId().equals("_")))
				{
					//output.println("\n\n\n@the return value is not nullelse");
					push_result_tas();
				}
				etat_courant = Etat.CALL_RESTORE_REG_ARGS;
				break;
				
			case CALL_RESTORE_REG_ARGS:
				restore_reg_args(app.getEs().size());
				etat_courant = Etat.CALL_POP_RESULT;
				break;
				
			case CALL_POP_RESULT:
				if(id != null && !(table_de_symbol.getPosition(id) instanceof Position_Registre) && !(id.getId().equals("_")))
				{
					//output.println("\n\n\n@the return value is not null\""+ id + "\"");
					pop_result(id);
				}
				else if (id != null && !(id.getId().equals("_"))){
					//output.println("\n\n\n@the return value is not nullelse");
					pop_result_tas(id);
				}
				etat_courant = Etat.FINISHED;
				break;
				
			case CALL_LD_LR://bypassed, fait dans mise en place environnment
				popRegStackTop("lr");
				etat_courant = Etat.FINISHED;
				break;
				
			default:
				etat_courant = Etat.FINISHED;
				break;
			}
		//fin du while
		etat_courant = Etat.DEBUT;
	}

	public void ecrire_callClosure(AppC_asml app, Id id) {
		// TODO Auto-generated method stub
		List<Var_asml> mem_plus_args = null; 
		while(etat_courant != Etat.FINISHED)
			switch(etat_courant)
			{
			case DEBUT:
				if((app.getE() instanceof Var_asml))
				{
					etat_courant = Etat.CALL_SAV_REG_ARGS;
					mem_plus_args = new ArrayList<Var_asml>();// On ajoute la "mémoire" aux arguments (en première position)
					mem_plus_args.add((Var_asml) app.getE());
					mem_plus_args.addAll(app.getEs());
					break;
				}
				etat_courant = Etat.FINISHED;
				break;
				
			case CALL_PUSH_LR://by passed done by ENV
				pushRegStack("lr");
				etat_courant = Etat.CALL_SAV_REG_ARGS;
				break;
				
			case CALL_SAV_REG_ARGS:
				save_reg_args(app.getEs().size() + 1);
				etat_courant = Etat.CALL_PUSH_ARGS;
				break;
				
			case CALL_PUSH_ARGS:
				push_arguments(mem_plus_args);
				etat_courant = Etat.CALL_MAKE_CALL;
				break;
				
			case CALL_MAKE_CALL:
				make_CallClosure();
				etat_courant = Etat.CALL_PUSH_RESULT;
				break;
				
			case CALL_PUSH_RESULT:
				if(id != null && !(table_de_symbol.getPosition(id) instanceof Position_Registre))
					putRegVar(id, 0);
				else if (id != null)
					push_result_tas();
				etat_courant = Etat.CALL_RESTORE_REG_ARGS;
				break;
				
			case CALL_RESTORE_REG_ARGS:
				restore_reg_args(app.getEs().size());
				etat_courant = Etat.CALL_POP_RESULT;
				break;
				
			case CALL_POP_RESULT:
				if(id != null && !(table_de_symbol.getPosition(id) instanceof Position_Registre))
					pop_result(id);
				else if (id != null)
					pop_result_tas(id);
				etat_courant = Etat.FINISHED;
				break;
					
			case CALL_LD_LR://bypassed done in ENV
				popRegStackTop("lr");
				etat_courant = Etat.FINISHED;
				break;
				
			default:
				etat_courant = Etat.FINISHED;
				break;
			}
		//fin du while
		etat_courant = Etat.DEBUT;
	}

	/**
	 * Ecris le début d'un If: etiq if(redondante), chargement des variables, puis compare & branchement puis etiquette else (redondante)
	 * @param cond	La condition à tester
	 * @param numLabel le numero des label à appeler
	 */
	public void ecrire_If(Exp_asml cond, int numLabel)
	{
		Exp_asml op1=null, op2=null;
		while(etat_courant != Etat.FINISHED)
			switch(etat_courant)
			{
				case DEBUT:
					output.println("if" + numLabel + ": ");
					if(cond instanceof Eq_asml)
					{
						this.Operation = "\tBEQ then"+ numLabel;
						op1 = ((Eq_asml)cond).getE1();
						op2 = ((Eq_asml)cond).getE2();
					}
					else if(cond instanceof LE_asml)
					{
						this.Operation = "\tBLE then"+ numLabel;
						op1 = ((LE_asml)cond).getE1();
						op2 = ((LE_asml)cond).getE2();
					}
					etat_courant = Etat.IF_COND_ARG1;
					break;
					
				case IF_COND_ARG1:
					if(op1 instanceof Var_asml)
						RegOP1 = this.getRegVar(((Var_asml) op1).getId(), 1);
					etat_courant = Etat.IF_COND_ARG2;
					break;
				
				case IF_COND_ARG2:
					if(op2 instanceof Var_asml)
						RegOP2 = this.getRegVar(((Var_asml) op2).getId(), 2);
					etat_courant = Etat.IF_COND_CMP;
					break;
					
				case IF_COND_CMP:
					output.println("\tCMP " + RegOP1 + ", " + RegOP2);
					output.println(Operation);
					etat_courant = Etat.FINISHED;
					break;
				default:
					etat_courant = Etat.FINISHED;
			}
		//Fin while
		etat_courant = Etat.DEBUT;
	}
	
	/**
	 * Ecris l'étiquette d'un else (redondant, juste pour la clarté du code
	 * @param numLabel numero de l'étiquette à écrire
	 */
	public void ecrire_ElseEtiq(int numLabel)
	{
		output.println("else" + numLabel + ": ");
	}
	
	/**
	 * Ecris la fin de la partie else d'un if: branchement vers continue
	 * @param numLabel le numero des label a appeler pour le branchement
	 */
	public void ecrire_BALContinue(int numLabel)
	{
		output.println("\tBAL continue" + numLabel);
	}
	
	/**
	 * Ecris l'etiquette then d'un if
	 * @param numLabel le numero du label à écrire
	 */
	public void ecrire_ThenEtiq(int numLabel)
	{
		output.println("then" + numLabel + ": ");
	}
	
	/**
	 * Ecris l'etiquette continue d'un if
	 * @param numLabel le numero du label à écrire
	 */
	public void ecrire_EtiqContinue(int numLabel)
	{
		output.println("continue" + numLabel + ": ");
	}
	
	/**
	 * Ecris une affectation: opération gérée: x <- Var | Int | Var OP (Var|Int)
	 * @param exp let x(id) = EXP in ---
	 * @param id id de la variable de destination
	 */
	public void ecrire_affectation(Exp_asml exp, Id id)
	{
		if(table_de_symbol.getPosition(id) instanceof Position_Registre)
			RegDest = "r" + ((Position_Registre)table_de_symbol.getPosition(id)).getNumReg();
		else
			RegDest = "r" + table_de_symbol.numReserve2();
		
		while(etat_courant != Etat.FINISHED)
		{
			switch(etat_courant)
			{
			case DEBUT:
				if(exp instanceof Var_asml)
				{
					Operation = "MOV ";
					etat_courant = Etat.LET_SINGLE_OP_VAR;
				}
				else if (exp instanceof Int_asml)
				{
					Operation = "MOV ";
					etat_courant = Etat.LET_SINGLE_OP_IMM;
					break;
				}
				else if(exp instanceof Bool_asml)
				{
					Operation = "MOV ";
					etat_courant = Etat.LET_SINGLE_OP_BOOL;
					break;
				}
				else if(exp instanceof Add_asml )
				{
					Operation = "ADD ";
					etat_courant = Etat.LET_FIRST_OP;
				}
				else if (exp instanceof Sub_asml)
				{
					Operation = "SUB ";
					etat_courant = Etat.LET_FIRST_OP;
				}
				else if( exp instanceof Get_asml)
					etat_courant = Etat.LET_GET_OP_ARRAY;
				else if( exp instanceof Neg_asml)
					etat_courant = Etat.LET_NEG;
				else
					etat_courant = Etat.FINISHED;
				break;
				
			case LET_NEG:
				if(((Neg_asml)exp).getE()instanceof Var_asml)
					RegOP1 = getRegVar(((Var_asml)((Neg_asml)exp).getE()).getId(), 1);
				else if( ((Neg_asml)exp).getE() instanceof Int_asml)
					RegOP1 = "#" + ((Int_asml)((Neg_asml)exp).getE()).getI();
				
				affecteNEG();
				doFinalStore(id);
				etat_courant = Etat.FINISHED;
				break;
				
			case LET_GET_OP_ARRAY:
				Get_asml get = (Get_asml) exp;
				RegOP1 = getRegVar(((Var_asml)get.getE1()).getId(),1);
				etat_courant = Etat.LET_GET_OP_OFFSET;
				break;
				
			case LET_GET_OP_OFFSET:
				Get_asml get1 = (Get_asml) exp;
				RegOP2 = getRegVar(((Var_asml)get1.getE2()).getId(),2);
				if(table_de_symbol.getPosition(id) instanceof Position_Registre)
					RegDest = "r" +((Position_Registre)table_de_symbol.getPosition(id)).getNumReg();
				else
					RegDest = "r" + table_de_symbol.numReserve2();
				
				ldrRegRegRegOffset(RegDest, RegOP1, RegOP2, true);
				etat_courant = Etat.FINAL_STORE;
				break;
				
			case LET_SINGLE_OP_VAR:
				RegOP1 = getRegVar(((Var_asml)exp).getId(),1);
				movRegReg(RegOP1,RegDest);
				etat_courant = Etat.FINAL_STORE;
				break;
			
			case LET_SINGLE_OP_IMM:
				RegOP1 = ("#" + ((Int_asml)exp).getI());
				etat_courant = Etat.LET_PRINT_OP_SINGLE;
				break;
				
			case LET_SINGLE_OP_BOOL:
				if(((Bool_asml)exp).isB())
					RegOP1 = ("#1");
				else
					RegOP1 = ("#0");
				etat_courant = Etat.LET_PRINT_OP_SINGLE;
				break;
				
			case LET_FIRST_OP:
				Id firstOp_id = ((Var_asml)dualOpGetFirst(exp)).getId();
				RegOP1 = getRegVar(firstOp_id,1);
				etat_courant = Etat.LET_SECOND_OP;
				break;
				
			case LET_SECOND_OP:
				if(dualOpGetSecond(exp) instanceof Var_asml)
				{
					Id secondOp_id = ((Var_asml)dualOpGetSecond(exp)).getId();
					RegOP2 = getRegVar(secondOp_id,2);
				}
				else if(dualOpGetSecond(exp) instanceof Int_asml)
					RegOP2 = "#" + ((Int_asml)dualOpGetSecond(exp)).getI();
				etat_courant = Etat.LET_PRINT_OP_DUAL;
				break;
				
			case LET_PRINT_OP_SINGLE:
				if( table_de_symbol.getPosition(id) instanceof Position_Registre)
					RegDest = ("r"+((Position_Registre)table_de_symbol.getPosition(id)).getNumReg());
				else
					RegDest = ("r"+ table_de_symbol.numReserve2());
				output.println("\t"+Operation + RegDest + ", " + RegOP1);
				etat_courant = Etat.FINAL_STORE;
				break;
				
			case LET_PRINT_OP_DUAL:
				if( table_de_symbol.getPosition(id) instanceof Position_Registre)
					RegDest = ("r"+((Position_Registre)table_de_symbol.getPosition(id)).getNumReg());
				else
					RegDest = ("r"+ table_de_symbol.numReserve2());
				output.println("\t"+Operation + RegDest + ", " + RegOP1 + ", " + RegOP2);
				etat_courant = Etat.FINAL_STORE;
				
			case FINAL_STORE:
				doFinalStore(id);
				etat_courant = Etat.FINISHED;
				break;
				
			case FINISHED:
				break;
				
			default:
				etat_courant = Etat.FINISHED;
				break;
			}
		}
		//End while
		etat_courant = Etat.DEBUT;
	}

	public void ecrire_put(Put_asml e) {
		Var_asml opArray = (Var_asml)e.getE1();
		Var_asml opOffset = (Var_asml)e.getE2();//TODO int
		Var_asml opSRC = (Var_asml)e.getE3();
		boolean stored_TMP = false;
		while(etat_courant != Etat.FINISHED)
			switch(etat_courant)
			{
			case DEBUT:
				etat_courant = Etat.PUT_SRC;
				break;
				
			case PUT_SRC:
				if(table_de_symbol.getPosition(opSRC.getId()) instanceof Position_Registre)
					RegDest = getRegVar(opSRC.getId(),1);
				else
				{
					RegDest = tmpReg0_ldr(opSRC.getId());
					stored_TMP = true;
				}
				etat_courant = Etat.PUT_ARRAY;
				break;
				
			case PUT_ARRAY:
				RegOP1 = getRegVar(opArray.getId(),1);
				etat_courant = Etat.PUT_OFFSET;
				break;
				
			case PUT_OFFSET:
				RegOP2 = getRegVar(opOffset.getId(),2);
				strRegRegRegOffset(RegDest,RegOP1, RegOP2, false);
				if(stored_TMP)
					tmpReg0_restore();
				etat_courant = Etat.FINISHED;
				break;
				
			default:
				etat_courant = Etat.FINISHED;
			}
		//fin while
		etat_courant = Etat.DEBUT;
		
	}

	/**
	 * ecris l'allocation de mémoire d'une closure (tableau?)
	 * @param e l'expression de l'allocation mémoire
	 */
	public void ecrire_allouer_mem(Let_memory_alloc_asml e) {
		String regR1 = "r" + table_de_symbol.numReserve1();
		String regR2 = "r" + table_de_symbol.numReserve2();
		String regDest;
		String labelTas = Table_de_symboles.getLabel_Tas();
		
		if(table_de_symbol.getPosition(e.getId())instanceof Position_Registre)
			regDest = getRegVar(e.getId(),1);
		else
			regDest = regR2;
		ldrRegLabel(regR1, labelTas);
		ldrRegReg(regR2,regR1);
		output.println("\tADD " + regDest + ", " + regR1 + ", " + regR2 + "\t\t@debut du tas + offset == valeur a retourner");
		doFinalStore(e.getId());
		
		output.println("\tADD "+regR2+", #" + ((Int_asml)e.getOffset()).getI() + ", LSL #2");
		strRegLabel(regR2, labelTas, regR1);
	}

	/**
	 * Ecrit un chargement depuis l'espace memoire d'une closure
	 * @param e l'expression du chargement memoire
	 */
	public void ecrire_mem_get(Let_memory_load_asml e){
		Position_variable pos = table_de_symbol.getPosition(e.getId());
		if(pos instanceof Position_Registre)
			RegDest = "r" + ((Position_Registre)pos).getNumReg();
		else
			RegDest = "r" + table_de_symbol.numReserve2();
	
		ldrRegRegRegOffset(RegDest,"r0","#" + ((Int_asml)e.getOffset()).getI(), false);
		doFinalStore(e.getId());
	}
	
	/**
	 * Ecrit une écriture dans l'espace mémoire d'une closure
	 * @param e l'expression de l'écriture mémoire
	 */
	public void ecrire_mem_put(Let_memory_store_asml e) {
		Id addr_id = e.getAddr().getId();
		Id val_id = e.getVal().getId();
		
		RegOP1 = getRegVar(addr_id,1);
		RegOP2 = getRegVar(val_id,2);
		
		strRegRegRegOffset(RegOP2,RegOP1,"#" + ((Int_asml)e.getOffset()).getI(), false);
	}

	/**
	 * Met en place l'environnement: sauvgarde le fp precedent & le met à jour,
	 *  puis alloue la place necessaire aux variables locales
	 */
	public void setup_env()
	{
		output.println("\tPUSH { fp } ");
		output.println("\tMOV fp, sp");
		//N'alloue de la place que si il y en a besoin
		if(table_de_symbol.getFirst_fit()+SIZE_OF_WORD != 0)
			output.println("\tSUB sp, sp, #" + (-1*table_de_symbol.getFirst_fit()-4));
	}
	
	/**
	 * Sauvegarde les registres utilisés (a utiliser dans les LETREC)
	 */
	public void setup_env_SavRegs()
	{
		if(table_de_symbol.modifiedReg() != null)
			output.println("\tSTMFD sp!, " + table_de_symbol.modifiedReg());
	}

	/**
	 * Restore les registres sauvegardé (doit correspondre au savRegs, sinon, BOUM!)
	 */
	public void restoreRegsReturn()
	{
		if(table_de_symbol.modifiedReg() != null)
			output.println("\tLDMFD sp!, " + table_de_symbol.modifiedReg());
		output.println("\tPOP { fp }");
		output.println("\tMOV pc, lr");
	}
	
	
	/**
	 * Push tous les arguments selon la convention: 0 à 4 dans reg 0 à 3, le reste dans la pile de +4 à +X/3
	 * @param es la liste des arguments (expression: Var, Int ou Bool(pas encore fait)
	 */
	private void push_arguments(List<Var_asml> es) {
		Var_asml arg_x,var_courante;
		Int_asml arg_i;
		Bool_asml arg_b;
		ArrayList<Integer> registres_critiques = new ArrayList<Integer>(); 
		for(int i=0; i< es.size(); i++)
		{
		//For pour vérifier si des arguments sont dans des registres qui vont être écrasés
			if(es.get(i) instanceof Var_asml)
			{
				var_courante = (Var_asml) es.get(i);
				if( table_de_symbol.getPosition(var_courante.getId()) instanceof Position_Registre)
				{
					Position_Registre pos = (Position_Registre) table_de_symbol.getPosition(var_courante.getId());
					if( pos.getNumReg() < NB_ARGS_PLACES_REG && pos.getNumReg() < i)
						// le registre est critique si: il est dans 0-3, et s'il sera écrit avant d'être "push"
						registres_critiques.add(pos.getNumReg());
				}
			}
			
		}
		saveRegCritique(es.size(), registres_critiques);
		
		for(int i=0; i < es.size(); i++)
		{
			Exp_asml arg_courant = es.get(i);
			// NOOOO! need to push first les variables dans les premiers registre en premier sinon ils risquent d'être écrasés
			// Yes, it should be pretty much done by now
			if(arg_courant instanceof Var_asml)
			{
			// Variable:
				arg_x = (Var_asml)arg_courant;
				if(table_de_symbol.getPosition(arg_x.getId()) instanceof Position_Registre)
				{
					// Pos_Reg:
					Position_Registre pos = (Position_Registre) table_de_symbol.getPosition(arg_x.getId());
					if(registres_critiques.contains(pos.getNumReg()))
					{
						// Reg_Critique:
						if( i < NB_ARGS_PLACES_REG)
							this.ldrRegCrit(es.size() - NB_ARGS_PLACES_REG, i, registres_critiques.indexOf(pos.getNumReg()));
						else
							this.ldrRegCrit(es.size() - i, i, registres_critiques.indexOf(pos.getNumReg()));
					}
					else
						//Reg non critique
						push_argument_pos(arg_x, i);
				}
				else
					// Pos_Pile:
					push_argument_pos(arg_x, i);
				
			}
			else if (arg_courant instanceof Int_asml)
			{
			// Int
				arg_i = (Int_asml)arg_courant;
				push_argument_pos(arg_i, i);
			}
			else if (arg_courant instanceof Bool_asml)
			{
			// Bool
				arg_b = (Bool_asml)arg_courant;
				push_argument_pos(arg_b, i);
			}
		}
		
	}
	
	/**
	 * Push un argument qui est une variable
	 * @param arg_x la variable a push
	 * @param i le numero de l'argument (dont on déduit la position a lui donner)
	 */
	private void push_argument_pos(Var_asml arg_x, int i) {
		if(i < NB_ARGS_PLACES_REG)
		{
			if(table_de_symbol.getPosition(arg_x.getId()) instanceof Position_Registre)
			{
				// Mov var
				Position_Registre pos_reg = (Position_Registre)table_de_symbol.getPosition(arg_x.getId());
				movRegReg(("r" + pos_reg.getNumReg()),"r"+i );
			}
			else if (table_de_symbol.getPosition(arg_x.getId()) instanceof Position_Pile)
			{
				// Ldr var
				Position_Pile pos_pile = (Position_Pile) table_de_symbol.getPosition(arg_x.getId());
				ldrRegStack(pos_pile.getOffset(), ("r" + i ));
			}
			else if (table_de_symbol.getPosition(arg_x.getId()) instanceof Label_Var)
			{
				// Ldr var
				Position_Pile pos_pile = (Position_Pile) table_de_symbol.getPosition(arg_x.getId());
				ldrRegStack(pos_pile.getOffset(), ("r" + i ));
			}
			//TODO position label
		}
		else
		{
			if(table_de_symbol.getPosition(arg_x.getId()) instanceof Position_Registre)
			{
				// Push var
				Position_Registre pos_reg = (Position_Registre)table_de_symbol.getPosition(arg_x.getId());
				pushRegStack("r" + pos_reg.getNumReg());
			}
			else if (table_de_symbol.getPosition(arg_x.getId()) instanceof Position_Pile)
			{
				// LDR var -> Push var
				Position_Pile pos_pile = (Position_Pile) table_de_symbol.getPosition(arg_x.getId());
				ldrRegStack(pos_pile.getOffset(), ("r" + table_de_symbol.numReserve1() ));
				pushRegStack("r" + table_de_symbol.numReserve1());
			}
			//TODO position label
		}
		
	}

	/**
	 * Push un argument de type booleen a sa place i
	 * @param arg_b l'argument a placer
	 * @param i le numero de l'argument pour savoir où le placer
	 */
	private void push_argument_pos(Bool_asml arg_b, int i) {
		if(i < NB_ARGS_PLACES_REG)
		{
			// Mov bool
			output.println("\tMOV r" + i + ", #" + (arg_b.isB() ? "1" : "0"));
		}
		else
		{
			// Push bool
			output.println("\tMOV r" + table_de_symbol.numReserve1() + ", #" + (arg_b.isB() ? "1" : "0") );
			pushRegStack("r" + table_de_symbol.numReserve1());
			
		}
	}

	/** 
	 * push un argument de type entier a sa place i
	 * @param arg_i l'argument a placer
	 * @param i le numero de l'argument pour savoir où le placer (0-3 -> reg, sinon pile)
	 */
	private void push_argument_pos(Int_asml arg_i, int i) {
		if(i < NB_ARGS_PLACES_REG)
		{
			// Mov int
			output.println("\tMOV r" + i + ", #" + arg_i.getI());
		}
		else
		{
			// Push int
			output.println("\tMOV r" + table_de_symbol.numReserve1() + ", #" + arg_i.getI());
			pushRegStack("r" + table_de_symbol.numReserve1());
			
		}
	}
	
	/**
	 * Saveugarde les registres qui vont être écrasés par la mises en place des arguments
	 * @param nb_args nombre d'argument à mettre en place
	 */
	private void save_reg_args(int nb_args)
	{
		String regEcraseArgs = table_de_symbol.modifiedRegArgs(nb_args);
		if( regEcraseArgs!= null)
			output.println("\tSTMFD sp!, " + regEcraseArgs);
	}
	
	/**
	 * Restore les registres qui ont été écrasés par la mises en place d'arguments
	 * @param nb_args
	 */
	private void restore_reg_args(int nb_args)
	{
		String reg_ecrases = table_de_symbol.modifiedRegArgs(nb_args);
		if(reg_ecrases != null)
			output.println("\tLDMFD sp!, " + reg_ecrases);
	}

	/**
	 * Sauvegarde tous les registre critique sur la pile, au dessus des arguments (! dépasse le sommet de pile)
	 * @param nbargs nombre d'arguments (offset p/r au sommet de pile
	 * @param listRegCrit liste des registres a sauver
	 */
	
	private void saveRegCritique(int nbargs, List<Integer> listRegCrit)
	{
		for(int i=0; i < listRegCrit.size(); i++)
		{
			// Offset: place des args + 1 pour se fait une place
			output.println("\tSTR r" + listRegCrit.get(i) + ", [ sp, #-" + ((nbargs - NB_ARGS_PLACES_REG) + i + 1)*SIZE_OF_WORD + " ]");
		}
	}

	/**
	 * Charge le registre critique qui a été sauvegardé et le place a bon endroit (selon son n° d'argument)
	 * @param nbargsRestant nombre d'arguments restant a placer ( qui seront dans la pile)
	 * @param numArg le numero de l'argument qu'on essaie de placer
	 * @param numRegCrit le numero de registre critique ~le i-éme registre critique rencontré
	 */
	private void ldrRegCrit(int nbargsRestantEnPile,int numArg, int numRegCrit)
	{
		String regDest;
		if(numArg < NB_ARGS_PLACES_REG)
		{
			// Charge le registre critique sauvegardé, et le met dans le bon registre
			regDest = "r" + numArg;
			// Offset: 	nb arguments restant a placer (tout ceux hors registres -> size - 4)
			// 		+	index dans la table des index critiques
			//		+	1 (parce que l'index commence à 0)
			output.println("\tLDR " + regDest + ", [ sp, #-" + ((nbargsRestantEnPile) + numRegCrit + 1)*SIZE_OF_WORD + " ]");
		}
		else
		{
			//charge le registre critique sauvegardé, et le met sur le sommet de pile
			regDest = "r" + table_de_symbol.numReserve1();
			// Offset: 	nb arguments restant a placer (ex: placé 5(index)/8(size) -> reste 3 a placer: n°{5,6,7}
			// 		+	index dans la table des index critiques
			//		+	1 (parce que l'index commence à 0)
			output.println("\tLDR " + regDest + ", [ sp, #-" + ((nbargsRestantEnPile) + numRegCrit + 1)*SIZE_OF_WORD + " ]");
			pushRegStack(regDest);
			
		}
	}

	/**
	 * Ecris un call, en cherchant eventuellement l'endroit de la fonction
	 * @param fonction Var: la fonction a appelée: (va être cherchée en Reg/pile/label)
	 * @param nb_args le nombre d'arguments de la fonction (pour savegarder les registres ecrésés par les arguments
	 */
	private void make_Call(Var_asml fonction)
	{
		if(table_de_symbol.getPosition(fonction.getId()) instanceof Label_Fonction)
		{
			output.println("\tBL " + fonction.getId().getId());
		}
		else if (table_de_symbol.getPosition(fonction.getId()) instanceof Position_Registre)
		{
			Position_Registre pos_reg = (Position_Registre) table_de_symbol.getPosition(fonction.getId());
			output.println("\tBLX r" + pos_reg.getNumReg());
		}
		else if  (table_de_symbol.getPosition(fonction.getId()) instanceof Position_Pile)
		{
			Position_Pile pos_pile = (Position_Pile) table_de_symbol.getPosition(fonction.getId());
			ldrRegStack(pos_pile.getOffset(), "r" + table_de_symbol.numReserve1());
			output.println("\tBLX r" + table_de_symbol.numReserve1());
		}
	}
	
	private void make_CallClosure()
	{
		String regfun = "r" + table_de_symbol.numReserve1();
		ldrRegReg(regfun,"r0");
		output.println("\tBLX " + regfun);
	}

	/**
	 * @deprecated use push & push_result and pop_result instead
	 * Si l'id du resultat existe et est en registre ou en pile, replace le resultat au bon endroit
	 * @param id l'id de la variable de retour.
	 */
	private void move_result(Id id) {
		Position_variable pos = table_de_symbol.getPosition(id);
		if(pos instanceof Position_Registre)
			movRegReg("r0", ("r" + ((Position_Registre) pos).getNumReg()));
		else if (pos instanceof Position_Pile)
			strRegStack(((Position_Pile) pos).getOffset() , "r0");
	}
	
	private void push_result(Id id){
		if(!(table_de_symbol.getPosition(id) instanceof Position_Registre))
		{
			
		}
	}
	
	private void pop_result(Id id){
		Position_variable pos = table_de_symbol.getPosition(id);
		
		if(pos instanceof Position_Registre){
			RegDest = "r" + ((Position_Registre) pos).getNumReg();
			popRegStackTop(RegDest);
		}
		else if (pos instanceof Position_Pile){
			RegDest = "r" + table_de_symbol.numReserve2();
			popRegStackTop(RegDest);
			strRegStack(((Position_Pile) pos).getOffset() , RegDest);
		}
	}
	
	private void push_result_tas()
	{
		push_sommet_tas("r0");
	}
	
	private void pop_result_tas(Id id)
	{
		Position_Registre pos = (Position_Registre) table_de_symbol.getPosition(id);
		pop_sommet_tas("r" + pos.getNumReg());
		 
	}
	
	/**
	 * Renvoie le premier operande d'un operation a deux operande
	 * @param e l'op a deux operande
	 * @return le premier operande
	 */
	private Exp_asml dualOpGetFirst(Exp_asml e)
	{
		if(e instanceof Add_asml)
			return ((Add_asml)e).getE1();
		else if(e instanceof Sub_asml)
			return ((Sub_asml)e).getE1();
		Var_asml trash = new Var_asml(new Id("trash"));
		return new Add_asml(trash,trash);
	}
	
	/**
	 * Renvoie le second operande d'un operation a deux operande
	 * @param e l'op a deux operande
	 * @return le second operande
	 */
	private Exp_asml dualOpGetSecond(Exp_asml e)
	{
		if(e instanceof Add_asml)
			return ((Add_asml)e).getE2();
		else if(e instanceof Sub_asml)
			return ((Sub_asml)e).getE2();
		Var_asml trash = new Var_asml(new Id("trash"));
		return new Add_asml(trash,trash);
	}
 
	/**
	 * Si la variable est dans un registre, ne fait rien car le resultat est déjà au bon endroit.
	 * Sinon, stock le resultat a l'adresse de la variable
	 * @param varID Id de la variable
	 */
	private void doFinalStore(Id varID)
	{
		Position_variable position = table_de_symbol.getPosition(varID);
		if(position instanceof Position_Pile)
		{
			int offSet = ((Position_Pile) position).getOffset();
			strRegStack(offSet, "r" + table_de_symbol.numReserve2());
		}
	}

	/**
	 * Regarde si la variable est dans un registre, sinon, la charge dans un des registre reservé
	 * @param varId nom de la variable
	 * @param num_res numéro du registre reservé
	 * @return rX x etant le numero du registre dans lequel est la variable
	 */
	private String getRegVar(Id varId, int num_res)
	{
		Position_variable position = table_de_symbol.getPosition(varId);
		if(position instanceof Position_Registre)
		{
			// Var en registre > Reg
			int numReg = ((Position_Registre) position).getNumReg();
			return ("r" + numReg);
		}
		else if(position instanceof Position_Pile)
		{
			// Var en pile: LDR > Reg
			int offSet = ((Position_Pile) position).getOffset();
			int regLibre;
			if(num_res == 1)
				regLibre = table_de_symbol.numReserve1();
			else
				regLibre = table_de_symbol.numReserve2();
			
			ldrRegStack(offSet, "r" + regLibre);
			return ("r" + regLibre);
		}
		else if(position instanceof Label_Fonction)
		{
			// Var en label (fonction) LDR > Reg
			String label = ((Label_Var) position).getId();
			int regLibre;
			if(num_res == 1)
				regLibre = table_de_symbol.numReserve1();
			else
				regLibre = table_de_symbol.numReserve2();
			
			ldrRegLabel("r" + regLibre,label);
			return ("r" + regLibre);
		}
		else if(position instanceof Label_Var || position instanceof Label_Int || position instanceof Label_Float)
		{
			// Var en label (fonction) LDR > Reg
			String label = ((Label_Var) position).getId();
			int regLibre;
			if(num_res == 1)
				regLibre = table_de_symbol.numReserve1();
			else
				regLibre = table_de_symbol.numReserve2();
			
			ldrRegLabel("r" + regLibre,label);
			ldrRegReg("r" + regLibre,"r" + regLibre);
			return ("r" + regLibre);
		}
		output.println(position.to_String());
		return null;
	}

	/**
	 * Prend une valeur dans un registre, et l'écris dans la variable, peu importe où elle est
	 * @param id Id de la variable
	 * @param numReg numero du registre de la valeur
	 */
	private void putRegVar(Id id, int numReg)
	{
		Position_variable pos = table_de_symbol.getPosition(id);
		if( pos instanceof Position_Registre)
			movRegReg("r" + numReg, "r" + ((Position_Registre)pos).getNumReg());
		else if( pos instanceof Position_Pile)
			strRegStack(((Position_Pile)pos).getOffset(), "r" + numReg);
		else if(pos instanceof Label_Var)
			strRegLabel("r" + numReg, ((Label_Var)pos).getId(), "r" + table_de_symbol.numReserve2());
	}
	/**
	 * Place une adresse pointée par un Label dans le registre indiqué
	 * @param registre dans lequel placer l'adresse
	 * @param label label a charger
	 */
	private void ldrRegLabel(String registre, String label) {
		output.println("\tLDR " + registre + ", =" + label);
	}


	/**
	 * Ecrit la valeur du registre en argument, dans le label
	 * @param registre à écrire en mémoire
	 * @param label label où on doit écrire
	 * @param registre temporaire où stocker l'adresse
	 */
	private void strRegLabel(String registre, String label, String regAdr) {
		output.println("\tLDR " + regAdr + ", =" + label);
		output.println("\tSTR " + registre +", [ " + regAdr + " ] ");
	}

	/**
	 * Charge dans le registre destination, la case memoire designée dans le registre src
	 * @param dest le registre dans lequel charger le mot mémoire
	 * @param src le registre qui contient l'adresse pointant sur le mot a charger
	 */
	private void ldrRegReg(String dest, String src)
	{
		output.println("\tLDR " + dest + ", [ " + src + " ] " );
	}
	
	/**
	 * Charge dans le registre destination, l'adresse pointée par le registre source décalé de offset *4
	 * @param dest registre destination
	 * @param src registre source
	 * @param offset registre decalage
	 */
	private void ldrRegRegRegOffset(String dest, String src, String offset, boolean b)
	{
		if(b)
			output.println("\tLDR " + dest + ", [ " + src + ", " + offset + ", LSL #2 ] ");
		else
			output.println("\tLDR " + dest + ", [ " + src + ", " + offset + " ] ");
	}
	/**
	 * Deplace le registre source dans le registre destination, s'ils ont le mêmes nom, ne fait rien 
	 * @param regSrc registe source
	 * @param regDest registre destination
	 */
	private void movRegReg(String regSrc, String regDest)
	{
		if(!regSrc.equals(regDest))
			output.println("\tMOV " + regDest + ", " + regSrc);
	}
	
	/**
	 * Ecris le chargement d'un offset p/r au FP dans un registre
	 * @param offset offset a charger
	 * @param regdest registre de destination
	 */
	private void ldrRegStack(int offset, String regdest)
	{
		output.println("\tLDR " + regdest + ", [ fp, #" + offset + " ]");
	}
	
	/**
	 * Ecris l'ecriture d'un registre en pile p/r a un offset au FP
	 * @param offset offset a charger
	 * @param regdest registre de destination
	 */
	private void strRegStack(int offset, String regSrc)
	{
		output.println("\tSTR " + regSrc + ", [ fp, #" + offset + " ]");
	}
	
	/**
	 * Ecris l'ecriture en memoire de la source a l'adresse décalée de l'offset
	 * @param regSRC
	 * @param regArray
	 * @param regOffset
	 * @param b 
	 */
	private void strRegRegRegOffset(String regSRC, String regArray, String regOffset, boolean b)
	{
		if(b)
			output.println("\tSTR " + regSRC + ", [ " + regArray + ", " + regOffset + ", LSL #2 ] ");
		else
			output.println("\tSTR " + regSRC + ", [ " + regArray + ", " + regOffset + " ] ");
	}

	/**
	 * ecris un registre sur le sommet de pile
	 * @param regSrc le registre source
	 */
	private void pushRegStack(String regSrc)
	{
		output.println("\tPUSH { " + regSrc  +" } ");
	}

	/**
	 * Met le sommet de pile dans le registre indiqué. diminue le sommet de pile dans l'action.
	 * @param regDest registre de destination
	 */
	private void popRegStackTop(String regDest)
	{
		output.println("\tPOP { " + regDest + " }");
	}
	
	private String tmpReg0_ldr(Id id)
	{
		pushRegStack("r0");
		if(table_de_symbol.getPosition(id) instanceof Position_Pile)
		{
			int offset = ((Position_Pile)table_de_symbol.getPosition(id)).getOffset();
			ldrRegStack(offset,"r0");
		}
		return "r0";
	}
	
	private void tmpReg0_restore()
	{
		popRegStackTop("r0");
	}
	
	private void affecteNEG()
	{
		String r2 = "r" + table_de_symbol.numReserve2();
		output.println("\tMOV " + r2 + ", #0");
		output.println("\tSUB " + RegDest+ ", " + r2 + ", " + RegOP1 );
	}
	
	private void push_sommet_tas(String regsrc)
	{
		String regTas = "r" + table_de_symbol.numReserve1();
		String regOffset = "r" + table_de_symbol.numReserve2();
		String labelTas = Table_de_symboles.getLabel_Tas();
		
		ldrRegLabel(regTas, labelTas);
		ldrRegReg(regOffset,regTas);
		strRegRegRegOffset(regsrc, regTas, regOffset, false);
	}
	
	private void pop_sommet_tas(String regDest)
	{
		String regTas = "r" + table_de_symbol.numReserve1();
		String labelTas = Table_de_symboles.getLabel_Tas();
		
		ldrRegLabel(regTas, labelTas);
		ldrRegReg(regDest,regTas);
		ldrRegRegRegOffset(regDest, regTas, regDest, false);
	}
}
