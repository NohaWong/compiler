package Normalization_Reduction;

import Code_fourni.Id;
/**
 * Tuple de 3 elements , utile pour la Beta-Reduction
 * 	 id1 : la var qui remplace
 *	 id2 : la var qui doit etre remplacé
 *	 keepAffectation : le boolean est faux si l'affection de id2 = id1 doit etre supprimé dans le code sinon vrai
 * @author jonathan
 *
 */
public class TriElement {
	private Id id1;
	private Id id2;
	private boolean keepAffectation;
	/**
	 * Constructeur
	 * @param e1 Une expression
	 * @param e2 Une Expression
	 * @param keepAffectation un boolean
	 */
	public TriElement(Id id1, Id id2, boolean keepAffectation)
	{
		this.id1 = id1;
		this.id2 = id2;
		this.keepAffectation = keepAffectation;
	}
	/**
	 * Affiche un TriElement
	 */
	public void afficher()
	{
		System.out.println("( "+id1.toString() +" ; "+ id2.toString() +" ; "+ keepAffectation+" )");
	}
	/**
	 * Récupere l'id 1,  la var qui remplace
	 * @return un Id
	 */
	public Id getId1() {
		return id1;
	}
	/**
	 * Met à jour l'id 1,  la var qui remplace
	 * @param id1 un Id
	 */
	public void setId1(Id id1) {
		this.id1 = id1;
	}
	/**
	 * Récupere l'id 2, la var qui doit etre remplacé
	 * @return un Id
	 */
	public Id getId2() {
		return id2;
	}
	
	/**
	 * Met à jour l'id 2, la var qui doit etre remplacé
	 * @param id1 un Id
	 */
	public void setE2(Id id2) {
		this.id2 = id2;
	}
	/**
	 * Récupere si l'affectation doit être gardé
	 * @return un boolean
	 */
	public boolean keepAffectation() {
		return keepAffectation;
	}
	/**
	 * Met à jour si l'affectation doit être gardé
	 * @param b un boolean
	 */
	public void setkeepAffectation(boolean b) {
		this.keepAffectation = b;
	}
	
	
	
}
