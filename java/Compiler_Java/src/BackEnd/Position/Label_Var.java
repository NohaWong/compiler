package BackEnd.Position;

public class Label_Var extends Position_variable{
	private String id;
	
	public Label_Var(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	@Override
	public String to_String() {
		return "Label_Var : "+id;
	}

}
