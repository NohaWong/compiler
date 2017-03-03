package Main;

public class Flag_arg {
	protected static final int AST							= (1<<0);
	protected static final int HEIGHT						= (1<<1);
	protected static final int DISPLAY_HELP					= (1<<16);
	protected static final int DISPLAY_VERSION				= (1<<17);
	protected static final int PRINT_COMPILATION_STEPS		= (1<<18);
	protected static final int PARSE_ONLY					= (1<<2) | PRINT_COMPILATION_STEPS;
	
	protected static final int TYPE_CHECK					= (1<<3);
	protected static final int TYPE_CHECK_ONLY				= (1<<4) | TYPE_CHECK | PRINT_COMPILATION_STEPS;
	
	protected static final int K_NORMALIZATION				= (1<<5) | TYPE_CHECK;
	protected static final int ALPHA_CONVERSION				= (1<<6) | TYPE_CHECK | K_NORMALIZATION;
	protected static final int BETA_REDUCTION				= (1<<7) | TYPE_CHECK | K_NORMALIZATION | ALPHA_CONVERSION;
	protected static final int LET_REDUCTION				= (1<<8) | TYPE_CHECK | K_NORMALIZATION | ALPHA_CONVERSION | BETA_REDUCTION;
	protected static final int ALL_REDUCTION = K_NORMALIZATION | ALPHA_CONVERSION | BETA_REDUCTION | LET_REDUCTION ;
	
	protected static final int OPTI_INLINE					= (1<<9) | TYPE_CHECK | ALL_REDUCTION;
	protected static final int OPTI_INLINE_CUSTOM 			= (1<<19)| OPTI_INLINE;
	protected static final int OPTI_CONSTANT_FOLDING		= (1<<10)| TYPE_CHECK | ALL_REDUCTION;
	protected static final int OPTI_UNUSED_DEFINITIONS		= (1<<11)| TYPE_CHECK | ALL_REDUCTION;
	protected static final int ALL_OPTIMISATION = OPTI_INLINE | OPTI_CONSTANT_FOLDING| OPTI_UNUSED_DEFINITIONS;
	protected static final int OPTI_ITER					= (1<<20)| ALL_OPTIMISATION;
	
	protected static final int ASML_TRANSLATE				= (1<<12)| TYPE_CHECK | ALL_REDUCTION;
	protected static final int OUTPUT_ASML					= (1<<13)| ASML_TRANSLATE;
	protected static final int BACK_END						= (1<<14)| ASML_TRANSLATE;
	protected static final int OUTPUT_FILE					= (1<<15)| BACK_END;
	
	protected static final int ALL							= TYPE_CHECK | ALL_REDUCTION | ALL_OPTIMISATION | BACK_END;

	
	
	private boolean isBlocked;
	
	private int MASK;
	/**
	 * Constructeur, Le mask est initialisé avec toutes les étapes de la compilation
	 */
	protected Flag_arg()
	{
		isBlocked = false;
		MASK = TYPE_CHECK | ALL_REDUCTION | ALL_OPTIMISATION | ASML_TRANSLATE | BACK_END ;
	}
	/**
	 * Met à jour le Mask
	 * @param flag un int qui correspond à l'une des constantes de la classe
	 */
	protected void MajMask(int flag)
	{
		if (flag == PARSE_ONLY ||flag == TYPE_CHECK_ONLY)
		{
			MASK = flag;
			isBlocked = true;
		}
		else if(!isBlocked)
		{
			MASK |= flag;
			
		}
	}
	/**
	 * Reset le mask pour ne plus avoir tous les flags entré à l'initialisation
	 */
	protected void ResetMask()
	{
		MASK = MASK^( TYPE_CHECK | ALL_REDUCTION | ALL_OPTIMISATION | ASML_TRANSLATE | BACK_END) ;
	}
	/**
	 * Test le mask avec un flag particulier
	 * @param flag un int qui correspond à l'une des constantes de la classe
	 * @return
	 */
	protected boolean TestMask(int flag)
	{
		return (MASK & flag) == flag;
	}
	
	
}
