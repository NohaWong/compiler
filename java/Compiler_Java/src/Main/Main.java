package Main;

import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;

import Asml.PrintAsml;
import Asml.Traductor;
import BackEnd.BackEnd_Runner;
import Code_fourni.Height;
import Code_fourni.HeightVisitor;
import Code_fourni.ObjVisitor;
import Code_fourni.PrintVisitor;
import Expression.Exp;
import Expression_asml.Exp_asml;
import K_Normal_Expression.KExp;
import K_Normal_Expression.KPrintVisitor;
import Normalization_Reduction.AlphaConverser;
import Normalization_Reduction.Beta_Reduction;
import Normalization_Reduction.K_Normalization;
import Normalization_Reduction.LetReducter;
import Optimization.ConstantFolder;
import Optimization.InlineExpander;
import Optimization.UnusedDefSuppresser;
import Type.Type;
import TypeCheck.KTypeChecker;
import TypeCheck.TypeChecker;

public class Main {
	private String Input_File;
	private String Output_File = "default.s";
	private String Output_File_asml;
	private final static String Version = "0.1";
	private int inline_threshold;
	private int iter_opti = 1;
	/**
	 * Constructeur de Main
	 * @param argv des arguments
	 */
	public Main(String argv[]) {
		
		//InlineOptions = new int[2];
		Flag_arg flag = new Flag_arg();
		ArrayList<Exp_asml> asml=null;
		gestion_argv(argv,flag);
		
		try {
			Parser p = new Parser(new Lexer(new FileReader(Input_File)));
			Exp expression = (Exp) p.parse().value;
			assert (expression != null);
			Hashtable<String,Type> symbol_table;
			MinMlLibrary.init();
			
			KExp K_expression = null;
			
			if (flag.TestMask(Flag_arg.AST)) {
				System.out.println("------ AST ------");
				expression.accept(new PrintVisitor());
				System.out.println();
			}

			if (flag.TestMask(Flag_arg.HEIGHT)) {
				System.out.println("----- Height of the AST -----");
				int height = Height.computeHeight(expression);
				System.out.println("using Height.computeHeight: " + height);

				ObjVisitor<Integer> v = new HeightVisitor();
				height = expression.accept(v);
				System.out.println("using HeightVisitor: " + height);
			}

			if (flag.TestMask(Flag_arg.TYPE_CHECK)) {
				TypeChecker tc = new TypeChecker(flag.TestMask(Flag_arg.PRINT_COMPILATION_STEPS));
				boolean ok = true;
				ok = tc.Start(expression);
				symbol_table = new Hashtable<String,Type>(tc.getSymboles());
				if(flag.TestMask(Flag_arg.PRINT_COMPILATION_STEPS))
				{
					System.out.println("--------- Type-Checker -------");
					System.out.println("Well-typed : " + (ok ? "yes" : "no"));
					System.out.println();
				}
				
			}

			if (flag.TestMask(Flag_arg.K_NORMALIZATION)) {
				
				K_Normalization Kn = new K_Normalization();
				K_expression = Kn.Start(expression);
				afficher_expression(K_expression,"------ K-Normalization ------",flag);
			}
			
			if(flag.TestMask(Flag_arg.ALPHA_CONVERSION))
			{
				AlphaConverser Ac = new AlphaConverser();
				K_expression = Ac.Start(K_expression);
				afficher_expression(K_expression,"------ Alpha-Conversion ------",flag);
			}

			if (flag.TestMask(Flag_arg.BETA_REDUCTION)) {
				Beta_Reduction Br = new Beta_Reduction();
				do {
					K_expression = Br.Start(K_expression);
				} while (Br.isChanged());
				afficher_expression(K_expression,"------ Beta-Reduction ------",flag);

			}
			if(flag.TestMask(Flag_arg.LET_REDUCTION))
			{
				LetReducter Lr = new LetReducter();
				K_expression = Lr.Start(K_expression);
				afficher_expression(K_expression,"------ Let-Reduction ------",flag);
			}
			for(int i=0;i<iter_opti; i++){
				if(flag.TestMask(Flag_arg.OPTI_INLINE))
				{
					
					InlineExpander ie;
					if(flag.TestMask(Flag_arg.OPTI_INLINE_CUSTOM))
						ie = new InlineExpander(inline_threshold);
					else
						ie = new InlineExpander();
					K_expression = new LetReducter().Start(ie.Start(K_expression));
					afficher_expression(K_expression,"------- Inline Expansion Optimization -------",flag);
				}
	
				if(flag.TestMask(Flag_arg.OPTI_CONSTANT_FOLDING))
				{
					ConstantFolder cf = new ConstantFolder();
					K_expression = new LetReducter().Start(cf.Start(K_expression));
					afficher_expression(K_expression,"------- Constant Folding Optimization -------",flag);
				}
				if(flag.TestMask(Flag_arg.OPTI_UNUSED_DEFINITIONS))
				{
					UnusedDefSuppresser udf = new UnusedDefSuppresser();
					K_expression = udf.Start(K_expression);
					afficher_expression(K_expression,"------- Unused Definition Suppression Optimization -------",flag);
				}
			}
			

			if (flag.TestMask(Flag_arg.ASML_TRANSLATE)) {
				KTypeChecker tc = new KTypeChecker(flag.TestMask(Flag_arg.PRINT_COMPILATION_STEPS));
				tc.Start(K_expression);
				symbol_table = new Hashtable<String,Type>(tc.getSymboles());
				Traductor trad = new Traductor(symbol_table);
				asml = new ArrayList<Exp_asml>(trad.Start(K_expression));
				if(flag.TestMask(Flag_arg.OUTPUT_ASML))
				{
					PrintStream ps = new PrintStream(Output_File_asml);
					new PrintAsml(ps).Start(asml);
				}
				if(flag.TestMask(Flag_arg.PRINT_COMPILATION_STEPS))
				{
					System.out.println("--------- ASML ----------");
					new PrintAsml(System.out).Start(asml);
				}
			}

			if (flag.TestMask(Flag_arg.BACK_END)) {
				
				BackEnd_Runner.run(asml,new PrintStream(Output_File), flag.TestMask(Flag_arg.PRINT_COMPILATION_STEPS));
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Fonction Main , point de depart
	 * @param argv Des arguments
	 */
	public static  void main (String[] argv)
	{
		new Main(argv);
	}
	
	public void afficher_aide()
	{
		System.out.println("usage : ./minmlc [options] <file.ml>");
		System.out.println("Available options :");
		System.out.println("-o <file>   	:	set the output file - \"default.s\" by default");
		System.out.println("-h          	:	display this help");
		System.out.println("-v          	:	display version");
		System.out.println("-t          	:	do type check ONLY");
		System.out.println("-p          	:	parse ONLY");
		System.out.println("-asml <file>	:	set an output file for the generated asml code");
		System.out.println("-ps         	:	print all the compilation and AST steps");
		System.out.println("\n ## Reductions ##");
		System.out.println("-allR       	:	do all reductions (K-Normalization, Alpha-Conversion, Beta-Reduction, Let-Reduction");
		System.out.println("-kn         	:	do K-Normalization");
		System.out.println("-ac         	:	do Alpha-Conversion");
		System.out.println("-br         	:	do Beta-Reduction");
		System.out.println("-lr         	:	do Let-Reduction");
		System.out.println("\n ## Optimizations ##");
		System.out.println("-allO       	:	do all optimizations (Inline Expansion, Constant Folding, Unused Definition Suppression");
		System.out.println("-io         	:	do Inline Expansion Optimization");
		System.out.println("-io_c <v>   	:	do Inline Expansion Optimization with a function size threshold of v");
		System.out.println("-cfo        	:	do Constant Folding Optimization");
		System.out.println("-udo        	:	do Unused Definition Suppression Optimization");
		System.out.println("-iter <v>   	:	do all optimizations v times");
		System.out.println("\n ## BackEnd ##");
		System.out.println("-as         	:	do asml translate");
		System.out.println("-be         	:	do Back-End");
	}
	
	
	public void gestion_argv(String[] argv, Flag_arg flag)
	{
		int i = 0;
		String error_no_ml_file = "Please specify an entry .ml file. (-h for help)";
		
		if(argv.length == 0)
		{
			System.err.println(error_no_ml_file);
			System.exit(1);
		}
		
		// Le dernier argument est forcement le fichier à compiler
		boolean reset_Mask = true;
		switch(argv[0])
		{
			case "-h":
				afficher_aide();
				System.exit(0);
				break;
			case "-v":
				System.out.println("Version : "+ Version);
				i++;
				if(argv.length == 1 )
					System.exit(0);
				break;
			default:
				break;
		}
		
		while(i<argv.length-1)
		{
			switch(argv[i])
			{
			case "-o":
				i++;
				if(argv[i].startsWith("-"))
				{
					System.err.println("Error : please specify an output file for generated ARM code.");
					System.exit(1);
				}
				Output_File = argv[i];
				flag.MajMask(Flag_arg.OUTPUT_FILE);
				break;
			case "-t":
				flag.MajMask(Flag_arg.TYPE_CHECK_ONLY);
				break;
			case "-v":
				System.out.println("Version : "+ Version);
				break;
			case "-p":
				flag.MajMask(Flag_arg.PARSE_ONLY);
				break;
			case "-asml":
				i++;
				if(argv[i].startsWith("-"))
				{
					System.err.println("Error : please specify an output file for asml generated code.");
					System.exit(1);
				}
				Output_File_asml = argv[i];
				flag.MajMask(Flag_arg.OUTPUT_ASML);
				break;
			case "-ps":
				flag.MajMask(Flag_arg.PRINT_COMPILATION_STEPS);
				break;
			case "-allR":
				reset_Mask = reset_Mask(reset_Mask,flag);
				flag.MajMask(Flag_arg.ALL_REDUCTION);
				break;
			case "-allO":
				reset_Mask = reset_Mask(reset_Mask,flag);
				flag.MajMask(Flag_arg.ALL_OPTIMISATION);
				break;
			case "-kn":
				reset_Mask = reset_Mask(reset_Mask,flag);
				flag.MajMask(Flag_arg.K_NORMALIZATION);
				break;
			case "-ac":
				reset_Mask = reset_Mask(reset_Mask,flag);
				flag.MajMask(Flag_arg.ALPHA_CONVERSION);
				break;
			case "-br":
				reset_Mask = reset_Mask(reset_Mask,flag);
				flag.MajMask(Flag_arg.BETA_REDUCTION);
				break;
			case "-lr":
				reset_Mask = reset_Mask(reset_Mask,flag);
				flag.MajMask(Flag_arg.LET_REDUCTION);
				break;
			case "-io":
				reset_Mask = reset_Mask(reset_Mask,flag);
				flag.MajMask(Flag_arg.OPTI_INLINE);
				break;
			case "io_O":
				reset_Mask = reset_Mask(reset_Mask,flag);
				flag.MajMask(Flag_arg.OPTI_INLINE_CUSTOM);
				i++;
				if(argv[i].startsWith("-"))
				{
					System.err.println("Error : please specify the size threshold for Inline Expander.");
					System.exit(1);
				}
				inline_threshold = Integer.parseInt(argv[i]);
			case "-iter":
				reset_Mask = reset_Mask(reset_Mask,flag);
				flag.MajMask(Flag_arg.OPTI_ITER);
				i++;
				if(argv[i].startsWith("-"))
				{
					System.err.println("Error : please specify the number of iterations on optimisations.");
					System.exit(1);
				}
				iter_opti = Integer.parseInt(argv[i]);
			case "-cfo":
				reset_Mask = reset_Mask(reset_Mask,flag);
				flag.MajMask(Flag_arg.OPTI_CONSTANT_FOLDING);
				break;
			case "-udo":
				reset_Mask = reset_Mask(reset_Mask,flag);
				flag.MajMask(Flag_arg.OPTI_UNUSED_DEFINITIONS);
				break;
			case "-as":
				reset_Mask = reset_Mask(reset_Mask,flag);
				flag.MajMask(Flag_arg.ASML_TRANSLATE);
				break;
			case "-be":
				reset_Mask = reset_Mask(reset_Mask,flag);
				flag.MajMask(Flag_arg.BACK_END);
				
				break;
				
			default:
				System.err.println("Error : unknown option " + argv[i] + ". Please try -h for a list of options.");
				System.exit(1);
				break;
				
			}
			i++;
			
		}
		
		if(i != argv.length -1)
		{
			System.err.println(error_no_ml_file);
			System.exit(1);
		}
		Input_File = argv[i];
	}
		
	
	/**
	 * Si reset est vrai , reset le mask et passe reset à faux
	 * @param reset un boolean 
	 * @param flag un Flag_arg
	 * @return reset qui est faux
	 */
	private boolean reset_Mask(boolean reset,Flag_arg flag)
	{
		if(reset)
		{
			flag.ResetMask();
			reset = false;
		}
		return reset;
	}
	
	/**
	 * Affiche l'expression avec un titre - si les flags le permettent
	 * @param e l'expression à afficher
	 * @param Titre le titre à affciher
	 * @param flag les flags de compilation
	 */
	public void afficher_expression(KExp e, String Titre,Flag_arg flag)
	{
		if(flag.TestMask(Flag_arg.PRINT_COMPILATION_STEPS))
		{	
			System.out.println(Titre);
			e.accept(new KPrintVisitor());
			System.out.println();
		}
	}
	/**
	 * Affiche que la fonction n'est pas encore implementé
	 * @param Title le titre de la fonction
	 * @param flag les flags de compilation
	 */
	public void print_not_implemented(String Title,Flag_arg flag)
	{
		if(flag.TestMask(Flag_arg.PRINT_COMPILATION_STEPS))
		{	
			System.out.println(Title);
			System.out.println("Not implemented yet");
			System.out.println();
		}

	}
	
}
