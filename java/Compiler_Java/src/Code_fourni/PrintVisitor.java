package Code_fourni;
import java.util.*;

import Expression.Add;
import Expression.App;
import Expression.Array;
import Expression.Bool;
import Expression.Eq;
import Expression.Exp;
import Expression.FAdd;
import Expression.FDiv;
import Expression.FMul;
import Expression.FNeg;
import Expression.FSub;
import Expression.Float;
import Expression.Get;
import Expression.If;
import Expression.Int;
import Expression.LE;
import Expression.Let;
import Expression.LetRec;
import Expression.LetTuple;
import Expression.Neg;
import Expression.Not;
import Expression.Put;
import Expression.Sub;
import Expression.Tuple;
import Expression.Unit;
import Expression.Var;

public class PrintVisitor implements Visitor {
	
	private int niveau;
	
    public PrintVisitor() {
    	niveau = 0;
	}
    
    private void niveler(){
    	for(int i=0; i<niveau; i++)
    		System.out.print("   ");
    }

	public void visit(Unit e) {
		niveler();
        System.out.print("()");
    }

    public void visit(Bool e) {
		niveler();
        System.out.print(e.isB());
    }

    public void visit(Int e) {
		niveler();
        System.out.print(e.getI());
    }

    public void visit(Float e) {
		niveler();
        String s = String.format("%.2f", e.getF());
        System.out.print(s);
    }

    public void visit(Not e) {
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(not ");
        e.getE().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(Neg e) {
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(- ");
        e.getE().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(Add e) {
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getE1().accept(this);
        System.out.print(" + ");
        e.getE2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(Sub e) {
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getE1().accept(this);
        System.out.print(" - ");
        e.getE2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(FNeg e){
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(-. ");
        e.getE().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(FAdd e){
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getE1().accept(this);
        System.out.print(" +. ");
        e.getE2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(FSub e){
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getE1().accept(this);
        System.out.print(" -. ");
        e.getE2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(FMul e) {
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getE1().accept(this);
        System.out.print(" *. ");
        e.getE2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(FDiv e){
		niveler();
		int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getE1().accept(this);
        System.out.print(" /. ");
        e.getE2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(Eq e){
		niveler();
        System.out.print("(");
        e.getE1().accept(this);
        System.out.print(" = ");
        e.getE2().accept(this);
        System.out.print(")");
    }

    public void visit(LE e){
		niveler();
        System.out.print("(");
        e.getE1().accept(this);
        System.out.print(" <= ");
        e.getE2().accept(this);
        System.out.print(")");
    }

    public void visit(If e){
    	int niveau_present = niveau;
    	niveler();
        System.out.print("if ");
        niveau = 0;
        e.getE1().accept(this);
        System.out.print(" then {");
        System.out.println();
        niveau = niveau_present;
        niveau ++;
        e.getE2().accept(this);
        System.out.println();
        niveau --;
        niveler();
        System.out.print("}");
        System.out.println();
        niveler();
        System.out.print("else {");
        System.out.println();
        niveau ++;
        e.getE3().accept(this);
        System.out.println();
        niveau --;
        niveler();
        System.out.print("}");
        //System.out.println();
    }

    public void visit(Let e) {
    	boolean do_ln = (e.getE1() instanceof App ||
    			e.getE1() instanceof If ||
    			e.getE1() instanceof Let ||
    			e.getE1() instanceof LetRec ||
    			e.getE1() instanceof LetTuple ||
    			e.getE1() instanceof Array ||
    			e.getE1() instanceof Put);
    	int niveau_present = niveau;
		niveler();
        System.out.print("let ");
        System.out.print(e.getId());
        System.out.print(" =");
        if(do_ln){
	        System.out.println();
	        niveau ++;
        }
        else{
        	System.out.print(" ");
        	niveau = 0;
        }
        e.getE1().accept(this);
        if(do_ln){
	        System.out.println();
	        niveau --;
	        niveler();
        }
        else{
        	System.out.print(" ");
        	niveau = niveau_present;
        }
        System.out.println("in");
        //niveau ++;
        e.getE2().accept(this);
        //niveau --;
        //System.out.println();
    }

    public void visit(Var e){
    	niveler();
    	System.out.print(e.getId());
    }


    // print sequence of identifiers 
    static <E> void printInfix(List<E> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<E> it = l.iterator();
        System.out.print(it.next());
        while (it.hasNext()) {
            System.out.print(op + it.next());
        }
    }

    // print sequence of Exp
    void printInfix2(List<Exp> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<Exp> it = l.iterator();
        int niveau_present = niveau;
    	niveau = 0;
        it.next().accept(this);
        while (it.hasNext()) {
            System.out.print(op);
            it.next().accept(this);
        }
        niveau = niveau_present;
    }

    public void visit(LetRec e){
    	niveler();
        System.out.print("let rec " + e.getFd().getId() + " ");
        printInfix(e.getFd().getArgs(), " ");
        System.out.print(" = {");
        System.out.println();
        niveau ++;
        e.getFd().getE().accept(this);
        System.out.println();
        niveau --;
        niveler();
        System.out.print("}");
        System.out.println();
        niveler();
        System.out.print("in {");
        System.out.println();
        niveau ++;
        e.getE().accept(this);
        System.out.println();
        niveau --;
        niveler();
        System.out.print("}");
        //System.out.println();
    }

    public void visit(App e){
    	niveler();
    	int niveau_present = niveau;
    	niveau = 0;
        System.out.print("(");
        e.getE().accept(this);
        System.out.print(" ");
        printInfix2(e.getEs(), " ");
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(Tuple e){
    	niveler();
        System.out.print("(");
        printInfix2(e.getEs(), ", ");
        System.out.print(")");
    }

    public void visit(LetTuple e){
		niveler();
        System.out.print("let (");
        printInfix(e.getIds(), ", ");
        System.out.print(") =");
        System.out.println();
        niveau ++;
        e.getE1().accept(this);
        System.out.println();
        niveau --;
        niveler();
        System.out.print("in {");
        System.out.println();
        niveau ++;
        e.getE2().accept(this);
        System.out.println();
        niveau --;
        niveler();
        System.out.print("}");
        //System.out.println();
    }

    public void visit(Array e){
    	niveler();
		int niveau_present = niveau;
		niveau = 0;
        System.out.print("(new Array [");
        e.getE1().accept(this);
        System.out.print("] : ");
        e.getE2().accept(this);
        System.out.print(")");
        niveau = niveau_present;
    }

    public void visit(Get e){
    	niveler();
		int niveau_present = niveau;
		niveau = 0;
        e.getE1().accept(this);
        System.out.print("[");
        e.getE2().accept(this);
        System.out.print("]");
        niveau = niveau_present;
    }

    public void visit(Put e){
    	niveler();
		int niveau_present = niveau;
		niveau = 0;
        e.getE1().accept(this);
        System.out.print("[");
        e.getE2().accept(this);
        System.out.print("] <- ");
        System.out.println();
        niveau = niveau_present;
        niveau ++;
        e.getE3().accept(this);
        niveau --;
    }
}


