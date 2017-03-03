package Squelette;

import java.util.ArrayList;
import java.util.List;

import Code_fourni.Id;
import Code_fourni.ObjVisitor;
import Expression.*;
import Expression.Float;
import Type.Type;


/**
 * Squelette de classe pour faire une modification de l'arbre Expression
 * TODO
 */

public class Squelette_Visitor_Editor_Exp implements ObjVisitor<Exp> {
	
	/**
	 * Constructeur par défaut
	 */
	public Squelette_Visitor_Editor_Exp()
	{
		
	}
	
	/**
	 * TODO
	 * @param AST 
	 * @return
	 */
	public Exp Start(Exp AST)
	{
		return AST.accept(this);
	}
	
	/**
	 * TODO
	 */
	public Exp visit(Unit e) {
		Unit retour = new Unit();
		return retour;
	}
	
	/**
	 * TODO
	 */
	public Exp visit(Bool e) {
		Bool retour = new Bool(e.isB());
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(Int e) {
		Int retour = new Int(e.getI());
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(Float e) {
		Float retour = new Float(e.getF());
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(Not e) {
		Exp e1 = e.getE().accept(this);
		Not retour = new Not(e1);
		return retour;	
	}
	
	/**
	 * TODO
	 */
	public Exp visit(Neg e) {
		Exp e1 = e.getE().accept(this);
		Neg retour = new Neg(e1);
		return retour;
	}
	
	/**
	 * TODO
	 */
	public Exp visit(Add e) {
		Exp e1 = e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);
		Add retour = new Add(e1,e2);
		return retour;
	}
	
	/**
	 * TODO
	 */
	public Exp visit(Sub e) {
		Exp e1 = e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);
		Sub retour = new Sub(e1,e2);
		return retour;
	}
	
	/**
	 * TODO
	 */
	public Exp visit(FNeg e) {
		Exp e1 = e.getE().accept(this);
		FNeg retour = new FNeg(e1);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(FAdd e) {
		Exp e1 = e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);
		FAdd retour = new FAdd(e1,e2);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(FSub e) {
		Exp e1 = e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);
		FSub retour = new FSub(e1,e2);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(FMul e) {
		Exp e1 = e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);
		FMul retour = new FMul(e1,e2);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(FDiv e) {
		Exp e1= e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);
		FDiv retour = new FDiv(e1,e2);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(Eq e) {
		Exp e1 = e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);
		Eq retour = new Eq(e1,e2);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(LE e) {
		Exp e1 = e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);
		LE retour = new LE(e1,e2);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(If e) {
		Exp e1 = e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);		
		Exp e3 = e.getE3().accept(this);
		If retour = new If(e1,e2,e3);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(Let e) {
		Id id = e.getId();
		Type t = e.getT();
		Exp e1 = e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);
		Let retour = new Let(id,t,e1,e2);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(Var e) {
		Id id = e.getId();
		Var retour = new Var(id);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(LetRec e) {
		Exp e1 = e.getFd().getE().accept(this);
		FunDef fd = new  FunDef(e.getFd().getId(),e.getFd().getType(),e.getFd().getArgs(),e1);
		Exp e2 = e.getE().accept(this);
		LetRec retour = new LetRec(fd,e2);
		return retour;
	}
	
	/**
	 * Visit d'une Liste d'Exp et la renvoie
	 * @param la liste d'Exp à visiter
	 * @return la liste d'Exp modifiée
	 */
	private List<Exp> accept_list(List<Exp> l) {
        List<Exp> retour = new ArrayList<Exp>();
		if (l.isEmpty())
            return retour;
		
        for(Exp e : l){
        	Exp exp = e.accept(this);
        	retour.add(exp);
        }
        return retour;
    }
	
	/**
	 * TODO
	 */
	public Exp visit(App e) {
		Exp e1 = e.getE().accept(this);
		List<Exp> le = new ArrayList<Exp>(accept_list(e.getEs()));
		App retour = new App(e1,le);
		return retour;
	}
	
	/**
	 * TODO
	 */
	public Exp visit(Tuple e) {
		List<Exp> le = new ArrayList<Exp>(accept_list(e.getEs()));
		Tuple retour = new Tuple(le);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(LetTuple e) {
		List<Id> ids = new ArrayList<Id>(e.getIds());
		List<Type> ts = new ArrayList<Type>(e.getTs());
		Exp e1 = e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);
		LetTuple retour = new LetTuple(ids,ts,e1,e2);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(Array e) {
		Exp e1 = e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);
		Array retour = new Array(e1,e2);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(Get e) {
		Exp e1 = e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);
		Get retour = new Get(e1,e2);
		return retour;
	}

	/**
	 * TODO
	 */
	public Exp visit(Put e) {
		Exp e1 = e.getE1().accept(this);
		Exp e2 = e.getE2().accept(this);		
		Exp e3 = e.getE3().accept(this);
		Put retour = new Put(e1,e2,e3);
		return retour;
	}
}
