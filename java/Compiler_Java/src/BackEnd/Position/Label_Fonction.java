package BackEnd.Position;

import java.util.List;

public class Label_Fonction extends Label_Var{
	private Table_de_symboles table_symbole;
	
	/**
	 * Constructeur: rempli la table des symboles, en mettant les 4 permiers arguments dans les reg 0-3, le reste dans la pile.
	 * @param id nom de la fonction
	 * @param args liste des arguments
	 */
	public Label_Fonction(String id, List<String> args) {
		super(id);
		this.table_symbole = new Table_de_symboles();
		
		int i=0;
		int size = args.size();
		while(i<size && i<4)
		{
			table_symbole.add(args.get(i), new Position_Registre(i));
			i++;
		}
		while(i < size)
		{
			table_symbole.add(args.get(i), new Position_Pile((size - i)*4));
			i++;
		}
	}
	
	public Table_de_symboles getTable_symbole() {
		return table_symbole;
	}

	@Override
	public String to_String() {
		return "Label_Fonction ";
	}
	
}
