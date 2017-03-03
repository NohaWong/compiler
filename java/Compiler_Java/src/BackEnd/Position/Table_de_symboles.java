package BackEnd.Position;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import BackEnd.VariableAllocator;
import Code_fourni.Id;

public class Table_de_symboles {
	final Hashtable<String,Position_variable> table_positions;
	final String[] registres_utilises;
	private static ArrayList<Label_Int> bigInts = new ArrayList<Label_Int>();
	private int first_fit;
	private static int numLabelIf =0;
	private static String label_Tas;
	//final ArrrayList<> used_offset;
	
	public static ArrayList<Label_Int> getBigInts(){
		return bigInts;
	}
	
	public static String getLabel_Tas() {
		return label_Tas;
	}

	public static void setLabel_Tas(String label_Tas) {
		Table_de_symboles.label_Tas = label_Tas;
	}

	public Table_de_symboles(){
		table_positions = new Hashtable<String,Position_variable>();
		registres_utilises = new String[VariableAllocator.nb_registre];
		first_fit = (-1)*VariableAllocator.taille_mot;
		registres_utilises[4] = "RESERVE n°1";
		registres_utilises[5] = "RESERVE n°2";
		registres_utilises[11] = "fp";
		registres_utilises[13] = "sp";
		registres_utilises[14] = "lr";
		registres_utilises[15] = "cpsr";
	}

	/**
	 * Ajoute une variable a la position indiquée
	 * @param varID id de la variable à ajouter
	 * @param pos position a laquelle ajouter la variable
	 */
	public void add(String varID, Position_variable pos){
		table_positions.put(varID, pos);
		// Si on ajoute dans un reg, on maintiens la table des reg a jour.
		if(pos instanceof Position_Registre)
			registres_utilises[((Position_Registre)pos).getNumReg()] = varID;
		else if(pos instanceof Position_Pile)
			first_fit -= VariableAllocator.taille_mot;
		else if (pos instanceof Position_tuple)
			System.out.println(varID+" tuple");
		else if (pos instanceof Label_Int)
			bigInts.add((Label_Int) pos);
	}
	
	/**
	 * Retourne la position de la variable dans la table des symboles
	 * @param varID
	 * @return
	 */
	public Position_variable getPosition(String varID){
		return table_positions.get(varID);
	}
	
	/**
	 * Retourne la position de la variable dans la table des symboles
	 * @param varID
	 * @return la position 
	 */
	public Position_variable getPosition(Id varID){
		return table_positions.get(varID.getId());
	}
	
	
	public void addRetour(String varID){
		table_positions.put(varID, new Position_Retour());
	}
	
	public void addClosure(String varID){
		table_positions.put(varID, new Position_Closure());
	}
	
	public void setPos(String varID,Position_variable pos){
		table_positions.put(varID, pos);
	}
	
	/**
	 * Indique si un registre est libre
	 * @return true: il existe un registre libre
	 */
	public boolean regLibre()
	{
		boolean res= false;
		int i=0;
		while(!res && i <16)
		{
			res = res || registres_utilises[i] ==null;
			i++;
		}
		return res;
	}
	
	public boolean isInTable(String varID){
		return table_positions.contains(varID);
	}
	
	public boolean isTuple(String varID){
		return table_positions.containsKey(varID) 
				&& (table_positions.get(varID) instanceof Position_tuple);
	}
	
	public boolean isInReg(String varID)
	{
		return table_positions.containsKey(varID) 
				&& (table_positions.get(varID) instanceof Position_Registre);
	}
	
	/**
	 * Indique si le label de nom varID existe dans la table des symboles
	 * @param varID le nom du label
	 * @return vrai si varID existe
	 */
	public boolean isExistingLabel(String varID){
		if(table_positions.containsKey(varID))
			return (table_positions.get(varID) instanceof Label_Fonction) 
					|| (table_positions.get(varID) instanceof Label_Float);
		return false;
	}
	
	/** 
	 * Indique le premier registre non utilisé
	 * @return numero du premier registre non utilisé
	 */
	public int numRegLibre()
	{
		boolean res= false;
		int i=0;
		while(!res && i <16)
		{
			res = res || registres_utilises[i] ==null;
			i++;
		}
		return i-1;
	}

	/**
	 * 
	 * @return
	 */
	public int numReserve2() {
		return 5;
	}

	public int numReserve1() {
		return 4;
	}
	
	///////////////////////////////////////////////////////////////////
	//						STACK
	
	/**
	 * Indique si varID existe dans la table des symboles et plus spécifiquement dans la pile
	 * @param varID le nom de la variable a tester
	 * @return vrai si varID est dans la pile
	 */
	public boolean isInStack(String varID){
		return table_positions.containsKey(varID) 
				&& table_positions.get(varID) instanceof Position_Pile ;
	}
	

	/**
	 * Retourne la première position non utilisée p/r au Frame pointer
	 * @return le premier offset libre
	 */
	public int getFirst_fit() {
		return first_fit;
	}
	
	/**
	 * Renvoie la liste des registres a mettre à jour: format: { rX, rX2, rX3, rX3 ... } ou null, si aucun registre n'est utilisé
	 */
	public String modifiedReg(){
		/*String retour = "{";
		boolean premier = true;
		for(int i=4; i < VariableAllocator.nb_registre; i++)
		{
			if(registres_utilises[i] != null && i < 13 && i != 11 && i != numReserve2() && i != numReserve1())
			{
				if(!premier)
					retour = retour + ",";
				retour = retour + " r" + i;
				premier = false;
			}
		}
		if(premier)
			retour = retour + " lr } ";
		else
			retour = retour + ", lr } ";*/
		return "{ lr }";
	}
	
	/**
	 * Renvoie les registres qui vont être écrasé par la mise en place des arguments
	 * @param nb_args le nombre d'argument a mettre en place
	 * @return { rX, rX2, rX2 ... } ou null, si aucun registre n'est écrasé
	 */
	public String modifiedRegArgs(int nb_args){
		/*String retour = "{";
		boolean premier = true;
		for(int i=0; i < nb_args && i < 4; i++)
		{
			if(registres_utilises[i] != null)
			{
				if(!premier)
					retour = retour + ",";
				retour = retour + " r" + i;
				premier = false;
			}
		}
		retour = retour + " } ";
		if(premier)
			retour = null;*/
		return "{ r0-r10,r12 }";
	}
	
	/**
	 * Affiche la table de symbole.
	 */
	public void afficheTableSymbole()
	{
		Position_variable pos;
		String varID;
		Enumeration<String> liste_varID = table_positions.keys();
		System.out.println("TABLE DE SYMBOLES :");
		while(liste_varID.hasMoreElements())
		{
			varID  = liste_varID.nextElement();
			pos = table_positions.get(varID);
			System.out.println("-- "+ varID + ": " + pos.to_String());

		}
	}
	
	/**
	 * Donne un numéro de label unique aux if
	 * @return le numéro de label du if actuel
	 */
	public int getNumLabelIf()
	{
		return Table_de_symboles.numLabelIf++;
	}
	
}
