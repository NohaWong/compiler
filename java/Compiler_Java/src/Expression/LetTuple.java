package Expression;

import java.util.ArrayList;
import java.util.List;

import Code_fourni.Id;
import Code_fourni.ObjVisitor;
import Code_fourni.Visitor;
import Type.Type;

public class LetTuple extends Exp {
	private final List<Id> ids;
	private final List<Type> ts;
	private final Exp e1;
	private final Exp e2;

	public LetTuple(List<Id> ids, List<Type> ts, Exp e1, Exp e2) {
		this.ids = ids;
		if(ts == null){
			// Parce que ça a tendance à être null à la sortie du parser
			ts = new ArrayList<Type>();
			for(int i=0; i<ids.size(); i++)
				ts.add(Type.gen());
		}
		this.ts = ts;
		this.e1 = e1;
		this.e2 = e2;
	}

	public <E> E accept(ObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public List<Id> getIds() {
		return ids;
	}

	public List<Type> getTs() {
		return ts;
	}

	public Exp getE1() {
		return e1;
	}

	public Exp getE2() {
		return e2;
	}
}