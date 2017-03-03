package BackEnd.Position;

public class Label_Int extends Label_Var {
	private int val;
	
	public Label_Int(String id,int val) {
		super(id);
		this.val = val;
	}

	public int getVal(){
		return val;
	}
	
}
