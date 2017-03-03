package BackEnd;

import Expression.Exp;

public class ImmediateOptimizer {
	private static int max_direct_int = 256;
	private static int max_half_rotation = 16;
	private int[] tableau;
	// which immediate constants are possible?:
	/* 12 bits sont reservés: 8 pour la constante elle même, plus 4 pour une rotations
	 * Ox ---- --FF -> val immediate
	 * 0x ---- -F-- -> rotation (elle est doublé: -> 2^4 * 2 = 32. On peux multiplier jusqu'à 2^32
	 * 
	 * En plus de celà, on peux aussi utiliser MVN (Move_Not),
	 * pour obtenir certaines valeurs non obtessible autrement. (seulement sur certain assembleurs)
	 * 
	 * 
	 * Il restera malgré tout des valeurs qui ne seront pas obtenssible, 
	 * -> il faut créer un label et charger la constante.
	 * */
	public ImmediateOptimizer (){
		tableau = new int[32];
	}
	
	/**
	 * Renvoie vrai s'il est possible d'utiliser cette constante directement
	 * @param i valeur à tester
	 * @return vrai: la constante est utilisable immediatement. faux: on ne peux l'utiliser immediatement.
	 */
	public boolean immediateIsPossible ( int var)
	{
		remplirTableau(var);
		return decalageIsPossible();
	}
	
	/**
	 * Renvoie si selon le tableau remplie, l'entier binaire est faisable immédiatement ie: on a une suite de 24 '0' ou de 24 '1'
	 * @return true, si faisaible en var immediate, false sinon
	 */
	private boolean decalageIsPossible() {
		int plus_long_seq_zero = 0;
		int plus_long_seq_un = 0;
		int zero_seq_courante = 0;
		int un_seq_courante = 0;
		boolean turn_done = false;
		int i=0;
		while(!turn_done || zero_seq_courante > 0 || un_seq_courante >0)
		{
			if(!turn_done)
			{//premier tour, tout va bien
				if(tableau[i] == 0)
				{
					if(zero_seq_courante == 0 && (i & 1) == 1);
						//do nothing, don't begin sequences on odd numbers
					else
					{
						zero_seq_courante++;
						if(zero_seq_courante > plus_long_seq_zero)
							plus_long_seq_zero = zero_seq_courante;
					}
					un_seq_courante = 0;
				}
				else
				{
					if( un_seq_courante == 0 && (i & 1) == 1);
						//do nothing, don't begin sequence on odd numbers
					else
					{
						un_seq_courante++;
						if(un_seq_courante > plus_long_seq_un)
							plus_long_seq_un = un_seq_courante;
					}
					zero_seq_courante = 0;
				}
			}
			else
			{// second tour, on ne rentre que si on est dans la continuité d'une sequence commencée avant la fin du tour
				if(tableau[i] == 0 )
				{
					if(zero_seq_courante > 0)
					{
						zero_seq_courante++;
						if(zero_seq_courante > plus_long_seq_zero)
							plus_long_seq_zero = zero_seq_courante;
					}
					un_seq_courante = 0;
				}
				else 
				{
					if (un_seq_courante > 0)
					{
						un_seq_courante++;
						if(un_seq_courante > plus_long_seq_un)
							plus_long_seq_un = un_seq_courante;
					}
					zero_seq_courante = 0;
				}
			}
			i++;
			if(i == 32)
			{
				i =0;
				turn_done = true;
			}
		}
		//affichage(plus_long_seq_un, plus_long_seq_zero);
		return plus_long_seq_un >= 24 || plus_long_seq_zero >= 24;
	}

	private void affichage(int un, int zero) {
		
		System.out.println("Plus longues seq:");
		System.out.println("\tde zero:" + zero);
		System.out.println("\tde un :" + un);
		
		System.out.print("{");
		for(int i=0; i<32; i++)
			System.out.print(tableau[i] + ", ");
		System.out.print("}");
		
		
	}

	/**
	 * Remplie le tableau binaire correspondant a un entier
	 * @param var l'entier dont on veut le binaire
	 */
	private void remplirTableau(int var)
	{
		if(var < 0)
			tableau[31] = 1;// C'est un entier négatif
		else
			tableau[31] = 0;// C'est un entier positif
		for(int i=0; i<31; i++)
		{
			tableau[i] = (var & 1);
			var = var/2;
		}
	}
}
