package Expression_asml;

import java.util.List;

import Code_fourni.Id;
import Type.Type;

public class FunDef_asml {
	private final Id id;
	private final Type type;
	private final List<Id> args;
	private final Exp_asml e;

	public FunDef_asml(Id id, Type t, List<Id> args, Exp_asml e) {
		this.id = id;
		this.type = t; // is it the right type?
		this.args = args;
		this.e = e;
	}

	public Id getId() {
		return id;
	}

	public Type getType() {
		return type;
	}

	public List<Id> getArgs() {
		return args;
	}

	public Exp_asml getE() {
		return e;
	}

}