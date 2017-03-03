package BackEnd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import Asml.PrintAsml;
import BackEnd.VariableAllocator.Strategie_allocation;
import BackEnd.Position.Label_Int;
import BackEnd.Position.Table_de_symboles;
import Code_fourni.PrintVisitor;
import Expression_asml.Exp_asml;
import Expression_asml.Let_asml;
import Expression_asml.LetRec_asml;
import Expression_asml.Exp_asml;

public class BackEnd_Runner {
	//private boolean debug = true;
	private static ArrayList<Label_Int> bigInts; 
	
	/**
	 * Fonction pour run avec une liste d'expression, ecris une trace de debug en console, et le resultat dans un fichier
	 * @param list_asmt list d'exp
	 * @param arm_file file output
	 */

	public static void run (List<Exp_asml> list_asmt,PrintStream arm_file, boolean debug)
	{
		PrintStream debug_out=System.out;
		bigInts = new ArrayList<Label_Int>();
		int size = list_asmt.size();
		if(debug){
			debug_out.println("###################");
			debug_out.println("Arbre ASML en entrée");
			for(int i=0;i<size;i++){
				Exp_asml exp = list_asmt.get(i);
				if(i==size-1)
					debug_out.println("Main");
				else if (exp instanceof Let_asml)
					debug_out.println("Float");
				else
					debug_out.println("Fonction ");
				exp.accept(new PrintAsml(debug_out));
				debug_out.println();
			}
			debug_out.println();
		}
		
		generate_tas("label_tas",1024,arm_file);
		
		debug_out.println();
		arm_file.println("\n\t.text");
		
		for(int i=0;i<size;i++){
			Exp_asml asmt = list_asmt.get(i);
			if(i==size-1){
				generate_start(arm_file);
				generator(asmt, debug_out,arm_file, debug);
			}
			if(!(asmt instanceof Let_asml)){
				arm_file.println( Backend_visitor.rename_fun(((LetRec_asml)asmt).getFd().getId()).getId() + ":" );
				generator(asmt, debug_out,arm_file, debug);
				arm_file.println();
			}
			else if(! ((Let_asml)asmt).getId().getId().equals("_"))
			{
				//TODO
				//Cas des floats
				arm_file.println( ((Let_asml)asmt).getId().getId() + ": @Float [NON GERE]" );
				generator(asmt, debug_out,arm_file, debug);
				arm_file.println();
			}
		}
		if(debug)
		{
			arm_file.println("\tBL min_caml_print_newline ");
			//arm_file.print("\d");
			System.out.println("--------- ARM Generation done ----------");
		}
		generate_labelInts(arm_file);
		arm_file.close();
	}
	
	
	
	public static void run(List<Exp_asml> list_asmt,PrintStream arm_file)
	{
		run(list_asmt, arm_file, false);
	}

	private static void generate_tas(String label_tas, int taille_tas,PrintStream output){
		output.println("\t.data");
		output.println("\n" + label_tas + " :");
		output.println("\t.word @offset");
		output.println("\t.skip " + taille_tas + " * 4");
		Table_de_symboles.setLabel_Tas(label_tas);
	}

	private static void generate_start(PrintStream output){
		output.println("\t.global _start");
		output.println("_start :");
		output.println("\tLDR r0, =" + Table_de_symboles.getLabel_Tas() );
		output.println("\tMOV r1, #4");
		output.println("\tSTR r1 , [ r0 ]");
	}
	
	private static void generate_labelInts(PrintStream output){
		output.println("\n\n");
		for(Label_Int bigInt : bigInts){
			output.println(bigInt.getId()+" :");
			output.println("\t.word "+bigInt.getVal());
		}
			
	}
	
	/**
	 * Génère le fichier ARM correspondant à l'arbre ASML en entrée
	 * @param asmt arbre asml à compiler
	 * @param output flux sur lequel écrire ( fichier ouvert/stdout)
	 */
	private static void generator ( Exp_asml asmt, PrintStream debug_out,PrintStream arm_file, boolean debug)
	{
		VariableAllocator allocator = new VariableAllocator(Strategie_allocation.BASIC);
		//TODO
		//ImmediateOptimizer optimizer = new ImmediateOptimizer();
		//Backend_visitor BE_visitor = new Backend_visitor(allocator,optimizer);
		Backend_visitor BE_visitor = new Backend_visitor(allocator,debug);
		Exp_asml to_print_asmt = asmt.accept(BE_visitor);
		if(debug){
			debug_out.println("###################");
			debug_out.println("Arbre ASML en sortie");
			
			to_print_asmt.accept(new PrintAsml(debug_out));
			debug_out.println();
			debug_out.println("###################");
			debug_out.println("Code ARM généré");
			allocator.getTable_symboles().afficheTableSymbole();
		}
		bigInts.addAll(Table_de_symboles.getBigInts());
		ARMPrinterVisitor printer;
		if(debug)
		{
			printer = new ARMPrinterVisitor(arm_file, debug_out, allocator.getTable_symboles());
		}
		else 
		{
			printer = new ARMPrinterVisitor(arm_file, allocator.getTable_symboles());
		}
		printer.setup_env();//TODO : setup env, ici ou dans les fonction?
		printer.Start(to_print_asmt);
		
		/*debug_out.println("\nLeftover from output :");
		left_overs.accept(new PrintAsml());
		debug_out.println();*/
	}
	
	/*
	public static void run(List<Exp_asml> list_asmt)
	{
		int size = list_asmt.size();
		PrintStream sys_out = System.out;
		debug = false;
		
		sys_out.println();
		sys_out.println("\t.data");
		sys_out.println();
		for(int i=0;i<size;i++){
			Exp_asml asmt = list_asmt.get(i);
			if(i==size-1){
				generate_start(sys_out);
			}
			try {
				generator(asmt,new PrintStream("/dev/null"),sys_out);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		sys_out.println("--------- ARM Generation done ----------");
	}
	*/
}
