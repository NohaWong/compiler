package Code_fourni;

import Expression.Add;
import Expression.App;
import Expression.Array;
import Expression.Bool;
import Expression.Eq;
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

public interface ObjVisitor<E> {
    E visit(Unit e);
    E visit(Bool e);
    E visit(Int e);
    E visit(Float e);
    E visit(Not e);
    E visit(Neg e);
    E visit(Add e);
    E visit(Sub e);
    E visit(FNeg e);
    E visit(FAdd e);
    E visit(FSub e);
    E visit(FMul e);
    E visit(FDiv e);
    E visit(Eq e);
    E visit(LE e);
    E visit(If e);
    E visit(Let e);
    E visit(Var e);
    E visit(LetRec e);
    E visit(App e);
    E visit(Tuple e);
    E visit(LetTuple e);
    E visit(Array e);
    E visit(Get e);
    E visit(Put e);
}


