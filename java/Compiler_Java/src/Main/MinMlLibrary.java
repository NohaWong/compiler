package Main;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import Type.TFloat;
import Type.*;

/**
 * Classe de définition des symboles de la bibliothèque minml
 * @author bizarda
 *
 */
public abstract class MinMlLibrary {
	private static Map<String,Type> predefined_map;
	private static List<String> predefined_symbols;
	
	/**
	 * Initialise la bibliothèque
	 */
	public static void init(){
		predefined_map = new Hashtable<String,Type>();
		predefined_symbols = new ArrayList<String>();
		ArrayList<Type> args = new ArrayList<Type>();
		args.add(new TInt());
		predefined_map.put("print_int",new TFun(args,new TUnit()));
		predefined_map.put("float_of_int",new TFun(args,new TFloat()));

		args = new ArrayList<Type>();
		args.add(new TUnit());
		predefined_map.put("print_newline",new TFun(args,new TUnit()));

		args = new ArrayList<Type>();
		args.add(new TFloat());
		predefined_map.put("sin",new TFun(args,new TFloat()));
		predefined_map.put("cos",new TFun(args,new TFloat()));
		predefined_map.put("sqrt",new TFun(args,new TFloat()));
		predefined_map.put("abs_float",new TFun(args,new TFloat()));
		predefined_map.put("int_of_float",new TFun(args,new TInt()));
		predefined_map.put("truncate",new TFun(args,new TInt()));

		predefined_symbols.add("print_int");
		predefined_symbols.add("float_of_int");
		predefined_symbols.add("print_newline");
		predefined_symbols.add("sin");
		predefined_symbols.add("cos");
		predefined_symbols.add("sqrt");
		predefined_symbols.add("abs_float");
		predefined_symbols.add("int_of_float");
		predefined_symbols.add("truncate");
	}
	
	/**
	 * Getter
	 * @return la liste des symboles prédéfinis
	 */
	public static List<String> getPredefinedSymbols(){
		return new ArrayList<String>(predefined_symbols);
	}
	
	/**
	 * Getter
	 * @return la liste des symboles définis et leurs types
	 */
	public static Map<String,Type> getPredefinedMap(){
		return new Hashtable<String,Type>(predefined_map);
	}
}