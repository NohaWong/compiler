package BackEnd;

import java.util.List;

import BackEnd.Position.*;
import Code_fourni.Id;

public class VariableAllocator {
	public final static int nb_registre = 16;
	public final static int taille_mot = 4;
	private final static int max_reg_param = 3;
	private final static int max_reg_usable = 12;
	
	private Table_de_symboles table_symboles;
	enum Strategie_allocation {BASIC,STACK};
	Strategie_allocation strategie;
	
	public VariableAllocator()
	{
		table_symboles = new Table_de_symboles();
		this.strategie = Strategie_allocation.BASIC;
	}
	
	public VariableAllocator(Table_de_symboles table_de_symbole)
	{
		table_symboles = table_de_symbole;
		this.strategie = Strategie_allocation.BASIC;
	}
	
	public VariableAllocator(Strategie_allocation strat)
	{
		table_symboles = new Table_de_symboles();
		this.strategie = strat;
	}
	
	public void assignerLabelFonction(String id,List<String>args){
		if(!table_symboles.isExistingLabel(id))
			table_symboles.add(id,new Label_Fonction(id, args));
	}
	
	public void assignerLabelFloat(String id, Float f){
		if(!table_symboles.isExistingLabel(id))
			table_symboles.add(id,new Label_Float(id,f));
	}
	

	public void assignerLabelInt(String id, int val){
		if(!table_symboles.isExistingLabel(id))
			table_symboles.add(id,new Label_Int(id,val));
	}
	

	public void assignerPosition(String varID,Position_variable pos){
		if( table_symboles.isInTable(varID) )
			table_symboles.setPos(varID, pos);
		table_symboles.add(varID, pos);
	}
	
	
	/**
	 * Rajoute varID dans la table des symboles en position pile avec l'offset first fit (empile varID)
	 * @param varID nom de la variable
	 * @return l'offset de varID dans la pile par rapport au fp
	 */
	public int allouerPile(String varID){
		if(table_symboles.isInReg(varID) || table_symboles.isTuple(varID))
			return -1;
		if(!table_symboles.isInStack(varID)){
			int offset = table_symboles.getFirst_fit();
			table_symboles.add(varID, new Position_Pile(offset));
			return offset;
		}
		return ((Position_Pile)table_symboles.getPosition(varID)).getOffset();
	}
	
	public void allouerTuple(String varID,List<String> tuple_vars){
		Position_tuple tuple = new Position_tuple(tuple_vars);
		for(String id : tuple_vars)
			tuple.add(table_symboles.getPosition(id));
		
		table_symboles.add(varID,tuple);
	}
	
	/**
	 * Renvoie le registre où est allouer la variable, ou alloue un registre. Alloue selon la stratégie définie au début.
	 * @param varID nom de la variable
	 * @return le numéro du registre, -1 si il n'a pas pu allouer.
	 */
	public int allouerVariable(String varID){
		switch (strategie)
		{
		case BASIC:
			return allouerRegistre(varID);
		case STACK:
			return allouerPile(varID);
		default:
			return -1;
		}
	}
	
	/**
	 * Renvoie le registre où est allouer la variable, ou alloue un registre. Alloue selon la stratégie définie au début.
	 * @param varID ID de la variable
	 * @return le numéro du registre, -1 si il n'a pas pu allouer.
	 */
	public int allouerRegistre(Id varID){
		return allouerRegistre(varID.getId());
	}
	
	/**
	 * Renvoie le registre où est allouer la variable, ou alloue le premier registre disponible.
	 * @param varID ID de la variable
	 * @return le numéro du registre, -1 si il n'a pas pu allouer.
	 */
	private int allouerRegistre( String varID){
		if(!table_symboles.regLibre() && !table_symboles.isInReg(varID))// si pas de place && pas en registre
		{
			//System.err.println("[BASIC ALLOCATOR] PLUS DE REGISTRES");
			strategie = Strategie_allocation.STACK;
			return allouerVariable(varID);
		}
		
		int  regDest = table_symboles.numRegLibre();
		// Il n'y a plus de registre utilisable a allouer.
		if(regDest > max_reg_usable)
			return -1;
			
		
		//Si elle n'est pas dans un registre, on met la variable dans un registre.
		//UPDATE : vu qu'une variable n'est d�finie qu'une seule fois selon la convention,
		//	on teste si elle n'existe pas d�j� sous une autre position avant de l'allouer
		if(table_symboles.isInStack(varID) || table_symboles.isTuple(varID)){
			return -1;
		}
		if(!table_symboles.isInReg(varID)){
			table_symboles.add(varID, new Position_Registre(regDest));
			return regDest;
		}
		return ((Position_Registre)table_symboles.getPosition(varID)).getNumReg();
	}

	/**
	 * Renvoie la position de la variable identifiée par S
	 * @param s l'ID (en string) de la variable dont on veux la position
	 * @return la position de la variable
	 */
	public Position_variable getPosition(String s)
	{
		return table_symboles.getPosition(s);
	}
	
	/**
	 * Renvoie la position de la variable identifiée par l'Id id
	 * @param id l'ID de la variable
	 * @return la position de la variable
	 */
	public Position_variable getPosition(Id id)
	{
		return table_symboles.getPosition(id.getId());
	}
	

	/**
	 * Preferrable not to use, use given function from allocator instead.
	 * @return la table de positions
	 */
	public Table_de_symboles getTable_symboles() {
		return table_symboles;
	}
}
