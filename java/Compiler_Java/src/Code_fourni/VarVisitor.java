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

public class VarVisitor implements ObjVisitor<Set<String>> {

    public Set<String> visit(Unit e) {
        return new HashSet<String>();
    }

    public Set<String> visit(Bool e) {
        return new HashSet<String>();
    }

    public Set<String> visit(Int e) {
        return new HashSet<String>();
    }

    public Set<String> visit(Float e) { 
        return new HashSet<String>();
    }

    public Set<String> visit(Not e) {
        @SuppressWarnings("unused")
		Set<String> fv = e.getE().accept(this);
        return e.getE().accept(this);
    }

    public Set<String> visit(Neg e) {
        Set<String> fv = e.getE().accept(this);
        return fv;
    }

    public Set<String> visit(Add e) {
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(Sub e) {
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(FNeg e){
        Set<String> fv = e.getE().accept(this);
        return fv;
    }

    public Set<String> visit(FAdd e){
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(FSub e){
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(FMul e) {
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(FDiv e){
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(Eq e){
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(LE e){
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);;
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(If e){
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);
        Set<String> fv3 = e.getE3().accept(this);
        fv1.addAll(fv2);
        fv1.addAll(fv3);
        return fv1;
    }

    public Set<String> visit(Let e) {
        Set<String> res = new HashSet<String>();
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);
        fv2.remove(e.getId().toString());
        res.addAll(fv1);
        res.addAll(fv2);
        return res;
    }

    public Set<String> visit(Var e){
        Set<String> res = new HashSet<String>();
        res.add(e.getId().toString());
        return res;
    }

    public Set<String> visit(LetRec e){
        Set<String> res = new HashSet<String>();
        Set<String> fv = e.getE().accept(this);
        Set<String> fv_fun = e.getFd().getE().accept(this);
        for (Id id : e.getFd().getArgs()) {
            fv_fun.remove(id.toString());
        }
        fv.remove(e.getFd().getId().toString());
        fv_fun.remove(e.getFd().getId().toString());
        res.addAll(fv);
        res.addAll(fv_fun);
        return res;
    }

    public Set<String> visit(App e){
        Set<String> res = new HashSet<String>();
        res.addAll(e.getE().accept(this));
        for (Exp exp : e.getEs()) {
            res.addAll(exp.accept(this));
        }
        return res;
    }

    public Set<String> visit(Tuple e){
        Set<String> res = new HashSet<String>();
        for (Exp exp : e.getEs()) {
            res.addAll(exp.accept(this));
        }
        return res;
    }

    public Set<String> visit(LetTuple e){
        Set<String> res = new HashSet<String>();
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);
        for (Id id : e.getIds()) {
            fv2.remove(id.toString());
        }
        res.addAll(fv1);
        res.addAll(fv2);
        return res;
        
    }

    public Set<String> visit(Array e){
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(Get e){
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);
        fv1.addAll(fv2);
        return fv1;
    }

    public Set<String> visit(Put e){
        Set<String> fv1 = e.getE1().accept(this);
        Set<String> fv2 = e.getE2().accept(this);
        Set<String> fv3 = e.getE3().accept(this);
        fv1.addAll(fv2);
        fv1.addAll(fv3);
        return fv1;
    }
}


