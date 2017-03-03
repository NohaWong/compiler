package Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Type tuple
 * 
 */
public class TTuple extends Type {
	private List<Type> elems_types;
	
	/**
	 * Constructeur
	 * @param elems_types la liste des Type des éléments du tuple
	 */
	public TTuple(List<Type> elems_types){
		this.elems_types = elems_types;
	}
	
	/**
	 * Constructeur de copie
	 * @param tt le tuple à copier
	 */
	public TTuple(TTuple tt){
		this.elems_types = tt.elems_types;
	}
	
	/**
	 * Constructeur par défaut
	 */
	@Deprecated
	public TTuple(){
		this.elems_types = new ArrayList<Type>();
	}
	
	/**
	 * Getter
	 * @return la liste des Type des éléments du tuple
	 */
	public List<Type> getElems(){
		return elems_types;
	}
	
    @Override
    public String toString() {
    	String res = "(";
    	res = res + elems_types.get(0);
    	for(int i=1; i<elems_types.size(); i++)
    		res = res + " * " + elems_types.get(i);
        return res + ")";
    }

    @Override
	public boolean equals(Object o){
    	if(o instanceof TTuple)
			return this.elems_types.equals(((TTuple)o).elems_types);
		else return false;
	}

	@Override
	public Type clone() {
		List<Type> types = new ArrayList<Type>();
		for(Type t : this.elems_types)
			types.add(t.clone());
		return new TTuple(types);
	}

	/**
	 * Détermine si le tuple est totalement déterminé (pas de type Variable dans les éléments)
	 * @return vrai ssi le tuple est totalement déterminé
	 */
	public boolean estCompletementDefini() {
		for(Type t : this.elems_types){
			if(t instanceof TVar)
				return false;
		}
		return true;
	}
}