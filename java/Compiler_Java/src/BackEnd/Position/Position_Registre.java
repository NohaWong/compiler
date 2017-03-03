package BackEnd.Position;

public class Position_Registre extends Position_variable{
	private int numReg;

	public int getNumReg() {
		return numReg;
	}

	public void setNumReg(int numReg) {
		this.numReg = numReg;
	}

	public Position_Registre(int numReg) {
		super();
		this.numReg = numReg;
	}

	@Override
	public String to_String() {
		return ("r" + numReg);
	}
	
}
