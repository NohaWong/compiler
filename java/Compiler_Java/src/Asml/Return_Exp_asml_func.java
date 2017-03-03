package Asml;
import java.util.Iterator;
import java.util.List;

import Expression_asml.Add_asml;
import Expression_asml.AppC_asml;
import Expression_asml.App_asml;
import Expression_asml.Array_asml;
import Expression_asml.Bool_asml;
import Expression_asml.Eq_asml;
import Expression_asml.Exp_asml;
import Expression_asml.FAdd_asml;
import Expression_asml.FDiv_asml;
import Expression_asml.FMul_asml;
import Expression_asml.FNeg_asml;
import Expression_asml.FSub_asml;
import Expression_asml.Float_asml;
import Expression_asml.Get_asml;
import Expression_asml.If_asml;
import Expression_asml.Int_asml;
import Expression_asml.LE_asml;
import Expression_asml.LetRec_asml;
import Expression_asml.Let_asml;
import Expression_asml.Let_memory_alloc_asml;
import Expression_asml.Let_memory_load_asml;
import Expression_asml.Let_memory_store_asml;
import Expression_asml.Neg_asml;
import Expression_asml.Not_asml;
import Expression_asml.Put_asml;
import Expression_asml.Sub_asml;
import Expression_asml.Unit_asml;
import Expression_asml.Var_asml;
/**
 * 
 * @author jonathan
 *
 *	Cette classe sert à trouver le membre le plus a droite d'une expression 
 */

public class Return_Exp_asml_func implements ObjVisitor_asml<Exp_asml> {

	
    public Exp_asml visit(Unit_asml e) {
    	return e;
    }

    public Exp_asml visit(Bool_asml e) {
    	return e;
    }

    public Exp_asml visit(Int_asml e) {
    	return e;
    }

    public Exp_asml visit(Float_asml e) {
    	return e;
    }

    public Exp_asml visit(Not_asml e) {
        e.getE().accept(this);
    	return e;
    }

    public Exp_asml visit(Neg_asml e) {
        e.getE().accept(this);
    	return e;
    }

    public Exp_asml visit(Add_asml e) {
        e.getE1().accept(this);
        e.getE2().accept(this);
    	return e;
    }

    public Exp_asml visit(Sub_asml e) {
        e.getE1().accept(this);
        e.getE2().accept(this);
    	return e;
    }

    public Exp_asml visit(FNeg_asml e){
        e.getE().accept(this);
    	return e;
    }

    public Exp_asml visit(FAdd_asml e){
        e.getE1().accept(this);
        e.getE2().accept(this);
    	return e;
    }

    public Exp_asml visit(FSub_asml e){
        e.getE1().accept(this);
        e.getE2().accept(this);
    	return e;
    }

    public Exp_asml visit(FMul_asml e) {
        e.getE1().accept(this);
        e.getE2().accept(this);
    	return e;
    }

    public Exp_asml visit(FDiv_asml e){
        e.getE1().accept(this);
        e.getE2().accept(this);
    	return e;
    }

    public Exp_asml visit(Eq_asml e){
        e.getE1().accept(this);
        e.getE2().accept(this);
    	return e;
    }

    public Exp_asml visit(LE_asml e){
        e.getE1().accept(this);
        e.getE2().accept(this);
    	return e;
    }
    /**
     * Attention cette fonction renvoi ce qu'il y a dans le else 
     */
    public Exp_asml visit(If_asml e){
        e.getE1().accept(this);
        e.getE2().accept(this);
        Exp_asml e3 = e.getE3().accept(this);
    	return e3;
    }

    public Exp_asml visit(Let_asml e) {
        e.getE1().accept(this);
        Exp_asml e2 = e.getE2().accept(this);
    	return e2;
    }
    

    public Exp_asml visit(Var_asml e){
    	return e;
    }


    // print sequence of identifiers 
    static <E> void printInfix(List<E> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<E> it = l.iterator();
        while (it.hasNext()) {
        }
    }

    // print sequence of Exp
    void printInfix2(List<Var_asml> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<Var_asml> it = l.iterator();
        it.next().accept(this);
        while (it.hasNext()) {
            it.next().accept(this);
        }
    }

    public Exp_asml visit(LetRec_asml e){
        printInfix(e.getFd().getArgs(), " ");
        Exp_asml e1 = e.getFd().getE().accept(this);
        e.getE().accept(this);
    	return e1;
    }

    public Exp_asml visit(App_asml e){
        e.getE().accept(this);
        printInfix2(e.getEs(), " ");
    	return e;
    }
    
    public Exp_asml visit(AppC_asml e) {
        e.getE().accept(this);
        printInfix2(e.getEs(), " ");
    	return e;
    }


    

    public Exp_asml visit(Array_asml e){
        e.getE1().accept(this);
        e.getE2().accept(this);
    	return e;
    }

    public Exp_asml visit(Get_asml e){
        e.getE1().accept(this);
        e.getE2().accept(this);
    	return e;
    }

    public Exp_asml visit(Put_asml e){
        e.getE1().accept(this);
        e.getE2().accept(this);
        e.getE3().accept(this);
    	return e;
    }

	@Override
	public Exp_asml visit(Let_memory_load_asml e) {
		Exp_asml e2 = e.getE2();
		return e2;
	}

	@Override
	public Exp_asml visit(Let_memory_store_asml e) {
		Exp_asml e2 = e.getE2();
		return e2;
	}

	@Override
	public Exp_asml visit(Let_memory_alloc_asml e) {
		Exp_asml e2 = e.getE2();
		return e2;
	}

	
}


