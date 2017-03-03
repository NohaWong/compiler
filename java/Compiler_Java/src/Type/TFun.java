package Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Type fonction
 * 
 */
public class TFun extends Type {
	private List<Type> types_args;
	private Type retour;
	
	/**
	 * Constructeur
	 * @param types_args la liste des Type des arguments de la fonction
	 * @param retour le Type de retour de la fonction
	 */
	public TFun(List<Type> types_args, Type retour){
		this.types_args = new ArrayList<Type>(types_args);
		if(types_args.isEmpty()){
			this.types_args.add(Type.gen());
		}
		this.retour = retour;
	}
	
	/**
	 * Constructeur de copie
	 * @param tf la TFun à copier
	 */
	public TFun(TFun tf){
		this.types_args = new ArrayList<Type>();
		for(Type t : tf.types_args)
			this.types_args.add(t.clone());
		this.retour = tf.retour.clone();
	}
	
	/**
	 * Constructeur par défaut
	 */
	@Deprecated
	public TFun(){
		this.types_args = new ArrayList<Type>();
		this.types_args.add(Type.gen());
		this.retour = Type.gen();
	}
	
	/**
	 * Constructeur
	 * @param nb_args le nombre d'arguments de la fonction
	 */
	private TFun(int nb_args){
		this.types_args = new ArrayList<Type>();
		for(int i=0; i<nb_args; i++)
			this.types_args.add(Type.gen());
		this.retour = Type.gen();
	}
	
	/**
	 * Génère une fonction selon son nombre d'arguments
	 * @param nb_args le nombre d'arguments de la fonction
	 * @return la TFun générée
	 */
	public static TFun gen(int nb_args){
		return new TFun(nb_args);
	}
	
	/**
	 * Getter
	 * @return le Type de retour de la fonction
	 */
	public Type getRetour(){
		return retour;
	}
	
	/**
	 * Getter
	 * @return la liste de Type des arguments
	 */
	public List<Type> getArgsTypes(){
		return types_args;
	}
	
    @Override
    public String toString() {
		String res = "(fun : ";
		int size = types_args.size();
		for(int i = 0; i<size; i++){
			res = res + types_args.get(i) + " -> ";
		}
		res = res + retour + ")";
        return res;
    }

	@Override
	public Type clone() {
		return new TFun(this);
	}
	
	/**
	 * Détermine si la fonction est totalement déterminée (pas de type Variable dans les arguments / le retour)
	 * @return vrai ssi la fonction est totalement déterminée
	 */
	public boolean estCompletementDefinie(){
		for(Type t : this.types_args){
			if(t instanceof TVar)
				return false;
		}
		return !(retour instanceof TVar);
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof TFun){
			return (this.types_args.equals(((TFun)o).types_args)) && (this.retour == ((TFun)o).retour);
		}
		else return false;
	}
}