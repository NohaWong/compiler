package BackEnd.Position;

import java.util.ArrayList;
import java.util.List;


public class Position_tuple extends Position_variable {
	private ArrayList<String> elements;
	private ArrayList<Position_variable> positions;
	
	public Position_tuple(){
		this.elements = new ArrayList<String>();
		this.positions = new ArrayList<Position_variable>();
	}

	public Position_tuple(List<String> elements){
		this.elements = (ArrayList<String>) elements;
		this.positions = new ArrayList<Position_variable>();
	}
	
	public void add(String id){
		elements.add(id);
	}
	
	public void add(Position_variable pos){
		positions.add(pos);
	}
	
	public int getNb() {
		return elements.size();
	}
	
	public ArrayList<String> getElements() {
		return elements;
	}

	@Override
	public String to_String() {
		String toS = "tuple : ";
		for(String id : elements){
			toS += id+", ";
		}
		return toS;
	}
	
}
