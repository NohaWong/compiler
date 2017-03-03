package BackEnd.Position;

public class Position_Pile extends Position_variable{
	private int offset;

	public Position_Pile(int offset) {
		super();
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public String to_String() {
		return ("Stack: [ fp " + offset + " ] " );
	}
	
}
