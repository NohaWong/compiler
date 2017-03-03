package Code_fourni;
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

// This is an "imperative-style" way of computing the height of
// an AST.
// Add  System.out.println(Height.computeHeight(result));
// in the main method where result is the AST produced by the parser
//
// Pros of this method:
//   very close to the recursive definition of the height function, 
//   familiar style of programming
//   much more concise than the equivalent visitor
//   same template can be used for all types of tree traversals
// Cons:
//   Casts are error-prone, we lose the benefits of static type checking
//   Not "object-oriented" philosophy 

public class Height {

    public static int computeHeight(Exp exp) {
        int res = 0;
        if (exp instanceof Unit) {
            //Unit e = (Unit) exp;
            res = 0;
        } else if (exp instanceof Bool) {
            //Bool e = (Bool) exp;
            res = 0;
        } else if (exp instanceof Int) {
            //Int e = (Int) exp;
            res = 0;
        } else if (exp instanceof Bool) {
            //Bool e = (Bool) exp;
            res = 0;
        } else if (exp instanceof Not) {
            Not e = (Not) exp;
            res = computeHeight(e.getE()) + 1;
        } else if (exp instanceof Neg) {
            Neg e = (Neg) exp;
            res = computeHeight(e.getE()) + 1;
        } else if (exp instanceof Add) {
            Add e = (Add) exp;
            res = Math.max(computeHeight(e.getE1()), computeHeight(e.getE2())) + 1;
        } else if (exp instanceof Sub) {
            Sub e = (Sub) exp;
            res = Math.max(computeHeight(e.getE1()), computeHeight(e.getE2())) + 1;
        } else if (exp instanceof FNeg) {
            FNeg e = (FNeg) exp;
            res = computeHeight(e.getE()) + 1;
        } else if (exp instanceof FAdd) {
            FAdd e = (FAdd) exp;
            res = Math.max(computeHeight(e.getE1()), computeHeight(e.getE2())) + 1;
        } else if (exp instanceof FSub) {
            FSub e = (FSub) exp;
            res = Math.max(computeHeight(e.getE1()), computeHeight(e.getE2())) + 1;
        } else if (exp instanceof FMul) {
            FMul e = (FMul) exp;
            res = Math.max(computeHeight(e.getE1()), computeHeight(e.getE2())) + 1;
        } else if (exp instanceof FDiv) {
            FDiv e = (FDiv) exp;
            res = Math.max(computeHeight(e.getE1()), computeHeight(e.getE2())) + 1;
        } else if (exp instanceof Eq) {
            Eq e = (Eq) exp;
            res = Math.max(computeHeight(e.getE1()), computeHeight(e.getE2())) + 1;
        } else if (exp instanceof LE) {
            LE e = (LE) exp;
            res = Math.max(computeHeight(e.getE1()), computeHeight(e.getE2())) + 1;
        } else if (exp instanceof If) {
            If e = (If) exp;
            res = Math.max(computeHeight(e.getE1()), Math.max(computeHeight(e.getE2()), computeHeight(e.getE3()))) + 1;
        } else if (exp instanceof Let) {
            Let e = (Let) exp;
            res = Math.max(computeHeight(e.getE1()), computeHeight(e.getE2())) + 1;
        } else if (exp instanceof Var) {
            //Var e = (Var) exp;
            res = 0;
        } else if (exp instanceof LetRec) {
            LetRec e = (LetRec) exp;
            res = Math.max(computeHeight(e.getE()), computeHeight(e.getFd().getE())) + 1;
        } else if (exp instanceof App) {
            App e = (App) exp;
            res = computeHeight(e.getE());
            for (Exp e1 : e.getEs()) {
               res = Math.max(computeHeight(e1), res);
            }
            res++;
        } else if (exp instanceof Tuple) {
            Tuple e = (Tuple) exp;
            res = 0;
            for (Exp e1 : e.getEs()) {
               res = Math.max(computeHeight(e1), res);
            }
            res++;
        } else if (exp instanceof LetTuple) {
            LetTuple e = (LetTuple) exp;
            res = Math.max(computeHeight(e.getE1()), computeHeight(e.getE2())) + 1;
        } else if (exp instanceof Array) {
            Array e = (Array) exp;
            res = Math.max(computeHeight(e.getE1()), computeHeight(e.getE2())) + 1;
        } else if (exp instanceof Get) {
            Get e = (Get) exp;
            res = Math.max(computeHeight(e.getE1()), computeHeight(e.getE2())) + 1;
        }  else if (exp instanceof Put) {
            Put e = (Put) exp;
            res = Math.max(computeHeight(e.getE1()), Math.max(computeHeight(e.getE2()), computeHeight(e.getE3()))) + 1;
        } else {
            // shouldn't happen
            assert(false);
        }
        return res;
    }
}

