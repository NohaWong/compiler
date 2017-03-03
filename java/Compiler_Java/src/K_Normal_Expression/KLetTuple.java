package K_Normal_Expression;

import java.util.ArrayList;
import java.util.List;

import Code_fourni.Id;
import Type.Type;

public class KLetTuple extends KExp {
	private final List<Id> ids;
	private final List<Type> ts;
	private final KExp e1;
	private final KExp e2;

	public KLetTuple(List<Id> ids, List<Type> ts, KExp e1, KExp e2) {
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

	public <E> E accept(KObjVisitor<E> v) {
		return v.visit(this);
	}

	public void accept(KVisitor v) {
		v.visit(this);
	}

	public List<Id> getIds() {
		return ids;
	}

	public List<Type> getTs() {
		return ts;
	}

	public KExp getE1() {
		return e1;
	}

	public KExp getE2() {
		return e2;
	}
}