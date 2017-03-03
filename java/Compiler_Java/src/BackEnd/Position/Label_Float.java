package BackEnd.Position;

public class Label_Float extends Label_Var{
	private float value;

	public Label_Float(String id) {
		super(id);
	}
	public Label_Float(String id, float value) {
		super(id);
		this.value=value;
	}

	public float getValue() {
		return value;
	}

	@Override
	public String to_String() {
		return "Label_Float ";
	}
}
