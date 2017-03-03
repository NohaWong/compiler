package BackEnd.AsmlSource;

import java.util.ArrayList;
import java.util.List;

import Code_fourni.Id;
import Expression.*;
import Expression.Float;

public abstract class Asml_SimpleArithmetic{
	
	public static Add expr1(){
		return new Add(new Int(12), new Int(24));
	}

	public static Add expr2(){
		return new Add(new Var(new Id("x")), new Int(3));
	}
	
	public static Add expr3(){
		return new Add(new Var(new Id("x")), new Var(new Id("x")));
	}
	
	public static Add expr4(){
		return new Add(new Int(66),new Neg(new Int(42)));
	}
	
	public static FAdd expr5(){
		return new FAdd(new Float((float) 3.0),new Float((float) 2.0));
	}
	
	//TODO
	//Rajouter des tests plus pertinents
	
	public static Let expr6(){
		//let(Id,Type,Exp,Exp)
		Let let1 = new Let(new Id("x"),null,new Int(2),new Var(new Id("x")));
		Let let2 = new Let(new Id("x"),null,new Add(new Int(12),new Int(24)),new Var(new Id("x")));
		Let let3 = new Let(new Id("x"),null,new Int(2),
				new Let(new Id("y"),null,new Int(4),new Add(new Var(new Id("x")),new Var(new Id("y")))));
		return let3;
	}
	
	//Test pas pertinent, mais pas totalement inutile non plus
	public static Let expr7(){
		List<Exp> list = new ArrayList<Exp>();
		list.add(new Var(new Id("x")));
		Let let = new Let(new Id("_"),null,
				new Let(new Id("x"),null,new Int(0),new App(new Var(new Id("_min_caml_print_int")),list)),
				new Var(Id.gen()));
		return let;
	}
	
}
