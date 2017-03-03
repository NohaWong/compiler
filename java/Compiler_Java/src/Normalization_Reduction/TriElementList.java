package Normalization_Reduction;

import java.util.ArrayList;
import java.util.List;

import Code_fourni.Id;
/**
 * Une liste de tuple utile pour la beta réduction 
 * @author jonathan
 *
 */
public class TriElementList {
	private List<TriElement> l;
	/**
	 * Constructeur
	 */
	public TriElementList()
	{
		l = new ArrayList<TriElement>();
	}
	/**
	 * Ajout un tri element 
	 * @param id1 l'id de la var qui remplace
	 * @param id2 l'id de la var qui doit etre remplacé
	 * @param b le boolean est faux si l'affection de id2 = id1 doit etre supprimé dans le code sinon vrai
	 */
	public void add(Id id1, Id id2, boolean b)
	{
		TriElement te = new TriElement(id1,id2,b);
		l.add(te);
	}
	
	/**
	 * Cherche l'id dans le 1er element de chaque tuple de la liste et renvoi son index, si il existe pas , renvoi -1
	 * @param id une Id
	 * @return un index de type entier
	 */
	public int Index_id_in_id1(Id id)
	{
		int index = 0;
		
		
		index = 0;
		//System.out.println("------TriElementList------");
		//this.afficher();
		while(index<l.size() && !(id.toString().equals(l.get(index).getId1().getId().toString()) ) )
		{
			//System.out.println("ID : " + id.getId().toString()+ " ID_TAB :" + l.get(index).getId1().toString());
			index++;
		}
		if(index == l.size() )
		{
			index = -1;
		}
		return index;	
	}
	
	
	/**
	 * Cherche l'id dans le 2eme element de chaque tuple de la liste et renvoi son index, si il existe pas renvoi -1
	 * @param id une Id
	 * @return un index de type entier
	 */
	public int Index_id_in_id2(Id id)
	{
		int index = 0;
		//System.out.println("------TriElementList------");
		//this.afficher();
		while(index<l.size() && !(id.toString().equals(l.get(index).getId2().getId().toString()) )) 
		{
			//System.out.println("ID : " + id.getId().toString()+ " ID_TAB :" + l.get(index).getId1().toString());
			index++;
		}
		if(index == l.size() )
		{
			index = -1;
		}
		return index;	
	}
	/**
	 * Récupere l'id de la var qui remplace par rapport à l'id de la var qui doit être remplacé fourni en parametre 
	 * si l'id fourni n'est pas dans la liste, alors cette id est renvoyé.
	 * @param id un id de var qui doit etre remplace
	 * @return un id de var qui remplace
	 */
	public Id id_to_replace(Id id)
	{
		Id  retour_id;

		int index = 0;

		while(index<l.size() && !(id.toString().equals(l.get(index).getId2().getId().toString()) ))
		{
			index++;
		}
		if(index < l.size() && !l.get(index).keepAffectation())
		{
			retour_id = l.get(index).getId1();
		}
		else
		{
			retour_id = id;
		}
		
		
		
		return retour_id;
	}
	/**
	 * Supprime le TriElement e de la liste
	 * @param e un TriElement ;
	 */
	public void removeElt (TriElement e){
		l.remove(e);
	}
	/**
	 * Met à jour le boolean KeepAffectation à l'index index
	 * @param index un entier naturel inferieur à la taille de la liste
	 * @param b un boolean
	 */
	public void setKeepAffectation (int index, boolean b)
	{
		l.get(index).setkeepAffectation(b);
	}
	/**
	 * Récupere le boolean KeepAffectation à l'index index
	 * @param index un entier naturel inferieur à la taille de la liste
	 * @return un boolean
	 */
	public boolean getKeepAffection(int index)
	{
		return l.get(index).keepAffectation();
	}
	/**
	 * Recupere la liste
	 * @return une liste composé de TriElement
	 */
	public List<TriElement> getL() {
		return l;
	}
	/**
	 * Affiche la liste 
	 */
	public void afficher(){
		//System.out.println("Affichage TriElementList");
		for(int i = 0; i < l.size();i++)
		{
			l.get(i).afficher();
		}
	}
	
}
